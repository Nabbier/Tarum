package com.tarum.io.content.type;

import com.tarum.io.content.ContentManager;
import com.tarum.util.MathUtils;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

public abstract class ContentMap extends ContentContainer implements Serializable {

    private transient HashMap<Long, Object> dataMap = new HashMap<>();
    private transient HashMap<String, Long> keyMap = new HashMap<>();

    private Class dataType;

    public HashMap<Long, Object> getDataMap() {
        return dataMap;
    }
    public void setDataMap(HashMap<Long, Object> dataMap) {
        this.dataMap = dataMap;
    }
    public HashMap<String, Long> getKeyMap() {
        return keyMap;
    }
    public void setKeyMap(HashMap<String, Long> keyMap) {
        this.keyMap = keyMap;
    }
    public Class getDataType() {
        return dataType;
    }
    public void setDataType(Class dataType) {
        this.dataType = dataType;
    }

    public ContentMap (ContentManager contentManager){
        this(contentManager, "ContentMap");
    }
    public ContentMap (ContentManager contentManager, String name){
        super(contentManager, name);
    }
    public ContentMap (ContentManager contentManager, File file){
        super(contentManager, file);
    }

    public Object get (long entryKey){
        if (entryKey < 0) return null;
        if (!dataMap.containsKey(entryKey)) return null;
        return dataMap.get(entryKey);
    }
    public boolean set (long entryKey, Object entry){
        if (entryKey < 0 || entry == null) return false;
        this.dataMap.put(entryKey, entry);
        return true;
    }
    public boolean put (long entryKey, Object entry){
        if (entryKey < 0 || entry == null) return false;
        this.dataMap.put(entryKey, entry);
        return true;
    }
    public long add (Object entry){
        if (entry == null) return -1;
        long uid = MathUtils.GenerateUID();
        dataMap.put(uid, entry);
        return uid;
    }

    public void clear(){
        if (dataMap == null) return;
        dataMap.clear();
    }
    public void clearKeys(){
        if (keyMap == null) return;
        keyMap.clear();
    }
    public void clearAll(){
        dataMap.clear();
        keyMap.clear();
    }

    public boolean isEmpty(){
        if (dataMap == null) return true;
        return dataMap.isEmpty();
    }



}
