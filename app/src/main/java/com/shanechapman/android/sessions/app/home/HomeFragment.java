package com.shanechapman.android.sessions.app.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.shanechapman.android.sessions.app.R;
import com.shanechapman.android.sessions.app.login.UserManager;
import com.shanechapman.android.sessions.app.session.NewSessionListActivity;
import com.shanechapman.android.sessions.app.session.SessionInProgressFragment;
import com.shanechapman.android.sessions.app.session.SessionsRecordedListActivity;

/**
 * Created by shanechapman on 5/3/14.
 */
public class HomeFragment extends Fragment {

    public int INTENT_REQUEST_IN_PROGRESS_SESSION = 1;

    private int mUserId;
    private TextView mSessionComplete;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mUserId = getActivity().getIntent().getIntExtra(UserManager.USER_ID, -1);

        mSessionComplete = (TextView)view.findViewById(R.id.session_completed);
        Intent i = getActivity().getIntent();
        Boolean isSessionComplete = i.getBooleanExtra(SessionInProgressFragment.INTENT_IS_FROM_SESSION, false);
        if (isSessionComplete) mSessionComplete.setVisibility(View.VISIBLE);

        Button mNewSession = (Button)view.findViewById(R.id.new_session_btn);
        mNewSession.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(getActivity(), NewSessionListActivity.class);
                i.putExtra(UserManager.USER_ID, mUserId);
                startActivity(i);
            }
        });

        Button pastSessions = (Button)view.findViewById(R.id.list_sessions_btn);
        pastSessions.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(getActivity(), SessionsRecordedListActivity.class);
                i.putExtra(UserManager.USER_ID, mUserId);
                startActivity(i);
            }
        });

        return view;
    }
}
