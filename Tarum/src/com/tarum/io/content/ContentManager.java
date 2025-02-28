package com.tarum.io.content;

import com.tarum.app.Application;
import com.tarum.io.content.parser.XMLParser;
import com.tarum.io.content.type.*;
import com.tarum.util.FileUtils;
import com.tarum.util.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ContentManager {

    private transient Application application;
    private String directory = DEFAULT_DIRECTORY;
    private String contentDirectory = DEFAULT_CONTENT_CONTAINER_DIRECTORY;
    private String configDirectory = DEFAULT_CONFIG_DIRECTORY;

    private transient Logger logger;

    private transient DefinitionManager definitionManager;

    private ContentMap contentFormatMap;
    public static final String DEFAULT_DIRECTORY = Application.getDefaultDirectory() +"content/";
    public static final String DEFAULT_CONTENT_CONTAINER_DIRECTORY = DEFAULT_DIRECTORY+"containers/";
    public static final String DEFAULT_CONFIG_DIRECTORY = DEFAULT_DIRECTORY + "config/";
    private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();
    public Application getApplication(){
        return this.application;
    }
    public void setApplication (Application application){
        this.application = application;
    }
    public String getDirectory() {
        return directory;
    }
    public void setDirectory(String directory) {
        this.directory = directory;
    }
    public String getContentDirectory() {
        return contentDirectory;
    }
    public void setContentDirectory(String contentDirectory) {
        this.contentDirectory = contentDirectory;
    }
    public String getConfigDirectory() {
        return configDirectory;
    }
    public void setConfigDirectory(String configDirectory) {
        this.configDirectory = configDirectory;
    }

    public Logger getLogger(){
        return this.logger;
    }
    public void setLogger (Logger logger){
        this.logger = logger;
    }

    public DefinitionManager getDefinitionManager() {
        return definitionManager;
    }
    public void setDefinitionManager(DefinitionManager definitionManager) {
        this.definitionManager = definitionManager;
    }

    public ContentManager (){
        this(null);
    }
    public ContentManager (String directory){
        this.directory = directory;

        initialize();
    }
    private void initialize(){
        this.logger = new Logger(Application.getDefaultDirectory() + "logs/ContentManager/");
        logger.start();

        File contentFormatDirectory = new File (getContentDirectory()+"content_format/");
        this.contentFormatMap = new ContentMapImpl(this, "content_format_map");

        this.definitionManager = new DefinitionManager(this);
    }

    public static boolean isWrapperType(Class<?> clazz)
    {
        return WRAPPER_TYPES.contains(clazz);
    }
    private static Set<Class<?>> getWrapperTypes() {
        Set<Class<?>> ret = new HashSet<Class<?>>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        return ret;
    }

    public ContentContainer createContentContainer (String name){
        ContentContainerImpl container = new ContentContainerImpl(this, name);

        File file = new File (getContentDirectory(), name + BasicContentContainer.DEFAULT_FILE_EXTENSION);
        container.setFile(file);

        return container;
    }

    public BasicContentContainer loadContentContainerFile (File file){
        if (file == null || !file.exists()) return null;

        XMLParser parser = new XMLParser (file);

        XMLDocument xmlDocument = parser.loadFile(file);
        Document document = xmlDocument.getDocument();
        Element rootElement = document.getDocumentElement();
        String documentName = rootElement.getNodeName();

        // TODO: **
        ContentContainerImpl container = new ContentContainerImpl(this, documentName);

        

        return container;
    }
    public BasicContentContainer loadContentContainerFile (File file, BasicContentContainer target){
        return null;
    }

    public boolean exportContentContainer (BasicContentContainer container){
        log ("ContentManager.exportContentContainer(BasicContentContainer): Exporting contents from container " +
                "(name: " + container.getName() + ", file_path: "+ container.getFile().getAbsolutePath() + ")");
        XMLParser parser = new XMLParser(container.getFile());
        XMLDocument document = null;

        try {
            document = parser.createXMLDocumentFromObject (container);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        log ("Successfully parsed object into a valid XML document (file_path:" + container.getFile().getAbsolutePath()  +")!");

        boolean containerExportationResult = document.export();

        if (containerExportationResult){
            log("Successfully exported content container to the specified XML document!");
        } else {
            logError("Failed to export content container to the specified XML document!");
        }

        return containerExportationResult;
    }

    public void log (String logEntryText){
        getLogger().logLine("ContentManager: " + logEntryText);
    }
    public void logError (String logEntryText){
        getLogger().logError("ContentManager: " + logEntryText);
    }

}
