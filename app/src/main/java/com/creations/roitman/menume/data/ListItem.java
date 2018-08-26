package com.creations.roitman.menume.data;

public interface ListItem {
    int TYPE_DISH = 0;
    int TYPE_ORDERED_DISH = 1;

    /**
     * Return the type of the dish.
     * @return the type
     */
    int getListItemType();
}
