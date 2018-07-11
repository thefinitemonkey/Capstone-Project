package com.finitemonkey.dougb.nflcrimewatch.network;

import android.net.Uri;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiRequestor {
    public static String getMostRecentTeamOffense(String teamId, String strBeginDate, String strEndDate) throws IOException {
        int limit = 1;
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        String strToday = dateFormat.format(today);
        String strBegin = "2000-01-01";

        Uri uri = new Uri.Builder().authority("nflarrext.com").appendPath(
                "api/v1/team/arrests/" + teamId).appendQueryParameter(
                "end_date", strEndDate).appendQueryParameter("start_date", strBeginDate).build();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(uri.toString()).build();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }
}
