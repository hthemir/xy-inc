package informacoes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.hugo.projeto_imdb.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import context.Contexto;

/**
 * Created by Hugo on 02/12/2016.
 */
public class Imdb {
    private String Title;
    private String Year;
    private String Rated;
    private String Released;
    private String Runtime;
    private String Genre;
    private String Director;
    private String Writer;
    private String Actors;
    private String Plot;
    private String Language;
    private String Country;
    private String Awards;
    private String Poster;
    private Bitmap imagem;
    private String imagemPath;
    private String Metascore;
    private String imdbRating;
    private String imdbVotes;
    private String imdbID;
    private String Type;

    public Imdb(String title, String id,String year,String imagem){
        this.Title = title;
        this.imdbID = id;
        this.Year = year;
        this.imagemPath = imagem;

        //se o filme nao tem poster, o caminho gerado eh null, logo preenchemos com a imagem base
        if (imagem==null){
            this.setImagem(BitmapFactory.decodeResource(Contexto.context().getResources(), R.drawable.imdb));
        }
    }

    public String getTitle() {
        return Title;
    }

    public String getYear() {
        return Year;
    }

    public String getRated() {
        return Rated;
    }

    public String getReleased() {
        return Released;
    }

    public String getRuntime() {
        return Runtime;
    }

    public String getGenre() {
        return Genre;
    }

    public String getDirector() {
        return Director;
    }

    public String getWriter() {
        return Writer;
    }

    public String getActors() {
        return Actors;
    }

    public String getPlot() {
        return Plot;
    }

    public String getLanguage() {
        return Language;
    }

    public String getCountry() {
        return Country;
    }

    public String getAwards() {
        return Awards;
    }

    public String getMetascore() {
        return Metascore;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public String getImdbVotes() {
        return imdbVotes;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getType() {
        return Type;
    }

    public String getPoster() {
        return Poster;
    }

    public Bitmap getImagem() {
        return imagem;
    }

    public void setImagem(Bitmap imagem) {
        this.imagem = imagem;
    }

    public String getImagemPath() {
        return imagemPath;
    }

    public void setImagemPath(String imagemPath) {
        this.imagemPath = imagemPath;
    }

    @Override
    public String toString(){
        return Title + ": " + Year +  imagem;
    }


}















