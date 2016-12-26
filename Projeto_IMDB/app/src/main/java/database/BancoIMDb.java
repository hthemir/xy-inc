package database;

import android.content.ContentValues;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

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
                + POSTER + " BLOB,"
                + METASCORE + " text,"
                + IMDBRATING + " text,"
                + IMDBVOTES + " text,"
                + IMDBID + " text PRIMARY KEY,"
                + TYPE + " text";
    }

    public static void putValues(ContentValues values, Imdb imdb){
        values.put(TITLE,imdb.getTitle());
        values.put(YEAR,imdb.getYear());
        values.put(RATED,imdb.getRated());
        values.put(RELEASED,imdb.getReleased());
        values.put(RUNTIME,imdb.getRuntime());
        values.put(GENRE,imdb.getGenre());
        values.put(DIRECTOR,imdb.getDirector());
        values.put(WRITER,imdb.getWriter());
        values.put(ACTORS,imdb.getActors());
        values.put(PLOT,imdb.getPlot());
        values.put(LANGUAGE,imdb.getLanguage());
        values.put(COUNTRY,imdb.getCountry());
        values.put(AWARDS,imdb.getAwards());
        //para salvar um bitmap, eh necessario tranforma-lo em um array de bytes
        values.put(POSTER,bitmapParaBlob(imdb.getImagem()));
        values.put(METASCORE,imdb.getMetascore());
        values.put(IMDBRATING,imdb.getImdbRating());
        values.put(IMDBVOTES,imdb.getImdbVotes());
        values.put(IMDBID,imdb.getImdbID());
        values.put(TYPE,imdb.getType());

        
    }
    public static byte[] bitmapParaBlob(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        return stream.toByteArray();
    }
}
