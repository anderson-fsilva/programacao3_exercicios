package br.ufpe.cin.if1001.rss.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
        cvalues.put(ITEM_UNREAD, false);

        /*
        // Verifica se já existe esse registro no banco
        if (!isItemExits(db, link)) {
            // Se o item não existir no banco, cria uma nova linha
            db.insert(DATABASE_TABLE,null, cvalues);
            db.close();
        } else {
            // Se o item já existir, atualiza a linha
            db.update(DATABASE_TABLE, cvalues, link + " = ?",
                    new String[] { String.valueOf(link)});
            db.close();
        }
        */

        return db.insert(DATABASE_TABLE,null, cvalues);
    }


    // Obtendo um ItemRSS que está no banco de dados

    public ItemRSS getItemRSS(String link) throws SQLException {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(DATABASE_TABLE, new String[] { ITEM_ROWID, ITEM_TITLE, ITEM_DATE, ITEM_DESC, ITEM_LINK, ITEM_UNREAD },
                ITEM_LINK + "=?" , new String[] { String.valueOf(link)},
                null,
                null,
                null,
                null);

        if (cursor "")

        return new ItemRSS("FALTA IMPLEMENTAR","FALTA IMPLEMENTAR","2018-04-09","FALTA IMPLEMENTAR");
    }
    public Cursor getItems() throws SQLException {
        return null;
    }
    public boolean markAsUnread(String link) {
        return false;
    }

    public boolean markAsRead(String link) {
        return false;
    }


    // Verifica se o item já foi inserido no banco

    public boolean isItemExits(SQLiteDatabase db, String rss_link) {

        // Faz uma consulta ao banco e retorna 1 caracter por conta do SELECT 1, caso seja encontrado o item em questão, no banco
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + DATABASE_TABLE
                + " WHERE rss_link = '" + rss_link + "'", new String[]{});

        // Conta a quantidade o tamanho que o curso contém, diante da execução da query acima
        boolean exists = (cursor.getCount() > 0);

        return exists;
    }

}
