package com.example.dchelper.scholar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.*;
import androidx.fragment.app.Fragment;

import com.example.dchelper.R;
import com.example.dchelper.login.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;


public class FirstFragment extends Fragment {
    private GoogleSignInClient mGoogleSignInClient;
    public FirstFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_first, container, false);

        Button btn=view.findViewById(R.id.button);
        //Button logout=view.findViewById(R.id.scholarLogout);
        TextView logout=view.findViewById(R.id.firstFragment);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),managePanelMembers.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
                startActivity(new Intent(getContext(),LoginActivity.class));
            }
        });
        return view;
    }

    private void signOut() {
        String token="662739377428-a8iohjrun6k730qeg8d9jajers2h680k.apps.googleusercontent.com";
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(token)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getView().getContext(), gso);
        mGoogleSignInClient.signOut();
    }
}
