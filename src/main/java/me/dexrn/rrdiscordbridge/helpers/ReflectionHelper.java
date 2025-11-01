package me.dexrn.rrdiscordbridge.helpers;

import java.lang.reflect.Method;

/** Provides basic utils for working with reflection */
public class ReflectionHelper {
    /**
     * @return {@code true} if the method exists
     */
    public static boolean doesMethodExist(String clazz, String method) {
        try {
            Class<?> clazzz = Class.forName(clazz);
            clazzz.getMethod(method);
            return true;
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            return false;
        }
    }

    /**
     * @return {@code true} if the class exists
     */
    public static boolean doesClassExist(String clazz) {
        try {
            Class.forName(clazz);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * @return The class (or null if the class doesn't exist)
     */
    public static Class<?> getClass(String clazz) {
        try {
            return Class.forName(clazz);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * @return The method (or null if the method doesn't exist)
     */
    public static Method getMethod(Class<?> clazz, String name, Class<?>... args) {
        try {
            return clazz.getMethod(name, args);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
