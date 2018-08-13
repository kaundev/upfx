package com.upfx.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class UtilApp {

    public static List<Field> getInheritedFields(Class<?> type) {
        List<Field> fields = new ArrayList<Field>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields;
    }

    public static Field getFieldByName(Class<?> type, String fieldName){
        for (Field field : getInheritedFields(type)) {
            if (field.getName().equals(fieldName)){
                return field;
            }
        }
        return null;
    }

    public static Method getClassMethodByName(Class<?> clazz, String methodName){
        for (Method method : clazz.getMethods()) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }

    public static final <T> T getSetElement(Set<T> elements, Integer position){
        int index = 0;
        for(T element : elements){
            if(index == position){
                return element;
            }
            index++;
        }
        return null;
    }


}
