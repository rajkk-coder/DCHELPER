package com.example.dchelper.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dchelper.R;
import com.example.dchelper.admin.AdminDashboardActivity;
import com.example.dchelper.scholar.homePage.ScholarDashboardActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    private long backPressedTime;
    private Toast backToast;
    GoogleSignInClient mGoogleSignInClient;
    final static int RC_SIGN_IN=100;
    SignInButton signInButton;
    final FirebaseAuth mAuth=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signInButton=findViewById(R.id.signInButton);
        String token="662739377428-a8iohjrun6k730qeg8d9jajers2h680k.apps.googleusercontent.com";
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(token)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        signInButton.setOnClickListener(view -> {
            signIn();
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if(account!=null){
            if(account.getEmail().equals("dchelper23@gmail.com"))
                startActivity(new Intent(this,AdminDashboardActivity.class));
            else if(account.getEmail().endsWith("@nitc.ac.in") && account.getEmail().contains("_p") ||
                    account.getEmail().endsWith("@nitc.ac.in") && account.getEmail().contains("_b"))
                startActivity(new Intent(this,ScholarDashboardActivity.class));
            finish();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //Login as an admin
        if(acct.getEmail().equals("dchelper23@gmail.com")){
            AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            mAuth.signInWithCredential(credential)
                    .addOnSuccessListener(this, authResult -> {
                        startActivity(new Intent(LoginActivity.this, AdminDashboardActivity.class));
                        finish();
                    })
                    .addOnFailureListener(this, e -> Toast.makeText(LoginActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show());
        }
        //Login as scholar: in this case scholar is B.tech student
        else if(acct.getEmail().contains("_b") && acct.getEmail().endsWith("@nitc.ac.in")){
            AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            mAuth.signInWithCredential(credential)
                    .addOnSuccessListener(this, authResult -> {

                        startActivity(new Intent(LoginActivity.this, ScholarDashboardActivity.class));
                        finish();
                        //Toast.makeText(this, "Finally Happy!!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(this, e -> Toast.makeText(LoginActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show());
        }
        //Else user is not authenticated
        else {
            Toast.makeText(this, "Please choose a valid email Id", Toast.LENGTH_SHORT).show();
            signOut();
        }

    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, task -> {
                    // ...
                    Toast.makeText(LoginActivity.this, "See you again", Toast.LENGTH_SHORT).show();
                });
    }
    @Override
    public void onBackPressed(){
        if(backPressedTime+2000>System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
            finish();
        }else {
            backToast=Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime=System.currentTimeMillis();
    }
}