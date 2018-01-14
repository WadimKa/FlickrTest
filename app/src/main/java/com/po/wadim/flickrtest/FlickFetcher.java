package com.po.wadim.flickrtest;

import android.net.Uri;
import android.nfc.Tag;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wadim on 03.01.2018.
 */

public class FlickFetcher {
    private static final String TAG = "FlickFetcher";
    private static final String API_KEY = "cbdc5083dc48d59a6929a89e48a11a37";

    public String getJSONString(String UrlSpec) throws IOException{
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(UrlSpec)
                .build();
        Response response = client.newCall(request).execute();
        String result = response.body().string();
        return result;
    }

    public List<PhotoItem> fetchItems(){
        List<PhotoItem> photoItems = new ArrayList<>();
        try {
            String url = Uri.parse("https://api.flickr.com/services/rest/")
                    .buildUpon()
                    .appendQueryParameter("method", "flickr.photos.getRecent")
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("extras", "url_s")
                    .build().toString();
            String jsonString = getJSONString(url);
            JSONObject jsonObject = new JSONObject(jsonString);
            parseItems(photoItems, jsonObject);
        }catch (IOException e){
            Log.e(TAG, "Load error", e);
        }catch (JSONException e){
            Log.e(TAG, "parsing error", e);
        }
        return photoItems;
    }

    private void parseItems(List<PhotoItem> photoItems, JSONObject jsonObject) throws IOException {
        try {


            JSONObject photoJSONOb = jsonObject.getJSONObject("photos");
            JSONArray photoJSONArray = photoJSONOb.getJSONArray("photo");

            for (int i = 0; i < photoJSONArray.length(); i++) {
                JSONObject photoJASONObj = photoJSONArray.getJSONObject(i);
                PhotoItem photoItem = new PhotoItem();
                photoItem.setId(photoJASONObj.getString("id"));
                photoItem.setCaption(photoJASONObj.getString("title"));

                if (!photoJASONObj.has("url_s")) {
                    continue;
                }

                photoItem.setUrl(photoJASONObj.getString("url_s"));
                photoItems.add(photoItem);
            }
        }catch (JSONException e){
            Log.e(TAG, "second");
        }

    }
}
