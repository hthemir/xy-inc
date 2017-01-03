package com.example.hugo.projeto_imdb.activity;

import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;

import com.example.hugo.projeto_imdb.R;
import com.example.hugo.projeto_imdb.adapter.CustomRecyclerAdapter;
import com.example.hugo.projeto_imdb.database.ControlDatabase;
import com.example.hugo.projeto_imdb.database.CreateDatabase;

import com.example.hugo.projeto_imdb.production.Imdb;

public class SavedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);

        Toolbar toolbar = (Toolbar)findViewById(R.id.my_toolbar_saved);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ControlDatabase banco = new ControlDatabase(getBaseContext());
        String campos[] = {CreateDatabase.tabela.IMDBID, CreateDatabase.tabela.TITLE, CreateDatabase.tabela.YEAR, CreateDatabase.tabela.POSTER};
        Cursor cursor = banco.consultarProducoes(CreateDatabase.TABELA,campos);

        ArrayList<Imdb> lista = new ArrayList<Imdb>();

        if(cursor.getCount()==0){
            Snackbar snackbar = Snackbar.make(findViewById(R.id.producoesSalvasLayout),"Sem produções salvas",Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else {

            for(int i=0; i<cursor.getCount();i++){
                String id = cursor.getString(0);
                String titulo = cursor.getString(1);
                String ano = cursor.getString(2);
                String image = cursor.getString(3);
                lista.add(new Imdb(titulo,id,ano,image));
                cursor.moveToNext();
            }
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listaReciclavelSalvos);
        recyclerView.setAdapter(new CustomRecyclerAdapter(lista,this,this));
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
