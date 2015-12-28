package com.example.anisha.popmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    GridView gridView;
    ImageAdapter imageAdapter;
    ArrayList<Movie> movieList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid);
        imageAdapter = new ImageAdapter(MainActivity.this);
        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (movieList.get(position).getOriginal_title() != "blank") {
                    Intent openMovieDetail = new Intent(getApplicationContext(), MovieDetail.class)
                            .putExtra("overview", movieList.get(position).getOverview())
                            .putExtra("release_date", movieList.get(position).getRelease_date())
                            .putExtra("original_title", movieList.get(position).getOriginal_title())
                            .putExtra("backdrop_path", movieList.get(position).getBackdrop_path())
                            .putExtra("poster_path", movieList.get(position).getPoster_path())
                            .putExtra("vote", Double.toString(movieList.get(position).getVote_average()) + "/10 (" + Integer.toString(movieList.get(position).getVote_count()) + ")");
                    startActivity(openMovieDetail);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateMoviePoster();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.refresh, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.onRefresh) {
            updateMoviePoster();
        } else if (id == R.id.sortbyPop) {
            item.setChecked(true);
            Log.e("sorting by", "Popularity");
            FetchMovieInfo fetchMovieInfo = new FetchMovieInfo();
            fetchMovieInfo.execute("popularity");
        } else if (id == R.id.sortbyRat) {
            item.setChecked(true);
            Log.e("sorting by", "Ratings");
            FetchMovieInfo fetchMovieInfo = new FetchMovieInfo();
            fetchMovieInfo.execute("ratings");
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateMoviePoster() {
        FetchMovieInfo fetchMovieInfo = new FetchMovieInfo();
        fetchMovieInfo.execute("popularity");
    }

    //Custom Adapter for Images
    public class ImageAdapter extends ArrayAdapter {
        private Context context;

        public ImageAdapter(Context context) {
            super(context, R.layout.grid_item, movieList);
            this.context = context;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
            }
            Log.e("width", String.valueOf(convertView.getWidth()));
            Picasso.with(context)
                    .load(movieList.get(position).getPoster_path()).placeholder(R.drawable.loading).error(R.drawable.nointernet).into((ImageView) convertView);
            Log.e("path", movieList.get(position).getPoster_path());
            return convertView;
        }
    }

    public class FetchMovieInfo extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader bufferedReader = null;
            String movieInfo = null;
            try {
                URL url;
                String api_key = "d0b10df79db5f6477ad936b816414e60";
//                String api_key="Enter Key Here";

                if (params[0] == "popularity") {
                    url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=" + api_key);
                } else {
                    url = new URL("http://api.themoviedb.org/3/discover/movie?vote_count.gte=1000&sort_by=vote_average.desc&api_key=" + api_key);
                }
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream == null) {
                    Log.e(getLocalClassName(), "Input Stream Null");
                    return null;
                }
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                StringBuffer buffer = new StringBuffer();
                while ((line = bufferedReader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    Log.e(getLocalClassName(), "Null received from server");
                    return null;
                }
                movieInfo = buffer.toString();
            } catch (Exception e) {
                Log.e("Api key missing", "add key");
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        Log.e(getLocalClassName(), "Error Closing Stream");
                    }
                }
            }
            try {
                Log.e("here", movieInfo);
                JSONObject movies = new JSONObject(movieInfo);
                JSONArray results = movies.getJSONArray("results");
                movieList.clear();
                for (int i = 0; i < results.length(); i++) {
                    JSONObject result = results.getJSONObject(i);
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

//                    Log.e("poster_path", m.getPoster_path());
//                    Log.e("overview", m.getOverview());
//                    Log.e("release_date", m.getRelease_date());
//                    Log.e("original_title", m.getOriginal_title());
//                    Log.e("backdrop_path", m.getBackdrop_path());
//                    Log.e("popularity", String.valueOf(m.getPopularity()));
//                    Log.e("vote_count", String.valueOf(m.getVote_count()));
//                    Log.e("vote_average", String.valueOf(m.getVote_average()));

                    movieList.add(m);
                }
            } catch (Exception e) {
                Log.e(getLocalClassName(), "Err in movieInfo/JSON Parsing");
            }
            if (movieList.size() == 0) {
                Movie dummy = new Movie();
                dummy.setPoster_path(String.valueOf(R.drawable.nointernet));
                dummy.setOriginal_title("blank");
                movieList.add(dummy);
                movieList.add(dummy);
                movieList.add(dummy);
                movieList.add(dummy);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void vvoid) {
            imageAdapter.notifyDataSetChanged();
            super.onPostExecute(vvoid);
        }
    }

}
