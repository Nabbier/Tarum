package com.tarum.io.content.type;
import com.tarum.io.content.ContentManager;

import java.io.File;
import java.io.Serializable;

public class BasicContentContainerImpl extends BasicContentContainer implements Serializable {

    public BasicContentContainerImpl(ContentManager contentManager){
        this(contentManager, "BasicContentContainer");
    }
    public BasicContentContainerImpl(ContentManager contentManager, String name){
        super(contentManager, name);
    }
    public BasicContentContainerImpl(ContentManager contentManager, File file){
        super(contentManager, file);
    }

}