package com.tarum.util;

import java.nio.ByteBuffer;
import java.util.UUID;

public class MathUtils {

    public static final long GenerateUID(){
        UUID uuid = UUID.randomUUID();
        long uid = convertUUIDToLong(uuid);
        return uid;
    }

    public static long convertUUIDToLong(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return Math.abs(bb.getLong(0)); // Ensure positive value
    }

}
