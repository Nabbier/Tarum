package com.tarum.io.content.parser;

import com.tarum.app.Application;
import com.tarum.io.content.type.XMLDocument;
import com.tarum.util.FileUtils;
import com.tarum.util.GlueList;
import com.tarum.util.IOUtils;
import com.tarum.util.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

public class XMLParser {

    private XMLDocument xmlDocument;
    private Logger logger;

    private boolean enableLogging = true;

    private DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder documentBuilder;

    public XMLDocument getXMLDocument() {
        return xmlDocument;
    }
    public void setXMLDocument(XMLDocument xmlDocument) {
        this.xmlDocument = xmlDocument;
    }
    public Logger getLogger(){
        return this.logger;
    }
    public void setLogger (Logger logger){
        this.logger = logger;
    }
    public boolean isLoggingEnabled() {
        return enableLogging;
    }
    public void setLoggingEnabled(boolean enableLogging) {
        this.enableLogging = enableLogging;
    }
    public Logger enableLogging(){
        return enableLogging(Application.getDefaultDirectory()+"logs/xml/");
    }
    public Logger enableLogging(String logsDirectory){
        this.logger = new Logger(logsDirectory);
        logger.start();
        return logger;
    }
    public DocumentBuilderFactory getDocumentBuilderFactory() {
        return documentBuilderFactory;
    }
    public void setDocumentBuilderFactory(DocumentBuilderFactory documentBuilderFactory) {
        this.documentBuilderFactory = documentBuilderFactory;
    }
    public DocumentBuilder getDocumentBuilder() {
        return documentBuilder;
    }
    public void setDocumentBuilder(DocumentBuilder documentBuilder) {
        this.documentBuilder = documentBuilder;
    }

    public XMLParser(){
        this(null);
    }
    public XMLParser(File file){
        this.xmlDocument = new XMLDocument(file);

        init();
    }

    private void init(){
        this.documentBuilderFactory = DocumentBuilderFactory.newDefaultInstance();
        try {
            this.documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * TODO: IMPLEMENT A METHOD TO DO A THOROUGH XML FILE CHECK,
     *  THIS METHOD ONLY CHECKS THE FILE EXTENSION
     * @param file - The specifiable file on the local filesystem to check
     * @return
     */
    public static boolean isValidXMLFile (File file){
        if (file == null || !file.exists()) return false;
        return FileUtils.getFileExtension(file).equalsIgnoreCase(".xml");
    }

    public XMLDocument createXMLDocumentFromObject (Object object) throws IllegalAccessException {
        return prepareXMLDocumentFromObject(object);
    }
    public XMLDocument prepareXMLDocumentFromObject (Object object) throws IllegalAccessException {
        return prepareXMLDocumentFromObject(object, null);
    }
    public XMLDocument prepareXMLDocumentFromObject (Object object, String documentName) throws IllegalAccessException {
        if (xmlDocument == null){
            xmlDocument = new XMLDocument();
        }

        Document document = xmlDocument.createDocument();
        String rootElementName = documentName == null ? object.getClass().getSimpleName().toString() : documentName;

        /**
         * TODO: USING REFLECTION CHECK THE CONTENT.TYPE.* PACKAGE AND CHECK IF THE
         * CLASS NAME MATCHES ANY OF THE CLASSES IN THAT PACKAGE, IF NOT - CHECK THE SUPER CLASS
         * OF THE OBJECT AND CHECK AGAIN
         */

        System.out.println("XMLDocument.prepareDocument(Object): Preparing a new XML document for the specified object " +
                "(" + rootElementName + ")..");
        Element rootElement = document.createElement(rootElementName);

        if (object.getClass().getSuperclass() != null) {
            rootElement.setAttribute("contentType", object.getClass().getSuperclass().getName());

            /**
             * TODO: IMPLEMENT A PROCEDURE TO CHECK IF THE OBJECT THE XML DOCUMENT IS BEING PREPARED FOR
             * IS A CONTENT_TYPE FILE SO THAT WE CAN
             */
//            if (FileUtils.doesPackageContainClass("com.tarum.io.content.type", object.getClass().getSuperclass().getName())){
//            }
        }

        document.appendChild(rootElement);

        Field[] fields = object.getClass().getDeclaredFields();

        if (fields == null){
            return null;
        }

        generateNodeInformation (document, object, object.getClass());
        if (object.getClass().getSuperclass() != null){
            generateNodeInformation(document, object, object.getClass().getSuperclass());

            // TODO: CONTINUE TO CHECK FOR SUPERCLASSES IN A LOOP UNTIL ALL OF THEM HAVE HAD THEIR FIELD
            // INFORMATION TURNED INTO NODE ELEMENTS FOR OUR XML DOCUMENT
        }

        xmlDocument.setDocument(document);
        return xmlDocument;
    }

    private void generateNodeInformation(Document document, Object object, Class targetClass) throws IllegalAccessException {
        Element rootElement = document.getDocumentElement();
        Field[] fields = targetClass.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Class fieldType = field.getType();

            Element element = document.createElement(field.getName());
            rootElement.appendChild(element);
            element.setAttribute("Type", fieldType.getName());

            if (fieldType.isArray()) {
                element.setAttribute("FieldType", "Array");

                if (fieldType.isPrimitive()) {
                    if (fieldType.equals(String.class)) {
                        String[] val = (String[]) field.get(object);
                        for (String entry : val){
                            Node node = document.createTextNode(entry);
                            element.appendChild(node);
                        }
                    } else if (fieldType.equals(Integer.class)) {
                        int[] val = (int[]) field.get(object);
                        for (int entry : val){
                            Node node = document.createTextNode(String.valueOf(entry));
                            element.appendChild(node);
                        }
                    } else if (fieldType.equals(Long.class)) {
                        long[] val = (long[]) field.get(object);
                        for (long entry : val){
                            Node node = document.createTextNode(String.valueOf(entry));
                            element.appendChild(node);
                        }
                    } else if (fieldType.equals(Byte.class)) {
                        byte[] val = (byte[]) field.get(object);
                        String converted = new String(val);
                        Node node = document.createTextNode(converted);
                        element.appendChild(node);
                    }
                } else {
                    Object[] val = (Object[]) field.get(object);

                    int indexPointer = 0;
                    for (int i = 0; i < val.length; i++){
                        Object obj = val[i];

                        if (obj instanceof Serializable) {
                            String serialized = IOUtils.SerializeObjectToString(obj);
                            Element fieldArrayEntryElement = document.createElement(field.getName()+"Entry");
                            fieldArrayEntryElement.setAttribute("index", String.valueOf(indexPointer));
                            element.appendChild(fieldArrayEntryElement);
                            Node node = document.createTextNode(serialized);
                            fieldArrayEntryElement.appendChild(node);
                            indexPointer++;
                        }
                    }
                }
            } else if (field.getType().getSuperclass().equals(AbstractMap.class)){
                element.setAttribute("FieldType", "AbstractMap");

                HashMap map = (HashMap) field.get(object);
                Iterator i = map.entrySet().iterator();

                while (i.hasNext()){
                    Map.Entry entry = (Map.Entry) i.next();
                    Object key = entry.getKey();
                    Object value = entry.getValue();

                    String keyType = key.getClass().getName();
                    String valueType = value.getClass().getName();

                    String serializedKey = null;
                    String serializedValue = null;

                    // SERIALIZE MAP ENTRY KEY
                    serializedKey = IOUtils.ConvertFieldToString(key);

                    // SERIALIZE MAP ENTRY VALUE
                    serializedValue = IOUtils.ConvertFieldToString(value);

                    System.out.println("AbstractMap map entry : " + serializedKey + " : " + serializedValue);

                    Element mapEntryElement = document.createElement(field.getName()+"Entry");
                    mapEntryElement.setAttribute("key", serializedKey);

                    // APPEND OUR NODE THAT REPRESENTS THE MAP ENTRY TO OUR MAP ELEMENT
                    element.appendChild(mapEntryElement);

                    Node mapEntryValueNode = document.createTextNode(serializedValue);
                    mapEntryElement.appendChild(mapEntryValueNode);
                }

            } else if (field.getType().getSuperclass().equals(List.class)) {
                GlueList list = (GlueList) field.get(object);
                element.setAttribute("FieldType", "List");

                if (list.isEmpty()) continue;

                int indexPointer = 0;
                for (int i = 0; i < list.size(); i++){
                    Object value = list.get(i);
                    String serializedValue = IOUtils.ConvertFieldToString(value);

                    if (serializedValue == null) continue;

                    Element listEntryElement = document.createElement(field.getName()+"Entry");
                    listEntryElement.setAttribute("index", String.valueOf(indexPointer));
                    Node listEntryValueNode = document.createTextNode(serializedValue);
                    listEntryElement.appendChild(listEntryValueNode);
                    indexPointer++;
                }
            } else {
                if (fieldType.isPrimitive()) {
                    if (fieldType.equals(String.class)) {
                        String[] val = (String[]) field.get(object);
                        for (String entry : val) {
                            Node node = document.createTextNode(entry);
                            element.appendChild(node);
                        }
                    } else if (fieldType.equals(Integer.class)) {
                        int[] val = (int[]) field.get(object);
                        for (int entry : val) {
                            Node node = document.createTextNode(String.valueOf(entry));
                            element.appendChild(node);
                        }
                    } else if (fieldType.equals(Long.class)) {
                        long[] val = (long[]) field.get(object);
                        for (long entry : val) {
                            Node node = document.createTextNode(String.valueOf(entry));
                            element.appendChild(node);
                        }
                    } else if (fieldType.equals(Byte.class)) {
                        byte[] val = (byte[]) field.get(object);
                        String converted = new String(val);
                        Node node = document.createTextNode(converted);
                        element.appendChild(node);
                    }
                } else {
                    Object o = field.get(object);

                    if (o instanceof Serializable) {
                        String serialized = IOUtils.SerializeObjectToString(o);
                        element.setAttribute("serialized", "true");
                        Node node = document.createTextNode(serialized);
                        element.appendChild(node);
                    }
                }
            }
        }

    }

    public Object parseXMLDocumentToObject (Object target){
        if (xmlDocument == null || xmlDocument.getDocument() == null) return null;

        Document document = xmlDocument.getDocument();

        Element rootElement = document.getDocumentElement();

        if (rootElement == null) return null;

        return target;
    }

    public static boolean loadXMLDocumentFile (XMLDocument target, File xmlDocumentFile){

        return true;
    }
    public XMLDocument loadFile(){
        if (getXMLDocument() == null) return null;
        return loadFile(getXMLDocument().getFile());
    }
    public XMLDocument loadFile (File file){
        if (file == null || !file.exists()) return null;

        this.xmlDocument = new XMLDocument(file);
        try {
            xmlDocument.setDocument(documentBuilder.parse(file));
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this.xmlDocument;
    }

    public boolean export (){
        if (xmlDocument == null) return false;
        return export(xmlDocument.getFile());
    }
    public boolean export (File file){
        return exportXMLDocument(xmlDocument, file);
    }
    public static boolean exportXMLDocument (XMLDocument xmlDocument, File dest){
        if ((dest == null && xmlDocument == null) || (dest == null && xmlDocument.getFile() == null)) return false;

        if (dest != null){
            xmlDocument.setFile(dest);
        }

        if (xmlDocument == null || xmlDocument.getDocument() == null){
            return false;
        }

        System.out.println("XMLDocument.export(File): Exporting XML document to the local filesystem" +
                "(file_path:" + dest.getAbsolutePath() + ")..");

        Transformer transformer = null;
        DOMSource source = null;
        StreamResult result = null;

        File dir = new File (xmlDocument.getFile().getParent());
        if (!dir.exists()){
            dir.mkdirs();
        }

        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            source = new DOMSource(xmlDocument.getDocument());
            result = new StreamResult(dest);
            transformer.transform(source, result);

            long fileLength = dest.length();

            System.out.println("XMLDocument.export(File): Successfully exported XML document to the local filesystem" +
                    "(file_path:" + dest.getAbsolutePath() + ")!");
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        } finally {
            return dest.exists();
        }
    }

}
