package com.tarum.io.content.type;

import com.tarum.io.content.ContentManager;

import java.io.File;
import java.io.Serializable;

public class ContentMapImpl extends ContentMap implements Serializable {

    public ContentMapImpl(ContentManager contentManager) {
        super(contentManager);
    }
    public ContentMapImpl(ContentManager contentManager, File file) {
        super(contentManager, file);
    }
    public ContentMapImpl(ContentManager contentManager, String name) {
        super(contentManager, name);
    }

}
