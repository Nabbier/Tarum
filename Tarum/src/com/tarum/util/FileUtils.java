package com.tarum.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class FileUtils {

    public static final String getFileName (File file){
        if (file == null) return null;

        String fileName = file.getName();
        int pos = fileName.lastIndexOf(".");
        if (pos > 0 && pos < (fileName.length() - 1)) { // If '.' is not the first or last character.
            fileName = fileName.substring(0, pos);
        }

        return fileName;
    }
    public static final String getFileExtension (File file){
        if (file == null) return null;

        String fileExtension = file.getName();
        int pos = fileExtension.lastIndexOf(".");
        if (pos > 0 && pos < (fileExtension.length() - 1)) { // If '.' is not the first or last character.
            fileExtension = fileExtension.substring(pos, fileExtension.length());
        }
        return fileExtension;
    }

    public static final boolean doesPackageContainClass (String packageName, String className){
        if (packageName == null || className == null) return false;
        GlueList<Class<?>> classList = getClassesInPackage(packageName);
        for (Class c : classList){
            if (c.getName().equalsIgnoreCase(className)) return true;
        }
        return false;
    }
    public static final GlueList<Class<?>> getClassesInPackage(String packageName) {
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
