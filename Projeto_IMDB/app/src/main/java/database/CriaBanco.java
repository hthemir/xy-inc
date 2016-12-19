package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Hugo on 14/12/2016.
 */
public class CriaBanco extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "datab.db";
    public static final String TABELA = "producoes";

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

    private static final int VERSAO = 2;

    //construtor
    public CriaBanco(Context context){
        super(context,NOME_BANCO,null,VERSAO);
    }
    //singleton
    private static CriaBanco instance;
    public static synchronized CriaBanco getInstance(Context context){
        if(instance == null)
            instance = new CriaBanco(context);
        return instance;
    }

    //chamado quando a aplicacao chama o bd pela primeira vez.
    // deve possuir todas as diretrizes de criacao e populacao inicial do banco
    @Override
    public void onCreate(SQLiteDatabase database){
        //codigo de criacao que segue o padrao JDBC
        String sql = "CREATE TABLE " + TABELA + "("
                   + TITLE + " text,"
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
                   + TYPE + " text"
                   + ")";
        //comando de criacao
        database.execSQL(sql);
    }

    //responsavel por atualizar o banco de dados se acontecer mudanca estrutural
    // sempre eh chamado quando uma atualizacao eh necessaria,
    // para nao haver inconsistencia de dados entre o banco existente e o novo
    //contem tambem a versao antiga e a nova
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
        database.execSQL("DROP TABLE IF EXISTS " + TABELA);
        onCreate(database);
    }
}
