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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeekView extends AppCompatActivity {

    TextView patientView, statusView, amView, pmView, notesView;
    TextView Sunday_AM, Monday_AM, Tuesday_AM, Wednesday_AM, Thursday_AM, Friday_AM, Saturday_AM;
    TextView Sunday_PM, Monday_PM, Tuesday_PM, Wednesday_PM, Thursday_PM, Friday_PM, Saturday_PM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.week_view);
        patientView = (TextView) findViewById(R.id.patient);
        statusView = (TextView) findViewById(R.id.status);
        amView = (TextView) findViewById(R.id.am);
        pmView = (TextView) findViewById(R.id.pm);
        notesView = (TextView) findViewById(R.id.notes);

        Sunday_AM = (TextView) findViewById(R.id.Sunday_AM);
        Monday_AM = (TextView) findViewById(R.id.Monday_AM);
        Tuesday_AM = (TextView) findViewById(R.id.Tuesday_AM);
        Wednesday_AM = (TextView) findViewById(R.id.Wednesday_AM);
        Thursday_AM = (TextView) findViewById(R.id.Thursday_AM);
        Friday_AM = (TextView) findViewById(R.id.Friday_AM);
        Saturday_AM = (TextView) findViewById(R.id.Saturday_AM);

        Sunday_PM = (TextView) findViewById(R.id.Sunday_PM);
        Monday_PM = (TextView) findViewById(R.id.Monday_PM);
        Tuesday_PM = (TextView) findViewById(R.id.Tuesday_PM);
        Wednesday_PM = (TextView) findViewById(R.id.Wednesday_PM);
        Thursday_PM = (TextView) findViewById(R.id.Thursday_PM);
        Friday_PM = (TextView) findViewById(R.id.Friday_PM);
        Saturday_PM = (TextView) findViewById(R.id.Saturday_PM);
    }

    public void viewCalendar(View v) {
        startActivity(new Intent(WeekView.this, CalendarView.class));
    }

    public void logout(View v){
        startActivity(new Intent(WeekView.this, MainActivity.class));
    }

    public void getHttp(View v) {
        Object name = new WeekView.OkHttpAync().execute(this, "get", "");
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
                final String patient, am, pm,  note;
                final JSONArray daysJSONArray;
                final String[] daysArray;

                final TextView[] textviewArray = new TextView[14];

                JSONObject jsonObject = new JSONObject(myResponse);
                // Values
                am = jsonObject.getString("am");
                pm = jsonObject.getString("pm");
                note = jsonObject.getString("note");
                patient = jsonObject.getString("patient");

                daysJSONArray = jsonObject.getJSONArray("days");
                int length = daysJSONArray.length();
                daysArray = new String[length];

                for (int i = 0; i < length; i++){
                    daysArray[i] = daysJSONArray.getString(i);
                }

                textviewArray[0] = Sunday_AM;
                textviewArray[1] = Sunday_PM;
                textviewArray[2] = Monday_AM;
                textviewArray[3] = Monday_PM;
                textviewArray[4] = Tuesday_AM;
                textviewArray[5] = Tuesday_PM;
                textviewArray[6] = Wednesday_AM;
                textviewArray[7] = Wednesday_PM;
                textviewArray[8] = Thursday_AM;
                textviewArray[9] = Thursday_PM;
                textviewArray[10] = Friday_AM;
                textviewArray[11] = Friday_PM;
                textviewArray[12] = Saturday_AM;
                textviewArray[13] = Saturday_PM;

                // Output to activity
                WeekView.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        patientView.setText("Patient: "+patient);

                        amView.setText("AM: "+am);
                        pmView.setText("PM: "+pm);
                        notesView.setText("Note: "+note);

                        for (int j = 0; j < 14; j++){
                            populateVirtualPillBox(daysArray[j], textviewArray[j]);
                        }

                        String s, status = "Good";
                        boolean b;
                        try {
                            for (int i = 0; i < 14; i++){
                                s = daysJSONArray.getString(i);
                                if (s.equalsIgnoreCase("missed") || s.equalsIgnoreCase("overdosed")){
                                    status = "Bad";
                                    break;
                                }
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        } finally {
                            if (status.equalsIgnoreCase("Bad")){
                                statusView.setText("Status: "+status);
                                statusView.setTextColor(getResources().getColor(R.color.redTextColor));
                            } else {
                                statusView.setText("Status: "+status);
                                statusView.setTextColor(getResources().getColor(R.color.greenTextColor));
                            }

                        }


                    }
                });
                return patient;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void populateVirtualPillBox(String val, TextView t){
        switch (val){
            case "taken":
                t.setBackgroundResource(R.drawable.greenbox);
                t.setText("");
                t.setTextSize(24);
                break;
            case "scheduled":
                t.setBackgroundResource(R.drawable.greenbox);
                t.setText("o");
                t.setTextSize(24);
                break;
            case "missed":
                t.setBackgroundResource(R.drawable.redbox);
                t.setText("o");
                t.setTextSize(24);
                break;
            case "overdose":
                t.setBackgroundResource(R.drawable.redbox);
                t.setText("");
                t.setTextSize(24);
                break;
            default:
                break;
        }
    }
}

