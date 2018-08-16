package com.creations.roitman.menume;

import com.creations.roitman.menume.data.Dish;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the menu of the restaurant.
 */
public class Menu {

    private String restName;
    private List<Dish> dishes;

    /**
     * Public constructor.
     * @param restName the name of the restaurant
     * @param dishes the dishes in the menu
     */
    public Menu(String restName, List<Dish> dishes) {
        this.restName = restName;
        this.dishes = dishes;
    }

    /**
     * An empty constructor.
     */
    public Menu() {
        this.restName = "";
        this.dishes = new ArrayList<Dish>();
    }

    /**
     * Sets the name of the restaurant.
     * @param name of the restaurant
     */
    public void setRestName(String name) {
        this.restName = name;
    }


    /**
     * Sets the menu of the restaurant.
     * @param dishList of the restaurant
     */
    public void setRestName(List<Dish> dishList) {
        this.dishes.clear();
        this.dishes.addAll(dishList);
    }

    /**
     * Gets the name of the restaurant.
     * @return the name of the restaurant
     */
    public String getRestName() {
        return this.restName;
    }

    /**
     * Gets the dishes in the menu.
     * @return the list that represents the dishes in the menu
     */
    public List<Dish> getDishes() {
        return this.dishes;
    }
}
