package app.com.example.android.project2a.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ShowMe on 7/4/16.
 */
public class MovieSQLiteOpenHelper extends android.database.sqlite.SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MOVIES_DB";
    public static final String MOVIE_LIST_TABLE_NAME = "MOVIE_LIST";

    public static final String COL_ID = "_id";
    public static final String COL_MOVIE_NAME = "MOVIE_NAME";
    public static final String COL_MOVIE_POSTER = "POSTER";
    public static final String COL_PLOT = "PLOT";
    public static final String COL_RATING = "RATING";
    public static final String COL_RELEASE = "RELEASE";
    public static final String[] MOVIE_COLUMNS = {COL_ID, COL_MOVIE_NAME, COL_MOVIE_POSTER, COL_PLOT, COL_RATING, COL_RELEASE};

    private static final String CREATE_MOVIE_LIST_TABLE =
            "CREATE TABLE " + MOVIE_LIST_TABLE_NAME +
                    "(" +
                    COL_ID + " TEXT, " +
                    COL_MOVIE_NAME + " TEXT, " +
                    COL_MOVIE_POSTER + " TEXT, " +
                    COL_PLOT + " TEXT, " +
                    COL_RATING + " TEXT, " +
                    COL_RELEASE + " TEXT )";

    private static MovieSQLiteOpenHelper mInstance;

    public static MovieSQLiteOpenHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MovieSQLiteOpenHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    public MovieSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MOVIE_LIST_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MOVIE_LIST_TABLE_NAME);
        this.onCreate(db);
    }

    public Cursor getMovieList() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(MOVIE_LIST_TABLE_NAME, // a. table
                MOVIE_COLUMNS, // b. column names
                null, // c. selections
                null, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
        return cursor;
    }

    public long addMovie(String id, String name, String poster, String plot, String rating, String release) {
        ContentValues values = new ContentValues();
        values.put(COL_ID, id);
        values.put(COL_MOVIE_NAME, name);
        values.put(COL_MOVIE_POSTER, poster);
        values.put(COL_PLOT, plot);
        values.put(COL_RATING, rating);
        values.put(COL_RELEASE, release);
        SQLiteDatabase db = this.getWritableDatabase();
        long returnId = db.insert(MOVIE_LIST_TABLE_NAME, null, values);
        db.close();
        return returnId;
    }

    public void removeMovie(String id) {
        String selection = "_id=  ?";
        String [] selectionArgs = {id};
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("MOVIE_LIST", selection, selectionArgs);
    }

    public Cursor searchMovieList(String query) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(MOVIE_LIST_TABLE_NAME, // a. table
                MOVIE_COLUMNS, // b. column names
                COL_ID + " LIKE ?", // c. selections
                new String[]{"%" + query + "%"}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
        return cursor;
    }


}
