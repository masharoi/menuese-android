package com.creations.roitman.menume;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.creations.roitman.menume.data.Restaurant;

import java.io.InputStream;
import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.NumberViewHolder> {

    private List<Restaurant> restaurants;
    private final OnItemClickListener listener;

    public RestaurantAdapter(List<Restaurant> restaurants, OnItemClickListener listener) {
        this.restaurants = restaurants;
        this.listener = listener;
    }

    /**
     * Interface for the Listener of the items in the RecyclerView.
     */
    public interface OnItemClickListener {
        void onItemClick(int id, String name);
    }

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        NumberViewHolder holder = new NumberViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {
        holder.bind(position, listener);
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    /**
     * Cache of the children views for a list item.
     */
    class NumberViewHolder extends RecyclerView.ViewHolder {

        TextView restName;
        TextView restAddress;
        ImageView restImage;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews and set an onClickListener to listen for clicks. Those will be handled in the
         * onClick method below.
         * @param itemView The View that you inflated in
         *                 {@link RestaurantAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        public NumberViewHolder(View itemView) {
            super(itemView);

            restName = (TextView) itemView.findViewById(R.id.textView_name);
            restAddress = (TextView) itemView.findViewById(R.id.textView_address);
            restImage = (ImageView) itemView.findViewById(R.id.image_restaurant);
        }

        /**
         * A method we wrote for convenience. This method will take an integer as input and
         * use that integer to display the appropriate text within a list item.
         * @param listIndex Position of the item in the list
         */
        void bind(final int listIndex, final OnItemClickListener listener) {
            restName.setText(restaurants.get(listIndex).getName());
            restAddress.setText(restaurants.get(listIndex).getAddress());
            restImage.setImageResource(R.drawable.demo_restaurant_photo);
            new DownloadImageTask(restImage).execute(restaurants.get(listIndex).getImageUrl());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(restaurants.get(listIndex).getRestId(),
                            restaurants.get(listIndex).getName());
                }
            });
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}


