package adaptador;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hugo.projeto_imdb.R;

import java.util.ArrayList;

import informacoes.Imdb;

/**
 * Created by Hugo on 07/12/2016.
 */

//Para criar um adaptador personalizado, é necessário implementer as 4 funcoes basicas dele:
    //getCount, getItem, getItemId, getView
public class CustomAdapter extends BaseAdapter {

    private final ArrayList<Imdb> lista;
    private final Activity activity;

    //Eh necessario receber a activity tambem para poder criar a View
    public CustomAdapter(ArrayList<Imdb> lista , Activity act){
        this.lista = lista;
        this.activity = act;
    }

    //Retorna o tamanho da lista
    @Override
    public int getCount(){
        return lista.size();
    }

    //Retorna um objeto de acordo com seu indice
    @Override
    public Object getItem (int position){
        return lista.get(position);
    }

    //Retorna o id do objeto. 0 ja que nao temos uso para id
    @Override
    public long getItemId (int position){
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        //O metodo getLayoutInflater da activity eh responsavel por inflar a view
        //O metodo inflate cria a view
        //parent eh o proprio ViewGroup, o layout que recebera a lista
        //false para que a view nao seja criada no momento de chamada da funcao
        View view = activity.getLayoutInflater().inflate(R.layout.custom_list,parent,false);
        //agora pegamos o imdb enviado
        Imdb imdb = lista.get(position);

        TextView txtTitulo = (TextView) view.findViewById(R.id.customListTitulo);
        TextView txtAno = (TextView) view.findViewById(R.id.customListAno);
        ImageView imgPoster = (ImageView) view.findViewById(R.id.customListImage);

        txtTitulo.setText(imdb.getTitle());
        txtAno.setText(imdb.getYear());
        imgPoster.setImageBitmap(imdb.getImagem());

        return view;
    }
}
