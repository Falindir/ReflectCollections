package com.company.finder;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by jimmy on 09/11/15.
 */
public class ClassFinder {

    public ClassFinder() {

    }

    public List<Class> getAllClass(Class cl, Boolean filter) { // TODO Pattern Filter
        List<Class> goodClass = new ArrayList<Class>();
        try {
            List<Class> classes = getClassesFromPackage(cl.getPackage().getName());
            if(filter) {
                for(Class c : classes) {
                    if(!c.isLocalClass() && !"".equals(c.getSimpleName()) && !c.isMemberClass()) {
                        goodClass.add(c);
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return goodClass;
    }

    private static List<Class> getClassesFromPackage(String pckgname) throws ClassNotFoundException {
        List<Class> result = new ArrayList<Class>();
        List<File> directories = new ArrayList<File>();
        Map<File, String> packageNames = null;
        try {
            ClassLoader cld = Thread.currentThread().getContextClassLoader();
            if (cld == null) {
                throw new ClassNotFoundException("Can’t get class loader.");
            }
            for (URL jarURL : ((URLClassLoader) Thread.currentThread().getContextClassLoader()).getURLs()) {
                //System.out.println("JAR: " + jarURL.getPath());
                getClassesInSamePackageFromJar(result, pckgname, jarURL.getPath());
                Enumeration<URL> resources = cld.getResources(pckgname);
                File directory = null;
                while (resources.hasMoreElements()) {
                    String path2 = resources.nextElement().getPath();
                    directory = new File(URLDecoder.decode(path2, "UTF-8"));
                    directories.add(directory);
                }
                if (packageNames == null) {
                    packageNames = new HashMap<File, String>();
                }
                packageNames.put(directory, pckgname);
            }
        } catch (NullPointerException x) {
            throw new ClassNotFoundException(pckgname + " does not appear to be a valid package (Null pointer exception)");
        } catch (UnsupportedEncodingException encex) {
            throw new ClassNotFoundException(pckgname + " does not appear to be a valid package (Unsupported encoding)");
        } catch (IOException ioex) {
            throw new ClassNotFoundException("IOException was thrown when trying to get all resources for " + pckgname);
        }
        for (File directory : directories) {
            if (directory.exists()) {
                String[] files = directory.list();
                for (String file : files) {
                    if (file.endsWith(".class")) {
                        try {
                            result.add(Class.forName(packageNames.get(directory).toString() + '.' + file.substring(0, file.length() - 6)));
                        } catch (Throwable ignored) {
                        }
                    }
                }
            } else {
                throw new ClassNotFoundException(pckgname + " (" + directory.getPath() + ") does not appear to be a valid package");
            }
        }
        return result;
    }

    private static void getClassesInSamePackageFromJar(List<Class> result, String packageName, String jarPath) {
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(jarPath);
            Enumeration<JarEntry> en = jarFile.entries();
            while (en.hasMoreElements()) {
                JarEntry entry = en.nextElement();
                String entryName = entry.getName();
                packageName = packageName.replace('.', '/');
                if (entryName != null && entryName.endsWith(".class") && entryName.startsWith(packageName)) {
                    try {

                        Class entryClass = Class.forName(entryName.substring(0, entryName.length()-6).replace('/', '.'));
                        if (entryClass != null) {
                            result.add(entryClass);
                        }
                    } catch (Throwable ignored) {
                    }
                }
            }
        } catch (Exception ignored) {
        } finally {
            try {
                if (jarFile != null) {
                    jarFile.close();
                }
            } catch (Exception ignored) {
            }
        }
    }
}

