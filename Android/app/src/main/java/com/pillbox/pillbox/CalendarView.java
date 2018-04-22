package com.pillbox.pillbox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CalendarView extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView amView, pmView, notesView, dateView;
    String url1 = "https://webhooks.mongodb-stitch.com/api/client/v2.0/app/pillbugapp-ylegd/service/addSchedule/incoming_webhook/calendar1?secret=panda";
    String url2 = "https://webhooks.mongodb-stitch.com/api/client/v2.0/app/pillbugapp-ylegd/service/addSchedule/incoming_webhook/calendar2?secret=panda";
    String url3 = "https://webhooks.mongodb-stitch.com/api/client/v2.0/app/pillbugapp-ylegd/service/addSchedule/incoming_webhook/calendar3?secret=panda";

    String na = "n/a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Updating calendar logs...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(1).setChecked(true);

        SlidingUpPanelLayout layout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout_calendar);
        layout.setAnchorPoint(0.4f);

        amView = (TextView) findViewById(R.id.am);
        pmView = (TextView) findViewById(R.id.pm);
        notesView = (TextView) findViewById(R.id.notes);
        dateView = (TextView) findViewById(R.id.date);

        MaterialCalendarView calendarView = findViewById(R.id.calendarView1);

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                if (date.getYear() == 2018 && date.getMonth()+1 == 4){
                    if (date.getDay() == 6) {
                        //Log.w("LOL", "got the 4/8");
                        new CalendarView.OkHttpAync().execute(CalendarView.this.getApplicationContext(), "get", url3);
                        Toast.makeText(CalendarView.this, "Success!", Toast.LENGTH_SHORT).show();
                    } else if (date.getDay() == 7) {
                        new CalendarView.OkHttpAync().execute(CalendarView.this.getApplicationContext(), "get", url2);
                        Toast.makeText(CalendarView.this, "Success!", Toast.LENGTH_SHORT).show();
                    } else if (date.getDay() == 8){
                        new CalendarView.OkHttpAync().execute(CalendarView.this.getApplicationContext(), "get", url1);
                        Toast.makeText(CalendarView.this, "Success!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CalendarView.this, "Not Available", Toast.LENGTH_SHORT).show();
                        new CalendarView.OkHttpAync().execute(CalendarView.this.getApplicationContext(), "na", na);
                    }
                } else {
                    Toast.makeText(CalendarView.this, "Not Available", Toast.LENGTH_SHORT).show();
                    new CalendarView.OkHttpAync().execute(CalendarView.this.getApplicationContext(), "na", na);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.calendar_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_week) {
            startActivity(new Intent(CalendarView.this, WeekView2.class));
        } else if (id == R.id.nav_calendar) {
            Toast.makeText(this, "Already opened", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_call) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_logout) {
            startActivity(new Intent(CalendarView.this, LoginActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getHttp(View v, String url) {
        Object name = new CalendarView.OkHttpAync().execute(this.getApplicationContext(), "get", url);
        //myText.setText(name.toString());
    }

    private class OkHttpAync extends AsyncTask<Object, Void, Object> {

        private Context contx;

        @Override
        protected Object doInBackground(Object... params) {
            contx = (Context) params[0];
            String requestType = (String) params[1];
            String url = (String) params[2];

            if ("get".equals(requestType)) {
                return getHttpResponse(url);
            } else if ("na".equals(requestType))
                return loadUnavailable();
            return null;
        }
    }

    public String getHttpResponse(String url) {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
                // This the the text obtained from GET request
                final String myResponse = response.body().string();
                final String am, pm, note, date;
                JSONObject jsonObject = new JSONObject(myResponse);
                // Values
                date = jsonObject.getString("date");
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
                        dateView.setText("Date: "+date);
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

    public String loadUnavailable() {
        try {
            // Output to activity
            CalendarView.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    amView.setText("AM: " + na);
                    pmView.setText("PM: " + na);
                    notesView.setText("Note: " + na);
                    dateView.setText("Date: " + na);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
