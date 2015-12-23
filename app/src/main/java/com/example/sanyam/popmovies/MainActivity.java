package com.example.sanyam.popmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

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

    public class ImageAdapter extends BaseAdapter {
        private Context mcontext;
        private Integer[] mThumbsIds = {
                R.drawable.sample_0,
                R.drawable.sample_1,
                R.drawable.sample_2,
                R.drawable.sample_3,
                R.drawable.sample_4,
                R.drawable.sample_5,
                R.drawable.sample_6,
                R.drawable.sample_7
        };

        public ImageAdapter(Context c) {
            mcontext = c;
        }

        public int getCount() {
            return mThumbsIds.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mcontext);
                //imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            } else {
                imageView = (ImageView) convertView;
            }
            imageView.setImageResource(mThumbsIds[position]);
            return imageView;
        }
    }
}
