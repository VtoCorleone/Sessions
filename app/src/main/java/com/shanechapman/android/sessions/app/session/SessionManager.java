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
    private int mPreviousQuestionIndex = 0;
    private boolean mHasFirstQuestionBeenAnswered = false;
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
        question = mSessionQuestions.get(0);
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
                    mPreviousQuestionIndex++;
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
        SessionQuestion tempQuestion;

        // Check if next question is in the previous questions list
        for (int p = 0; p < mPreviousQuestions.size(); p++){
            // Found a match
            if (mPreviousQuestions.get(p).getId() == id){
                // Check if this is the last question in the previous questions list
                if ((p + 1) != mPreviousQuestions.size()){
                    // Did they user give the same answer as the first time?  If so, get the next question from previous since it has the answer loaded in it
                    if (mPreviousQuestions.get(p).getAnswer().equals(answer)){
                        question = mPreviousQuestions.get(p + 1);
                        mPreviousQuestionIndex++;
                        break;
                    }
                    // Different answer.  Clear out previous list objects after this element
                    else{
                        for (int q = (mPreviousQuestions.size() - 1); q >= p; q--){
                            mPreviousQuestions.remove(q);
                        }
                        break;
                    }
                }
                else {
                    // This is the last question on the previous list, remove it and run the normal "next question" logic
                    mPreviousQuestions.remove(p);
                }
            }
        }

        // The next question was not in the previous question list
        if (question == null) {
            // Find the current question
            for (int i = 0; i < mSessionQuestions.size(); i++) {
                tempQuestion = mSessionQuestions.get(i);
                // Find the current question in list
                if (tempQuestion.getId() == id) {
                    // Save current answer
                    saveCurrentAnswer(id, answer);
                    // Multi choice question
                    if (tempQuestion.getPossibleAnswers().size() > 0) {
                        for (int j = 0; j < tempQuestion.getPossibleAnswers().size(); j++) {
                            // Find the selected answer
                            if (tempQuestion.getPossibleAnswers().get(j).getValue().equals(answer)) {
                                question = getSessionQuestionById(tempQuestion.getPossibleAnswers().get(j).getNextQuestion());
                                break;
                            }
                        }
                    }
                    // Input text or plain text
                    else {
                        // -1 is the the last question
                        if (tempQuestion.getNextQuestion() != -1)
                            question = getSessionQuestionById(tempQuestion.getNextQuestion());
                    }
                    break;
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

        // Only happens on first question first time if the user tries to go back
        if (listSize == 0 && !mHasFirstQuestionBeenAnswered) {
            question = getFirstQuestion();
        }
        else if (mPreviousQuestionIndex == 1) {
            question = mPreviousQuestions.get(0);
            mPreviousQuestionIndex--;
        }
        else if(mPreviousQuestionIndex == 0) {
            question = mPreviousQuestions.get(0);
        }
        else {
            question = mPreviousQuestions.get(--mPreviousQuestionIndex);
        }

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
            if (i == (mPreviousQuestions.size() - 1))
                // Remove the question from the last statement for detail list view
                detail.setQuestion(" ");
            else
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

