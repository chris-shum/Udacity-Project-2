package app.com.example.android.project2a.SQLite;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by ShowMe on 7/4/16.
 */
public class DBAssetHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "RESTAURANTS_DB";
    private static final int DATABASE_VERSION = 1;

    public DBAssetHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
