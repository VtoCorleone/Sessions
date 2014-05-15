package com.shanechapman.android.sessions.app.session;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shanechapman.android.sessions.app.R;
import com.shanechapman.android.sessions.app.db.SQLiteCursorLoader;

public class SessionDetailListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private int mSessionId;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mSessionId = getActivity().getIntent().getIntExtra(SessionManager.SESSION_ID, -1);
        getActivity().setTitle("Session Details");

        // Initialize the loader to load the list of sessions
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        return new SessionDetailListCursorLoader(getActivity(), mSessionId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor){
        SessionDetailListCursorAdapter adapter = new SessionDetailListCursorAdapter(getActivity(), (SessionDataBaseHelper.SessionDetailCursor)cursor);
        setListAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        setListAdapter(null);
    }



    // Cursor loader to load the data
    private static class SessionDetailListCursorLoader extends SQLiteCursorLoader {

        private int mSessionId;

        public SessionDetailListCursorLoader(Context context, int sessionId){
            super(context);
            mSessionId = sessionId;
        }

        @Override
        protected Cursor loadCursor(){
            SessionManager manager = new SessionManager(getContext());
            manager.setSessionId(mSessionId);
            return manager.querySessionDetails();
        }

    }

    // Adapter for ListFragment
    private static class SessionDetailListCursorAdapter extends CursorAdapter {

        private SessionDataBaseHelper.SessionDetailCursor mCursor;
        private int counter = 1;

        public SessionDetailListCursorAdapter(Context context, SessionDataBaseHelper.SessionDetailCursor cursor){
            super(context, cursor, 0);
            mCursor = cursor;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent){
            // use a layout inflater to get a row view
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater.inflate(R.layout.list_item_session_detail, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor){
            // get the session for the current row
            SessionDetail detail = mCursor.getSessionDetail();

            TextView counterTxt = (TextView)view.findViewById(R.id.detail_counter_txt);
            counterTxt.setText(counter++ + ") ");

            TextView question = (TextView)view.findViewById(R.id.detail_question_txt);
            question.setText(detail.getQuestion());

            TextView answer = (TextView)view.findViewById(R.id.detail_answer_txt);
            answer.setText(detail.getAnswer());
        }

    }

}
