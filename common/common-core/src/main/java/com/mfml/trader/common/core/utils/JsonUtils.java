package com.mfml.trader.common.core.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mfml.trader.common.core.utils.gson.Date2LongTypeAdapter;
import com.mfml.trader.common.core.utils.gson.FixNumberTypeAdapter;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: caozhou
 * @data: 2022-10-08 16:43
 */
public class JsonUtils {
    private static final Gson gson;

    static {
        FixNumberTypeAdapter fixNumberTypeAdapter = new FixNumberTypeAdapter(new Gson());
        gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new Date2LongTypeAdapter()) // data -> long
                .registerTypeAdapter(new TypeToken<Map<String, Object>>() {}.getType(), fixNumberTypeAdapter)
                .registerTypeAdapter(new TypeToken<List<Object>>() {}.getType(), fixNumberTypeAdapter)
                .create();
    }

    public static String toJSONString(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T parseObject(String jsonStr, Class<T> objClass) {
        return gson.fromJson(jsonStr, objClass);
    }

    public static <T> T parseObject(String jsonStr, Type typeOfT) {
        return gson.fromJson(jsonStr, typeOfT);
    }

    public static String jsonPretty(String compressedJson) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement je = JsonParser.parseString(compressedJson);
        return gson.toJson(je);
    }
}
