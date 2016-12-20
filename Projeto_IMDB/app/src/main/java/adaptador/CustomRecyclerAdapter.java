package adaptador;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.hugo.projeto_imdb.MainActivity;
import com.example.hugo.projeto_imdb.ProducaoInfo;
import com.example.hugo.projeto_imdb.R;
import com.google.gson.Gson;

import java.util.ArrayList;

import context.Contexto;
import informacoes.Imdb;

/**
 * Created by Hugo on 09/12/2016.
 */
public class CustomRecyclerAdapter extends RecyclerView.Adapter{
    private ArrayList<Imdb> lista;
    private Context context;

    public CustomRecyclerAdapter(ArrayList<Imdb> lista, Context context){
        this.lista = lista;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //View view = LayoutInflater.from(context).inflate(R.layout.custom_list, parent, false);
        View view = LayoutInflater.from(context).inflate(R.layout.cardview, parent, false);
        CustomRecyclerViewHolder holder = new CustomRecyclerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position){
        CustomRecyclerViewHolder holder = (CustomRecyclerViewHolder) viewHolder;
        final Imdb imdb = lista.get(position);
        holder.txtTitulo.setText(imdb.getTitle());
        holder.txtAno.setText(imdb.getYear());
        holder.imgPoster.setImageBitmap(imdb.getImagem());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Contexto.context(), ProducaoInfo.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("imdb",imdb.getImdbID());
                intent.putExtra("contexto",context.getClass().toString());
                Contexto.context().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount(){
        return lista.size();
    }

}
