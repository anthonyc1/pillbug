package com.pillbox.pillbox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Callback;

public class BackendService extends AppCompatActivity {

    String getreq_url = "https://webhooks.mongodb-stitch.com/api/client/v2.0/app/pillbugapp-ylegd/service/addSchedule/incoming_webhook/getSchedule?secret=panda";

    OkHttpClient client = new OkHttpClient();
    TextView myText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.week_view);
        myText = (TextView) findViewById(R.id.message);

        getRequest(getreq_url);
    }

    // Call this method
    void getRequest(String url) {
        try {
            run(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void run(String url) throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                func1(response);
            }
        });
    }

    void func1(Response response) {
        // Convert obtained text to JSON object
        try {
            // This the the text obtained from GET request
            final String myResponse = response.body().string();
            final String name;

            JSONObject jsonObject = new JSONObject(myResponse);

            // Values
            name = jsonObject.getString("email");

            // Output to activity
            BackendService.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    myText.setText(name);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void func2(Response response) {
        // Convert obtained text to JSON object
        try {
            final String myResponse = response.body().string();

            byte[] b = myResponse.getBytes();
            InputStream is = new ByteArrayInputStream(b);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            // Output to activity
            BackendService.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    myText.setText(myResponse);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


