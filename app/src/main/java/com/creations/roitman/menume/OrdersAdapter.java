package com.creations.roitman.menume;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.creations.roitman.menume.data.DishItem;
import com.creations.roitman.menume.data.Order;

import java.util.ArrayList;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    List<Order> orders;
    OnItemClickListener listener;

    public OrdersAdapter(List<Order> orders, OrdersAdapter.OnItemClickListener listener) {
        this.orders = orders;
        this.listener = listener;
    }

    /**
     * Interface for the Listener of the items in the RecyclerView.
     */
    public interface OnItemClickListener {
        void onItemClick();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.list_item_order;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        OrderViewHolder holder = new OrderViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.bind(position, listener);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView totalPrice;
        TextView restName;
        TextView orderDate;

        public OrderViewHolder(View itemView) {
            super(itemView);

            totalPrice = itemView.findViewById(R.id.total_order_price);
            restName = itemView.findViewById(R.id.restaurant_name);
            orderDate = itemView.findViewById(R.id.date);

        }

        void bind(int position, final OnItemClickListener listener) {
            totalPrice.setText(String.valueOf(orders.get(position).getTotal()));
            restName.setText(orders.get(position).getRestName());
            orderDate.setText("29.08.18");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick();
                }
            });
        }

    }
}
