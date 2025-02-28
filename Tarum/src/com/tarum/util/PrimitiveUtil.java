package com.tarum.util;

public class PrimitiveUtil {

    public static boolean isPrimitiveArray(Object obj) {
        return obj != null
                && obj.getClass().isArray()
                && obj.getClass().getComponentType() != null
                && obj.getClass().getComponentType().isPrimitive();
    }

}
