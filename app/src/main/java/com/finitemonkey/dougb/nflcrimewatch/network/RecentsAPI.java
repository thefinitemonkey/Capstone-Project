package com.finitemonkey.dougb.nflcrimewatch.network;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.finitemonkey.dougb.nflcrimewatch.R;
import com.finitemonkey.dougb.nflcrimewatch.data.converters.ArrestsJsonAdapter;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.Arrests;
import com.finitemonkey.dougb.nflcrimewatch.utils.ArrestsUtils;
import com.finitemonkey.dougb.nflcrimewatch.utils.Logos;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RecentsAPI implements ArrestsUtils.ArrestUpdateData {
    private static final String TAG = RecentsAPI.class.getSimpleName();
    private final RecentsAPI recentApiRef = this;
    private Context mContext;
    private int mCounter;
    private List<Arrests> mRecents;
    private RecentByTeamsListener mListener;
    private JsonAdapter<List<Arrests>> mJsonAdapter;
    private int mSourceType;

    public void getRecents(Context context, int sourceType, String[] ids, String strBeginDate, String strEndDate, int limit) {
        // Store the context reference
        mContext = context;

        // Store the listener object reference
        mListener = (RecentByTeamsListener) context;

        // Store the number of request to process
        mCounter = ids.length;

        // Set up the Moshi adapter and the list of TeamRecents
        Moshi moshi = new Moshi.Builder().add(new ArrestsJsonAdapter()).build();
        Type type = Types.newParameterizedType(List.class, Arrests.class);
        mJsonAdapter = moshi.adapter(type);
        mRecents = new ArrayList<Arrests>();

        String strLimit = limit + "";

        /*
        Iterate over all the teams to initiate the requests for background loading
        NOTE: Yes, this is horribly inefficient compared to how it should be, but
        the nflarrests.com services do not provide a method for retrieving the most
        recent incident for each team in a single call. So we will be making a separate
        call per NFL team and sorting bodies from there.
        */
        // Determine the api path to use
        String apiPath;
        mSourceType = sourceType;
        Resources resources = mContext.getResources();
        int tapValue = resources.getInteger(R.integer.source_type_team);
        int papValue = resources.getInteger(R.integer.source_type_position);
        int capValue = resources.getInteger(R.integer.source_type_crime);

        if (sourceType == tapValue) {
            apiPath = resources.getString(R.string.api_team_arrests_path);
        } else if (sourceType == papValue) {
            apiPath = resources.getString(R.string.api_position_arrests_path);
        } else if (sourceType == capValue) {
            apiPath = resources.getString(R.string.api_crime_arrests_path);
        } else {
            Log.d(TAG, "getRecents: sourceType value not recognized -- " + sourceType);
            return;
        }

        for (String paramId: ids
             ) {
            // Get the pieces from the string constants for this api
            String scheme = resources.getString(R.string.api_scheme);
            String authority = resources.getString(R.string.api_authority);
            String path = apiPath + paramId;
            String endParam = resources.getString(R.string.api_end_date);
            String startParam = resources.getString(R.string.api_start_date);
            //String limitParam = resources.getString(R.string.api_limit);

            Uri uri = new Uri.Builder().scheme(scheme).authority(authority).appendEncodedPath(
                    path).appendQueryParameter(
                    endParam, strEndDate).appendQueryParameter(
                    startParam, strBeginDate).build();
            new RecentsAsync().execute(uri);

        }
    }

    private void decrementCount() {
        // Decrement the counter. When we hit 0 do the update to the database.
        mCounter--;
        if (mCounter == 0) {
            mListener.onRecentByTeamsLoadComplete(mRecents);
        }
    }

    @Override
    public void onArrestDataUpdated(Arrests arrests) {
        mRecents.add(arrests);
        decrementCount();
    }

    public interface RecentByTeamsListener {
        void onRecentByTeamsLoadComplete(List<Arrests> tr);
    }

    private class RecentsAsync extends AsyncTask<Uri, Boolean, List<Arrests>> {

        @Override
        protected List<Arrests> doInBackground(Uri... uris) {
            Log.d(TAG, "doInBackground: " + uris[0].toString());
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(uris[0].toString()).build();
            Response response;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                response = null;
            }

            if (response == null) return null;

            // Transform the response into a TeamRecents object and add to the list
            String json = "";
            try {
                json = response.body().string();
            } catch (IOException e) {
                json = "[{\"response\":\"error\"}]";
            }
            List<Arrests> tr = null;
            try {
                tr = mJsonAdapter.fromJson(json);
            } catch (IOException e) {

            }

            return tr;
        }

        @Override
        protected void onPostExecute(List<Arrests> tr) {
            super.onPostExecute(tr);

            if (tr != null && tr.size() > 0) {
                for (Arrests trItem : tr
                        ) {
                    trItem.setLogo(Logos.lookupIdByTeam(trItem.getTeam()));
                }

                String sourceParam;
                Resources resources = mContext.getResources();
                int tapValue = resources.getInteger(R.integer.source_type_team);
                int papValue = resources.getInteger(R.integer.source_type_position);
                int capValue = resources.getInteger(R.integer.source_type_crime);

                if (mSourceType == tapValue) {
                    sourceParam = tr.get(0).getTeam();
                } else if (mSourceType == papValue) {
                    sourceParam = tr.get(0).getPlayerPosition();
                } else if (mSourceType == capValue) {
                    sourceParam = tr.get(0).getEncounter();
                } else {
                    return;
                }

                ArrestsUtils.updateGroupArrests(mContext, recentApiRef, mSourceType, sourceParam, tr);
            }
        }

    }
}
