package com.shanechapman.android.sessions.app.login;

/**
 * Created by shanechapman on 5/3/14.
 */
public class User {

    private int mId;
    private String mName;
    private String mPass;

    public User(){
        mId = -1;
    }

    public int getId(){
        return mId;
    }

    public void setId(int id){
        mId = id;
    }

    public String getName(){
        return mName;
    }

    public void setName(String name){
        mName = name;
    }

    public String getPass(){
        return mPass;
    }

    public void setPass(String pass){
        mPass = pass;
    }
}
