package com.example.demo;

import com.example.demo.model.persistence.Item;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TestUtils {
    public static void injectObjects(Object target, String fieldName, Object objectToInject) {
        boolean wasPrivate = false;
        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            if (!f.isAccessible()) {
                f.setAccessible(true);
                wasPrivate = true;
            }
            f.set(target, objectToInject);
            if (wasPrivate) {
                f.setAccessible(false);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
