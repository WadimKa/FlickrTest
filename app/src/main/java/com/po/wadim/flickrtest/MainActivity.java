package com.po.wadim.flickrtest;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private List<PhotoItem> galleryItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.photo_list_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        new PhotosLoader().execute();
        setupAdapter();
    }

    private void setupAdapter() {
        recyclerView.setAdapter(new GalleryAdapter(galleryItems));
    }

    private class GalleryViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImageView;

        public GalleryViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            super(layoutInflater.inflate(R.layout.item, viewGroup, false));
            itemImageView = itemView.findViewById(R.id.photo_gallery_image_view);
        }
    }

    public class GalleryAdapter extends RecyclerView.Adapter<GalleryViewHolder> {
        private List<PhotoItem> photoItems;

        public GalleryAdapter(List<PhotoItem> photoItems) {
            this.photoItems = photoItems;
        }

        @Override
        public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
            return new GalleryViewHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(GalleryViewHolder holder, int position) {
            PhotoItem photoItem = photoItems.get(position);
            Picasso.with(MainActivity.this).load(photoItem.getUrl()).into(holder.itemImageView);

        }

        @Override
        public int getItemCount() {
            return photoItems.size();
        }
    }


    private class PhotosLoader extends AsyncTask<Void, Void, List<PhotoItem>> {
        @Override
        protected List<PhotoItem> doInBackground(Void... voids) {
            return new FlickFetcher().fetchItems();
        }

        @Override
        protected void onPostExecute(List<PhotoItem> photoItems) {
            galleryItems = photoItems;
            setupAdapter();

        }
    }
}
