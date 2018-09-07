package com.creations.roitman.menume;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.creations.roitman.menume.data.DishItem;

import java.util.List;

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.ReceiptViewHolder> {

    List<DishItem> dishes;

    public ReceiptAdapter(List<DishItem> dishes) {
        this.dishes = dishes;
    }


    @NonNull
    @Override
    public ReceiptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.list_item_complete_order;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        ReceiptViewHolder holder = new ReceiptViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

    class ReceiptViewHolder extends RecyclerView.ViewHolder {

        TextView quantPrice;
        TextView dishName;

        public ReceiptViewHolder(View itemView) {
            super(itemView);

            quantPrice = itemView.findViewById(R.id.quantity);
            dishName = itemView.findViewById(R.id.name);

        }

        void bind(final int position) {
            quantPrice.setText(dishes.get(position).getQuantity() + " × "
                    + dishes.get(position).getPrice() + "₽");
            dishName.setText(dishes.get(position).getName());
        }

    }
}
