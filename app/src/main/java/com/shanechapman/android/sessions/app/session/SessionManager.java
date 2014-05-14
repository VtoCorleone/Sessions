package com.shanechapman.android.sessions.app.session;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class SessionManager {

    private static final String TAG = "SessionManager";
    public static final String SESSION_ID = "com.shanechapman.android.sessions.app.SESSION_ID";
    public static final String SESSION_TITLE = "com.shanechapman.android.sessions.app.SESSION_TITLE";

    private int mUserId;
    private int mSessionId;
    private String mSessionTitle;
    private String mCurrentAnswer;

    private ArrayList<Session> mSessions;
    private ArrayList<SessionQuestion> mSessionQuestions;
    private ArrayList<SessionQuestion> mPreviousQuestions;

    private JSONArray mJsonSessions;
    private JSONArray mJsonSessionQuestions;
    private JSONObject mSession;

    private SessionDataBaseHelper mHelper;
    private static SessionManager sSessionManager;
    private Context mAppContext;

    public SessionManager(Context appContext){
        mAppContext = appContext;
        mHelper = new SessionDataBaseHelper(mAppContext);

        SessionQuestionJSONSerializer s = new SessionQuestionJSONSerializer(mAppContext, "sessions.json");

        mSessions = new ArrayList<Session>();
        mPreviousQuestions = new ArrayList<SessionQuestion>();

        try {
            mSessions = s.getSessions();
        }
        catch(IOException e){ }
        catch(JSONException je){ }
    }

//    public static SessionManager get(Context c){
//        if (sSessionManager == null){
//            sSessionManager = new SessionManager(c.getApplicationContext());
//        }
//        return sSessionManager;
//    }

    public void setUserId(int userId){
        mUserId = userId;
    }

    public void setSessionId(int sessionId){
        mSessionId = sessionId;
    }

    public void setSessionTitle(String title){
        mSessionTitle = title;
    }

    public void setSession(){

        SessionQuestionJSONSerializer s = new SessionQuestionJSONSerializer(mAppContext, "sessions.json");

        mSessions = new ArrayList<Session>();

        try {
            // Get the list of questions for the session in JSON format
            mJsonSessionQuestions = s.getQuestions(mSessionId);

            // Instantiate a new array of SessionQuestions
            mSessionQuestions = new ArrayList<SessionQuestion>();

            // Convert the JSON to array of SessionQuestion objects
            for(int i = 0; i < mJsonSessionQuestions.length(); i++){
                mSessionQuestions.add(new SessionQuestion(mJsonSessionQuestions.getJSONObject(i)));
            }
        }
        catch(IOException e){ }
        catch(JSONException je){ }
    }

    public SessionDataBaseHelper.SessionCursor querySessions(){
        return mHelper.querySessions(mUserId);
    }

    public ArrayList<Session> getSessions(){
        return mSessions;
    }

    public SessionDataBaseHelper.SessionDetailCursor querySessionDetails(){
        return mHelper.querySessionDetail(mSessionId);
    }

    public SessionQuestion getFirstQuestion(){
        SessionQuestion question = null;
//        try {
//            question = new SessionQuestion(mJsonSessionQuestions.getJSONObject(0));
            question = mSessionQuestions.get(0);
//        }
//        catch(JSONException je){}

        return question;
    }

    public void saveCurrentAnswer(int id, String answer){
        for (int i = 0; i < mJsonSessionQuestions.length(); i++) {
            try {
                // If there is a "next question", return it
                if (mJsonSessionQuestions.getJSONObject(i).getInt(SessionQuestion.JSON_ID) == id) {
                    // Get the current answer and save it in the list of previousAnswers
                    SessionQuestion q = new SessionQuestion(mJsonSessionQuestions.getJSONObject(i));
                    q.setAnswer(answer);
                    mPreviousQuestions.add(q);
                    mCurrentAnswer = answer;
                    break;
                }
            } catch (JSONException je) {
                Log.e(TAG, "JSON Error: " + je);
            }
        }
    }

    public SessionQuestion getNextQuestion(int id, String answer){
        SessionQuestion question = null;
        SessionQuestion tempQuestion = null;

        for (int i = 0; i < mSessionQuestions.size(); i++){
            tempQuestion = mSessionQuestions.get(i);
            // Find the current question in list
            if (tempQuestion.getId() == id){
                // Multi choice question
                if (tempQuestion.getPossibleAnswers().size() > 0){
                    for (int j = 0; j < tempQuestion.getPossibleAnswers().size(); j++){
                        if (tempQuestion.getPossibleAnswers().get(j).getValue().equals(answer)){
                            question = getSessionQuestionById(tempQuestion.getPossibleAnswers().get(j).getNextQuestion());
                            break;
                        }
                    }
                }
                else{
                   if (tempQuestion.getNextQuestion() != -1)
                       question = getSessionQuestionById(tempQuestion.getNextQuestion());
                }
            }
        }

        return question;
    }

    public SessionQuestion getSessionQuestionById(int id){
        SessionQuestion question = null;

        for (int i = 0; i < mSessionQuestions.size(); i++){
            if (mSessionQuestions.get(i).getId() == id)
                return mSessionQuestions.get(i);
        }

        return question;
    }

//

    public SessionQuestion getPreviousQuestion(){
        SessionQuestion question = null;

        int listSize = mPreviousQuestions.size();

        if (listSize > 0)
            question = mPreviousQuestions.get(listSize - 1);

        return question;
    }

    public void saveSession(){
        long sessionId = mHelper.insertSession(mUserId, mSessionTitle);
        SessionDetail detail;
        SessionQuestion question;

        for (int i = 0; i < mPreviousQuestions.size(); i++){
            detail = new SessionDetail();
            question = mPreviousQuestions.get(i);
            detail.setSessionId((int)sessionId);
            detail.setQuestionId(question.getId());
            detail.setQuestion(question.getQuestion());
            detail.setAnswer(question.getAnswer());

            mHelper.insertSessionDetail(detail);
        }
    }

//    public SessionQuestion getNextQuestion(int id, String answer){
//        SessionQuestion question = null;
//
//        for (int i = 0; i < mJsonSessionQuestions.length(); i++) {
//            try {
//                Log.i(TAG, "getNextQuestion: " + Integer.toString(mJsonSessionQuestions.getJSONObject(i).getInt(SessionQuestion.JSON_ID)));
//
//                // If there is a "next question", return it
//                int currentId = mJsonSessionQuestions.getJSONObject(i).getInt(SessionQuestion.JSON_ID);
//                if (currentId == id && (i + 1) < mJsonSessionQuestions.length()) {
//
//                    for(int prevId = 0; prevId < mPreviousQuestions.size(); prevId++){
//                        if (currentId == mPreviousQuestions.get(prevId).getId()){
//                            if (mPreviousQuestions.get(prevId).getAnswer() == answer) {
//
//                            }
//                        }
//                    }
//
//                    question = new SessionQuestion(mJsonSessionQuestions.getJSONObject(++i));
//                    break;
//                }
//            } catch (JSONException je) {
//                Log.e(TAG, "JSON Error: " + je);
//            }
//        }
//
//        return question;
//    }

}

