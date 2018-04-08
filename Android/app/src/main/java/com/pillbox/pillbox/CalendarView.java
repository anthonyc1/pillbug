package com.pillbox.pillbox;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CalendarView extends AppCompatActivity {

    TextView amView, pmView, notesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_view);
        amView = (TextView) findViewById(R.id.am);
        pmView = (TextView) findViewById(R.id.pm);
        notesView = (TextView) findViewById(R.id.notes);
    }

    public void viewWeek(View v){
        startActivity(new Intent(CalendarView.this, WeekView.class));
    }
    public void logout(View v){
        startActivity(new Intent(CalendarView.this, MainActivity.class));
    }

    public void getHttp(View v) {
        Object name = new CalendarView.OkHttpAync().execute(this, "get", "");
        //myText.setText(name.toString());
    }

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
                final String patient, days, am, pm, email, start, end, note;
                JSONObject jsonObject = new JSONObject(myResponse);
                // Values
                am = jsonObject.getString("am");
                pm = jsonObject.getString("pm");
                note = jsonObject.getString("note");
                // Output to activity
                CalendarView.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        amView.setText("AM: "+am);
                        pmView.setText("PM: "+pm);
                        notesView.setText("Note: "+note);
                    }
                });
                return am;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
