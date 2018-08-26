package com.creations.roitman.menume.data;

import android.arch.persistence.room.Entity;

@Entity
public class OrderedDish extends DishItem implements ListItem{

    private int state;

    public OrderedDish(int state, String name, int quantity, double price) {
        super(name, quantity, price);
        this.state = state;
    }


    /**
     * Get the state of the dish.
     * @return the integer that represents that state
     */
    public int getState() {
        return state;
    }

    /**
     * Set the state of the dish.
     * @param state integer that represents that state
     */
    public void setState(int state) {
        this.state = state;
    }


    @Override
    public int getListItemType() {
        return ListItem.TYPE_ORDERED_DISH;
    }
}
