package com.tarum.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class FileUtils {

    public static String getFileName (File file){
        if (file == null) return null;

        String fileName = file.getName();
        int pos = fileName.lastIndexOf(".");
        if (pos > 0 && pos < (fileName.length() - 1)) { // If '.' is not the first or last character.
            fileName = fileName.substring(0, pos);
        }

        return fileName;
    }
    public static String getFileExtension (File file){
        if (file == null) return null;

        String fileExtension = file.getName();
        int pos = fileExtension.lastIndexOf(".");
        if (pos > 0 && pos < (fileExtension.length() - 1)) { // If '.' is not the first or last character.
            fileExtension = fileExtension.substring(pos, fileExtension.length());
        }
        return fileExtension;
    }

    public static List<File> GetAllDirectories (String targetDir){
        List<File> fileList = new ArrayList<>();

        File dir = new File (targetDir);

        if (!dir.exists()){
            return fileList;
        }

        File[] files = dir.listFiles();

        for (File file : files){
            if (file.isDirectory()){
                fileList.add(file);
            }
        }

        return fileList;
    }
    public static List<File> GetAllFiles (String targetDir){
        List<File> fileList = new ArrayList<>();

        File dir = new File (targetDir);

        if (!dir.exists()){
            return fileList;
        }

        File[] files = dir.listFiles();

        for (File file : files){
            fileList.add(file);
        }

        return fileList;
    }

    /**
     * TODO: ANALYZE OPERATIONS ONCE MORE - GLUELIST IS RETURNING ERRORS WITH THE T[] ELEMENT ARRAY
     * AND THE 'ADD' FUNCTION
     * @param targetDir
     * @return
     */
    public static List<File> GetAllFilesRecursively (String targetDir){
        ArrayList<File> fileList = new ArrayList<>();
        if (targetDir == null || !new File(targetDir).exists()) return fileList;

        File[] files = new File (targetDir).listFiles();

        if (files == null) return fileList;

        for (File file : files){
            if (file.isDirectory()){
                List<File> subFiles = GetAllFilesRecursively(file.getAbsolutePath());
                if (!subFiles.isEmpty()) {
                    fileList.addAll(subFiles);
                }
            } else {
                fileList.add(file);
            }
        }

        return fileList;
    }

    public static byte[] RetrieveFileData (File targetFile){
        if (targetFile == null || !targetFile.exists()) return null;

        /**
         * CHECK FILE PERMISSIONS
         */
        if (!targetFile.canRead()){
            return null;
        }

        byte[] result = new byte[(int) targetFile.length()];
        FileInputStream fis = null;
        int readCount = 0;
        try {
            fis = new FileInputStream(targetFile);
            readCount = fis.read(result);

            // TODO: UTILIZE A LOOP AND THE READ() FUNCTION, CYCLE UNTIL THE READCOUNT IS CORRECT
            //  OR UNTIL READ() REPORTS '-1', I BELIEVE.
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    public static boolean WriteFile (byte[] fileData, File targetDest){
        if (targetDest == null) return false;

        File dir = targetDest.getParentFile();

        /**
         * TODO: ***************
         */
        System.out.println("FileUtils.WriteFile(byte[] fileData, File targetDest): targetDest.getParentFile() == " + dir.getAbsolutePath());
        if (!dir.exists()) dir.mkdirs();

        /**
         * CHECK FILE PERMISSIONS
         */
        if (!targetDest.canWrite()){
            return false;
        }

        if (targetDest.exists()){
            targetDest.delete();
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(targetDest);
            fos.write(fileData);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return (targetDest.exists() && ((int) targetDest.length() >= fileData.length));
    }
    public static boolean CopyFile (File originalFile, String targetDirectory){
        if (originalFile == null || !originalFile.exists() || targetDirectory == null) return false;

        File dir = new File (targetDirectory);
        if (!dir.exists()) dir.mkdirs();

        File targetFile = new File (targetDirectory, originalFile.getName());

        /**
         * TODO: CHECK FILE PERMISSIONS AGAIN TO AVOID BEGINNING THE EXECUTION OF ANTOHER FUNCTION
         * UNNECESSARILY
         */

        byte[] fileData = RetrieveFileData(originalFile);

        if (fileData == null){
            System.out.println("FileUtils.CopyFile(File originaFile, String targetDirectory): Failed to retrieve file data" +
                    "fileData = RetrieveFileData (originalFile) RETURNS NULL");
            return false;
        }

        return WriteFile(fileData, targetFile);
    }

    public static boolean doesPackageContainClass (String packageName, String className){
        if (packageName == null || className == null) return false;
        GlueList<Class<?>> classList = getClassesInPackage(packageName);
        for (Class c : classList){
            if (c.getName().equalsIgnoreCase(className)) return true;
        }
        return false;
    }
    public static GlueList<Class<?>> getClassesInPackage(String packageName) {
        if (packageName == null) return null;

        String path = null;

        if (packageName.contains("\\.")){
            path = packageName.replaceAll("\\.", File.separator);
        }

        GlueList<Class<?>> classes = new GlueList<>();
        String[] classPathEntries = System.getProperty("java.class.path").split(System.getProperty("path.separator"));

        String name;
        for (String classpathEntry : classPathEntries) {
            if (classpathEntry.endsWith(".jar")) {
                File jar = new File(classpathEntry);
                try {
                    JarInputStream is = new JarInputStream(new FileInputStream(jar));
                    JarEntry entry;
                    while((entry = is.getNextJarEntry()) != null) {
                        name = entry.getName();
                        if (name.endsWith(".class")) {
                            if (name.contains(path) && name.endsWith(".class")) {
                                String classPath = name.substring(0, entry.getName().length() - 6);
                                classPath = classPath.replaceAll("[\\|/]", ".");
                                classes.add(Class.forName(classPath));
                            }
                        }
                    }
                } catch (Exception ex) {
                    // Silence is gold
                }
            } else {
                try {
                    File base = new File(classpathEntry + File.separatorChar + path);
                    for (File file : base.listFiles()) {
                        name = file.getName();
                        if (name.endsWith(".class")) {
                            name = name.substring(0, name.length() - 6);
                            classes.add(Class.forName(packageName + "." + name));
                        }
                    }
                } catch (Exception ex) {
                    // Silence is gold
                }
            }
        }

        return classes;
    }

}
