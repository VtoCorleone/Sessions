package com.shanechapman.android.sessions.app.login;

import android.support.v4.app.Fragment;

import com.shanechapman.android.sessions.app.SingleFragmentActivity;

public class LoginActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return new LoginFragment();
    }
}
