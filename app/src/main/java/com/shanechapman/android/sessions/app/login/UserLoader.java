package com.shanechapman.android.sessions.app.login;

import android.content.Context;
import android.util.Log;

import com.shanechapman.android.sessions.app.db.DataLoader;

/**
 * Created by shanechapman on 5/3/14.
 */
class UserLoader extends DataLoader<User> {

    private static final String TAG = "UserLoader";

    private String mName;
    private String mPass;

    public UserLoader(Context context, String name, String pass){
        super(context);
        mName = name;
        mPass = pass;
    }

    @Override
    public User loadInBackground(){
        Log.i(TAG, "loadInBackground");
        return UserManager.get(getContext()).getUser(mName, mPass);
    }

}
