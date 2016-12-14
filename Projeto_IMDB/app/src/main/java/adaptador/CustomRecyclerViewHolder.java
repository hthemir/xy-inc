package adaptador;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hugo.projeto_imdb.R;

/**
 * Created by Hugo on 09/12/2016.
 */
public class CustomRecyclerViewHolder extends RecyclerView.ViewHolder {
    final TextView txtTitulo;
    final TextView txtAno;
    final ImageView imgPoster;

    public CustomRecyclerViewHolder(View view){
        super(view);
        txtTitulo = (TextView) view.findViewById(R.id.customListTitulo);
        txtAno = (TextView) view.findViewById(R.id.customListAno);
        imgPoster = (ImageView) view.findViewById(R.id.customListImage);
    }
}
