package com.creations.roitman.menume;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.creations.roitman.menume.data.Dish;

import java.util.List;

public class DishesAdapter extends RecyclerView.Adapter<DishesAdapter.NumberViewHolder> {


    private List<Dish> dishes;
    private final DishesAdapter.OnItemClickListener listener;

    public DishesAdapter(List<Dish> dishes, DishesAdapter.OnItemClickListener listener) {
        this.dishes = dishes;
        this.listener = listener;
    }

    /**
     * Interface for the Listener of the items in the RecyclerView.
     */
    public interface OnItemClickListener {
        void onItemClick();
    }

    @Override
    public DishesAdapter.NumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_item_dish;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        DishesAdapter.NumberViewHolder holder = new DishesAdapter.NumberViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(DishesAdapter.NumberViewHolder holder, int position) {
        holder.bind(position, listener);
    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

    /**
     * Cache of the children views for a list item.
     */
    class NumberViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView ingredients;
        TextView description;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews and set an onClickListener to listen for clicks. Those will be handled in the
         * onClick method below.
         * @param itemView The View that you inflated in
         *                 {@link RestaurantAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        public NumberViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.textView_name);
            ingredients = (TextView) itemView.findViewById(R.id.textView_ingredients);
            description = (TextView) itemView.findViewById(R.id.textView_description);
        }

        /**
         * A method we wrote for convenience. This method will take an integer as input and
         * use that integer to display the appropriate text within a list item.
         * @param listIndex Position of the item in the list
         */
        void bind(int listIndex, final DishesAdapter.OnItemClickListener listener) {
            name.setText(dishes.get(listIndex).getName());
            ingredients.setText(dishes.get(listIndex).getIngredients());
            description.setText(dishes.get(listIndex).getDescription());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick();
                }
            });
        }
    }
}
