package com.tarum.io.content.type;

import java.io.File;
import java.io.Serializable;

public class Configuration extends ContentMap implements Serializable {

    public Configuration(){
        this(null);
    }
    public Configuration (File file){
        super(null, file);
    }

}
