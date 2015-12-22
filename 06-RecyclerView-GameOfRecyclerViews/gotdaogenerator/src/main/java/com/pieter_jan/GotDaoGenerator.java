package com.pieter_jan;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GotDaoGenerator
{
    private final static String PROJECT_DIR = System.getProperty("user.dir").replace("\\", "/");

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "com.pieter_jan.kappagrrm.model");
        schema.enableKeepSectionsByDefault();

        Entity character = schema.addEntity("Character");
        character.implementsInterface("android.os.Parcelable");
        character.addIdProperty().autoincrement();
        character.addIntProperty("imageId");
        character.addStringProperty("name");
        character.addStringProperty("description");

        new DaoGenerator().generateAll(schema, PROJECT_DIR + "/app/src/main/java");
    }
}
