package com.shanechapman.android.sessions.app.session;

/**
 * Created by shanechapman on 5/10/14.
 */
public class SessionDetail {

    private int mId;
    private int mSessionId;
    private int mQuestionId;
    private String mQuestion;
    private String mAnswer;

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public int getSessionId() {
        return mSessionId;
    }

    public void setSessionId(int mSessionId) {
        this.mSessionId = mSessionId;
    }

    public int getQuestionId() {
        return mQuestionId;
    }

    public void setQuestionId(int mQuestionId) {
        this.mQuestionId = mQuestionId;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public void setQuestion(String mQuestion) {
        this.mQuestion = mQuestion;
    }

    public String getAnswer() {
        return mAnswer;
    }

    public void setAnswer(String mAnswer) {
        this.mAnswer = mAnswer;
    }


}
