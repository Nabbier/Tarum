package com.tarum.util;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Base64;

public class IOUtils {

    public static boolean doesObjectContainField(Object object, String fieldName) {
        Class<?> objectClass = object.getClass();
        for (Field field : objectClass.getFields()) {
            if (field.getName().equals(fieldName)) {
                return true;
            }
        }
        return false;
    }

    public static final byte[] SerializeObject (Object object){
        if (object == null) return null;

        ObjectOutputStream oos = null;
        ByteArrayOutputStream bos = null;
        byte[] result = null;

        try{
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);

            oos.writeObject(object);
            result = bos.toByteArray();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            return result;
        }
    }
    public static final String SerializeObjectToString (Object object){
        if (object == null) return null;

        String result = null;
        ObjectOutputStream objectOutputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try{
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if (objectOutputStream != null){
                try {
                    objectOutputStream.close();
                } catch (IOException e){
                    e.printStackTrace();
                } finally {
                    result = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
                }
            }
        }

        return result;
    }

    public static String ConvertFieldToString (Object target){
        if (target == null) return null;
        String result = null;

        if (target.getClass().equals(String.class)){
            result = (String) target;
        } else if (target.getClass().equals(Integer.class)){
            int data = (int) target;
            result = String.valueOf(data);
        } else if (target.getClass().equals(Long.class)){
            long data = (long) target;
            result = String.valueOf(data);
        } else if (target.getClass().equals(Byte.class)){
            byte[] data = (byte[]) target;
            result = new String(data);
        } else if (target instanceof Serializable){
            result = IOUtils.SerializeObjectToString(target);
        }

        return result;
    }

    public static final Object DeserializeObject (String serializedObject){
        if (serializedObject == null) return null;
        return DeserializeObject(serializedObject.getBytes());
    }
    public static final Object DeserializeObject (byte[] serializedObject){
        if (serializedObject == null) return null;

        Object result = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(serializedObject);
            ObjectInputStream ois = new ObjectInputStream(bis);

            result = ois.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * TODO: IMPLEMENT READING OF THE FILE USING A 'CONTENTFORMAT' TO DETERMINE WHAT CHARACTERS DEFINE
     * A FILE HEADER, IF NO 'CONTENTFORMAT' IS SPECIFIED USE A DEFAULT 'CONTENTFORMAT'
     * @param file
     * @return
     */
    public static final GlueList<String> loadFileHeader (File file){
        if (file == null) return null;

        GlueList<String> fileContent = new GlueList<>();

        BufferedReader reader = null;
        String ln = null;

        try{
            reader = new BufferedReader(new FileReader(file));

            while ((ln = reader.readLine()) != null){
                //
                if (ln.replaceAll(" ", "") == "}"){
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        return fileContent;
    }

    public static final GlueList<String> LoadFileContent (File file){
        GlueList<String> result = new GlueList<>();
        if (file == null || !file.exists()) return result;

        BufferedReader reader = null;
        FileReader fileReader = null;
        String ln = null;

        try{
            fileReader = new FileReader(file);
            reader = new BufferedReader(fileReader);

            while ((ln = reader.readLine()) != null){
                result.add(ln);
            }

        } catch (IOException e){
            e.printStackTrace();
        } finally{
            try{
                if (reader != null){
                    reader.close();
                }
                if (fileReader != null){
                    fileReader.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            return result;
        }
    }

}
