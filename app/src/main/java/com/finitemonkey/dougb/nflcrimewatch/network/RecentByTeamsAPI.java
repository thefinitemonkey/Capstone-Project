package com.finitemonkey.dougb.nflcrimewatch.network;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import com.finitemonkey.dougb.nflcrimewatch.data.converters.TeamRecentsJsonAdapter;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.TeamRecents;
import com.finitemonkey.dougb.nflcrimewatch.utils.Logos;
import com.finitemonkey.dougb.nflcrimewatch.utils.TeamRecentsUtils;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RecentByTeamsAPI implements TeamRecentsUtils.TeamRecentsUpdateData{
    private static final String TAG = RecentByTeamsAPI.class.getSimpleName();
    private Context mContext;
    private int mCounter;
    private List<TeamRecents> mTeamRecents;
    private RecentByTeamsListener mListener;
    private JsonAdapter<List<TeamRecents>> mJsonAdapter;

    public void getRecentByTeams(Context context, String[] teamIds, String strBeginDate, String strEndDate) {
        // Store the context reference
        mContext = context;

        // Store the listener object reference
        mListener = (RecentByTeamsListener) context;

        // Store the number of request to process
        mCounter = teamIds.length;

        // Set up the Moshi adapter and the list of TeamRecents
        Moshi moshi = new Moshi.Builder().add(new TeamRecentsJsonAdapter()).build();
        Type type = Types.newParameterizedType(List.class, TeamRecents.class);
        mJsonAdapter = moshi.adapter(type);
        mTeamRecents = new ArrayList<TeamRecents>();

        String limit = "1";

        // Iterate over all the teams to initiate the requests for background loading
        for (int i = 0; i < teamIds.length ; i++) {
            String teamId = teamIds[i];
            Uri uri = new Uri.Builder().scheme("http").authority("nflarrest.com").appendEncodedPath(
                    "api/v1/team/arrests/" + teamId).appendQueryParameter(
                    "end_date", strEndDate).appendQueryParameter(
                    "start_date", strBeginDate).appendQueryParameter("limit", limit).build();
            new RecentByTeamsAsync().execute(uri);

        }
        Log.d(TAG, "getRecentByTeams: all calls to retrieve data initialized");
    }

    private void decrementCount() {
        // Decrement the counter. When we hit 0 do the update to the database.
        mCounter--;
        if (mCounter == 0) {
            TeamRecentsUtils.updateTeamRecents(mContext, mTeamRecents);
        }
    }

    @Override
    public void onTeamRecentsDataUpdated(List<TeamRecents> teamRecents) {
        mListener.onRecentByTeamsLoadComplete(teamRecents);
    }

    public interface RecentByTeamsListener {
        void onRecentByTeamsLoadComplete(List<TeamRecents> tr);
    }

    private class RecentByTeamsAsync extends AsyncTask<Uri, Boolean, List<TeamRecents>> {

        @Override
        protected List<TeamRecents> doInBackground(Uri... uris) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(uris[0].toString()).build();
            Response response;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                response = null;
            }

            // Transform the response into a TeamRecents object and add to the list
            String json = "";
            try {
                json = response.body().string();
            } catch (IOException e) {
                json = "[{\"response\":\"error\"}]";
            }
            List<TeamRecents> tr = null;
            try {
                tr = mJsonAdapter.fromJson(json);
            } catch (IOException e) {

            }

            return tr;
        }

        @Override
        protected void onPostExecute(List<TeamRecents> tr) {
            super.onPostExecute(tr);

            if (tr != null) {
                for (TeamRecents trItem: tr
                     ) {
                    trItem.setLogo(Logos.lookupIdByTeam(trItem.getTeam()));
                    mTeamRecents.add(trItem);
                }
            }

            decrementCount();
        }

    }
}
