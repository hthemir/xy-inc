package com.example.hugo.projeto_imdb.assynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

import com.example.hugo.projeto_imdb.production.Imdb;
import com.example.hugo.projeto_imdb.webservice.Connection;

/**
 * Created by Hugo on 05/12/2016.
 */
public class AssyncTaskArray extends AsyncTask<String, Void, ArrayList<Imdb>> {
    private Context context;
    private ProgressDialog load;

    public AssyncTaskArray(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute(){
        load = ProgressDialog.show(context,"","Carregando",true);
    }

    @Override
    protected ArrayList<Imdb> doInBackground(String... params){
        return Connection.getInformacaoArrayImdb(params[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<Imdb> imdb){
        load.dismiss();
    }
}
