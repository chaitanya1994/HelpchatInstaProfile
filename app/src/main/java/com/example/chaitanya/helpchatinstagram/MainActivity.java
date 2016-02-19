package com.example.chaitanya.helpchatinstagram;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chaitanya.helpchatinstagram.response.Counts;
import com.example.chaitanya.helpchatinstagram.response.Data;
import com.example.chaitanya.helpchatinstagram.response.ResponseClass;
import com.example.chaitanya.helpchatinstagram.retrofit.App;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    private TextView userName, fullName, bio, website, media, follows, followedBy;
    private String unameText, fullNameText, bioText, webText, mediaText, followsText, followedByText, pictureURI;
    private ImageView profilePicture;
    Map<String, String> map = new HashMap<>();

    Data dataOfUser = new Data();
    Counts count = new Counts();
    ResponseClass responseObject = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        userName = (TextView) findViewById(R.id.userName);
        fullName = (TextView) findViewById(R.id.nameContent);
        bio = (TextView) findViewById(R.id.bioContent);
        website = (TextView) findViewById(R.id.webContent);
        media = (TextView) findViewById(R.id.mediaContent);
        follows = (TextView) findViewById(R.id.followsContent);
        followedBy = (TextView) findViewById(R.id.followedByContent);
        profilePicture = (ImageView) findViewById(R.id.imageOfUser);

        hitAPI("855595876.5b9e1e6.d3f4aa5294a74cfdbd1c1a97c87b6619");
    }


    public void hitAPI(String accessToken) {
        map.clear();
        map.put("access_token", accessToken);
        App.getApi().showPopularMedia(map, new Callback<ResponseClass>() {
            @Override
            public void success(ResponseClass responseClass, Response response) {
                //passToUllas(responseClass);
                setViews(responseClass);
                Log.d("Success", "Response Populated Successfully");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Failure", "Response Error " + error.getMessage());
            }
        });
    }

    void setResponseObject(ResponseClass responseClass) {
        responseObject = responseClass;
    }

    void setViews(ResponseClass responseObject) {
        if (responseObject == null) {
            return;
        }
        dataOfUser = responseObject.getData();
        count = dataOfUser.getCounts();

        pictureURI = dataOfUser.getProfilePicture();
        unameText = dataOfUser.getUsername();
        fullNameText = dataOfUser.getFullName();
        bioText = dataOfUser.getBio();
        webText = dataOfUser.getWebsite();
        if (count != null) {
            mediaText = count.getMedia().toString();
            followsText = count.getFollows().toString();
//            followedByText = count.getFollowedBy().toString();


            media.setText(mediaText);
            follows.setText(followsText);
//            followedBy.setText(followedByText);
        }
        GetXMLTask task = new GetXMLTask();
        task.execute(new String[]{pictureURI});

//        profilePicture.setImageURI(Uri.parse(pictureURI));
        userName.setText(unameText);
        fullName.setText(fullNameText);
        bio.setText(bioText);
        website.setText(webText);
    }

    private class GetXMLTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap map = null;
            for (String url : urls) {
                map = downloadImage(url);
            }
            return map;
        }

        // Sets the Bitmap returned by doInBackground
        @Override
        protected void onPostExecute(Bitmap result) {
            profilePicture.setImageBitmap(result);
        }

        // Creates Bitmap from InputStream and returns it
        private Bitmap downloadImage(String url) {
            Bitmap bitmap = null;
            InputStream stream = null;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;

            try {
                stream = getHttpConnection(url);
                bitmap = BitmapFactory.
                        decodeStream(stream, null, bmOptions);
                stream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return bitmap;
        }

        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString)
                throws IOException {
            InputStream stream = null;
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try {
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    stream = httpConnection.getInputStream();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }
    }
}

