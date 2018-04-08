package com.pillbox.pillbox;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeekView extends AppCompatActivity {

    TextView myText;
    String myResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.week_view);
        myText = (TextView) findViewById(R.id.message);
    }

    public void viewCalendar(View v) {
        startActivity(new Intent(WeekView.this, CalendarView.class));
    }

    public void getHttp(View v) {
        Object name = new WeekView.OkHttpAync().execute(this, "get", "");
        //myText.setText(name.toString());
    }

    // Call this method
    private class OkHttpAync extends AsyncTask<Object, Void, Object> {

        private Context contx;

        @Override
        protected Object doInBackground(Object... params) {
            contx = (Context) params[0];
            String requestType = (String) params[1];
            String requestParam = (String) params[2];

            if ("get".equals(requestType)) {
                return getHttpResponse();
            }
            return null;
        }
    }

    public String getHttpResponse() {
        try {
            OkHttpClient client = new OkHttpClient();

            String url = "https://webhooks.mongodb-stitch.com/api/client/v2.0/app/pillbugapp-ylegd/service/addSchedule/incoming_webhook/getSchedule?secret=panda";

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
                // This the the text obtained from GET request
                final String myResponse = response.body().string();
                final String name;
                JSONObject jsonObject = new JSONObject(myResponse);
                // Values
                name = jsonObject.getString("email");
                System.out.println(name);
                                            // Output to activity
                WeekView.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myText.setText(name);
                    }
                });
                return name;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

