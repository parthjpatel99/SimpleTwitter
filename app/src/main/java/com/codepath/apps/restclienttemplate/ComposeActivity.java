package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcel;
import org.parceler.Parcels;

import okhttp3.Headers;


public class ComposeActivity extends AppCompatActivity {
    public static final String TAG = "ComposeActivity";
    EditText etCompose;
    Button btnTweet;
    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApp.getRestClient(this);
        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.btnTweet);

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweetContent = etCompose.getText().toString();

                if (tweetContent.isEmpty()) {
                    Toast.makeText(ComposeActivity.this, "Empty Tweet!", Toast.LENGTH_SHORT).show();
                } else {
                    if (tweetContent.length() > 140) {
                        Toast.makeText(ComposeActivity.this, "Limit 140 Characters!", Toast.LENGTH_SHORT).show();
                    } else {
                        client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Log.i(TAG,"Successfully published the tweet!");
                                try {
                                    Tweet tweet = Tweet.fromJson(json.jsonObject);
                                    Intent i = new Intent();
                                    i.putExtra("tweet", Parcels.wrap(tweet));
                                    setResult(RESULT_OK, i);
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Log.e(TAG, "Failed to publish tweet!", throwable);
                            }
                        });
                    }
                }
            }
        });
    }
}