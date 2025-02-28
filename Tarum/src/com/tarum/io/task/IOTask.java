package com.tarum.io.task;

public abstract class IOTask {

    private long uid;
    private int id;
    private String name;

    private String[] tags;

    private int priority = TaskPriority.NORMAL.ordinal();

    public static enum TaskPriority{
        VERY_LOW, LOW, NORMAL, HIGH, VERY_HIGH
    }

}
