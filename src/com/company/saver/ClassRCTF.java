package com.company.saver;

import com.company.graph.DiGraph;
import com.company.inspector.Inspector;
import com.company.inspector.filter.field.FieldFilter;
import com.company.inspector.filter.method.MethodFilter;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Permet de créer un fichier au format rctf
 * contenant une table regroupant les classes et leur propriétées
 */
public class ClassRCTF {

    /**
     * la liste des propriétées de toutes nos classes
     */
    private List<String> CharacteristicList = new ArrayList<String>();

    /**
     * la table des propriétées
     */
    private Boolean[][] table;

    /**
     * la liste de classes
     */
    private List<Class> classes = new ArrayList<Class>();

    /**
     * les filtres sur les method
     */
    private MethodFilter[] filtersM;

    /**
     * les filtres sur les attributs
     */
    private FieldFilter[] filtersF;

    /**
     * la structure d'arbre
     */
    private DiGraph g;

    /**
     * indique si pour les méthodes de la classe on veut la signature ou juste son nom
     */
    private boolean signature;

    /**
     * l'inspecteur de classe
     */
    private Inspector inspector = new Inspector();

    public ClassRCTF(List<Class> classes, MethodFilter[] filtersM, FieldFilter[] filtersF, DiGraph g, boolean signature) {
        this.classes = classes;
        this.filtersM = filtersM;
        this.filtersF = filtersF;
        this.g = g;
        this.signature = signature;
    }

    /**
     * Permet d'initialisé notre table de propriété celon la liste des classes
     */
    public void initTable() {
        for(Class c : classes) { // on recup l'ensemble de toutes les propriétés de toutes les classes
            List<String> temp = getPropertyNameOfClass(c);
            for(String s : temp)
                if(!CharacteristicList.contains(s))
                    CharacteristicList.add(s);
        }

        table = new Boolean[classes.size()][CharacteristicList.size()];

        for (int i=0; i< table.length; i++)
            for (int j=0; j< table[0].length; j++)
                table[i][j] = false;

        for (Class cl : classes) {
            for (String ca:CharacteristicList){
                if (haveThisProperty(cl, ca)) // si la classe possède cette propriété
                    table[classes.indexOf(cl)][CharacteristicList.indexOf(ca)] = true;
            }
        }
    }

    /**
     * Permet de savoir si une classes possède une propriété donné
     */
    private boolean haveThisProperty(Class c, String property) {

        for(String s : getPropertyNameOfClass(c))
            if(property.equals(s))
                return true;

        return false;
    }
    
    private String getMethodSignature(Method m) {
        String name = m.getName();
        String returnType = m.getReturnType().getName();
        List<String> parametersType = new ArrayList<String>();
        
        for (Class parameterType : m.getParameterTypes()) {
        	parametersType.add(parameterType.getName());
        }
        
    	return returnType + " " + name + parametersType.toString();
    }

    /**
     * Permet de récuperer pour une classe donnée l'ensemble de nom ses propriétés
     */
    private List<String> getPropertyNameOfClass(Class c) {

        List<String> results = new ArrayList<String>();

        List<Method> tempMethods = new ArrayList<Method>();
        tempMethods.addAll(inspector.inspectAllMethods(c, filtersM, g));
        for (Method m : tempMethods) {
            String methodString;
            if(signature)
                methodString = getMethodSignature(m);
            else
                methodString = m.getName();

            if (!results.contains(methodString))
                results.add(methodString);
        }

        List<Field> tempField = new ArrayList<Field>();
        tempField.addAll(inspector.inspectAllFields(c, filtersF, g));
        for(Field f : tempField) {
            String name = f.getName();
            if(!results.contains(name))
                results.add(name);
        }

        return results;
    }

    /**
     * Permet d'ecrire dans un fichier.rctf notre table de propriété pour notre liste de classes
     * @param fileName : le nom du fichier
     * @throws IOException
     */
    public void writeTable(String fileName) throws IOException {
        if (table == null) return;
        BufferedWriter ff = new BufferedWriter
                (new FileWriter(fileName));

        ff.write("FormalContext Collections"+"\n"+"| |");
        for (String aCharacteristicList : CharacteristicList)
            ff.write(aCharacteristicList + "|");

        ff.newLine();
        for (int i=0; i< classes.size(); i++){
            ff.write("|"+classes.get(i).getName()+"|");
            for (int j=0; j< CharacteristicList.size(); j++){
                if (table[i][j])
                    ff.write("x");
                else
                    ff.write(" ");
                ff.write("|");
            }
            ff.newLine();
        }
        ff.close();
    }
}
