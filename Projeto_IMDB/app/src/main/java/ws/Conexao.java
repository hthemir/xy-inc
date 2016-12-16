package ws;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.DisplayMetrics;

import com.example.hugo.projeto_imdb.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import context.Contexto;
import informacoes.Imdb;

/**
 * Created by Hugo on 02/12/2016.
 */
public class Conexao {

    //usar singleton para ter soh uma instancia

    public static ArrayList<Imdb> getInformacaoArrayImdb(String url){

        try {
            String json = pedidoJson(url);
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("Search");
            Gson gson = new Gson();
            Type collectionType = new TypeToken<ArrayList<Imdb>>(){}.getType();
            ArrayList<Imdb> enums = gson.fromJson(jsonArray.toString(), collectionType);

            for (Imdb imdb: enums){
                imdb.setImagem(downloadImagem(imdb.getPoster()));
            }
            return enums;
        } catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    public static Imdb getInformacaoImdb(String url){
            String json = pedidoJson(url);
            Gson gson = new Gson();
            Imdb imdb = gson.fromJson(json,Imdb.class);
            imdb.setImagem(downloadImagem(imdb.getPoster()));
            return imdb;
    }

    public static String pedidoJson (String url){
        String retorno = "";
        try {
            URL api = new URL(url);
            int codigoResposta;
            HttpURLConnection connection;
            InputStream inputStream;

            //Faz um cast na url api para usar o protocolo HTTP na conexao
            connection = (HttpURLConnection) api.openConnection();
            //Utiliza o metodo get para pegar os dados providos
            connection.setRequestMethod("GET");
            //Tempos de timeout em milissegundos (15s)
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            //Estabelece a conexao
            connection.connect();

            //O metodo getRespondeCode() retorna o codigo da conexao(200 para OK, 404 para nao encontrado etc)
            codigoResposta = connection.getResponseCode();
            //HTTP_BAD_REQUEST tem valor 400, ele e os codigos acima sao de erro, logo se vier um codigo menor houve sucesso
            if(codigoResposta < HttpURLConnection.HTTP_BAD_REQUEST){
                inputStream = connection.getInputStream();
            } else {
                inputStream = connection.getErrorStream(); //criar uma exceção pra jogar pra qm da catch pra exibir toast com erro
            }

            //Converte o json recebido para string, para poder ser usado pelo aplicativo
            retorno = inputStreamParaString(inputStream);
            //Encerra o input stream e fecha a conexao
            inputStream.close();
            connection.disconnect();

        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return retorno;
    }

    private static String inputStreamParaString(InputStream inputStream) {
        //Este eh responsavel por montar a string
        StringBuffer buffer = new StringBuffer();
        try{
            //Este faz a leitura do input stream
            BufferedReader bufferedReader;
            //A linha salva a leitura atual
            String linha;

            //O buffered reader eh inicializado com a classe que le o input stream
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            //Enquanto nao vem uma linha em branco(enquanto ha dados), a leitura eh concatenada no buffer de string
            while ((linha=bufferedReader.readLine())!=null){
                buffer.append(linha);
            }
            //Encerra o buffered reader
            bufferedReader.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        //Para retornar a string contida em buffer, utilizamos .toString()
        return buffer.toString();
    }

    private static Bitmap downloadImagem(String url) {
        try {
            if(url.equals("N/A")){
                return BitmapFactory.decodeResource(Contexto.context().getResources(),R.drawable.imdb);
            } else {
                //Transforma a string url no formato url
                URL endereco = new URL(url);
                //Abre uma conexao com o endereco
                InputStream inputStream = endereco.openStream();
                //Baixa a imagem
                Bitmap imagem = BitmapFactory.decodeStream(inputStream);
                imagem = redefinirImagem(imagem);
                //Fecha a conexao
                inputStream.close();
                return imagem;
            }
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    private static Bitmap redefinirImagem(Bitmap originalImage){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        int width = (int)(1.0 * metrics.widthPixels);
        int height = (int)(1.0 * metrics.heightPixels);

        Bitmap background = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        float originalWidth = originalImage.getWidth();
        float originalHeight = originalImage.getHeight();

        Canvas canvas = new Canvas(background);

        float scale = width / originalWidth;

        float xTranslation = 0.0f;
        float yTranslation = (height - originalHeight * scale) / 2.0f;

        Matrix transformation = new Matrix();
        transformation.postTranslate(xTranslation, yTranslation);
        transformation.preScale(scale, scale);

        Paint paint = new Paint();
        paint.setFilterBitmap(true);

        canvas.drawBitmap(originalImage, transformation, paint);

        return background;
    }
}
