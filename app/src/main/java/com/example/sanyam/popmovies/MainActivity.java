package com.example.sanyam.popmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(new ImageAdapter(this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, " " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private final class ImageAdapter extends BaseAdapter {
        private final List<Item> mItems = new ArrayList<Item>();
        private final LayoutInflater mInflater;

        public ImageAdapter(Context context) {
            mInflater = LayoutInflater.from(context);

            mItems.add(new Item("Red", R.drawable.sample_0));
            mItems.add(new Item("Magenta", R.drawable.sample_1));
            mItems.add(new Item("Dark Gray", R.drawable.sample_2));
            mItems.add(new Item("Gray", R.drawable.sample_3));
            mItems.add(new Item("Green", R.drawable.sample_4));
            mItems.add(new Item("Cyan", R.drawable.sample_5));
            mItems.add(new Item("Cyan", R.drawable.sample_6));
            mItems.add(new Item("Cyan", R.drawable.sample_7));
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Item getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mItems.get(position).drawableId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView picture;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.grid_item, parent, false);
                convertView.setTag(R.id.picture, convertView.findViewById(R.id.picture));
            }

            picture = (ImageView) convertView.getTag(R.id.picture);

            Item item = getItem(position);

            picture.setImageResource(item.drawableId);

            return convertView;
        }

        private class Item {
            public final String name;
            public final int drawableId;

            Item(String name, int drawableId) {
                this.name = name;
                this.drawableId = drawableId;
            }
        }
    }
}
