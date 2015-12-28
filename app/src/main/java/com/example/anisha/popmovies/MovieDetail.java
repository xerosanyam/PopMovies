package com.example.anisha.popmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_item_detail);

        Intent intent = getIntent();
        if (intent != null) {
            TextView title = (TextView) findViewById(R.id.title);
            TextView overview = (TextView) findViewById(R.id.overview);
            TextView date = (TextView) findViewById(R.id.date);
            TextView vote = (TextView) findViewById(R.id.vote);

            title.setText(intent.getStringExtra("original_title"));
            overview.setText(intent.getStringExtra("overview"));
            date.setText(intent.getStringExtra("release_date"));
            vote.setText(intent.getStringExtra("vote"));
            Log.e("vote", String.valueOf(intent.getStringExtra("vote")));
        }
        final ImageView imageViewPoster = (ImageView) findViewById(R.id.thumbnail);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

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
        ImageView imageViewBackdrop = (ImageView) findViewById(R.id.backdrop);
        Picasso.with(this).load(intent.getStringExtra("backdrop_path")).fit().centerCrop().placeholder(R.drawable.loading).into(imageViewBackdrop);
    }
}
