package database;

import android.content.ContentValues;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import informacoes.Imdb;

/**
 * Created by Hugo on 26/12/2016.
 */
public class BancoIMDb {
    public static final String TITLE = "title";
    public static final String YEAR = "year";
    public static final String RATED = "rated";
    public static final String RELEASED = "released";
    public static final String RUNTIME = "runtime";
    public static final String GENRE = "genre";
    public static final String DIRECTOR = "director";
    public static final String WRITER = "writer";
    public static final String ACTORS = "actors";
    public static final String PLOT = "plot";
    public static final String LANGUAGE = "language";
    public static final String COUNTRY = "country";
    public static final String AWARDS = "awards";
    public static final String POSTER = "poster";
    public static final String METASCORE = "metascore";
    public static final String IMDBRATING = "imdbrating";
    public static final String IMDBVOTES = "imdbvotes";
    public static final String IMDBID = "_id";
    public static final String TYPE = "type";

    public String campos(){
        return TITLE + " text,"
                + YEAR + " text,"
                + RATED + " text,"
                + RELEASED + " text,"
                + RUNTIME + " text,"
                + GENRE + " text,"
                + DIRECTOR + " text,"
                + WRITER + " text,"
                + ACTORS + " text,"
                + PLOT + " text,"
                + LANGUAGE + " text,"
                + COUNTRY + " text,"
                + AWARDS + " text,"
                + POSTER + " text,"
                + METASCORE + " text,"
                + IMDBRATING + " text,"
                + IMDBVOTES + " text,"
                + IMDBID + " text PRIMARY KEY,"
                + TYPE + " text";
    }

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
