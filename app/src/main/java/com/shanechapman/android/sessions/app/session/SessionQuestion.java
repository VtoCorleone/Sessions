package com.shanechapman.android.sessions.app.session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shanechapman on 5/8/14.
 */
public class SessionQuestion {

    public static final String TYPE_MULTI = "multi";
    public static final String TYPE_OPEN = "open";
    public static final String TYPE_GENERAL = "general";

    public static final String JSON_ID = "id";
    private static final String JSON_QUESTION = "question";
    private static final String JSON_TYPE = "type";
    private static final String JSON_POSSIBLE_ANSWERS = "possibleAnswers";
    private static final String JSON_NEXT_QUESTION = "nextQuestion";

    private int mId;
    private String mQuestion;
    private String mType;
    private ArrayList<PossibleAnswer> mPossibleAnswers;
    private int mNextQuestion;
    private String mAnswer;

    public SessionQuestion(){
         mPossibleAnswers = new ArrayList<PossibleAnswer>();
    }

    public SessionQuestion(JSONObject json) throws JSONException {
        mPossibleAnswers = new ArrayList<PossibleAnswer>();
        mId = json.getInt(JSON_ID);
        mQuestion = json.getString(JSON_QUESTION);
        mType = json.getString(JSON_TYPE);
        JSONArray array = json.getJSONArray(JSON_POSSIBLE_ANSWERS);
        if (array.length() > 0){
            JSONObject possibleAnswerJson;
            PossibleAnswer possibleAnswer;
            for (int i = 0; i < array.length(); i++){
                possibleAnswerJson = array.getJSONObject(i);
                String key = "" + i;
                possibleAnswer = new PossibleAnswer(key, possibleAnswerJson.getString(key), possibleAnswerJson.getInt(JSON_NEXT_QUESTION));
                mPossibleAnswers.add(possibleAnswer);
            }
        }
        if (mPossibleAnswers.size() == 0)
            mNextQuestion = json.getInt(JSON_NEXT_QUESTION);
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId);
        json.put(JSON_QUESTION, mQuestion);
        json.put(JSON_TYPE, mType);
        return json;
    }

    public int getId(){
        return mId;
    }

    public void setId(int id){
        mId = id;
    }

    public String getQuestion(){
        return mQuestion;
    }

    public void setQuestion(String question){
        mQuestion = question;
    }

    public String getAnswer() {
        return mAnswer;
    }

    public void setAnswer(String answer){
        mAnswer = answer;
    }

    public String getType(){
        return mType;
    }

    public void setType(String type){
        mType = type;
    }

    public ArrayList<PossibleAnswer> getPossibleAnswers() {
        return mPossibleAnswers;
    }

    public void setPossibleAnswers(ArrayList<PossibleAnswer> mPossibleAnswers) {
        this.mPossibleAnswers = mPossibleAnswers;
    }

    public int getNextQuestion() {
        return mNextQuestion;
    }

    public void setNextQuestion(int mNextQuestion) {
        this.mNextQuestion = mNextQuestion;
    }

    public class PossibleAnswer{
        private int mNextQuestion;
        private String mKey;
        private String mValue;

        public PossibleAnswer(String key, String value, int nextQuestion){
            mKey = key;
            mValue = value;
            mNextQuestion = nextQuestion;
        }

        public int getNextQuestion() {
            return mNextQuestion;
        }

        public void setNextQuestion(int nextQuestion) {
            mNextQuestion = nextQuestion;
        }

        public String getKey() {
            return mKey;
        }

        public void setKey(String key) {
            mKey = key;
        }

        public String getValue(){
            return mValue;
        }

        public void setValue(String value){
            mValue = value;
        }
    }

}
