package com.finitemonkey.dougb.nflcrimewatch.network;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;

import com.finitemonkey.dougb.nflcrimewatch.R;
import com.finitemonkey.dougb.nflcrimewatch.data.converters.TeamRecentsJsonAdapter;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.Arrests;
import com.finitemonkey.dougb.nflcrimewatch.data.tables.TeamRecents;
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

public class ArrestsAPI implements ArrestsUtils.ArrestUpdateData {
    private static final String TAG = ArrestsAPI.class.getSimpleName();
    private final ArrestsAPI arrestsAPIRef = this;
    private Context mContext;
    private List<Arrests> mArrests;
    private JsonAdapter<List<Arrests>> mJsonAdapter;

    public void getArrestsByTeam(Context context, String teamId, String strBeginDate, String strEndDate) {
        // Store the context reference
        mContext = context;

        // Set up the Moshi adapter and the list of Arrests
        Moshi moshi = new Moshi.Builder().add(new TeamRecentsJsonAdapter()).build();
        Type type = Types.newParameterizedType(List.class, TeamRecents.class);
        mJsonAdapter = moshi.adapter(type);
        mArrests = new ArrayList<Arrests>();

        // Begin background loading for the arrests request
        // Get the pieces from the string constants for this api
        Resources resources = mContext.getResources();
        String scheme = resources.getString(R.string.api_scheme);
        String authority = resources.getString(R.string.api_authority);
        String path = resources.getString(R.string.api_team_arrests_path);
        String endParam = resources.getString(R.string.api_end_date);
        String startParam = resources.getString(R.string.api_start_date);

        Uri uri = new Uri.Builder().scheme(scheme).authority(authority).appendEncodedPath(
                path + teamId).appendQueryParameter(
                endParam, strEndDate).appendQueryParameter(
                startParam, strBeginDate).build();
        new ArrestsByTeamAsync().execute(uri);
    }

    public void getArrestsByPosition(Context context, String positionId, String strBeginDate, String strEndDate) {
        // Store the context reference
        mContext = context;

        // Set up the Moshi adapter and the list of Arrests
        Moshi moshi = new Moshi.Builder().add(new TeamRecentsJsonAdapter()).build();
        Type type = Types.newParameterizedType(List.class, TeamRecents.class);
        mJsonAdapter = moshi.adapter(type);
        mArrests = new ArrayList<Arrests>();

        // Begin background loading for the arrests request
        // Get the pieces from the string constants for this api
        Resources resources = mContext.getResources();
        String scheme = resources.getString(R.string.api_scheme);
        String authority = resources.getString(R.string.api_authority);
        String path = resources.getString(R.string.api_position_arrests_path);
        String endParam = resources.getString(R.string.api_end_date);
        String startParam = resources.getString(R.string.api_start_date);

        Uri uri = new Uri.Builder().scheme(scheme).authority(authority).appendEncodedPath(
                path + positionId).appendQueryParameter(
                endParam, strEndDate).appendQueryParameter(
                startParam, strBeginDate).build();
        new ArrestsByTeamAsync().execute(uri);
    }

    public void getArrestsByCrime(Context context, String crimeId, String strBeginDate, String strEndDate) {
        // Store the context reference
        mContext = context;

        // Set up the Moshi adapter and the list of Arrests
        Moshi moshi = new Moshi.Builder().add(new TeamRecentsJsonAdapter()).build();
        Type type = Types.newParameterizedType(List.class, TeamRecents.class);
        mJsonAdapter = moshi.adapter(type);
        mArrests = new ArrayList<Arrests>();

        // Begin background loading for the arrests request
        // Get the pieces from the string constants for this api
        Resources resources = mContext.getResources();
        String scheme = resources.getString(R.string.api_scheme);
        String authority = resources.getString(R.string.api_authority);
        String path = resources.getString(R.string.api_crime_arrests_path);
        String endParam = resources.getString(R.string.api_end_date);
        String startParam = resources.getString(R.string.api_start_date);

        Uri uri = new Uri.Builder().scheme(scheme).authority(authority).appendEncodedPath(
                path + crimeId).appendQueryParameter(
                endParam, strEndDate).appendQueryParameter(
                startParam, strBeginDate).build();
        new ArrestsByTeamAsync().execute(uri);
    }

    @Override
    public void onArrestDataUpdated(Arrests arrest) {
        mArrests.add(arrest);
    }

    private class ArrestsByTeamAsync extends AsyncTask<Uri, Boolean, List<Arrests>> {

        @Override
        protected List<Arrests> doInBackground(Uri... uris) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(uris[0].toString()).build();
            Response response;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                response = null;
            }

            if (response == null) return null;

            // Transform the response into an Arrests object
            String json = "";
            try {
                json = response.body().string();
            } catch (IOException e) {
                json = "[{\"response\":\"error\"}]";
            }
            List<Arrests> arrests = null;
            try {
                arrests = mJsonAdapter.fromJson(json);
            } catch (IOException e) {

            }

            return arrests;
        }

        @Override
        protected void onPostExecute(List<Arrests> arrests) {
            super.onPostExecute(arrests);

            if (arrests != null) {
                for (Arrests arrest : arrests
                        ) {
                    arrest.setLogo(Logos.lookupIdByTeam(arrest.getTeam()));
                    ArrestsUtils.updateSingleArrest(mContext, arrestsAPIRef, arrest);
                }
            }
        }

    }
}
