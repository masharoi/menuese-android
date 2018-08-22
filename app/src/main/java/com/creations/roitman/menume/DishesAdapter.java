package com.creations.roitman.menume;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.creations.roitman.menume.data.Dish;
import com.creations.roitman.menume.data.MenuDatabase;

import java.util.List;

public class DishesAdapter extends RecyclerView.Adapter<DishesAdapter.NumberViewHolder> {


    final private String LOG_TAG = DishesAdapter.class.getName();
    private List<Dish> dishes;
    private final DishesAdapter.OnItemClickListener listener;
    private MenuDatabase mDb;
    private TextView total;
    private static final String APP_PREFERENCES = "myprefs";
    private static final String APP_PREFERENCES_PRICE = "totalPrice";
    SharedPreferences mSettings;

    public DishesAdapter(List<Dish> dishes, DishesAdapter.OnItemClickListener listener, TextView total) {
        this.dishes = dishes;
        this.listener = listener;
        this.total = total;
    }

    public DishesAdapter(List<Dish> dishes, DishesAdapter.OnItemClickListener listener) {
        this.dishes = dishes;
        this.listener = listener;
    }

    public void setDb(MenuDatabase db) {
        this.mDb = db;
    }

    public void setSettings(SharedPreferences s) { this.mSettings = s; }

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

        int itemQuantity;

        TextView name;
        TextView ingredients;
        TextView description;
        private ImageButton remove_button;
        private ImageButton add_button;
        private TextView quantity;
        private Button first_addition;

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
            remove_button = (ImageButton) itemView.findViewById(R.id.remove_button);
            add_button = (ImageButton) itemView.findViewById(R.id.add_button);
            quantity = (TextView) itemView.findViewById(R.id.item_quantity);
            first_addition = (Button) itemView.findViewById(R.id.first_add);
        }

        private void setInitial() {
            remove_button.setVisibility(View.INVISIBLE);
            add_button.setVisibility(View.INVISIBLE);
            quantity.setVisibility(View.INVISIBLE);
            first_addition.setVisibility(View.VISIBLE);
        }

        private void setItemAdded() {
            remove_button.setVisibility(View.VISIBLE);
            add_button.setVisibility(View.VISIBLE);
            quantity.setVisibility(View.VISIBLE);
            first_addition.setVisibility(View.INVISIBLE);
        }

        private void setQuantity(int idx) {
            dishes.get(idx).setQuantity(itemQuantity);
            quantity.setText(String.valueOf(itemQuantity));
        }

        private void updateDb(final Dish d) {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.daoAccess().updateDish(d);
                }
            });
        }

        private void updateTotal() {
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putFloat(APP_PREFERENCES_PRICE, 0);
            editor.apply();
            double result;
            result = mSettings.getFloat(APP_PREFERENCES_PRICE, 0);
            Log.e(LOG_TAG, "This is the initial result" + result);
            for (int i = 0; i < dishes.size(); i++) {
                double price = dishes.get(i).getPrice() * dishes.get(i).getQuantity();
                Log.e(LOG_TAG, "This is the price (local)" + price);
                result = result + price;
            }
            if (!(total == null)) {
                Log.e(LOG_TAG, "this is the total price " + result);
                total.setText(String.valueOf(result));
            }
            Log.e(LOG_TAG, "This is newly calculated result " + result);
            editor.putFloat(APP_PREFERENCES_PRICE, (float) result);
            editor.apply();
            double price = mSettings.getFloat(APP_PREFERENCES_PRICE, 0);
            Log.e(LOG_TAG, "This is the price " + price);

        }

        /**
         * A method we wrote for convenience. This method will take an integer as input and
         * use that integer to display the appropriate text within a list item.
         * @param listIndex Position of the item in the list
         */
        void bind(final int listIndex, final DishesAdapter.OnItemClickListener listener) {
            itemQuantity = Integer.parseInt(String.valueOf(dishes.get(listIndex).getQuantity()));

            name.setText(dishes.get(listIndex).getName());
            ingredients.setText(dishes.get(listIndex).getIngredients());
            description.setText(dishes.get(listIndex).getDescription());
            quantity.setText(String.valueOf(itemQuantity));
            first_addition.setText(String.valueOf(dishes.get(listIndex).getPrice() + "\u20BD"));

            if (itemQuantity > 0) {
               // Log.e(LOG_TAG, "Item quantity is greater than 0");
                setItemAdded();
            } else {
                setInitial();
            }


            remove_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemQuantity == 1) {
                        setInitial();
                        dishes.get(listIndex).setQuantity(0);
                        updateDb(dishes.get(listIndex));
                        updateTotal();
                    } else {
                        itemQuantity--;
                        setQuantity(listIndex);
                        updateDb(dishes.get(listIndex));
                        updateTotal();
                    }
                }
            });
            add_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemQuantity++;
                    setQuantity(listIndex);
                    updateDb(dishes.get(listIndex));
                    updateTotal();
                }
            });

            first_addition.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   setItemAdded();
                   itemQuantity++;
                   setQuantity(listIndex);
                   updateDb(dishes.get(listIndex));
                   updateTotal();

                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick();
                }
            });
        }
    }
}
