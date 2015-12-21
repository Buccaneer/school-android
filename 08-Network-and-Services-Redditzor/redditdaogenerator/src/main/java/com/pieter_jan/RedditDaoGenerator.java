package com.pieter_jan;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class RedditDaoGenerator
{
    private final static String PROJECT_DIR = System.getProperty("user.dir").replace("\\", "/");

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "com.pieter_jan.redditzor.model");
        schema.enableKeepSectionsByDefault();

        Entity listing = schema.addEntity("Listing");
        listing.addIdProperty().autoincrement();
        listing.addStringProperty("before");
        listing.addStringProperty("after");
        listing.addStringProperty("category").unique();

        Entity post = schema.addEntity("Post");
        post.implementsInterface("android.os.Parcelable");
        post.addIdProperty().autoincrement();
        post.addStringProperty("title");
        post.addStringProperty("text");
        post.addStringProperty("url");
        post.addStringProperty("thumbnail");
        post.addStringProperty("image");

        Property listingId = post.addLongProperty("listingId").getProperty();
        post.addToOne(listing, listingId);

        ToMany listingToPost = listing.addToMany(post, listingId);
        listingToPost.setName("posts");


        new DaoGenerator().generateAll(schema, PROJECT_DIR + "/app/src/main/java");
    }

}
