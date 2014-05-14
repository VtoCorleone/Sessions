package com.shanechapman.android.sessions.app.session;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.shanechapman.android.sessions.app.SingleFragmentActivity;
import com.shanechapman.android.sessions.app.login.UserManager;

public class SessionDetailListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return new SessionDetailListFragment();
    }
}
