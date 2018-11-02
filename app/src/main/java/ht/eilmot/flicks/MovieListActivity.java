package ht.eilmot.flicks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import ht.eilmot.flicks.models.Movie;

import static com.loopj.android.http.AsyncHttpClient.log;
import static ht.eilmot.flicks.R.layout.activity_movie_list;


public class MovieListActivity extends AppCompatActivity {
    //constants
    //the base URL for the API
    public final static String API_BASE_URL="https://image.tmdb.org/t/p/";
    //the parameter name for the API key
    public final static String API_KEY_PARAM="api_key";
   /* //the API key - TOOO move to a secure location
    public final static String API_KEY="";*/
    //tag for logging this activity
    public final static String TAG = "MovieListActivity";

    //instance fields
    AsyncHttpClient client;
    // the base url for loading images
    String imageBaseUrl;
    // the poster size to use when fetching images, part of the url
    String posterSize;
    // the list of the currently playing movie
    ArrayList<Movie> movies;
    // The Recycler view
    RecyclerView rvMovies;
    // the adapter wired to the Recycler View
    MovieAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_movie_list);
        client=new AsyncHttpClient();
        //initialize the list of movie
        movies = new ArrayList<>();
        // initialize the adapter ---- Movies Array cannot be reinitialized after yhis point
        adapter = new MovieAdapter(movies);
        // resolve the Recycler view and connect the layout manager and the adapter
        rvMovies = /*(RecyclerView)*/ findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);


        // get the configuration on app creation
        getConfiguration();

    }

    // get the list of the currently playing movies from the API

    private void getNowPlaying(){

        //create the url
        String url = API_BASE_URL + "/movie/now_playing";
        //set the request parameter
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); //API key always required
        //execute a GET request expecting a JSON object response
        client.get(url,params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // load th result into movies list
                try {
                    JSONArray results = response.getJSONArray("reults");
                    // iterate through result set and create Movie objects
                    for (int i=0; i < results.length();i++){
                        Movie movie = new Movie(results.getJSONObject(i));
                        movies.add(movie);
                        // notify adapter that a row was added
                        adapter.notifyItemInserted(movies.size() -1);
                    }
                    log.i(TAG, String.format("Loaded %s movies", results.length()));
                } catch (JSONException e) {
                    logError("Failed to parse now playing movies", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data from now playing endpoint", throwable, true);
            }
        });

    }
    //get the configuration from the API
    private void getConfiguration(){


        //create the url
        String url = API_BASE_URL + "/configuration";
        //set the request parameter
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); //API key always required
        //execute a GET request expecting a JSON object response
        client.get(url, params,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    JSONObject images = response.getJSONObject("images");

                    // get the image base url
                    imageBaseUrl = images.getString("secure_base_url");
                    // get the poster size
                    JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
                    //use the option at index 3 or w342 as a fallback
                    posterSize = posterSizeOptions.optString(3,"w342");
                    log.i(TAG, String.format("Loaded configuration with imageBaseUrl %s and posterSize %s", imageBaseUrl, posterSize));
                    // get the now playing movie list
                    getNowPlaying();
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
