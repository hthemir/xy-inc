package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

import informacoes.Imdb;

/**
 * Created by Hugo on 14/12/2016.
 */
public class ControlaBanco {
    private SQLiteDatabase database;
    private CriaBanco criaBanco;

    public ControlaBanco(Context context){
        criaBanco = CriaBanco.getInstance(context);
    }

    public synchronized String inserirProducao(Imdb imdb){
        ContentValues values = new ContentValues();
        long resultado;

        database = criaBanco.getWritableDatabase();
        putValues(values,imdb);
        resultado = database.insert(CriaBanco.TABELA,null,values);
        //database.close();

        if(resultado==-1)
            return "Erro";
        else
            return "Sucesso";
    }

    //entao eu vou criar uma nova activity que disponibiliza todas as pesquisas salvas
    //sempre que entrar nas informações de um filme, adiciona tais informações a esse item se ele estiver salvo no bd
    public synchronized Cursor consultarProducoes(){
        //cursor eh uma classe que salvara as informacoes retornadas por uma query em um BD
        Cursor cursor;
        //estes sao os campos que serao retornados
        String campos[] = {criaBanco.IMDBID,criaBanco.TITLE,criaBanco.YEAR,criaBanco.POSTER};
        //abre o banco somente para leitura
        database = criaBanco.getReadableDatabase();
        //faz a pesquisa
        cursor = database.query(criaBanco.TABELA,campos,null,null,null,null,null,null);
        if(cursor!=null){
            //move o cursor para a primeira linha
            cursor.moveToFirst();
        }
        //fecha o banco de dados
        //database.close();

        return cursor;
    }

    public synchronized void deletarProducao(String id){
        //passa o id para identificar o item a ser removido
        String where = CriaBanco.IMDBID + "=" + "'"+id+"'";
        database = criaBanco.getReadableDatabase();
        database.delete(CriaBanco.TABELA,where,null);
        //database.close();
    }

    public void putValues(ContentValues values, Imdb imdb){
        values.put(CriaBanco.TITLE,imdb.getTitle());
        values.put(CriaBanco.YEAR,imdb.getYear());
        values.put(CriaBanco.RATED,imdb.getRated());
        values.put(CriaBanco.RELEASED,imdb.getReleased());
        values.put(CriaBanco.RUNTIME,imdb.getRuntime());
        values.put(CriaBanco.GENRE,imdb.getGenre());
        values.put(CriaBanco.DIRECTOR,imdb.getDirector());
        values.put(CriaBanco.WRITER,imdb.getWriter());
        values.put(CriaBanco.ACTORS,imdb.getActors());
        values.put(CriaBanco.PLOT,imdb.getPlot());
        values.put(CriaBanco.LANGUAGE,imdb.getLanguage());
        values.put(CriaBanco.COUNTRY,imdb.getCountry());
        values.put(CriaBanco.AWARDS,imdb.getAwards());
        //para salvar um bitmap, eh necessario tranforma-lo em um array de bytes
        values.put(CriaBanco.POSTER,bitmapParaBlob(imdb.getImagem()));
        values.put(CriaBanco.METASCORE,imdb.getMetascore());
        values.put(CriaBanco.IMDBRATING,imdb.getImdbRating());
        values.put(CriaBanco.IMDBVOTES,imdb.getImdbVotes());
        values.put(CriaBanco.IMDBID,imdb.getImdbID());
        values.put(CriaBanco.TYPE,imdb.getType());
    }

    public static byte[] bitmapParaBlob(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        return stream.toByteArray();
    }
}
