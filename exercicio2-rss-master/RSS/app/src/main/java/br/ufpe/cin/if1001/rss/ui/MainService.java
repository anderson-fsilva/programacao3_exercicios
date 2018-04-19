package br.ufpe.cin.if1001.rss.ui;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import br.ufpe.cin.if1001.rss.db.SQLiteRSSHelper;
import br.ufpe.cin.if1001.rss.domain.ItemRSS;
import br.ufpe.cin.if1001.rss.util.ParserRSS;

import android.support.v4.content.LocalBroadcastManager;



public class MainService extends IntentService{

    public static final String DOWNLOAD_COMPLETE = "DOWNLOAD_COMPLETE";

    private ListView conteudoRSS;
    private SQLiteRSSHelper db;

    public MainService() {
        super("MainService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        List<ItemRSS> items = null;

        db = SQLiteRSSHelper.getInstance(this);

        try {

            String feed = getRssFeed(intent.getData().toString());          // Obter o que foi passado pela Intent
            items = ParserRSS.parse(feed);

            for (ItemRSS i : items) {
                Log.d("DB", "Buscando no Banco por link: " + i.getLink());
                ItemRSS item = db.getItemRSS(i.getLink());
                if (item == null) {
                    Log.d("DB", "Encontrado pela primeira vez: " + i.getTitle());
                    db.insertItem(i);

                    // Para o caso em que o app estiver em segundo plano
                    sendBroadcast(new Intent("broadcast.new.report"));
                }
            }

            // Enviando o broadcast quando terminar de carregar os items
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(DOWNLOAD_COMPLETE));

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        db.close();
    }



    // getRssFeed retorna o xml de acordo com a url passada.


    private String getRssFeed(String feed) throws IOException {
        InputStream in = null;
        String rssFeed = "";
        try {
            URL url = new URL(feed);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            in = conn.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int count; (count = in.read(buffer)) != -1; ) {
                out.write(buffer, 0, count);
            }
            byte[] response = out.toByteArray();
            rssFeed = new String(response, "UTF-8");
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return rssFeed;
    }


}
