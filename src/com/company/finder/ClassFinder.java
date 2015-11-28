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
 * Permet de récupérer l'ensemble des classes contenues dans un package
 */
public class ClassFinder {

    public ClassFinder() {
    }

    /**
     * Permet pour une classe donnée de trouver l'ensemble de
     * toutes les classes contenues dans le même package (et sous packages)
     * @param cl Class : la classe que l'on utilise pour avoir notre package
     * @param filter Boolean : si on veut filtrer les classes retournées
     * @return List<Class>
     */
    public List<Class> getAllClass(Class cl, Boolean filter) {
        List<Class> goodClass = new ArrayList<Class>();
        try {
            List<Class> classes = getClassesFromPackage(cl.getPackage().getName());
            if(filter) {
                for (Class c : classes)
                    if (acceptClass(c))
                        goodClass.add(c);
            }
            else
                goodClass.addAll(classes);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return goodClass;
    }


    /**
     * Permet de savoir si une classe respecte les critères que l'on veut pour
     * quelle soit acceptée
     *  + pas une class local
     *  + pas une class member
     *  + la class doit avoir un simpleName
     * @param c : La classe à tester
     * @return boolean : true si la classe est acceptée, false sinon
     */
    private boolean acceptClass(Class c) {
        return !c.isLocalClass() && !c.isMemberClass() && !"".equals(c.getSimpleName());
    }

    /**
     * Permet d'avoir les classes contenues dans le package donné
     * @param pckgname String : le nom du package que l'on veut explorer
     * @return List<Class>
     * @throws ClassNotFoundException
     */
    private List<Class> getClassesFromPackage(String pckgname) throws ClassNotFoundException {
        List<Class> result = new ArrayList<Class>();
        List<File> directories = new ArrayList<File>();
        Map<File, String> packageNames = null;
        try {
            ClassLoader cld = Thread.currentThread().getContextClassLoader();
            if (cld == null) {
                throw new ClassNotFoundException("Can’t get class loader.");
            }
            for (URL jarURL : ((URLClassLoader) Thread.currentThread().getContextClassLoader()).getURLs()) {
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

    /**
     * Permet d'avoir l'ensemble des classes contenues dans le package donné
     * @param result List<Class> la liste des classes à laquelle on va ajouter les classes trouvées
     * @param packageName String : le chemin vers le package
     * @param jarPath String : le chemin vers le jar
     */
    private void getClassesInSamePackageFromJar(List<Class> result, String packageName, String jarPath) {
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
