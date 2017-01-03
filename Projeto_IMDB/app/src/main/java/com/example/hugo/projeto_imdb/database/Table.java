package com.example.hugo.projeto_imdb.database;

import android.content.ContentValues;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Hugo on 28/12/2016.
 */
public abstract class Table {
    public abstract String campos();

    public static ContentValues putValues(Map<String,String> mapa, ContentValues values){
        Set<Map.Entry<String,String>> set = mapa.entrySet();
        Iterator it = set.iterator();

        while(it.hasNext()){
            Map.Entry<String,String> entry = (Map.Entry)it.next();
            values.put(entry.getKey(),entry.getValue());
        }
        return values;
    }
}
