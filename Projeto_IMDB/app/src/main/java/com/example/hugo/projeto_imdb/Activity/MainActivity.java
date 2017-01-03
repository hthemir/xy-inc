package com.example.hugo.projeto_imdb.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.example.hugo.projeto_imdb.R;
import com.example.hugo.projeto_imdb.adapter.CustomRecyclerAdapter;

import com.example.hugo.projeto_imdb.production.Imdb;
import com.example.hugo.projeto_imdb.assynctask.AssyncTaskArray;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Associa a variavel toolbar à my_toolbar no layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //Coloca a toolbar passada por parametro para funcionar como a ActionBar para essa activity
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //Infla a toolbar definida em toolbar_main.xml
        getMenuInflater().inflate(R.menu.toolbar_main, menu);
        //Requere acesso direto ao search service
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        //Associa searchView à barra de pesquisa no layout
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            //Usar false torna o icone de busca sempre visivel, o padrao eh true
            //searchView.setIconifiedByDefault(false);
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Esconde o teclado
                searchView.clearFocus();
                //Pega o titulo pesquisado. Se for mais de um nome ele troca espaços por + (ex.: the flash -> the+flash)
                String titulo = searchView.getQuery().toString().replace(' ', '+');
                //Esse endereco retorna uma lista com os filmes que contenham a pesquisa em seu titulo
                String endereco = "http://www.omdbapi.com/?s=" + titulo;
                //Cria uma thread para fazer a pesquisa
                ArrayList<Imdb> lista = callTask(endereco);
                if (lista == null) {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.mainLayout), "Filme nao encontrado", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return false;
                } else {
                    //Associa a variavel recyclerView à listaReciclavel no layout
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listaReciclavel);
                    //Cria um adapter custom com a lista resultado da pesquisae entao preenche a lista reciclavel
                    recyclerView.setAdapter(new CustomRecyclerAdapter(lista, MainActivity.this, MainActivity.this));
                    //Cria um layout grid e define como o layout da lista reciclavel
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2);
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

            //O usuario clicou no disquete para salvar a busca feita no BD
            case R.id.action_save:
                Intent intent = new Intent(MainActivity.this, SavedActivity.class);
                startActivity(intent);
                return true;

            //O usuario clicou na lupa para abrir a caixa de pesquisa
            case R.id.action_search:
                return true;

            //Se chegar no default, o usuario utilizou uma opcao nao implementada
            default:
                Snackbar snackbar = Snackbar.make(findViewById(R.id.mainLayout), "Acao invalida", Snackbar.LENGTH_LONG);
                snackbar.show();
                return super.onOptionsItemSelected(item);
        }
    }

    private ArrayList<Imdb> callTask(String endereco) {
        //Cria uma assync task, que executa no plano de fundo do aplicativo
        AssyncTaskArray task = new AssyncTaskArray(MainActivity.this);
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