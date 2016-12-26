package assynctask;

import android.os.AsyncTask;

import informacoes.Imdb;
import ws.Conexao;

/**
 * Created by Hugo on 12/12/2016.
 */
public class TarefaAssincronaObj extends AsyncTask<String, Void, Imdb> {
    @Override
    protected Imdb doInBackground(String... params){
        return Conexao.getInformacaoImdb(params[0]);
    }
}
