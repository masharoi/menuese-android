package com.creations.roitman.menume.utilities;

import com.creations.roitman.menume.data.DishItem;

import java.util.ArrayList;
import java.util.List;

public class GenUtils {

    public static List<String> listToString(List<DishItem> l) {
        List<String> out = new ArrayList<>();
        for (int i = 0; i < l.size(); i++) {
            out.add(l.get(i).getName() + " " + l.get(i).getQuantity());
        }
        return out;
    }
}
