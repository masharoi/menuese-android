package com.creations.roitman.menume.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * This class represents the dish in the menu.
 */
@Entity
public class Dish extends DishItem implements ListItem{
    @NonNull
    @PrimaryKey
    private int dishId;
    private String ingredients;
    private String description;



    /**
     * Public constructor.
     * @param name of the dish
     * @param ingredients the ingredients
     * @param description the description
     * @param dishId the id of the item
     * @param quantity of the dish
     * @param price of the dish
     */
    @Ignore
    public Dish(int quantity, int dishId, String name, String ingredients, String description, Double price) {
        super(name, quantity, price);
        this.ingredients = ingredients;
        this.description = description;
        this.dishId = dishId;
    }



    /**
     * Public constructor that does not specify the id of the dish.
     * @param name of the dish
     * @param ingredients the ingredients
     * @param description the description
     * @param quantity of the dish
     * @param price of the dish
     */

    public Dish(int quantity, String name, String ingredients, String description, Double price) {
        super(name, quantity, price);
        this.ingredients = ingredients;
        this.description = description;
    }


    /**
     * Gets the id of the dish.
     * @return integer that represents that value
     */
    public int getDishId() {
        return this.dishId;
    }

    /**
     * Sets the id of the dish.
     * @param id of the dish
     */
    public void setDishId(int id) {
        this.dishId = id;
    }

    /**
     * Gets the ingredients of the dish.
     * @return the ingredients
     */
    public String getIngredients() {
        return this.ingredients;
    }

    /**
     * Get the description of the dish.
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }


    /**
     * Sets the ingredients of the dish.
     * @param ing ingredients
     */
    public void setIngredients(String ing) {
        this.ingredients = ing;
    }

    /**
     * Set the description of the dish.
     * @param desc description of the dish
     */
    public void setDescription(String desc) {
        this.description = desc;
    }

    @Override
    public int getListItemType() {
        return ListItem.TYPE_DISH;
    }

    @Override
    public String toString() {
        return this.getName() + this.getQuantity();
    }
}
