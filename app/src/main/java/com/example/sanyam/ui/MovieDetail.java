package com.example.sanyam.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sanyam.R;
import com.example.sanyam.model.Favorite;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MovieDetail extends AppCompatActivity {
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.overview)
    TextView overview;
    @Bind(R.id.date)
    TextView date;
    @Bind(R.id.vote)
    TextView vote;
    @Bind(R.id.thumbnail)
    ImageView imageViewPoster;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.backdrop)
    ImageView imageViewBackdrop;
    @Bind(R.id.favroite)
    Button favoriteButton;

    String mid;
    RealmConfiguration realmConfiguration;
    Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_item_detail);
        ButterKnife.bind(this);


        Intent intent = getIntent();
        if (intent != null) {
            mid=String.valueOf(intent.getIntExtra("mid", 0));
            Log.e("mid after intent", mid);
            title.setText(intent.getStringExtra("original_title"));
            overview.setText(intent.getStringExtra("overview"));
            date.setText(intent.getStringExtra("release_date"));
            vote.setText(intent.getStringExtra("vote"));
        }

        realmConfiguration=new RealmConfiguration.Builder(this).build();
        realm=Realm.getInstance(realmConfiguration);

        RealmResults<Favorite> realmResults=realm.where(Favorite.class).contains("mid", mid).findAll();
        if(realmResults.size()!=0){
            favoriteButton.setText("Unmark as Favorite");
        }
        //Main Poster
        Picasso.with(this).load(intent.getStringExtra("poster_path"))
                .into(imageViewPoster, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                            imageViewPoster.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError() {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                            imageViewPoster.setImageResource(R.drawable.nointernet);
                            imageViewPoster.setVisibility(View.VISIBLE);
                        }
                    }
                });

        //Banner Poster
        Picasso.with(this).load(intent.getStringExtra("backdrop_path")).fit().centerCrop().placeholder(R.drawable.loading)
                .into(imageViewBackdrop,new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        imageViewBackdrop.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {
                        imageViewBackdrop.setImageResource(R.drawable.nointernet);
                        imageViewBackdrop.setVisibility(View.VISIBLE);
                    }
                });
    }

    public void markAsFavorite(View view){
        Favorite favorite = new Favorite();
        favorite.setId(mid);
        if(favoriteButton.getText()=="Unmark as Favorite"){
            realm.beginTransaction();
            realm.where(Favorite.class).contains("mid",mid).findFirst().removeFromRealm();
            realm.commitTransaction();
            favoriteButton.setText("Mark as Favorite");
        }else {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(favorite);
            realm.commitTransaction();
            favoriteButton.setText("Unmark as Favorite");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(realm!=null)
            realm.close();
    }
}