package com.example.sanyam.popmovies;

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
import android.widget.BaseAdapter;
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
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    // references to our images
    public Integer[] mThumbIds = {
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
    };
    GridView gridView;
    ImageAdapter imageAdapter;
    //    ArrayList<String> mImages=new ArrayList<>();
//    String[] mImages=new String[20];
    ArrayList<Movie> movieList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid);
        imageAdapter = new ImageAdapter(this);
        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent openMovieDetail = new Intent(getApplicationContext(), MovieDetail.class)
                        .putExtra("overview", movieList.get(position).getOverview())
                        .putExtra("release_date", movieList.get(position).getRelease_date())
                        .putExtra("original_title", movieList.get(position).getOriginal_title())
                        .putExtra("backdrop_path", movieList.get(position).getBackdrop_path())
                        .putExtra("vote", Double.toString(movieList.get(position).getVote_average()) + "/10 (" + Integer.toString(movieList.get(position).getVote_count()) + ")");
                startActivity(openMovieDetail);
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
            Collections.sort(movieList, new PopComparator());
            imageAdapter.notifyDataSetChanged();
        } else if (id == R.id.sortbyRat) {
            item.setChecked(true);
            Collections.sort(movieList, new RatComparator());
            imageAdapter.notifyDataSetChanged();

        }
        return super.onOptionsItemSelected(item);
    }

    private void updateMoviePoster() {
        FetchMovieInfo fetchMovieInfo = new FetchMovieInfo();
        fetchMovieInfo.execute();
    }

    public class PopComparator implements Comparator<Movie> {

        @Override
        public int compare(Movie lhs, Movie rhs) {
            return (int) (rhs.getPopularity() - lhs.getPopularity());
        }
    }

    public class RatComparator implements Comparator<Movie> {
        @Override
        public int compare(Movie lhs, Movie rhs) {
            return (int) (rhs.getVote_average() - lhs.getVote_average());
        }
    }

    //Custom Adapter for Images
    public class ImageAdapter extends BaseAdapter {

        private Context mContext;

        public ImageAdapter(Context context) {
            mContext = context;
//            mImages[0]="http://image.tmdb.org/t/p/w500/fYzpM9GmpBlIC893fNjoWCwE24H.jpg";
//            mImages[1]="http://image.tmdb.org/t/p/w500/D6e8RJf2qUstnfkTslTXNTUAlT.jpg";
//            mImages[2]="http://image.tmdb.org/t/p/w500/jjBgi2r5cRt36xF6iNUEhzscEcb.jpg";
//            mImages[3]="http://image.tmdb.org/t/p/w500/5aGhaIHYuQbqlHWvWYqMCnj40y2.jpg";
//            mImages[4]="http://image.tmdb.org/t/p/w500/kqjL17yufvn9OVLyXYpvtyrFfak.jpg";
//            mImages[5]="http://image.tmdb.org/t/p/w500/z2sJd1OvAGZLxgjBdSnQoLCfn3M.jpg";
//            mImages[6]="http://image.tmdb.org/t/p/w500/s5uMY8ooGRZOL0oe4sIvnlTsYQO.jpg";
//            mImages[7]="http://image.tmdb.org/t/p/w500/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg";
//            mImages[8]="http://image.tmdb.org/t/p/w500/mSvpKOWbyFtLro9BjfEGqUw5dXE.jpg";
//            mImages[9]="http://image.tmdb.org/t/p/w500/nN4cEJMHJHbJBsp3vvvhtNWLGqg.jpg";
//            mImages[10]="http://image.tmdb.org/t/p/w500/z3nGs7UED9XlqUkgWeT4jQ80m1N.jpg";
//            mImages[11]="http://image.tmdb.org/t/p/w500/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg";
//            mImages[12]="http://image.tmdb.org/t/p/w500/3zQvuSAUdC3mrx9vnSEpkFX0968.jpg";
//            mImages[13]="http://image.tmdb.org/t/p/w500/oXUWEc5i3wYyFnL1Ycu8ppxxPvs.jpg";
//            mImages[14]="http://image.tmdb.org/t/p/w500/aAmfIX3TT40zUHGcCKrlOZRKC7u.jpg";
//            mImages[15]="http://image.tmdb.org/t/p/w500/vgAHvS0bT3fpcpnJqT6uDTUsHTo.jpg";
//            mImages[16]="http://image.tmdb.org/t/p/w500/fqe8JxDNO8B8QfOGTdjh6sPCdSC.jpg";
//            mImages[17]="http://image.tmdb.org/t/p/w500/cWERd8rgbw7bCMZlwP207HUXxym.jpg";
//            mImages[18]="http://image.tmdb.org/t/p/w500/t90Y3G8UGQp0f0DrP60wRu9gfrH.jpg";
//            mImages[19]="http://image.tmdb.org/t/p/w500/tvSlBzAdRE29bZe5yYWrJ2ds137.jpg";
        }

        @Override
        public int getCount() {
            return movieList.size();
//            return movieList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ImageView imageView;
            if (convertView == null) {
                imageView = (ImageView) LayoutInflater.from(mContext).inflate(R.layout.grid_item, parent, false);
                imageView.setTag(R.id.picture, imageView.findViewById(R.id.picture));
            } else {
                imageView = (ImageView) convertView.getTag(R.id.picture);
            }
//            if(mImages.length>position){
//                Log.e(getLocalClassName(),"I am using Picasso");
//                Picasso p=Picasso.with(MainActivity.this);
//                            p.setIndicatorsEnabled(true);
//                            p.load(mImages[position])
//                                    .into(imageView);
//            }else{
//                Log.e(getLocalClassName(),"I am not using Picasso");
//                imageView.setImageResource(mThumbIds[position]);
//            }
//

            Picasso p = Picasso.with(mContext);
            p.setIndicatorsEnabled(true);
            p.load(movieList.get(position).getPoster_path()).error(R.drawable.sample_no).tag(mContext).into(imageView);
            return imageView;
        }
    }

    public class FetchMovieInfo extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader bufferedReader = null;
            String movieInfo = null;
            try {
                URL url = new URL("http://api.themoviedb.org/3/discover/movie?api_key=d0b10df79db5f6477ad936b816414e60");
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

//                    Log.e("path", "http://image.tmdb.org/t/p/w500"+posterPath);
//                    mImages[i]="http://image.tmdb.org/t/p/w500"+posterPath;
                    Movie m = new Movie();
                    m.setPoster_path("http://image.tmdb.org/t/p/w500" + posterPath);
                    m.setOverview(overview);
                    m.setRelease_date(release_date);
                    m.setOriginal_title(original_title);
                    m.setBackdrop_path("http://image.tmdb.org/t/p/w500" + backdrop_path);
                    m.setPopularity(popularity);
                    m.setVote_count(vote_count);
                    m.setVote_average(vote_average);

                    Log.e("poster_path", m.getPoster_path());
                    Log.e("overview", m.getOverview());
                    Log.e("release_date", m.getRelease_date());
                    Log.e("original_title", m.getOriginal_title());
                    Log.e("backdrop_path", m.getBackdrop_path());
                    Log.e("popularity", String.valueOf(m.getPopularity()));
                    Log.e("vote_count", String.valueOf(m.getVote_count()));
                    Log.e("vote_average", String.valueOf(m.getVote_average()));

                    movieList.add(m);
                }
            } catch (Exception e) {
                Log.e(getLocalClassName(), "Err in movieInfo/JSON Parsing");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Collections.sort(movieList, new PopComparator());
            imageAdapter.notifyDataSetChanged();
            super.onPostExecute(aVoid);
        }
    }

}
