package com.shanechapman.android.sessions.app.login;

import android.app.Activity;
import android.support.v4.app.LoaderManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shanechapman.android.sessions.app.R;
import com.shanechapman.android.sessions.app.home.HomeActivity;


/**
 * Created by shanechapman on 5/3/14.
 */
public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";
    private static final String ARG_USER_ID = "USER_ID";
    private static final String ARG_USER_NAME = "USER_NAME";
    private static final String ARG_USER_PASS = "USER_PASS";

    private static final int INTENT_REQUEST_REGISTER_USER = 1;

    private User mUser;

    private EditText mNameEditText;
    private EditText mPassEditText;
    private Button mLoginButton;
    private Button mRegisterButton;
    private TextView mRegisterSuccess;
    private LoaderManager mLoadManager;
    private TextView mErrTxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mErrTxt = (TextView)view.findViewById(R.id.err_lbl);

        mNameEditText = (EditText)view.findViewById(R.id.userName_txt);
        mPassEditText = (EditText)view.findViewById(R.id.password_txt);
        mRegisterSuccess = (TextView)view.findViewById(R.id.register_success_lbl);

        mLoginButton = (Button)view.findViewById(R.id.login_btn);
        mLoginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // Get name and password
                String name = mNameEditText.getText().toString();
                String pass = mPassEditText.getText().toString();

                mRegisterSuccess.setVisibility(View.INVISIBLE);

                if (name == null || name.isEmpty() || pass == null || pass.isEmpty()){
                    mErrTxt.setVisibility(View.VISIBLE);
                    return;
                }

                Log.i(TAG, "Login clicked");
                // Put name and password into Bundle object to pass into loader
                Bundle args = new Bundle();
                args.putString(ARG_USER_NAME, name);
                args.putString(ARG_USER_PASS, pass);
                if (mLoadManager == null) {
                    // If this is the first click, create a new loaderManager and init
                    mLoadManager = getLoaderManager();
                    mLoadManager.initLoader(0, args, new UserLoaderCallbacks());
                }
                else{
                    // If this is any click after the first, tell the loader to restart
                    mLoadManager.restartLoader(0, args, new UserLoaderCallbacks());
                }
            }
        });

        mRegisterButton = (Button)view.findViewById(R.id.register_btn);
        mRegisterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // If the user successfully registers, return back to the login
                Intent i = new Intent(getActivity(), RegisterActivity.class);
                startActivityForResult(i, INTENT_REQUEST_REGISTER_USER);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (INTENT_REQUEST_REGISTER_USER == requestCode && resultCode == Activity.RESULT_OK)
            // Show the user that they successfully registered
            mRegisterSuccess.setVisibility(View.VISIBLE);
    }

    private void validateUser(){
        // If a user is found in the DB, they are valid and send them to the home screen
        if (mUser != null){
            Intent i = new Intent(getActivity(), HomeActivity.class);
            i.putExtra(UserManager.USER_ID, mUser.getId());
            startActivity(i);
        }
    }

    private class UserLoaderCallbacks implements LoaderCallbacks<User> {

        @Override
        public Loader<User> onCreateLoader(int id, Bundle args) {
            return new UserLoader(getActivity(), args.getString(ARG_USER_NAME), args.getString(ARG_USER_PASS));
        }

        @Override
        public void onLoadFinished(Loader<User> loader, User user) {
            mUser = user;
            validateUser();
        }

        @Override
        public void onLoaderReset(Loader<User> loader) {
            // do nothing
        }
    }

}
