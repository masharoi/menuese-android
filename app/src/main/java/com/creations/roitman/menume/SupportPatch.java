package com.creations.roitman.menume;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class SupportPatch {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void main(String... args) throws IOException {
        allowMethods("PATCH");

        HttpURLConnection conn = (HttpURLConnection) new URL(args[0]).openConnection();
        conn.setRequestMethod("PATCH");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static void allowMethods(String... methods) {
        try {
            Field methodsField = HttpURLConnection.class.getDeclaredField("methods");

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(methodsField, methodsField.getModifiers() & ~Modifier.FINAL);

            methodsField.setAccessible(true);

            String[] oldMethods = (String[]) methodsField.get(null);
            Set<String> methodsSet = new LinkedHashSet<>(Arrays.asList(oldMethods));
            methodsSet.addAll(Arrays.asList(methods));
            String[] newMethods = methodsSet.toArray(new String[0]);

            methodsField.set(null/*static field*/, newMethods);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }
}
