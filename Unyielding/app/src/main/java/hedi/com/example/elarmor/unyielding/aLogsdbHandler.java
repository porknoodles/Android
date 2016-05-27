package hedi.com.example.elarmor.unyielding;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eLarmor on 11/5/2015.
 */
public class aLogsdbHandler extends SQLiteOpenHelper{

    private static final int db_version = 1;
    private final String TAG="AttendancesdbHandler";
    private static final String db_name = "attendance.db";
    public static final String db_table = "attendance";
    public static final String db_column_id = "_id";
    public static final String db_column_location = "_location";
    public static final String db_colum_time = "_date";
    public static final String db_column_content = "_content";

    public aLogsdbHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, db_name, null, db_version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + db_table + "("+
                db_column_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                db_column_location + " VARCHAR, " +
                db_column_content + " VARCHAR, " +
                db_colum_time + " VARCHAR "+
                ");";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + db_table;
        db.execSQL(query);
        onCreate(db);
    }

    public void addAttendance(Attendance attendance){

        ContentValues aValues = new ContentValues();
        aValues.put(db_column_location, attendance.get_location());
        aValues.put(db_column_content, attendance.get_content());
        aValues.put(db_colum_time, attendance.get_date());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(db_table, null, aValues);
        db.close();

    }

    public void deleteAttendance(String attendance){

        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + db_table + " WHERE " + db_column_content + "=\"" + attendance + "\";";
        db.execSQL(query);

    }

    public List<Attendance> dbtoList(){

        List<Attendance> dbtoAttendances = new ArrayList<Attendance>();

        SQLiteDatabase db = getWritableDatabase();
        String query  = "SELECT *" +
                "FROM " + db_table +
                " ORDER BY " + db_column_id + " DESC;";

        Cursor c = db.rawQuery(query, null);

        try {
            if (c.moveToFirst()) {
                do {
                    Attendance attendance = new Attendance();
                    attendance.set_location(c.getString(c.getColumnIndex(db_column_location)));
                    attendance.set_content(c.getString(c.getColumnIndex(db_column_content)));
                    attendance.set_date(c.getString(c.getColumnIndex(db_colum_time)));

                    // adding to todo list
                    dbtoAttendances.add(attendance);
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
        return dbtoAttendances;

    }

}
