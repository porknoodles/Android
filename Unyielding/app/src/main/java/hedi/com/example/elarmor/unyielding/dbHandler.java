package hedi.com.example.elarmor.unyielding;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class dbHandler extends SQLiteOpenHelper{

    private static final int db_version = 1;
    private static final String db_name = "announcement.db";
    public static final String db_table = "announcement";
    public static final String db_column_id = "_id";
    public static final String db_column_title = "_title";
    public static final String db_column_location = "_location";
    public static final String db_column_content = "_content";
    private final String TAG = "AnnouncementdbHandler";

    public dbHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, db_name, factory, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE IF NOT EXISTS " + db_table + "("+
                db_column_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                db_column_location + " VARCHAR, " +
                db_column_title + " VARCHAR, " +
                db_column_content + " VARCHAR " +
                ");";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + db_table;
        db.execSQL(query);
        onCreate(db);
    }

    public void addAnnouncement(Announcement announcement){

        ContentValues aValues = new ContentValues();
        aValues.put(db_column_location, announcement.get_location());
        aValues.put(db_column_title, announcement.get_title());
        aValues.put(db_column_content, announcement.get_content());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(db_table, null, aValues);
        db.close();

    }

    public void deleteAnnouncement(String announcement){

        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + db_table + " WHERE " + db_column_id + "=\"" + announcement + "\";";
        db.execSQL(query);

    }

    public List<Announcement> dbtoList(){
        List<Announcement> dbtoAnnouncement = new ArrayList<Announcement>();

        SQLiteDatabase db = getWritableDatabase();
        String query  = "SELECT *" +
                " FROM " + db_table +
                " ORDER BY " + db_column_id + " DESC";

        Cursor c = db.rawQuery(query, null);

        try {
            if (c.moveToFirst()) {
                do {
                    Announcement announcement = new Announcement();
                    announcement.set_title(c.getString(c.getColumnIndex(db_column_title)));
                    announcement.set_content(c.getString(c.getColumnIndex(db_column_content)));

                    // adding to todo list
                    dbtoAnnouncement.add(announcement);
                } while (c.moveToNext());
            }
        }catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        }

        db.close();
        return dbtoAnnouncement;

    }
}