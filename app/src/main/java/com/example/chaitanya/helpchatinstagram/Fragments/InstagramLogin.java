package com.example.chaitanya.helpchatinstagram.Fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.chaitanya.helpchatinstagram.Data.InstagramData;
import com.example.chaitanya.helpchatinstagram.Data.InstagramSession;
import com.example.chaitanya.helpchatinstagram.MainActivity;
import com.example.chaitanya.helpchatinstagram.R;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by admin on 19/02/16.
 */
public class InstagramLogin extends Fragment {
    private static final String TAG = "Instagram-WebView";
    private static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/";
    private static final String TOKEN_URL = "https://api.instagram.com/oauth/access_token";
    private static String mCallbackUrl = InstagramData.CALLBACK_URL;
    private static final String API_URL = "https://api.instagram.com/v1";
    private String mTokenUrl;
    private String requestToken;
    private String mClientId = InstagramData.CLIENT_ID;
    private String mClientSecrect = InstagramData.CLIENT_SECRET;
    private ProgressDialog mProgress;
    private InstagramSession mSession;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.web_view, container, false);
        mTokenUrl = TOKEN_URL + "?client_id=" + mClientId + "&client_secret="
                + mClientSecrect + "&redirect_uri=" + mCallbackUrl + "&grant_type=authorization_code";
        String mAuthUrl = AUTH_URL + "?client_id=" + mClientId + "&redirect_uri="
                + mCallbackUrl + "&response_type=code&display=touch&scope=likes+comments+relationships";
        Log.d("Fragment", "InstagramLogin");
        WebView wv1 = (WebView) view.findViewById(R.id.webView);
        wv1.setVerticalScrollBarEnabled(false);
        wv1.setHorizontalScrollBarEnabled(false);
        wv1.setWebViewClient(new MyBrowser());
        wv1.getSettings().setJavaScriptEnabled(true);
        wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv1.loadUrl(mAuthUrl);
        mProgress = new ProgressDialog(getActivity());
        mProgress.setCancelable(false);
        return view;
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mProgress.setMessage("Loading ...");
            mProgress.show();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "Redirecting URL " + url);
            if (url.startsWith(mCallbackUrl)) {
                String urls[] = url.split("=");
                int length = urls.length;
                requestToken = urls[1];
                for (int i = 0; i < length; i++) {
                    Log.d("response", urls[i]);
                }
                getAccessToken task = new getAccessToken();
                task.execute();
                return true;
            }
            view.loadUrl(url);
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            //Page load finished
            super.onPageFinished(view, url);
            mProgress.dismiss();
        }
    }


    private class getAccessToken extends AsyncTask<Void, Void, Integer> {
        String mAccessToken;
        String name;
        String id;
        String user;

        protected void onPreExecute() {
            mProgress.setMessage("Get Access Token...");
            mProgress.show();
        }

        @Override
        protected Integer doInBackground(Void... params) {

            try {
                URL url = new URL(mTokenUrl);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setDoInput(true);
                httpsURLConnection.setDoOutput(true);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpsURLConnection.getOutputStream());
                outputStreamWriter.write("client_id=" + mClientId +
                        "&client_secret=" + mClientSecrect +
                        "&grant_type=authorization_code" +
                        "&redirect_uri=" + mCallbackUrl +
                        "&code=" + requestToken);

                outputStreamWriter.flush();
                String response = streamToString(httpsURLConnection.getInputStream());
                JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();
                mAccessToken = jsonObject.getString("access_token");
                Log.d("Access Token=", mAccessToken);
                id = jsonObject.getJSONObject("user").getString("id");
                user = jsonObject.getJSONObject("user").getString("username");
                name = jsonObject.getJSONObject("user").getString("full_name");
                //This is how you can get the user info. You can explore the JSON sent by Instagram as well to know what info you got in a response

            } catch (Exception e) {
                e.printStackTrace();
            }
            return 1;
        }

        protected void onPostExecute(Integer accessToken) {
            MainActivity activity = new MainActivity();
            Toast.makeText(getActivity(), mAccessToken + " " + name, Toast.LENGTH_LONG).show();
            activity.hitAPI(mAccessToken);
            mProgress.dismiss();

        }
    }

    public String streamToString(InputStream is) throws IOException {
        String string = "";
        if (is != null) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is));
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                reader.close();
            } finally {
                is.close();
            }
            string = stringBuilder.toString();
        }
        return string;
    }
}
