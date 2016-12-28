package com.example.hugo.projeto_imdb;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import context.Contexto;
import database.BancoIMDb;
import database.ControlaBanco;
import database.CriaBanco;
import informacoes.Imdb;
import assynctask.TarefaAssincronaObj;

public class ProducaoInfo extends AppCompatActivity {

    private ControlaBanco banco;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_producao_info);

        banco = new ControlaBanco(getBaseContext());

        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("imdb");
        String contexto = bundle.getString("contexto");

        final Button salvar = (Button) findViewById(R.id.btnSalvar);

        if (contexto.equals("class com.example.hugo.projeto_imdb.ProducoesSalvas")) {
            salvar.setText("Remover");
            String campos[] = {CriaBanco.tabela.TITLE,
                    CriaBanco.tabela.YEAR,
                    CriaBanco.tabela.RATED,
                    CriaBanco.tabela.RELEASED,
                    CriaBanco.tabela.RUNTIME,
                    CriaBanco.tabela.GENRE,
                    CriaBanco.tabela.DIRECTOR,
                    CriaBanco.tabela.WRITER,
                    CriaBanco.tabela.ACTORS,
                    CriaBanco.tabela.PLOT,
                    CriaBanco.tabela.LANGUAGE,
                    CriaBanco.tabela.COUNTRY,
                    CriaBanco.tabela.AWARDS,
                    CriaBanco.tabela.POSTER,
                    CriaBanco.tabela.METASCORE,
                    CriaBanco.tabela.IMDBRATING,
                    CriaBanco.tabela.IMDBVOTES,
                    CriaBanco.tabela.IMDBID,
                    CriaBanco.tabela.TYPE
            };
            final String where = CriaBanco.tabela.IMDBID + "=" + "'" + id + "'";
            Cursor cursor = banco.buscaProducao(CriaBanco.TABELA, campos, where);
            final Imdb imdb = setImdbCursor(cursor);
            setView(imdb);
            salvar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    banco.deletarProducao(CriaBanco.TABELA, where);
                    Intent intent = new Intent(ProducaoInfo.this, ProducoesSalvas.class);
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
                    //BancoIMDb.putValues(values,imdb);
                    Map<String, String> mapa = new HashMap<String, String>();
                    preencherMapa(mapa, imdb);
                    ContentValues values = new ContentValues();
                    values = BancoIMDb.putValues(mapa, values);
                    Long resultado = banco.inserirProducao(CriaBanco.TABELA, values);
                    if (resultado == -1) {
                        Toast.makeText(ProducaoInfo.this, "Erro ao adicionar", Toast.LENGTH_SHORT).show();
                        if (!caminho.equals(null)) {
                            File file = new File(caminho);
                            file.delete();
                        }
                    } else {
                        Toast.makeText(ProducaoInfo.this, "Adicionado com sucesso", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }
            });
        }
    }

    private Imdb callTask(String endereco){
        //Cria uma assync task, que executa no plano de fundo do aplicativo
        TarefaAssincronaObj task = new TarefaAssincronaObj(ProducaoInfo.this);
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
        imdb.setGenre(cursor.getString(3));
        imdb.setDirector(cursor.getString(4));
        imdb.setWriter(cursor.getString(5));
        imdb.setActors(cursor.getString(6));
        imdb.setPlot(cursor.getString(7));
        imdb.setLanguage(cursor.getString(8));
        imdb.setCountry(cursor.getString(9));
        imdb.setAwards(cursor.getString(10));
        imdb.setImagemPath(cursor.getString(11));
        imdb.setMetascore(cursor.getString(12));
        imdb.setImdbRating(cursor.getString(13));
        imdb.setImdbVotes(cursor.getString(14));
        imdb.setImdbID(cursor.getString(15));
        imdb.setType(cursor.getString(16));
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
        mapa.put(BancoIMDb.TITLE,imdb.getTitle());
        mapa.put(BancoIMDb.YEAR,imdb.getYear());
        mapa.put(BancoIMDb.RATED,imdb.getRated());
        mapa.put(BancoIMDb.RELEASED,imdb.getReleased());
        mapa.put(BancoIMDb.RUNTIME,imdb.getRuntime());
        mapa.put(BancoIMDb.GENRE,imdb.getGenre());
        mapa.put(BancoIMDb.DIRECTOR,imdb.getDirector());
        mapa.put(BancoIMDb.DIRECTOR,imdb.getWriter());
        mapa.put(BancoIMDb.ACTORS,imdb.getActors());
        mapa.put(BancoIMDb.PLOT,imdb.getPlot());
        mapa.put(BancoIMDb.LANGUAGE,imdb.getLanguage());
        mapa.put(BancoIMDb.COUNTRY,imdb.getCountry());
        mapa.put(BancoIMDb.AWARDS,imdb.getAwards());
        mapa.put(BancoIMDb.POSTER,imdb.getImagemPath());
        mapa.put(BancoIMDb.METASCORE,imdb.getMetascore());
        mapa.put(BancoIMDb.IMDBRATING,imdb.getImdbRating());
        mapa.put(BancoIMDb.IMDBVOTES,imdb.getImdbVotes());
        mapa.put(BancoIMDb.IMDBID,imdb.getImdbID());
        mapa.put(BancoIMDb.TYPE,imdb.getType());
    }
}
