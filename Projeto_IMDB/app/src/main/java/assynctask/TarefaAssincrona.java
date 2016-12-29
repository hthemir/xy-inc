package assynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

import informacoes.Imdb;
import ws.Conexao;

/**
 * Created by Hugo on 05/12/2016.
 */
public class TarefaAssincrona extends AsyncTask<String, Void, ArrayList<Imdb>> {
    private Context context;
    private ProgressDialog load;

    public TarefaAssincrona(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute(){
        load = ProgressDialog.show(context,"","Carregando",true);
    }

    @Override
    protected ArrayList<Imdb> doInBackground(String... params){
        return Conexao.getInformacaoArrayImdb(params[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<Imdb> imdb){
        load.dismiss();
    }
}
