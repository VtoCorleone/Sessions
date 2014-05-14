package com.shanechapman.android.sessions.app.session;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.shanechapman.android.sessions.app.R;
import com.shanechapman.android.sessions.app.login.UserManager;

import java.util.ArrayList;

/**
 * Created by shanechapman on 5/6/14.
 */
public class NewSessionListFragment extends ListFragment {

    ArrayList<Session> mSessions;
    private int mUserId;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Create a New Health Session");

        mUserId = getActivity().getIntent().getIntExtra(UserManager.USER_ID, -1);

        SessionManager manager = new SessionManager(getActivity());
        manager.setUserId(mUserId);

        mSessions = manager.getSessions();
        NewSessionAdapter adapter = new NewSessionAdapter(mSessions);
        setListAdapter(adapter);
        setRetainInstance(true);
    }

    public void onListItemClick(ListView l, View v, int position, long id){
        // get the session from the adapter
        Session s = ((NewSessionAdapter)getListAdapter()).getItem(position);

        Intent i = new Intent(getActivity(), SessionInProgressActivity.class);
        i.putExtra(UserManager.USER_ID, mUserId);
        i.putExtra(SessionManager.SESSION_ID, s.getId());
        i.putExtra(SessionManager.SESSION_TITLE, s.getTitle());
        startActivity(i);
    }

    private class NewSessionAdapter extends ArrayAdapter<Session>{

        public NewSessionAdapter(ArrayList<Session> sessions){
            super(getActivity(), android.R.layout.simple_list_item_1, sessions);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            // if we weren't given a view, inflate one
            if (convertView == null)
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_new_session, null);

            Session session = getItem(position);

            TextView title = (TextView)convertView.findViewById(R.id.new_session_list_item_title);
            title.setText(session.getTitle());

            return convertView;
        }

    }

}
