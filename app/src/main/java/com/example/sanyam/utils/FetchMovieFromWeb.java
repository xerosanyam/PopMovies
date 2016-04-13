package com.example.sanyam.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.sanyam.BuildConfig;
import com.example.sanyam.model.Movie;
import com.example.sanyam.ui.MainActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by Sanyam Jain
 */
//Gets Images from Web
//Stores Images to DB
public class FetchMovieFromWeb extends AsyncTask<String, Void, Void> {
    Context mContext;
    public FetchMovieFromWeb(Context context) {
        mContext=context;
    }

    @Override
    protected Void doInBackground(String... params) {

        //Get from Web
        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;
        String movieInfo = null;
        try {
            URL url;
            String api_key = BuildConfig.API_KEY;

            if (params[0] == "popularity") {
                url = new URL("http://api.themoviedb.org/3/movie/popular?api_key=" + api_key);
            } else {
                url = new URL("http://api.themoviedb.org/3/movie/top_rated?api_key=" + api_key);
            }
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream == null) {
                Log.e("FetchMovieFromWeb", "Input Stream Null");
                return null;
            }
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                Log.e("FetchMovieFromWeb", "Null received from server");
                return null;
            }
            movieInfo = buffer.toString();
        } catch (Exception e) {
            Log.e("FetchMovieFromWeb.class", "Get from Web");
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    Log.e("FetchMovieFromWeb.class", "Error Closing Stream");
                }
            }
        }

        //JSON Parsing
        //Store in DB
        RealmConfiguration mrealmConfiguration;
        Realm realm=null;
        try {
            mrealmConfiguration=new RealmConfiguration.Builder(mContext).build();
            realm = Realm.getInstance(mrealmConfiguration);
            Log.e("here", movieInfo);
            JSONObject movies = new JSONObject(movieInfo);
            JSONArray results = movies.getJSONArray("results");

            MainActivity.movieList.clear();

            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i);
                int mid=result.getInt("id");
                String posterPath = result.getString("poster_path");
                String overview = result.getString("overview");
                String release_date = result.getString("release_date");
                String original_title = result.getString("original_title");
                String backdrop_path = result.getString("backdrop_path");
                double popularity = result.getDouble("popularity");
                int vote_count = result.getInt("vote_count");
                double vote_average = result.getDouble("vote_average");

                Movie m = new Movie();
                m.setPoster_path("http://image.tmdb.org/t/p/w342" + posterPath);
                m.setOverview(overview);
                m.setRelease_date(release_date);
                m.setOriginal_title(original_title);
                m.setBackdrop_path("http://image.tmdb.org/t/p/w300" + backdrop_path);
                m.setPopularity(popularity);
                m.setVote_count(vote_count);
                m.setVote_average(vote_average);
                m.setId(mid);

                realm.beginTransaction();
                realm.copyToRealmOrUpdate(m);
                realm.commitTransaction();

                MainActivity.movieList.add(m);
            }
        } catch (Exception e) {
            Log.e("FetchMovieFromWeb.class", "Err in JSON Parsing/DB storage");
            Log.e(e.getLocalizedMessage(),e.getMessage());
        } finally {
            RealmResults<Movie> movies=realm.where(Movie.class).findAll();
            Log.e("Movies Web -> DB", String.valueOf(movies.size()));
            if (realm != null) {
                realm.close();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void vvoid) {
        MainActivity.imageAdapter.notifyDataSetChanged();
        MainActivity.setRefreshActionButtonState(false);
        super.onPostExecute(vvoid);
    }
}