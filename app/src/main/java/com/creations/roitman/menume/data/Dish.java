package com.creations.roitman.menume.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * This class represents the dish in the menu.
 */
@Entity
public class Dish {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int dishId;
    private String name;
    private String ingredients;
    private String description;
    //private Double price;


    /**
     * Public constructor.
     * @param name of the dish
     * @param ingredients the ingredients
     * @param description the description
     * @param dishId the id of the item
     */
    public Dish(int dishId, String name, String ingredients, String description) {
        this.name = name;
        this.ingredients = ingredients;
        this.description = description;
        this.dishId = dishId;
    }

    /**
     * Public constructor that does not specify the id of the dish.
     * @param name of the dish
     * @param ingredients the ingredients
     * @param description the description
     */
    @Ignore
    public Dish(String name, String ingredients, String description) {
        this.name = name;
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
     * Gets the name of the dish.
     * @return the name of the dish
     */
    public String getName() {
        return this.name;
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
     * Sets the name of the dish.
     * @param name of the dish
     */
    public void setName(String name) {
        this.name = name;
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
}
