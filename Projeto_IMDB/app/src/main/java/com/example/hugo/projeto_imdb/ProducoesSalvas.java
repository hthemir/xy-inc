package com.example.hugo.projeto_imdb;

import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import java.util.ArrayList;

import adaptador.CustomRecyclerAdapter;
import database.ControlaBanco;
import database.CriaBanco;
import informacoes.Imdb;

public class ProducoesSalvas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producoes_salvas);

        ControlaBanco banco = new ControlaBanco(getBaseContext());
        Cursor cursor = banco.consultarProducoes();

        ArrayList<Imdb> lista = new ArrayList<Imdb>();

        for(int i=0; i<cursor.getCount();i++){
            String id = cursor.getString(0);
            String titulo = cursor.getString(1);
            String ano = cursor.getString(2);
            byte[] image = cursor.getBlob(3);
            lista.add(new Imdb(titulo,id,ano, BitmapFactory.decodeByteArray(image,0,image.length)));
            cursor.moveToNext();
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listaReciclavelSalvos);
        recyclerView.setAdapter(new CustomRecyclerAdapter(lista,this));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
    }
}
