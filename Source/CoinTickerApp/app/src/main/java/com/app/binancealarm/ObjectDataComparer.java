package com.app.binancealarm;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ObjectDataComparer {

    /*
    public static boolean compare(Object left, Object right) {
        try {
            if (!left.getClass().equals(right.getClass())){
                throw new Exception("left and right type must be same");
            }
            Field[] fields = left.getClass().getDeclaredFields();
            for (Field field : fields) {
                Field fieldRight = right.getClass().getDeclaredField(field.getName());
                field.setAccessible(true);
                fieldRight.setAccessible(true);
                if (field.get(left).hashCode() != fieldRight.get(right).hashCode()) {
                    return true;
                }
            }
        } catch (Exception e) {
            App.log(e);
            return false;
        }
        return false;
    }
    */

    private static UIFields changedFields = new UIFields();

    public static UIFields compare(Object left, Object right) {
        try {
            changedFields.clear();
            if (!left.getClass().equals(right.getClass())){
                throw new Exception("left and right type must be same");
            }
            Field[] fields = left.getClass().getDeclaredFields();
            for (Field field : fields) {
                Field fieldRight = right.getClass().getDeclaredField(field.getName());
                field.setAccessible(true);
                fieldRight.setAccessible(true);
                if (field.get(left).hashCode() != fieldRight.get(right).hashCode()) {
                    UIField uiField = field.getAnnotation(UIField.class);
                    if (uiField != null){
                        changedFields.add(uiField);
                    }
                }
            }
        } catch (Exception e) {
            App.log(e);
        }
        return changedFields;
    }
}
