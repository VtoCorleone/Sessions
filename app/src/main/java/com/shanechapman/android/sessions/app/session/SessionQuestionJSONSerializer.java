package com.shanechapman.android.sessions.app.session;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by shanechapman on 5/8/14.
 */
public class SessionQuestionJSONSerializer {

    private Context mContext;
    private String mFileName;

    public SessionQuestionJSONSerializer(Context c, String f){
        mContext = c;
        mFileName = f;
    }

    public ArrayList<Session> getSessions() throws IOException, JSONException {
        ArrayList<Session> sessions = new ArrayList<Session>();
        BufferedReader reader = null;

        try{
            // open and read the file into a StringBuilder
            //InputStream in = mContext.openFileInput(mFileName);
            InputStream in = mContext.getAssets().open(mFileName);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null){
                // line breaks are omitted and irrelevant
                jsonString.append(line);
            }
            // parse the JSON using JSONTokener
            JSONArray jsonSessions = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            // build the array of sessions from JSONObjects
            for(int i = 0; i < jsonSessions.length(); i++){
                sessions.add(new Session(jsonSessions.getJSONObject(i)));
            }

        } catch(FileNotFoundException e){
            // ignore this one, since it happens when we start fresh
        } finally {
            if (reader != null)
                reader.close();
        }

        return sessions;
    }

    public JSONObject getSession() throws IOException, JSONException {
        JSONObject session = new JSONObject();
        BufferedReader reader = null;

        try{
            // open and read the file into a StringBuilder
            //InputStream in = mContext.openFileInput(mFileName);
            InputStream in = mContext.getAssets().open(mFileName);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null){
                // line breaks are omitted and irrelevant
                jsonString.append(line);
            }
            // parse the JSON using JSONTokener
            session = (JSONObject) new JSONTokener(jsonString.toString()).nextValue();

        } catch(FileNotFoundException e){
            // ignore this one, since it happens when we start fresh
        } finally {
            if (reader != null)
                reader.close();
        }

        return session;
    }

    public JSONArray getQuestions(int sessionId) throws IOException, JSONException {
        JSONArray questions = new JSONArray();
        JSONObject session = new JSONObject();
        BufferedReader reader = null;

        try{
            // open and read the file into a StringBuilder
            //InputStream in = mContext.openFileInput(mFileName);
            InputStream in = mContext.getAssets().open(mFileName);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null){
                // line breaks are omitted and irrelevant
                jsonString.append(line);
            }
            // parse the JSON using JSONTokener
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();

            for (int i = 0; i < array.length(); i++){
                if (array.getJSONObject(i).getInt("id") == sessionId)
                    questions = array.getJSONObject(i).getJSONArray("session");
            }

        } catch(FileNotFoundException e){
            // ignore this one, since it happens when we start fresh
        } finally {
            if (reader != null)
                reader.close();
        }

        return questions;
    }

    public ArrayList<SessionQuestion> loadQuestions() throws IOException, JSONException{
        ArrayList<SessionQuestion> questions = new ArrayList<SessionQuestion>();
        BufferedReader reader = null;
        try{
            // open and read the file into a StringBuilder
            //InputStream in = mContext.openFileInput(mFileName);
            InputStream in = mContext.getAssets().open(mFileName);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null){
                // line breaks are omitted and irrelevant
                jsonString.append(line);
            }
            // parse the JSON using JSONTokener
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();

            // build the array of questions from JSONObjects
            for (int i = 0; i < array.length(); i++){
                questions.add(new SessionQuestion(array.getJSONObject(i)));
            }
        } catch(FileNotFoundException e){
            // ignore this one, since it happens when we start fresh
        } finally {
            if (reader != null)
                reader.close();
        }

        return questions;
    }
}
