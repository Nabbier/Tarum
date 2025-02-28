package com.tarum.io.content.type;
import com.tarum.app.Application;
import com.tarum.io.content.ContentManager;
import com.tarum.util.MathUtils;

import java.io.File;
import java.io.Serializable;

public abstract class BasicContentContainer implements Serializable {

    private transient ContentManager contentManager;

    private long uid;
    private int id;
    private String name = "BasicContentContainer";
    private String filePath;
    private String fileExtension = DEFAULT_FILE_EXTENSION;
    private transient File file;
    private ContentFormat.Type contentFormatType = ContentFormat.Type.XML_DOCUMENT;

    private int contentLength;
    private byte[] content;

    public static final String DEFAULT_FILE_EXTENSION = ".dat";

    public ContentManager getContentManager() {
        return contentManager;
    }
    public void setContentManager(ContentManager contentManager) {
        this.contentManager = contentManager;
    }
    public long getUID() {
        return uid;
    }
    public void setUID(long uid) {
        this.uid = uid;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getFilePath(){
        return this.filePath;
    }
    public void setFilePath (String filePath){
        this.filePath = filePath;
    }
    public String getFileExtension(){
        return this.fileExtension;
    }
    public void setFileExtension (String fileExtension){
        this.fileExtension = fileExtension;
    }
    public File getFile() {
        return new File (filePath);
    }
    public void setFile(File file) {
        this.filePath = file.getAbsolutePath();
    }
    public int getContentLength() {
        return contentLength;
    }
    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }
    public byte[] getContent() {
        return content;
    }
    public void setContent(byte[] content) {
        this.content = content;
    }
    public ContentFormat.Type getContentFormatType(){
        return this.contentFormatType;
    }
    /**
     * TODO: ******
     */
    public void setContentFormat (ContentFormat.Type type){
        this.contentFormatType = type;
    }

    public BasicContentContainer (){
        this(new ContentManager());
    }
    public BasicContentContainer(ContentManager contentManager){
        this(contentManager, "BasicContentContainer");
    }
    public BasicContentContainer(File file){
        this(new ContentManager(), file);
    }
    public BasicContentContainer(ContentManager contentManager, File file){
        this.contentManager = contentManager == null ? new ContentManager() : contentManager;
        if (file != null){
            this.file = file;
        } else {
            this.file = generateOutputFile();
        }
        this.filePath = file != null ? file.getAbsolutePath() : generateOutputFile().getAbsolutePath();
        this.uid = MathUtils.GenerateUID();
    }
    public BasicContentContainer (ContentManager contentManager, String name){
        this.contentManager = contentManager == null ? Application.getDefaultContentManager() : contentManager;
        this.name = name == null ? "BasicContentContainer" : name;
        this.uid = MathUtils.GenerateUID();
        this.file = generateOutputFile();
        this.filePath = file.getAbsolutePath();
    }

    public File generateOutputFile(){
        Class superClass = this.getClass().getSuperclass();
        String parentClass = null;
        String dirPath = getContentManager().getContentDirectory();

        if (superClass != null){
            parentClass = this.getClass().getSuperclass().getSimpleName();
        }
        if (parentClass != null){
            dirPath += parentClass +"/";
        }

        File dir = new File (dirPath);
        String filePath = dir.getAbsolutePath();
        String fileName = null;

        if (getName() == null){
            fileName = this.getClass().getSuperclass().getSimpleName();
            setName(fileName);
        } else {
            fileName = getName();
        }

        if (getFileExtension() == null){
            setFileExtension(DEFAULT_FILE_EXTENSION);
        }

        if (!dir.exists()){
            dir.mkdirs();
        } else {
            int fileCount = dir.listFiles().length;
            if (fileCount > 0){
                fileName += "_";
            }
        }
        fileName += getFileExtension();
        return this.file = new File (filePath, fileName);
    }

    /**
     * IMPORTATION
     */
    public boolean load(){
        return loadContainerFile(getFile());
    }
    public boolean loadContainerFile (File file){
        if (file != null && file.exists()){
            setFile(file);
        }
        return performContentImportation(getFile());
    }
    public boolean performContentImportation (File file){
        if (file == null || !file.exists()) return false;

        onContentImportationStarted(file);

        boolean result = contentManager.loadContentContainerFile(file, this) != null;
        onContentImportationCompleted(file);
        return result;
    }

    public void onContentImportationStarted(File file){

    }
    public void onContentImportationCompleted (File file){
    }

    /**
     * EXPORTATION
     */
    public boolean export(){
        return export(getFile());
    }
    public boolean export (File file){
        return performContentExportation(file);
    }
    public boolean performContentExportation (File file){
        if (file != null) {
            setFile(file);
        }
        if (getFile() == null){
            setFile(generateOutputFile());
        }

        boolean result = contentManager.exportContentContainer(this);

        onContentExportationCompleted(getFile());

        return result;
    }

    public void onContentExportationCompleted (File file){

    }

}
