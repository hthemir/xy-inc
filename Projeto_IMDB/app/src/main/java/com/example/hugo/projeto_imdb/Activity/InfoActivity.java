package com.example.hugo.projeto_imdb.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hugo.projeto_imdb.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.example.hugo.projeto_imdb.database.IMDbDatabase;
import com.example.hugo.projeto_imdb.database.ControlDatabase;
import com.example.hugo.projeto_imdb.database.CreateDatabase;
import com.example.hugo.projeto_imdb.production.Imdb;
import com.example.hugo.projeto_imdb.assynctask.AssyncTaskObj;

public class InfoActivity extends AppCompatActivity {

    private ControlDatabase banco;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Toolbar toolbar = (Toolbar)findViewById(R.id.my_toolbar_info);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        banco = new ControlDatabase(getBaseContext());

        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("imdb");
        String contexto = bundle.getString("contexto");


        final Button salvar = (Button) findViewById(R.id.btnSalvar);

        if (contexto.equals("class com.example.hugo.projeto_imdb.activity.SavedActivity")) {
            salvar.setText("Remover");
            String campos[] = {CreateDatabase.tabela.TITLE,
                    CreateDatabase.tabela.YEAR,
                    CreateDatabase.tabela.RATED,
                    CreateDatabase.tabela.RELEASED,
                    CreateDatabase.tabela.RUNTIME,
                    CreateDatabase.tabela.GENRE,
                    CreateDatabase.tabela.DIRECTOR,
                    CreateDatabase.tabela.WRITER,
                    CreateDatabase.tabela.ACTORS,
                    CreateDatabase.tabela.PLOT,
                    CreateDatabase.tabela.LANGUAGE,
                    CreateDatabase.tabela.COUNTRY,
                    CreateDatabase.tabela.AWARDS,
                    CreateDatabase.tabela.POSTER,
                    CreateDatabase.tabela.METASCORE,
                    CreateDatabase.tabela.IMDBRATING,
                    CreateDatabase.tabela.IMDBVOTES,
                    CreateDatabase.tabela.IMDBID,
                    CreateDatabase.tabela.TYPE
            };
            final String where = CreateDatabase.tabela.IMDBID + "=" + "'" + id + "'";
            Cursor cursor = banco.buscaProducao(CreateDatabase.TABELA, campos, where);
            final Imdb imdb = setImdbCursor(cursor);
            setView(imdb);
            salvar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    banco.deletarProducao(CreateDatabase.TABELA, where);
                    Intent intent = new Intent(InfoActivity.this, SavedActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        } else {
            salvar.setText("Salvar");
            final Imdb imdb = callTask("http://www.omdbapi.com/?i=" + id);
            setView(imdb);
            salvar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String caminho = new String();
                    //se a producao nao tem poster, nao salva a imagem do imdb no bd
                    if (!imdb.getPoster().equals("N/A")) {
                        caminho = salvarImagem(imdb.getImagem());
                        imdb.setImagemPath(caminho);
                    }
                    //ContentValues values = new ContentValues();
                    //IMDbDatabase.putValues(values,imdb);
                    Map<String, String> mapa = new HashMap<String, String>();
                    preencherMapa(mapa, imdb);
                    ContentValues values = new ContentValues();
                    values = IMDbDatabase.putValues(mapa, values);
                    Long resultado = banco.inserirProducao(CreateDatabase.TABELA, values);
                    if (resultado == -1) {
                        try {
                            Toast.makeText(InfoActivity.this, "Erro ao adicionar", Toast.LENGTH_SHORT).show();
                            if (!caminho.equals(null)) {
                                File file = new File(caminho);
                                file.delete();
                            }
                        } catch (Exception e) {
                            e.getMessage();
                            //// TODO: 02/01/2017 ajustar catch
                        }

                    } else {
                        Toast.makeText(InfoActivity.this, "Adicionado com sucesso", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }
            });
        }
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

    private Imdb callTask(String endereco){
        //Cria uma assync task, que executa no plano de fundo do aplicativo
        AssyncTaskObj task = new AssyncTaskObj(InfoActivity.this);
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

    private Imdb setImdbCursor(Cursor cursor){
        Imdb imdb = new Imdb();
        imdb.setTitle(cursor.getString(0));
        imdb.setYear(cursor.getString(1));
        imdb.setRated(cursor.getString(2));
        imdb.setReleased(cursor.getString(3));
        imdb.setRuntime(cursor.getString(4));
        imdb.setGenre(cursor.getString(5));
        imdb.setDirector(cursor.getString(6));
        imdb.setWriter(cursor.getString(7));
        imdb.setActors(cursor.getString(8));
        imdb.setPlot(cursor.getString(9));
        imdb.setLanguage(cursor.getString(10));
        imdb.setCountry(cursor.getString(11));
        imdb.setAwards(cursor.getString(12));
        imdb.setImagemPath(cursor.getString(13));
        imdb.setMetascore(cursor.getString(14));
        imdb.setImdbRating(cursor.getString(15));
        imdb.setImdbVotes(cursor.getString(16));
        imdb.setImdbID(cursor.getString(17));
        imdb.setType(cursor.getString(18));
        return imdb;
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

    private static String salvarImagem(Bitmap imagem){
        File pictureFile = getOutputMediaFile();
        if(pictureFile == null){
            Log.d("TAG","Erro ao criar o arquivo");
            return null;
        }

        try{
            FileOutputStream fos = new FileOutputStream(pictureFile);
            imagem.compress(Bitmap.CompressFormat.JPEG,90,fos);
            fos.close();
            return pictureFile.getPath();
        } catch (FileNotFoundException e){
            Log.d("TAG","Arquivo nao encontrado: "+e.getMessage());
            return null;
        } catch (IOException e){
            Log.d("TAG","Erro ao acessar arquivo: "+e.getMessage());
            return null;
        }
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/imdb");

        if(!mediaStorageDir.exists()){
            if(!mediaStorageDir.mkdirs()){
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmssSSS").format(new Date());
        String imageName = "poster" + timeStamp + ".jpg";
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + imageName);
        return mediaFile;
    }

    private void preencherMapa(Map<String,String> mapa,Imdb imdb){
        mapa.put(IMDbDatabase.TITLE,imdb.getTitle());
        mapa.put(IMDbDatabase.YEAR,imdb.getYear());
        mapa.put(IMDbDatabase.RATED,imdb.getRated());
        mapa.put(IMDbDatabase.RELEASED,imdb.getReleased());
        mapa.put(IMDbDatabase.RUNTIME,imdb.getRuntime());
        mapa.put(IMDbDatabase.GENRE,imdb.getGenre());
        mapa.put(IMDbDatabase.DIRECTOR,imdb.getDirector());
        mapa.put(IMDbDatabase.WRITER,imdb.getWriter());
        mapa.put(IMDbDatabase.ACTORS,imdb.getActors());
        mapa.put(IMDbDatabase.PLOT,imdb.getPlot());
        mapa.put(IMDbDatabase.LANGUAGE,imdb.getLanguage());
        mapa.put(IMDbDatabase.COUNTRY,imdb.getCountry());
        mapa.put(IMDbDatabase.AWARDS,imdb.getAwards());
        mapa.put(IMDbDatabase.POSTER,imdb.getImagemPath());
        mapa.put(IMDbDatabase.METASCORE,imdb.getMetascore());
        mapa.put(IMDbDatabase.IMDBRATING,imdb.getImdbRating());
        mapa.put(IMDbDatabase.IMDBVOTES,imdb.getImdbVotes());
        mapa.put(IMDbDatabase.IMDBID,imdb.getImdbID());
        mapa.put(IMDbDatabase.TYPE,imdb.getType());
    }
}
