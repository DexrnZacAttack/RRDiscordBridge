package io.github.dexrnzacattack.rrdiscordbridge.helpers;

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
}
