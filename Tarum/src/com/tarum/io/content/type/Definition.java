package com.tarum.io.content.type;
import com.tarum.io.content.ContentManager;
import com.tarum.io.content.DefinitionManager;

import java.io.File;
import java.io.Serializable;

public class Definition extends ContentMap implements Serializable {

    private DefinitionManager definitionManager;

    public Definition(DefinitionManager definitionManager) {
        this(definitionManager, "Definition");
    }
    public Definition(DefinitionManager definitionManager, File file) {
        super(definitionManager.getContentManager(), file);
    }
    public Definition(DefinitionManager definitionManager, String name) {
        super(definitionManager.getContentManager(), name);
    }

    @Override
    public void onContentImportationCompleted(File file){

    }
    @Override
    public void onContentExportationCompleted(File file){

    }

}