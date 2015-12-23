package com.example.sanyam.popmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

public class MovieDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_item_detail);

//        Intent intent = getIntent();
//        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
//            int position = intent.getIntExtra(Intent.EXTRA_TEXT, 0);
//            ImageView imageView = (ImageView) findViewById(R.id.thumbnail);
//            imageView.setImageResource(position);
//        }
        final ImageView imageView = (ImageView) findViewById(R.id.thumbnail);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Picasso.with(this).load("http://i.imgur.com/DvpvklR.png")
                .into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                            imageView.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError() {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                            imageView.setImageResource(R.drawable.sample_no);
                            imageView.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}
