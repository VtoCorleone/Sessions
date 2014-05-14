package com.shanechapman.android.sessions.app.home;

import android.support.v4.app.Fragment;

import com.shanechapman.android.sessions.app.SingleFragmentActivity;

public class HomeActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return new HomeFragment();
    }

}
