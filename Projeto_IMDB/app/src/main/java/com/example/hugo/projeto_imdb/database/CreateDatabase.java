package com.example.hugo.projeto_imdb.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Hugo on 14/12/2016.
 */
public class CreateDatabase extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "bancoimdb.db";
    public static final String TABELA = "producoes";
    public static IMDbDatabase tabela = new IMDbDatabase();
    private static final int VERSAO = 1;

    //construtor
    public CreateDatabase(Context context){
        super(context,NOME_BANCO,null,VERSAO);
    }
    //singleton
    private static CreateDatabase instance;
    public static synchronized CreateDatabase getInstance(Context context){
        if(instance == null)
            instance = new CreateDatabase(context);
        return instance;
    }

    //chamado quando a aplicacao chama o bd pela primeira vez.
    // deve possuir todas as diretrizes de criacao e populacao inicial do banco
    @Override
    public void onCreate(SQLiteDatabase database){
        //codigo de criacao que segue o padrao JDBC
        String sql = "CREATE TABLE " + TABELA + "(" + tabela.campos() + ")";
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
