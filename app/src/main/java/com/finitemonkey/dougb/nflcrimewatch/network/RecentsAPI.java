package com.finitemonkey.dougb.nflcrimewatch.network;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.finitemonkey.dougb.nflcrimewatch.R;
import com.finitemonkey.dougb.nflcrimewatch.data.converters.RecentsJsonAdapter;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.Recents;
import com.finitemonkey.dougb.nflcrimewatch.utils.Logos;
import com.finitemonkey.dougb.nflcrimewatch.utils.RecentsUtils;
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

public class RecentsAPI implements RecentsUtils.TeamRecentUpdateData {
    private static final String TAG = RecentsAPI.class.getSimpleName();
    private final RecentsAPI recentApiRef = this;
    private Context mContext;
    private int mCounter;
    private List<Recents> mRecents;
    private RecentByTeamsListener mListener;
    private JsonAdapter<List<Recents>> mJsonAdapter;
    private int mSourceType;

    public void getRecents(Context context, int sourceType, String[] ids, String strBeginDate, String strEndDate, int limit) {
        // Store the context reference
        mContext = context;

        // Store the listener object reference
        mListener = (RecentByTeamsListener) context;

        // Store the number of request to process
        mCounter = ids.length;

        // Set up the Moshi adapter and the list of TeamRecents
        Moshi moshi = new Moshi.Builder().add(new RecentsJsonAdapter()).build();
        Type type = Types.newParameterizedType(List.class, Recents.class);
        mJsonAdapter = moshi.adapter(type);
        mRecents = new ArrayList<Recents>();

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

        for (int i = 0; i < ids.length; i++) {
            String paramId = ids[i];

            // Get the pieces from the string constants for this api
            String scheme = resources.getString(R.string.api_scheme);
            String authority = resources.getString(R.string.api_authority);
            String path = apiPath + paramId;
            String endParam = resources.getString(R.string.api_end_date);
            String startParam = resources.getString(R.string.api_start_date);
            String limitParam = resources.getString(R.string.api_limit);

            Uri uri = new Uri.Builder().scheme(scheme).authority(authority).appendEncodedPath(
                    path).appendQueryParameter(
                    endParam, strEndDate).appendQueryParameter(
                    startParam, strBeginDate).appendQueryParameter(limitParam, strLimit).build();
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
    public void onRecentDataUpdated(Recents recent) {
        mRecents.add(recent);
        decrementCount();
    }

    public interface RecentByTeamsListener {
        void onRecentByTeamsLoadComplete(List<Recents> tr);
    }

    private class RecentsAsync extends AsyncTask<Uri, Boolean, List<Recents>> {

        @Override
        protected List<Recents> doInBackground(Uri... uris) {
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
            List<Recents> tr = null;
            try {
                tr = mJsonAdapter.fromJson(json);
            } catch (IOException e) {

            }

            return tr;
        }

        @Override
        protected void onPostExecute(List<Recents> tr) {
            super.onPostExecute(tr);

            if (tr != null) {
                for (Recents trItem : tr
                        ) {
                    trItem.setLogo(Logos.lookupIdByTeam(trItem.getTeam()));
                    trItem.setSourceType(mSourceType);
                    RecentsUtils.updateSingleRecent(mContext, recentApiRef, mSourceType, trItem);
                }
            }
        }

    }
}
