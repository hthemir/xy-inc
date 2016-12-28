package assynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.hugo.projeto_imdb.MainActivity;
import com.example.hugo.projeto_imdb.ProducoesSalvas;

import context.Contexto;
import informacoes.Imdb;
import ws.Conexao;

/**
 * Created by Hugo on 12/12/2016.
 */
public class TarefaAssincronaObj extends AsyncTask<String, Void, Imdb> {
    private Context context;
    private ProgressDialog load;

    public TarefaAssincronaObj(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute(){
        load = ProgressDialog.show(context,"","Carregando",true);
    }

    @Override
    protected Imdb doInBackground(String... params){
        return Conexao.getInformacaoImdb(params[0]);
    }

    @Override
    protected void onPostExecute(Imdb imdb){
        load.dismiss();
    }
}
