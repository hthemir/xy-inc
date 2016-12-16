package com.example.hugo.projeto_imdb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;

import database.ControlaBanco;
import informacoes.Imdb;
import thread.TarefaAssincronaObj;

public class ProducaoInfo extends AppCompatActivity {

    private ControlaBanco banco;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producao_info);

        banco = new ControlaBanco(getBaseContext());

        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("imdb");
        final Imdb imdb = callTask("http://www.omdbapi.com/?i=" + id);

        setView(imdb);

        Button salvar = (Button) findViewById(R.id.btnSalvar);
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resultado = banco.inserirProducao(imdb);
                Toast.makeText(getApplicationContext(),resultado,Toast.LENGTH_LONG).show();
            }
        });
    }

    private Imdb callTask(String endereco){
        //Cria uma assync task, que executa no plano de fundo do aplicativo
        TarefaAssincronaObj task = new TarefaAssincronaObj();
        //execute faz com que a task execute seus metodos( doInBackground necessario + 2 opcionais)
        task.execute(endereco);

        try {
            //retorna um objeto da classe Imdb
            return task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setView(Imdb imdb){
        TextView textView = (TextView) findViewById(R.id.meuTitulo);
        TextView textView1 = (TextView) findViewById(R.id.meuAno);
        TextView textView2 = (TextView) findViewById(R.id.minhaClassificacao);
        TextView textView3 = (TextView) findViewById(R.id.minhaData);
        TextView textView4 = (TextView) findViewById(R.id.minhaDuracao);
        TextView textView5 = (TextView) findViewById(R.id.meuGenero);
        TextView textView6 = (TextView) findViewById(R.id.meuDiretor);
        TextView textView7 = (TextView) findViewById(R.id.meusEscritores);
        TextView textView8 = (TextView) findViewById(R.id.meusAtores);
        TextView textView9 = (TextView) findViewById(R.id.minhaSinopse);
        TextView textView10 = (TextView) findViewById(R.id.minhaLingua);
        TextView textView11 = (TextView) findViewById(R.id.meuPais);
        TextView textView12 = (TextView) findViewById(R.id.meusPremios);
        TextView textView13 = (TextView) findViewById(R.id.meuMetascore);
        TextView textView14 = (TextView) findViewById(R.id.minhaNota);
        TextView textView15 = (TextView) findViewById(R.id.meusVotos);
        TextView textView16 = (TextView) findViewById(R.id.meuID);
        TextView textView17 = (TextView) findViewById(R.id.meuTipo);

        textView.setText(imdb.getTitle());
        textView1.setText(imdb.getYear());
        textView2.setText(imdb.getRated());
        textView3.setText(imdb.getReleased());
        textView4.setText(imdb.getRuntime());
        textView5.setText(imdb.getGenre());
        textView6.setText(imdb.getDirector());
        textView7.setText(imdb.getWriter());
        textView8.setText(imdb.getActors());
        textView9.setText(imdb.getPlot());
        textView10.setText(imdb.getLanguage());
        textView11.setText(imdb.getCountry());
        textView12.setText(imdb.getAwards());
        textView13.setText(imdb.getMetascore());
        textView14.setText(imdb.getImdbRating());
        textView15.setText(imdb.getImdbVotes());
        textView16.setText(imdb.getImdbID());
        textView17.setText(imdb.getType());
    }
}
