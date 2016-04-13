package com.example.sanyam.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.sanyam.R;
import com.example.sanyam.model.Movie;
import com.example.sanyam.utils.FetchFavMovieFromDB;
import com.example.sanyam.utils.FetchMovieFromDB;
import com.example.sanyam.utils.FetchMovieFromWeb;
import com.example.sanyam.utils.ImageAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity {
    @Bind(R.id.gridView)
    GridView gridView;
    public static ImageAdapter imageAdapter;
    public static ArrayList<Movie> movieList=movieList = new ArrayList<Movie>();
    private int id;

    private static Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid);
        ButterKnife.bind(this);
        imageAdapter = new ImageAdapter(this);
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (movieList.get(position).getOriginal_title() != "blank") {
                    Intent openMovieDetail = new Intent(getApplicationContext(), MovieDetail.class)
                            .putExtra("mid",movieList.get(position).getId())
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
        if(isNetworkAvailable()) {
            updateMoviePoster();
        }else if(isOfflineDataAvailable()){
            getDatafromDB();
        }
        else{
            Toast.makeText(this,"Internet Unavailable",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isOfflineDataAvailable() {
        RealmConfiguration realmConfiguration= new RealmConfiguration.Builder(this).build();
        Realm realm=Realm.getInstance(realmConfiguration);
        RealmResults<Movie> movies=realm.where(Movie.class).findAll();
        Log.e("Movies in DB", String.valueOf(movies.size()));
        if(movies.size()==0){
            realm.close();
            return false;
        }
        realm.close();
        return true;
    }
    private void getDatafromDB(){
        FetchMovieFromDB fetchMovieFromDB = new FetchMovieFromDB(this);
        fetchMovieFromDB.execute();
    }
    private void getFavoriteFromDB(){
        FetchFavMovieFromDB fetchFavMovieFromDB=new FetchFavMovieFromDB(this);
        fetchFavMovieFromDB.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu=menu;
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_refresh, menu);
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        id = item.getItemId();
        Log.e("id is", String.valueOf(id));
        if (id == R.id.sortbyPop) {
            if(!isNetworkAvailable()){
                Toast.makeText(MainActivity.this, R.string.No_Internet, Toast.LENGTH_SHORT).show();
            }else {
                item.setChecked(true);
                id = item.getItemId();
                Toast.makeText(this, "Sorting Movies by Popularity", Toast.LENGTH_SHORT).show();
                FetchMovieFromWeb fetchMovieFromWeb = new FetchMovieFromWeb(this);
                fetchMovieFromWeb.execute("popularity");
            }
        } else if (id == R.id.sortbyRat) {
            if(!isNetworkAvailable()){
                Toast.makeText(MainActivity.this, R.string.No_Internet, Toast.LENGTH_SHORT).show();
            }else {
                item.setChecked(true);
                id = item.getItemId();
                Toast.makeText(this, "Sorting Movies by Ratings", Toast.LENGTH_SHORT).show();
                FetchMovieFromWeb fetchMovieFromWeb = new FetchMovieFromWeb(this);
                fetchMovieFromWeb.execute("ratings");
            }
        }else if(id==R.id.sortbyFav){
            item.setChecked(true);
            id = item.getItemId();
            Log.e("sorting by", "Favorites");
            Toast.makeText(this,"Showing only your Favorite Movies",Toast.LENGTH_SHORT).show();
            getFavoriteFromDB();
        }
        return super.onOptionsItemSelected(item);
    }
    public void updateMoviePoster(MenuItem item){
        setRefreshActionButtonState(true);
        if(!isNetworkAvailable()){
            Toast.makeText(MainActivity.this, R.string.No_Internet, Toast.LENGTH_SHORT).show();
            setRefreshActionButtonState(false);
        }else {
            updateMoviePoster();
        }
    }
    private void updateMoviePoster() {
        Log.e("id is ", String.valueOf(id));
        if (id == R.id.sortbyRat) {
            Toast.makeText(this,"Sorting Movies by Ratings",Toast.LENGTH_SHORT).show();
            FetchMovieFromWeb fetchMovieFromWeb = new FetchMovieFromWeb(this);
            fetchMovieFromWeb.execute("ratings");
        } else if(id==R.id.sortbyFav) {
            Toast.makeText(this,"Showing only your Favorite movies",Toast.LENGTH_SHORT).show();
            getFavoriteFromDB();
            setRefreshActionButtonState(false);
        }
        else{
            Toast.makeText(this,"Sorting Movies by Popularity",Toast.LENGTH_SHORT).show();
            FetchMovieFromWeb fetchMovieFromWeb = new FetchMovieFromWeb(this);
            fetchMovieFromWeb.execute("popularity");
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public static void setRefreshActionButtonState(final boolean refreshing) {
        if (menu != null) {
            final MenuItem refreshItem =menu.findItem(R.id.onRefresh);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(R.layout.actionbar_indeterminate_progressbar);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }
}

