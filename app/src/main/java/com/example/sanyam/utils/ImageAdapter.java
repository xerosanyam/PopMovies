package com.example.sanyam.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.sanyam.R;
import com.example.sanyam.ui.MainActivity;
import com.squareup.picasso.Picasso;

/**
 * Created by Sanyam Jain
 */
//Custom Adapter for Images
//Provides view for images
public class ImageAdapter extends ArrayAdapter {
    private Context context;

    public ImageAdapter(Context context) {
        super(context, R.layout.grid_item, MainActivity.movieList);
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
        }

        Picasso p = Picasso.with(context);
        p.setIndicatorsEnabled(true);
        p.load(MainActivity.movieList.get(position).getPoster_path())
                .placeholder(R.drawable.loading)
                .error(R.drawable.nointernet)
                .into((ImageView) convertView);

        return convertView;
    }
}