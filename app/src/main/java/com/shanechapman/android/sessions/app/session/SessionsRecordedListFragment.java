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
import android.widget.ListView;
import android.widget.TextView;

import com.shanechapman.android.sessions.app.R;
import com.shanechapman.android.sessions.app.db.SQLiteCursorLoader;
import com.shanechapman.android.sessions.app.login.UserManager;

import java.text.Format;
import java.text.SimpleDateFormat;

public class SessionsRecordedListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private int mUserId;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mUserId = getActivity().getIntent().getIntExtra(UserManager.USER_ID, -1);

        // Initialize the loader to load the list of sessions
        getLoaderManager().initLoader(0, null, this);
    }

    public void onListItemClick(ListView l, View v, int position, long id){
        Intent i = new Intent(getActivity(), SessionDetailListActivity.class);
        i.putExtra(UserManager.USER_ID, mUserId);
        i.putExtra(SessionManager.SESSION_ID, (int)id);

        startActivity(i);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        return new SessionRecordedListCursorLoader(getActivity(), mUserId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor){
        SessionsRecordedListCursorAdapter adapter = new SessionsRecordedListCursorAdapter(getActivity(), (SessionDataBaseHelper.SessionCursor)cursor);
        setListAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        setListAdapter(null);
    }

    // Cursor loader to load the data
    private static class SessionRecordedListCursorLoader extends SQLiteCursorLoader{

        private int mUserId;

        public SessionRecordedListCursorLoader(Context context, int userId){
            super(context);
            mUserId = userId;
        }

        @Override
        protected Cursor loadCursor(){
            SessionManager manager = new SessionManager(getContext());
            manager.setUserId(mUserId);
            return manager.querySessions();
        }

    }

    // Adapter for ListFragment
    private static class SessionsRecordedListCursorAdapter extends CursorAdapter {

        private SessionDataBaseHelper.SessionCursor mSessionCursor;

        public SessionsRecordedListCursorAdapter(Context context, SessionDataBaseHelper.SessionCursor cursor){
            super(context, cursor, 0);
            mSessionCursor = cursor;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent){
            // use a layout inflater to get a row view
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater.inflate(R.layout.list_item_session_recorded, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor){
            // get the session for the current row
            Session session = mSessionCursor.getSession();

            TextView title = (TextView)view.findViewById(R.id.session_title_txt);
            title.setText(session.getTitle());

            Format formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String s = formatter.format(session.getAdded());
            TextView added = (TextView)view.findViewById(R.id.session_added_txt);
            added.setText(s);

//            TextView sessionTakenTextView = (TextView)view;
//            sessionTakenTextView.setText("Session taken on: " + session.getAdded().toString());
        }

    }

}