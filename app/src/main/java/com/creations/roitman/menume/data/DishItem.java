package com.creations.roitman.menume.data;

public abstract class DishItem implements ListItem {
    private String name;
    private int quantity;
    private double price;


    DishItem(String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }



    /**
     * Get the price of the dish.
     * @return price of the dish
     */
    public Double getPrice() {
        return price;
    }

    /**
     * Sets the price of the dish.
     * @param price of the dish
     */
    public void setPrice(Double price) {
        this.price = price;
    }


    /**
     * Gets the quantity of the dish
     * @return the quantity
     */

    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the dish.
     * @param quantity of the dish
     */

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    /**
     * Sets the name of the dish.
     * @param name of the dish
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the dish.
     * @return the name of the dish
     */
    public String getName() {
        return this.name;
    }


}
