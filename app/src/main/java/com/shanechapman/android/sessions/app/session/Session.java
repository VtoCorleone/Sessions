package com.shanechapman.android.sessions.app.session;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by shanechapman on 5/10/14.
 */
public class Session {

    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";

    private int mId;
    private int mUserId;
    private String mTitle;
    private Date mAdded;

    public Session(){}

    public Session(JSONObject json) throws JSONException {
        mId = json.getInt(JSON_ID);
        mTitle = json.getString(JSON_TITLE);
    }

    public int getId(){
        return mId;
    }

    public void setId(int id){
        mId = id;
    }

    public int getUserId(){
        return mUserId;
    }

    public void setUserId(int id){
        mUserId = id;
    }

    public String getTitle(){
        return mTitle;
    }

    public void setTitle(String title){
        mTitle = title;
    }

    public Date getAdded(){
        return mAdded;
    }

    public void setAdded(Date added){
        mAdded = added;
    }

}
