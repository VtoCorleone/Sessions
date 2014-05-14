package com.shanechapman.android.sessions.app.session;

import android.support.v4.app.Fragment;
import com.shanechapman.android.sessions.app.SingleFragmentActivity;

public class SessionsRecordedListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return new SessionsRecordedListFragment();
    }

}
