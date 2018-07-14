package com.finitemonkey.dougb.nflcrimewatch.network;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;

import com.finitemonkey.dougb.nflcrimewatch.R;
import com.finitemonkey.dougb.nflcrimewatch.data.converters.TeamRecentsJsonAdapter;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.TeamRecents;
import com.finitemonkey.dougb.nflcrimewatch.utils.Logos;
import com.finitemonkey.dougb.nflcrimewatch.utils.TeamRecentsUtils;
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

public class RecentByTeamsAPI implements TeamRecentsUtils.TeamRecentUpdateData{
    private static final String TAG = RecentByTeamsAPI.class.getSimpleName();
    private Context mContext;
    private int mCounter;
    private List<TeamRecents> mTeamRecents;
    private RecentByTeamsListener mListener;
    private JsonAdapter<List<TeamRecents>> mJsonAdapter;
    private final RecentByTeamsAPI recentByTeamAPIRef = this;

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

        /*
        Iterate over all the teams to initiate the requests for background loading
        NOTE: Yes, this is horribly inefficient compared to how it should be, but
        the nflarrests.com services do not provide a method for retrieving the most
        recent incident for each team in a single call. So we will be making a separate
        call per NFL team and sorting bodies from there.
        */
        for (int i = 0; i < teamIds.length ; i++) {
            String teamId = teamIds[i];

            // Get the pieces from the string constants for this api
            Resources resources = mContext.getResources();
            String scheme = resources.getString(R.string.api_scheme);
            String authority = resources.getString(R.string.api_authority);
            String path = resources.getString(R.string.api_team_arrests_path);
            String endParam = resources.getString(R.string.api_end_date);
            String startParam = resources.getString(R.string.api_start_date);
            String limitParam = resources.getString(R.string.api_limit);

            Uri uri = new Uri.Builder().scheme(scheme).authority(authority).appendEncodedPath(
                    path + teamId).appendQueryParameter(
                    endParam, strEndDate).appendQueryParameter(
                    startParam, strBeginDate).appendQueryParameter(limitParam, limit).build();
            new RecentByTeamsAsync().execute(uri);
        }
    }

    private void decrementCount() {
        // Decrement the counter. When we hit 0 do the update to the database.
        mCounter--;
        if (mCounter == 0) {
            mListener.onRecentByTeamsLoadComplete(mTeamRecents);
        }
    }

    @Override
    public void onTeamRecentDataUpdated(TeamRecents teamRecent) {
        mTeamRecents.add(teamRecent);
        decrementCount();
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

            if (response == null) return null;

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
                    TeamRecentsUtils.updateSingleTeamRecents(mContext, recentByTeamAPIRef, trItem);
                }
            }
        }

    }
}
