package com.example.hugo.projeto_imdb.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hugo.projeto_imdb.activity.InfoActivity;
import com.example.hugo.projeto_imdb.R;

import java.util.ArrayList;

import com.example.hugo.projeto_imdb.context.Contexto;
import com.example.hugo.projeto_imdb.production.Imdb;

/**
 * Created by Hugo on 09/12/2016.
 */
public class CustomRecyclerAdapter extends RecyclerView.Adapter{
    private ArrayList<Imdb> lista;
    private Context context;
    private Activity pai;

    public CustomRecyclerAdapter(ArrayList<Imdb> lista, Context context, Activity pai){
        this.lista = lista;
        this.context = context;
        this.pai = pai;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.view_recycleradapter, parent, false);
        CustomRecyclerViewHolder holder = new CustomRecyclerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position){
        CustomRecyclerViewHolder holder = (CustomRecyclerViewHolder) viewHolder;
        final Imdb imdb = lista.get(position);
        holder.txtTitulo.setText(imdb.getTitle());
        holder.txtAno.setText(imdb.getYear());
        //se o caminho for null, a producao nao esta salva
        //se esta salva, basta pega-la no bd
        if(imdb.getImagemPath()==null){
            holder.imgPoster.setImageBitmap(imdb.getImagem());
        } else {
            String path = imdb.getImagemPath();
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            holder.imgPoster.setImageBitmap(bitmap);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Contexto.context(), InfoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("imdb",imdb.getImdbID());
                intent.putExtra("contexto",context.getClass().toString());
                Contexto.context().startActivity(intent);
                if(!context.getClass().toString().equals("class com.example.hugo.projeto_imdb.activity.MainActivity")) {
                    pai.finish();
                }
            }
        });
    }

    @Override
    public int getItemCount(){
        return lista.size();
    }

    public class CustomRecyclerViewHolder extends RecyclerView.ViewHolder {

        final CardView cardView;
        final TextView txtTitulo;
        final TextView txtAno;
        final ImageView imgPoster;

        public CustomRecyclerViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.card_view);
            txtTitulo = (TextView) view.findViewById(R.id.cardTitulo);
            txtAno = (TextView) view.findViewById(R.id.cardAno);
            imgPoster = (ImageView) view.findViewById(R.id.cardImage);
        }
    }
}
