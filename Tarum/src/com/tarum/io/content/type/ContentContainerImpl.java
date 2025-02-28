package com.tarum.io.content.type;

import com.tarum.io.content.ContentManager;

import java.io.File;
import java.io.Serializable;

public class ContentContainerImpl extends ContentContainer implements Serializable{

    public ContentContainerImpl(ContentManager contentManager) {
        super(contentManager);
    }
    public ContentContainerImpl(ContentManager contentManager, File file) {
        super(contentManager, file);
    }
    public ContentContainerImpl(ContentManager contentManager, String name) {
        super(contentManager, name);
    }

}
