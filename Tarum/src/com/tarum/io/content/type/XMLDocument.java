package com.tarum.io.content.type;

import com.tarum.io.content.parser.XMLParser;
import com.tarum.util.FileUtils;
import com.tarum.util.GlueList;
import com.tarum.util.IOUtils;
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
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

public class XMLDocument extends BasicContentContainer implements Serializable {

    private DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder documentBuilder;
    private Document document;

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
    public Document getDocument() {
        return document;
    }
    public void setDocument(Document document) {
        this.document = document;
    }

    public XMLDocument(){
        this(null);
    }
    public XMLDocument (File file){
        super(file);

        init();
    }

    private void init(){
        try {
            this.documentBuilderFactory = documentBuilderFactory.newDefaultInstance();
            this.documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public Document createDocument(){
        this.document = documentBuilder.newDocument();
        document.normalizeDocument();
        return document;
    }
    public void setRootElement (String elementName){
        Element root = document.getDocumentElement();
        if (root == null){
            root = document.createElement(elementName);
        } else {
            document.getDocumentElement().setTextContent(elementName);
        }
    }

    public boolean prepareObject (Object object){
        if (document == null) return false;

        Element rootElement = document.getDocumentElement();
        String rootElementName = rootElement.getNodeName();

        return true;
    }

    public boolean load (){
        return loadFile (getFile());
    }
    public boolean loadFile (File file){
        if (file == null || !file.exists()) return false;
        setFile(file);
        return XMLParser.loadXMLDocumentFile(this, file);
    }
    public boolean export (){
        return export(getFile());
    }
    public boolean export (File file){
        return true;
    }

}
