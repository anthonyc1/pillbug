package com.pillbox.pillbox;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.Calendar;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

public class CalendarLogView extends AppCompatActivity {

    TextView amView, pmView, notesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_view);
        amView = (TextView) findViewById(R.id.am);
        pmView = (TextView) findViewById(R.id.pm);
        notesView = (TextView) findViewById(R.id.notes);

        MaterialCalendarView calendarView = findViewById(R.id.calendarView1);

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                if (date.getYear() == 2018 && date.getMonth()+1 == 4){
                    if (date.getDay() == 8) {
                        //Log.w("LOL", "got the 4/8");
                        new CalendarLogView.OkHttpAync().execute(CalendarLogView.this.getApplicationContext(), "get", "");
                        //Toast.makeText(CalendarLogView.this, "got it", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void viewWeek(View v){
        startActivity(new Intent(CalendarLogView.this, WeekView.class));
    }
    public void logout(View v){
        startActivity(new Intent(CalendarLogView.this, MainActivity.class));
    }

    public void getHttp(View v) {
        Object name = new CalendarLogView.OkHttpAync().execute(this.getApplicationContext(), "get", "");
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
                final String am, pm, note;
                JSONObject jsonObject = new JSONObject(myResponse);
                // Values
                am = jsonObject.getString("am");
                pm = jsonObject.getString("pm");
                note = jsonObject.getString("note");
                // Output to activity
                CalendarLogView.this.runOnUiThread(new Runnable() {
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
