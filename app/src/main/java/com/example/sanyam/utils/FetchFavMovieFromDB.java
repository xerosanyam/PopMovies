package com.example.sanyam.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.sanyam.model.Favorite;
import com.example.sanyam.model.Movie;
import com.example.sanyam.ui.MainActivity;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by sanyam on 20/3/16.
 */
public class FetchFavMovieFromDB extends AsyncTask<Void, Void, Void> {
    Context mcontext;
    RealmConfiguration realmConfiguration;

    public FetchFavMovieFromDB(Context context) {
        mcontext=context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Log.e("FetchFavMovieFromDB", "Fetching Favorite movies from DB");
        Realm realm = null;
        //Parsing starts here
        try {
            realmConfiguration = new RealmConfiguration.Builder(mcontext).build();
            realm = Realm.getInstance(realmConfiguration);
            MainActivity.movieList.clear();
            RealmResults<Favorite> favorites=realm.where(Favorite.class).findAll();
            for(Favorite favorite:favorites){
                Movie realmMovie = realm.where(Movie.class).equalTo("id",Integer.parseInt(favorite.getId())).findFirst();
                Movie m = new Movie();
                m.setId(realmMovie.getId());
                m.setBackdrop_path(realmMovie.getBackdrop_path());
                m.setPoster_path(realmMovie.getPoster_path());
                m.setOriginal_title(realmMovie.getOriginal_title());
                m.setOverview(realmMovie.getOverview());
                m.setPopularity(realmMovie.getPopularity());
                m.setRelease_date(realmMovie.getRelease_date());
                m.setVote_average(realmMovie.getVote_average());
                m.setVote_count(realmMovie.getVote_count());
                MainActivity.movieList.add(m);
            }
        }
        catch (Exception e) {
            Log.e(e.getLocalizedMessage(),e.getMessage());
        } finally {
            Log.e("Movies DB -> FavList", String.valueOf(MainActivity.movieList.size()));
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
