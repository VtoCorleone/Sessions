package com.shanechapman.android.sessions.app.session;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

/**
 * Created by shanechapman on 5/10/14.
 */
public class SessionDataBaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "users.sqlite";
    private static final int VERSION = 1;

    private static final String TABLE_SESSION = "session";
    private static final String COLUMN_SESSION_ID = "_id";
    private static final String COLUMN_SESSION_USER_ID = "userId";
    private static final String COLUMN_SESSION_TITLE = "title";
    private static final String COLUMN_SESSION_ADDED = "added";

    private static final String TABLE_SESSION_DETAILS = "session_details";
    private static final String COLUMN_SESSION_DETAILS_ID = "_id";
    private static final String COLUMN_SESSION_ID_REL = "sessionId";
    private static final String COLUMN_SESSION_QUESTION_ID = "questionId";
    private static final String COLUMN_SESSION_QUESTION = "question";
    private static final String COLUMN_SESSION_ANSWER = "answer";

    public SessionDataBaseHelper(Context context){
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        // Create the user table
        db.execSQL("CREATE TABLE " + TABLE_SESSION + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, userId INTEGER NOT NULL, title STRING NOT NULL, added INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE session_details (_id INTEGER PRIMARY KEY AUTOINCREMENT, sessionId INTEGER NOT NULL, questionId INTEGER NOT NULL, question STRING NOT NULL, answer STRING NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // Implement when upgrading DB versions
    }

    public SessionCursor querySessions(int userId){
        String query = "SELECT * FROM session WHERE userId = ?";
        Cursor wrapped = getReadableDatabase().rawQuery(query, new String[]{"" + userId});
        return new SessionCursor(wrapped);
    }

    public SessionDetailCursor querySessionDetail(int sessionId){
        String query = "SELECT * FROM session_details WHERE sessionId = ?";
        Cursor wrapped = getReadableDatabase().rawQuery(query, new String[]{"" + sessionId});
        return new SessionDetailCursor(wrapped);
    }

    public long insertSession(int userId, String title){
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_SESSION_USER_ID, userId);
        cv.put(COLUMN_SESSION_TITLE, title);
        cv.put(COLUMN_SESSION_ADDED, new Date().getTime());

        return getWritableDatabase().insert(TABLE_SESSION, null, cv);
    }

    public long insertSessionDetail(SessionDetail detail){
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_SESSION_ID_REL, detail.getSessionId());
        cv.put(COLUMN_SESSION_QUESTION_ID, detail.getQuestionId());
        cv.put(COLUMN_SESSION_QUESTION, detail.getQuestion());
        cv.put(COLUMN_SESSION_ANSWER, detail.getAnswer());

        return getWritableDatabase().insert(TABLE_SESSION_DETAILS, null, cv);
    }

    public static class SessionCursor extends CursorWrapper{

        public SessionCursor(Cursor c){
            super(c);
        }

        public Session getSession(){
            if (isBeforeFirst() || isAfterLast())
                return null;

            Session session = new Session();
            session.setId(getInt(getColumnIndex(COLUMN_SESSION_ID)));
            session.setUserId(getInt(getColumnIndex(COLUMN_SESSION_USER_ID)));
            session.setTitle(getString(getColumnIndex(COLUMN_SESSION_TITLE)));
            session.setAdded(new Date(getLong(getColumnIndex(COLUMN_SESSION_ADDED))));

            return session;
        }

    }

    public static class SessionDetailCursor extends CursorWrapper{

        public SessionDetailCursor(Cursor c) {super(c); }

        public SessionDetail getSessionDetail(){
            if (isBeforeFirst() || isAfterLast())
                return null;

            SessionDetail detail = new SessionDetail();
            detail.setId(getInt(getColumnIndex(COLUMN_SESSION_DETAILS_ID)));
            detail.setSessionId(getInt(getColumnIndex(COLUMN_SESSION_ID_REL)));
            detail.setQuestionId(getInt(getColumnIndex(COLUMN_SESSION_QUESTION_ID)));
            detail.setQuestion(getString(getColumnIndex(COLUMN_SESSION_QUESTION)));
            detail.setAnswer(getString(getColumnIndex(COLUMN_SESSION_ANSWER)));

            return detail;
        }

    }

}
