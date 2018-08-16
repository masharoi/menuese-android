package com.creations.roitman.menume;

/**
 * This class represents contains the data for the restaurant.
 */
public class Restaurant {

    private String name;
    private String address;
   // private String type;
    private int restId;

    /**
     * Public constructor.
     * @param name name of the restaurant
     * @param address the address of the restaurant
     * @param restId the id of the restaurant
     */

    public Restaurant(String name, String address, int restId) {
        this.name = name;
        this.address = address;
        this.restId = restId;
        //this.type = type;
    }

    /**
     * Sets the name of the restaurant.
     * @param name the name of the restaurant
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the address of the restaurant.
     * @param address the address of the restaurant
     */

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Sets the id of the restaurant.
     * @param id the id of the restaurant
     */

    public void setRestId(int id) {
        this.restId = id;
    }

    /**
     * Gets the id of the restaurant.
     * @return the id of the restaurant
     */

    public int getRestId() {
        return this.restId;
    }

    /**
     * Gets the address of the restaurant.
     * @return the address of the restaurant
     */

    public String getAddress() {
        return this.address;
    }


    /**
     * Gets the name of the restaurant.
     * @return the name of the restaurant
     */

    public String getName() {
        return this.name;
    }


}
