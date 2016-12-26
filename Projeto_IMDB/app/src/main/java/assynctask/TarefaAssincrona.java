package assynctask;

import android.os.AsyncTask;

import java.util.ArrayList;

import informacoes.Imdb;
import ws.Conexao;

/**
 * Created by Hugo on 05/12/2016.
 */
public class TarefaAssincrona extends AsyncTask<String, Void, ArrayList<Imdb>> {
    @Override
    protected ArrayList<Imdb> doInBackground(String... params){
        return Conexao.getInformacaoArrayImdb(params[0]);
    }
}
