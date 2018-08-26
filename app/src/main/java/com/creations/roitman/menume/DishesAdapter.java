package com.creations.roitman.menume;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.creations.roitman.menume.data.Dish;
import com.creations.roitman.menume.data.DishItem;
import com.creations.roitman.menume.data.ListItem;
import com.creations.roitman.menume.data.MenuDatabase;
import com.creations.roitman.menume.utilities.PreferencesUtils;

import java.util.List;

public class DishesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    final private String LOG_TAG = DishesAdapter.class.getName();
    private List<DishItem> dishes;
    private final DishesAdapter.OnItemClickListener listener;
    private MenuDatabase mDb;
    private TextView total;
    private static final String UPDATE_ORDER = "isUpdateDue";
    private Context context;
    SharedPreferences mSettings;

    public DishesAdapter(List<DishItem> dishes, DishesAdapter.OnItemClickListener listener, TextView total) {
        this.dishes = dishes;
        this.listener = listener;
        this.total = total;
    }

    public DishesAdapter(List<DishItem> dishes, DishesAdapter.OnItemClickListener listener) {
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
    public int getItemViewType(int position) {
        return dishes.get(position).getListItemType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        if (viewType== ListItem.TYPE_DISH) {
            int layoutIdForListItem = R.layout.list_item_dish;
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(layoutIdForListItem, parent, false);
            DishViewHolder holder = new DishViewHolder(view);
            return holder;
        } else if (viewType==ListItem.TYPE_ORDERED_DISH) {
            int layoutIdForListItem = R.layout.list_item_dish_in_check;
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(layoutIdForListItem, parent, false);
            OrderedDishViewHolder holder = new OrderedDishViewHolder(view);
            return holder;
        }
        throw new RuntimeException("This type is invalid: " + viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DishViewHolder) {
            DishViewHolder h = (DishViewHolder) holder;
            h.bind(position, listener);
        } else if (holder instanceof OrderedDishViewHolder) {
            OrderedDishViewHolder h = (OrderedDishViewHolder) holder;
            h.bind(position, listener);
        }
    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

    class OrderedDishViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView quantity;
        private ImageButton stateAccepted;
        private ImageButton stateIsCooked;
        private ImageButton stateReadyToServe;

        public OrderedDishViewHolder(View itemView) {
            super(itemView);

            stateAccepted = (ImageButton) itemView.findViewById(R.id.state_ordered);
            stateIsCooked = (ImageButton) itemView.findViewById(R.id.state_is_cooking);
            stateReadyToServe = (ImageButton) itemView.findViewById(R.id.state_ready_to_serve);
            quantity = (TextView) itemView.findViewById(R.id.quantity);
            name = (TextView) itemView.findViewById(R.id.name);
        }

        void bind(final int listIndex, final DishesAdapter.OnItemClickListener listener) {
            name.setText(dishes.get(listIndex).getName());
            quantity.setText(String.valueOf(dishes.get(listIndex).getQuantity()));
            stateAccepted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "The order is accepted.", Toast.LENGTH_SHORT).show();
                }
            });

            stateIsCooked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "The dish is being cooked.", Toast.LENGTH_SHORT).show();
                }
            });

            stateReadyToServe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "This dish will be served shortly.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void updateTotal() {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putFloat(PreferencesUtils.TOTAL_CURRENT_PRICE, 0);
        editor.apply();
        double result;
        result = mSettings.getFloat(PreferencesUtils.TOTAL_CURRENT_PRICE, 0);
//        Log.e(LOG_TAG, "This is the initial result" + result);
        for (int i = 0; i < dishes.size(); i++) {
            if (dishes.get(i) instanceof Dish) {
                double price = dishes.get(i).getPrice() * dishes.get(i).getQuantity();
                result = result + price;
            }
        }

        double finalPrice = result + mSettings.getInt(PreferencesUtils.TOTAL_PRICE, 0);
        if (!(total == null)) {
//            Log.e(LOG_TAG, "this is the total price " + result);
            total.setText(String.valueOf(finalPrice));
        }
//        Log.e(LOG_TAG, "This is newly calculated result " + result);
        editor.putFloat(PreferencesUtils.TOTAL_CURRENT_PRICE, (float) finalPrice);
        editor.putBoolean(PreferencesUtils.UPDATE_ORDER, true);
        editor.apply();

    }

    /**
     * Cache of the children views for a list item.
     */
    class DishViewHolder extends RecyclerView.ViewHolder {

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
        public DishViewHolder(View itemView) {
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


        private void updateOrderBtn() {
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putBoolean(UPDATE_ORDER, true);
            editor.apply();
        }

        /**
         * A method we wrote for convenience. This method will take an integer as input and
         * use that integer to display the appropriate text within a list item.
         * @param listIndex Position of the item in the list
         */
        void bind(final int listIndex, final DishesAdapter.OnItemClickListener listener) {
            itemQuantity = Integer.parseInt(String.valueOf(dishes.get(listIndex).getQuantity()));

            name.setText(dishes.get(listIndex).getName());
            ingredients.setText(((Dish)dishes.get(listIndex)).getIngredients());
            description.setText(((Dish)dishes.get(listIndex)).getDescription());
            quantity.setText(String.valueOf(itemQuantity));
            first_addition.setText(String.valueOf(((Dish)dishes.get(listIndex)).getPrice() + "\u20BD"));

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
                        updateDb((Dish)dishes.get(listIndex));
                        updateTotal();
                    } else {
                        itemQuantity--;
                        setQuantity(listIndex);
                        updateDb((Dish)dishes.get(listIndex));
                        updateTotal();
                    }
                }
            });
            add_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemQuantity++;
                    setQuantity(listIndex);
                    updateDb((Dish)dishes.get(listIndex));
                    updateTotal();
                    updateOrderBtn();
                }
            });

            first_addition.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   setItemAdded();
                   itemQuantity++;
                   setQuantity(listIndex);
                   updateDb((Dish)dishes.get(listIndex));
                   updateTotal();
                   updateOrderBtn();

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
