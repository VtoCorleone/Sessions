package com.shanechapman.android.sessions.app.login;

import android.content.Context;

/**
 * Created by shanechapman on 5/3/14.
 */
public class UserManager {

    public static final String USER_ID = "com.shanechapman.android.sessions.app.USER_ID";

    private static final String TAG = "UserManager";

    private static UserManager sUserManager;
    private Context mAppContext;
    private UserDataBaseHelper mHelper;
    private User mUser;

    private UserManager(Context appContext){
        mAppContext = appContext;
        mHelper = new UserDataBaseHelper(mAppContext);
        mUser = null;
    }

    public static UserManager get(Context c){
        if (sUserManager == null)
            sUserManager = new UserManager(c.getApplicationContext());
        return sUserManager;
    }

    public User getUser(String name, String pass){

        UserDataBaseHelper.UserCursor cursor = mHelper.queryUser(name, pass);
        cursor.moveToFirst();
        // if we got a row, get a user
        if (!cursor.isAfterLast())
            mUser = cursor.getUser();
        cursor.close();
        return mUser;
    }

    public long insertUser(String name, String pass){
        long newUserId = mHelper.insertUser(name, pass);
        return newUserId;
    }

}
