package com.pieter_jan.redditzor;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pieter-Jan on 19/12/2015.
 */
public class JsonListingDeserializer implements JsonDeserializer<Listing>
{
    @Override
    public Listing deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        JsonObject root = json.getAsJsonObject();
        JsonObject data = root.getAsJsonObject("data");
        JsonElement before = data.get("before");
        JsonElement after = data.get("after");
        JsonArray children = data.getAsJsonArray("children");
        List<Post> posts = new ArrayList<>();
        for (JsonElement c : children)
        {
            JsonObject childData = c.getAsJsonObject().getAsJsonObject("data");
            JsonElement title = childData.get("title");
            JsonElement text = childData.get("selftext"); //selftext_html
            JsonElement url = childData.get("url");
            JsonElement thumbnail = childData.get("thumbnail");
            JsonPrimitive image = null;
            JsonElement preview = childData.get("preview");
            if (preview != null) {
                JsonArray images = preview.getAsJsonObject().getAsJsonArray("images");
                JsonElement first = images.get(0);
                JsonObject source = first.getAsJsonObject().getAsJsonObject("source");
                image = source.getAsJsonPrimitive("url");
            }
            posts.add(new Post(getContent(title), getContent(text), getContent(url), getContent(thumbnail), getContent(image)));
        }
        return new Listing(posts, getContent(before), getContent(after));
    }

    private String getContent(JsonElement el)
    {
        if (el != null && !el.isJsonNull())
            return el.getAsString();
        return null;
    }
}
