package com.example.hugo.projeto_imdb;

import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import adaptador.CustomAdapter;
import adaptador.CustomRecyclerAdapter;
import context.Contexto;
import informacoes.Imdb;
import thread.TarefaAssincrona;
import ws.Conexao;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        //Associa a variavel toolbar à my_toolbar no layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //Coloca a toolbar passada por parametro para funcionar como a ActionBar para essa activity
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        //Infla a toolbar definida em maintoolbar.xml
        //q q ta rolando dps
        getMenuInflater().inflate(R.menu.maintoolbar,menu);
        //Requere acesso direto ao search service
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        //Associa searchView à barra de pesquisa no layout
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        if(searchView != null){
            //1.component name = nome da activity
            //2.get searchable info = pega informacao sobre uma activity pesquisavel
            //3.set searchable info = define a informacao pesquisavel para esse search view
            //4.??
            //5.profit
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            //Usar false torna o campo de busca sempre visivel
            searchView.setIconifiedByDefault(false);
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Pega o titulo pesquisado. Se for mais de um nome ele troca espaços por + (ex.: the flash -> the+flash)
                String titulo = searchView.getQuery().toString().replace(' ','+');
                //Esse endereco retorna uma lista com os filmes que contenham a pesquisa em seu titulo
                String endereco = "http://www.omdbapi.com/?s=" + titulo;
                //Cria uma thread para fazer a pesquisa
                ArrayList<Imdb> lista = callTask(endereco);
                if(lista == null){
                    Toast.makeText(MainActivity.this,"Filme nao encontrado",Toast.LENGTH_LONG).show();
                    return false;
                }else{
                    //Associa a variavel recyclerView à listaReciclavel no layout
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listaReciclavel);
                    //Cria um adapter custom com a lista resultado da pesquisae entao preenche a lista reciclavel
                    recyclerView.setAdapter(new CustomRecyclerAdapter(lista,MainActivity.this));
                    //Cria um layout vertical e define como o layout da lista reciclavel
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false);
                    recyclerView.setLayoutManager(layoutManager);
                    return true;
                }
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_save:
                //O usuario clicou no disquete para salvar a busca feita no BD
                Toast.makeText(this, "Resultados da busca salvos", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_search:
                //O usuario clicou na lupa para abrir a caixa de pesquisa
                //Toast.makeText(this, "Pesquisando", Toast.LENGTH_SHORT).show();
                return true;

            default:
                //Se chegar no default, o usuario utilizou uma opcao nao implementada
                Toast.makeText(this, "Acao invalida",Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
        }
    }

    private ArrayList<Imdb> callTask(String endereco){
        //Cria uma assync task, que executa no plano de fundo do aplicativo
        TarefaAssincrona task = new TarefaAssincrona();
        //execute faz com que a task execute seus metodos( doInBackground necessario + 2 opcionais)
        task.execute(endereco);

        try {
            //retorna uma lista de objetos da classe Imdb
            return task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

}