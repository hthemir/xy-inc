package com.example.hugo.projeto_imdb.assynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.hugo.projeto_imdb.production.Imdb;
import com.example.hugo.projeto_imdb.webservice.Connection;

/**
 * Created by Hugo on 12/12/2016.
 */
public class AssyncTaskObj extends AsyncTask<String, Void, Imdb> {
    private Context context;
    private ProgressDialog load;

    public AssyncTaskObj(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute(){
        load = ProgressDialog.show(context,"","Carregando",true);
    }

    @Override
    protected Imdb doInBackground(String... params){
        return Connection.getInformacaoImdb(params[0]);
    }

    @Override
    protected void onPostExecute(Imdb imdb){
        load.dismiss();
    }
}
