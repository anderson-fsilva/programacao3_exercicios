package br.ufpe.cin.if1001.rss.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.if1001.rss.domain.ItemRSS;


public class SQLiteRSSHelper extends SQLiteOpenHelper {
    //Nome do Banco de Dados
    private static final String DATABASE_NAME = "rss";
    //Nome da tabela do Banco a ser usada
    public static final String DATABASE_TABLE = "items";
    //Versão atual do banco
    private static final int DB_VERSION = 1;

    //alternativa
    Context c;

    private SQLiteRSSHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
        c = context;
    }

    private static SQLiteRSSHelper db;

    //Definindo Singleton
    public static SQLiteRSSHelper getInstance(Context c) {
        if (db==null) {
            db = new SQLiteRSSHelper(c.getApplicationContext());
        }
        return db;
    }

    //Definindo constantes que representam os campos do banco de dados
    public static final String ITEM_ROWID = RssProviderContract._ID;
    public static final String ITEM_TITLE = RssProviderContract.TITLE;
    public static final String ITEM_DATE = RssProviderContract.DATE;
    public static final String ITEM_DESC = RssProviderContract.DESCRIPTION;
    public static final String ITEM_LINK = RssProviderContract.LINK;
    public static final String ITEM_UNREAD = RssProviderContract.UNREAD;

    //Definindo constante que representa um array com todos os campos
    public final static String[] columns = { ITEM_ROWID, ITEM_TITLE, ITEM_DATE, ITEM_DESC, ITEM_LINK, ITEM_UNREAD};

    //Definindo constante que representa o comando de criação da tabela no banco de dados
    private static final String CREATE_DB_COMMAND = "CREATE TABLE " + DATABASE_TABLE + " (" +
            ITEM_ROWID +" integer primary key autoincrement, "+
            ITEM_TITLE + " text not null, " +
            ITEM_DATE + " text not null, " +
            ITEM_DESC + " text not null, " +
            ITEM_LINK + " text not null, " +
            ITEM_UNREAD + " boolean not null);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Executa o comando de criação de tabela
        db.execSQL(CREATE_DB_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //estamos ignorando esta possibilidade no momento
        throw new RuntimeException("nao se aplica");
    }

	//IMPLEMENTAR ABAIXO
    //Implemente a manipulação de dados nos métodos auxiliares para não ficar criando consultas manualmente
    public long insertItem(ItemRSS item) {
        return insertItem(item.getTitle(),item.getPubDate(),item.getDescription(),item.getLink());
    }



    public long insertItem(String title, String pubDate, String description, String link) {

        // Obtendo a instância do banco de dados
        SQLiteDatabase db = this.getWritableDatabase();

        // Criando um objeto, do tipo ContentValues, para facilitar a manipulação do banco
        ContentValues cvalues = new ContentValues();

        // Inserindo os parametros no objeto
        cvalues.put(ITEM_TITLE, title);
        cvalues.put(ITEM_DATE, pubDate);
        cvalues.put(ITEM_DESC, description);
        cvalues.put(ITEM_LINK, link);
        cvalues.put(ITEM_UNREAD, markAsUnread(ITEM_LINK));

        return db.insert(DATABASE_TABLE,null, cvalues);
    }



    // Método que retorna um item do banco


    public ItemRSS getItemRSS(String link) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        ItemRSS item = null;

        // Realiza a busca no banco de acordo com o link passado
        Cursor cursor = db.query(DATABASE_TABLE, new String[] { ITEM_ROWID, ITEM_TITLE, ITEM_DATE, ITEM_DESC, ITEM_LINK, ITEM_UNREAD },
                ITEM_LINK + "=?", new String[] {link},
                null,
                null,
                null,
                null);

        // Verifica se o cursor contém algo. Se sim, move o ponteiro do cursor para o início.
        if (cursor != null) {
            cursor.moveToFirst();
        }

        // Se ouver algum retorno na busca, o curso terá um tamanho maior que 0. Desta forma, vamos criar um novo item atribuindo os parâmetros obtidos
        if (cursor.getCount() > 0) {
            item = new ItemRSS(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
        }

        return item;
    }



    // Método que retorna todos os items


    public Cursor getItems() throws SQLException {

        // Selecionando todos os registros e colocando em um cursor
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DATABASE_TABLE, new String[] {ITEM_ROWID, ITEM_TITLE, ITEM_DATE, ITEM_DESC, ITEM_LINK, ITEM_UNREAD},
                String.valueOf(ITEM_UNREAD) + "=?",
                new String[] {"1"},
                null,
                null,
                ITEM_ROWID); //rawQuery(selectRecords, null);

        return cursor;
    }



    public boolean markAsUnread(String link) {
        return true;
    }


    public boolean markAsRead(String link) {

        // Obtendo a instância do banco de dados
        SQLiteDatabase db = this.getWritableDatabase();

        // Criando um objeto, do tipo ContentValues, para facilitar a manipulação do banco
        ContentValues cvalues = new ContentValues();

        // Criando parâmetros do acesso ao banco
        String where = ITEM_LINK + " LIKE ?";
        String[] whereArgs = new String[] { link };

        Cursor cursor = db.query(DATABASE_TABLE, new String[] {ITEM_ROWID, ITEM_TITLE, ITEM_DATE, ITEM_DESC, ITEM_LINK, ITEM_UNREAD},
            ITEM_LINK + "=?", new String[] {link},
            null,
            null,
            null,
            null
        );

        cursor.moveToFirst();


        // Inserindo os parametros no objeto ContentValues
        cvalues.put(ITEM_TITLE, cursor.getString(1));
        cvalues.put(ITEM_DATE, cursor.getString(2));
        cvalues.put(ITEM_DESC, cursor.getString(3));
        cvalues.put(ITEM_LINK, link);
        cvalues.put(ITEM_UNREAD, false);

        db.update(DATABASE_TABLE, cvalues, where, whereArgs ); //.insert(DATABASE_TABLE,null, cvalues);

        return false;
    }

}
