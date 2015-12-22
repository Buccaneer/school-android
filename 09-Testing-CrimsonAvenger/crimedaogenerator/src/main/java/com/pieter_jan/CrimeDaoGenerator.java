package com.pieter_jan;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class CrimeDaoGenerator
{
    private final static String PROJECT_DIR = System.getProperty("user.dir").replace("\\", "/");

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "com.bignerdranch.criminalintent.model");
        schema.enableKeepSectionsByDefault();

        Entity crime = schema.addEntity("Crime");
        crime.addIdProperty().autoincrement();
        crime.addStringProperty("title");
        crime.addDateProperty("date");
        crime.addBooleanProperty("solved");
        crime.addStringProperty("suspect");
        crime.addStringProperty("suspectMail");
        crime.addStringProperty("photo");

        new DaoGenerator().generateAll(schema, PROJECT_DIR + "/app/src/main/java");
    }
}
