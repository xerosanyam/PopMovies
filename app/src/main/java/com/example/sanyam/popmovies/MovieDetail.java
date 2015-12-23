package com.example.sanyam.popmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class MovieDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_item_detail);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            int position = intent.getIntExtra(Intent.EXTRA_TEXT, 0);
            ImageView imageView = (ImageView) findViewById(R.id.thumbnail);
            imageView.setImageResource(position);
        }
    }
}
