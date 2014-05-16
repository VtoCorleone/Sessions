package com.shanechapman.android.sessions.app.login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by shanechapman on 5/3/14.
 */
public class UserDataBaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "sessions.sqlite";
    private static final int VERSION = 1;

    private static final String TABLE_USER = "user";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_NAME = "name";
    private static final String COLUMN_USER_PASSWORD = "password";

    public UserDataBaseHelper(Context context){
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        // Create the user table
        db.execSQL("CREATE TABLE user (id integer primary key autoincrement, name string, password string)");
        db.execSQL("INSERT INTO user (name, password) VALUES('CS646', 'SP2014')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // Implement when upgrading DB versions
    }

    public UserCursor queryUser(String name, String pass){
        String query = "SELECT * FROM user WHERE name = ? AND password = ?";
        Cursor wrapped = getReadableDatabase().rawQuery(query, new String[]{name, pass});
        return new UserCursor(wrapped);
    }

    public long insertUser(String name, String pass){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER_NAME, name);
        cv.put(COLUMN_USER_PASSWORD, pass);
        return getWritableDatabase().insert(TABLE_USER, null, cv);
    }

    public static class UserCursor extends CursorWrapper{

        public UserCursor(Cursor c){
            super(c);
        }

        // Returns a login user/password object, or null if the current row is invalid
        public User getUser(){
            if (isBeforeFirst() || isAfterLast())
                return null;

            User user = new User();

            user.setId(getInt(getColumnIndex(COLUMN_USER_ID)));
            user.setName(getString(getColumnIndex(COLUMN_USER_NAME)));
            user.setPass(getString(getColumnIndex(COLUMN_USER_PASSWORD)));

            return user;
        }
    }

}
