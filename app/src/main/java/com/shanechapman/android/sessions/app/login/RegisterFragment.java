package com.shanechapman.android.sessions.app.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shanechapman.android.sessions.app.R;

/**
 * Created by shanechapman on 5/4/14.
 */
public class RegisterFragment extends Fragment {

    public static final String NEW_USER_ID = "com.shanechapman.android.sessions.app.new_user_id";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getActivity().setTitle("New User Registration");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        final EditText userTxt = (EditText)view.findViewById(R.id.userName_txt);
        final EditText passTxt = (EditText)view.findViewById(R.id.password_txt);
        final TextView err = (TextView)view.findViewById(R.id.err_lbl);

        Button mRegisterUserBtn = (Button)view.findViewById(R.id.register_btn);
        mRegisterUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = userTxt.getText().toString();
                String pass = passTxt.getText().toString();

                if (name == null || name.isEmpty() || pass == null || pass.isEmpty()){
                    err.setVisibility(View.VISIBLE);
                    return;
                }

                long newUserId = UserManager.get(getActivity()).insertUser(name, pass);
                if (newUserId > 0){
                    Intent i = new Intent();
                    i.putExtra(NEW_USER_ID, newUserId);
                    getActivity().setResult(Activity.RESULT_OK, i);
                    getActivity().finish();
                }
            }
        });

        Button cancelBtn = (Button)view.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getActivity().finish();
            }
        });

        return view;
    }

}
