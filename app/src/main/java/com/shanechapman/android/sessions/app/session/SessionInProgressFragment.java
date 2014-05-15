package com.shanechapman.android.sessions.app.session;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.shanechapman.android.sessions.app.R;
import com.shanechapman.android.sessions.app.home.HomeActivity;
import com.shanechapman.android.sessions.app.login.UserManager;

import org.w3c.dom.Text;

import java.security.MessageDigest;
import java.util.ArrayList;

/**
 * Created by shanechapman on 5/6/14.
 */
public class SessionInProgressFragment extends Fragment {

    public static final String INTENT_IS_FROM_SESSION = "com.shanechapman.android.sessions.app.IS_FROM_SESSION";

    private Button mPreviousBtn;
    private Button mNextBtn;
    private Button mCompleteBtn;
    private TextView mQuestionText;
    private RadioGroup mAnswerRadioGroup;
    private EditText mAnswerEdit;

    private SessionManager mSessionManager;
    private SessionQuestion mCurrentQuestion;

    private int mUserId;

    private float downX;
    private float upX;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Intent i = getActivity().getIntent();
        mUserId = i.getIntExtra(UserManager.USER_ID, -1);
        int sessionId = i.getIntExtra(SessionManager.SESSION_ID, -1);
        String sessionTitle = i.getStringExtra(SessionManager.SESSION_TITLE);
        mSessionManager = new SessionManager(getActivity());
        mSessionManager.setUserId(mUserId);
        mSessionManager.setSessionId(sessionId);
        mSessionManager.setSessionTitle(sessionTitle);
        mSessionManager.setSession();
        mCurrentQuestion = mSessionManager.getFirstQuestion();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_session_in_progress, container, false);

        assert view != null;
        view.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event){

                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        downX = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        upX = event.getX();
                        // swipe left to right - get previous question
                        if (downX < upX){
                            navigatePrevious();
                        }
                        // swipe right to left - get next question
                        else if (downX > upX){
                            navigateNext(v);
                        }
                        break;
                }

                return true;
            }
        });

        mQuestionText = (TextView)view.findViewById(R.id.session_question_txt);
        mQuestionText.setText(mCurrentQuestion.getQuestion());

        mAnswerEdit = (EditText)view.findViewById(R.id.session_answer_edit);

        mAnswerRadioGroup = (RadioGroup)view.findViewById(R.id.answer_radio_group);

        if (mCurrentQuestion.getType().equals(SessionQuestion.TYPE_MULTI)){
            mAnswerRadioGroup.setVisibility(View.VISIBLE);
            setRadioGroupButtons();
        }
        else if (mCurrentQuestion.getType().equals(SessionQuestion.TYPE_OPEN)){
            mAnswerEdit.setVisibility(View.VISIBLE);
        }
        else if (mCurrentQuestion.getType().equals(SessionQuestion.TYPE_GENERAL)){

        }

//        mPreviousBtn = (Button)view.findViewById(R.id.previous_btn);
//        mPreviousBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                mCurrentQuestion = mSessionManager.getPreviousQuestion();
//                if (mCurrentQuestion != null) {
//                    mQuestionText.setText(mCurrentQuestion.getQuestion());
//                    mNextBtn.setVisibility(View.VISIBLE);
//                    mCompleteBtn.setVisibility(View.INVISIBLE);
//                }
//                else{
//                    mPreviousBtn.setEnabled(false);
//                }
//            }
//        });
//
//        mNextBtn = (Button)view.findViewById(R.id.next_btn);
//        mNextBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mSessionManager.setAnswer(mCurrentQuestion.getId(), mAnswerEdit.getText().toString());
//                mCurrentQuestion = mSessionManager.getNextQuestion(mCurrentQuestion.getId());
//                mQuestionText.setText(mCurrentQuestion.getQuestion());
//
//                if (mSessionManager.getNextQuestion(mCurrentQuestion.getId()) != null) {
//                    mPreviousBtn.setEnabled(true);
//                }
//                else{
//                    mNextBtn.setVisibility(View.INVISIBLE);
//                    mCompleteBtn.setVisibility(View.VISIBLE);
//                }
//            }
//        });

        mCompleteBtn = (Button)view.findViewById(R.id.complete_btn);
        mCompleteBtn.setVisibility(View.INVISIBLE);
        mCompleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (mCurrentQuestion.getNextQuestion() == -1)
                    mSessionManager.saveCurrentAnswer(mCurrentQuestion.getId(), mCurrentQuestion.getQuestion());
                else
                    mSessionManager.saveCurrentAnswer(mCurrentQuestion.getId(), mAnswerEdit.getText().toString());

                mSessionManager.saveSession();

                Intent i = new Intent(getActivity(), HomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra(INTENT_IS_FROM_SESSION, true);
                i.putExtra(UserManager.USER_ID, mUserId);
                startActivity(i);
            }
        });

        return view;
    }

    public void navigatePrevious(){
        mCurrentQuestion = mSessionManager.getPreviousQuestion();
        if (mCurrentQuestion != null) {
            mQuestionText.setText(mCurrentQuestion.getQuestion());
            //mNextBtn.setVisibility(View.VISIBLE);
//                                mCompleteBtn.setVisibility(View.INVISIBLE);
//
//                                if (mCurrentQuestion.getType().equals(SessionQuestion.TYPE_MULTI)){
//                                    setRadioGroupButtons();
//                                    for (int i = 0; i < mCurrentQuestion.getPossibleAnswers().size(); i++){
//                                        if (mCurrentQuestion.getPossibleAnswers().get(i).getValue().equals(mCurrentQuestion.getAnswer())){
//                                            mAnswerRadioGroup.check(i);
//                                        }
//                                    }
//                                }
//                                else if (mCurrentQuestion.getType().equals(SessionQuestion.TYPE_OPEN)){
//                                    if (mCurrentQuestion.getAnswer() != null)
//                                        mAnswerEdit.setText(mCurrentQuestion.getAnswer());
//                                }
        }
        else{
            //mPreviousBtn.setEnabled(false);
        }
//                            setDisplayInputs(mCurrentQuestion.getType());
        formatDisplay();
    }

    public void navigateNext(View v){

        if (mCurrentQuestion != null) {
            int curId = mCurrentQuestion.getId();
            String curAnswer = null;
            if (mCurrentQuestion.getType().equals(SessionQuestion.TYPE_MULTI)){
                int selected = mAnswerRadioGroup.getCheckedRadioButtonId();
                RadioButton b = (RadioButton)v.findViewById(selected);
                curAnswer = b.getText().toString();
                mCurrentQuestion = mSessionManager.getNextQuestion(mCurrentQuestion.getId(), curAnswer);
                //setDisplayInputs(mCurrentQuestion.getType());

//                                    setRadioGroupButtons();


            }
            else if (mCurrentQuestion.getType().equals(SessionQuestion.TYPE_OPEN)) {
                mCurrentQuestion = mSessionManager.getNextQuestion(mCurrentQuestion.getId(), mAnswerEdit.getText().toString());
                //setDisplayInputs(mCurrentQuestion.getType());

//                                    if (mCurrentQuestion.getAnswer() == null)
//                                        mAnswerEdit.setText("");
//                                    else
//                                        mAnswerEdit.setText(mCurrentQuestion.getAnswer());
            }
            else if (mCurrentQuestion.getType().equals(SessionQuestion.TYPE_GENERAL)) {

            }

//                                setDisplayInputs(mCurrentQuestion.getType());

            formatDisplay();

//                                mQuestionText.setText(mCurrentQuestion.getQuestion());

//                                if (mCurrentQuestion.getNextQuestion() == -1) {
//                                    mCompleteBtn.setVisibility(View.VISIBLE);
//                                }
//                                mCompleteBtn.setVisibility(mCurrentQuestion.getNextQuestion() == -1 ? View.VISIBLE : View.INVISIBLE);
        }

    }

    public void formatDisplay(){
        if (mCurrentQuestion.getType().equals(SessionQuestion.TYPE_MULTI)){
            setRadioGroupButtons();
            if (mCurrentQuestion.getAnswer() != null){
                for (int i = 0; i < mCurrentQuestion.getPossibleAnswers().size(); i++){
                    if (mCurrentQuestion.getPossibleAnswers().get(i).getValue().equals(mCurrentQuestion.getAnswer())){
                        mAnswerRadioGroup.check(i);
                    }
                }
            }

            mAnswerRadioGroup.setVisibility(View.VISIBLE);
            mAnswerEdit.setVisibility(View.INVISIBLE);
        }
        else if (mCurrentQuestion.getType().equals(SessionQuestion.TYPE_OPEN)){
            if (mCurrentQuestion.getAnswer() == null)
                mAnswerEdit.setText("");
            else
                mAnswerEdit.setText(mCurrentQuestion.getAnswer());

            mAnswerRadioGroup.setVisibility(View.INVISIBLE);
            mAnswerEdit.setVisibility(View.VISIBLE);
        }
        else if (mCurrentQuestion.getType().equals(SessionQuestion.TYPE_GENERAL)){
            mAnswerRadioGroup.setVisibility(View.INVISIBLE);
            mAnswerEdit.setVisibility(View.INVISIBLE);
        }

        mQuestionText.setText(mCurrentQuestion.getQuestion());
        mCompleteBtn.setVisibility(mCurrentQuestion.getNextQuestion() == -1 ? View.VISIBLE : View.INVISIBLE);
    }

    public void setRadioGroupButtons(){
        mAnswerRadioGroup.clearCheck();
        mAnswerRadioGroup.removeAllViews();
        RadioButton rb;
        for (int i = 0; i < mCurrentQuestion.getPossibleAnswers().size(); i++) {
            rb = new RadioButton(getActivity());
            rb.setId(Integer.parseInt(mCurrentQuestion.getPossibleAnswers().get(i).getKey()));
            rb.setText(mCurrentQuestion.getPossibleAnswers().get(i).getValue());
            mAnswerRadioGroup.addView(rb);
        }
    }

    public void setDisplayInputs(String type){
        if (type.equals(SessionQuestion.TYPE_MULTI)){
            mAnswerRadioGroup.setVisibility(View.VISIBLE);
            mAnswerEdit.setVisibility(View.INVISIBLE);
        }
        else if (type.equals(SessionQuestion.TYPE_OPEN)){
            mAnswerRadioGroup.setVisibility(View.INVISIBLE);
            mAnswerEdit.setVisibility(View.VISIBLE);
        }
        else if (type.equals(SessionQuestion.TYPE_GENERAL)){
            mAnswerRadioGroup.setVisibility(View.INVISIBLE);
            mAnswerEdit.setVisibility(View.INVISIBLE);
        }
    }

}
