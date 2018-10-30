package ht.eilmot.flicks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class MovieListActivity extends AppCompatActivity {
    //constants
    //the base URL for the API
    public final static String API_BASE_URL="https://image.tmdb.org/t/p/";
    //the parameter name for the API key
    public final static String API_KEY_PARAM="api_key";
    //the API key - TOOO move to a secure location
    public final static String API_KEY="a07e22bc18f5cb106bfe4cc1f83ad8ed";
    //tag for logging this activity
    public final static String TAG = "MovieListActivity";

    //instance fields
    AsyncHttpClient client;
    // the base url for loading images
    String imageBaseUrl;
    // the postter size to use when fetching images, part of the url
    String posterSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        client=new AsyncHttpClient();
    }
    //get the configuration from the API
    private void getConfiguration(){
        //create the url
        String url = API_BASE_URL + "/configuration";
        //set the request parameter
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, API_KEY); //API key always required
        //execute a GET request expecting a JSON object response
        client.get(url, params,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // get the image base url
                try {
                    imageBaseUrl = response.getString("secure_base_url");
                } catch (JSONException e) {
                    logError("Failed parsing configuration", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed on getting configuration", throwable, true);
            }


        });
    }
    //handle errors, log and alert user
    private void logError(String message, Throwable error, boolean alertUser){
        //always log th error
        Log.e(TAG, message, error);
        // alert th user to avoid silent errors
        if (alertUser){
            // show a log toast with the error message
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }

}
