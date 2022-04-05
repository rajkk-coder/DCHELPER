package com.example.dchelper.scholar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.*;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.dchelper.R;
import com.example.dchelper.login.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FirstFragment extends Fragment {
    private GoogleSignInClient mGoogleSignInClient;

    private FirebaseUser user;
    public FirstFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_first, container, false);

        Button btn=view.findViewById(R.id.button);
        TextView welcome=view.findViewById(R.id.textView6);
        user=FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null)
            welcome.setText(user.getDisplayName());
        //Button logout=view.findViewById(R.id.scholarLogout);
        ImageButton logout=view.findViewById(R.id.firstFragment);
        
        //CardView
        CardView cardView=view.findViewById(R.id.dash_cardView);
        TextView venue=view.findViewById(R.id.dash_venue);
        TextView status=view.findViewById(R.id.dash_status);
        TextView time=view.findViewById(R.id.dash_time);
        TextView date=view.findViewById(R.id.dash_date);

        cardView.setVisibility(View.GONE);
        //Blocked or Booked status

        FirebaseDatabase.getInstance().getReference()
                .child("scholars")
                .child(user.getUid())
                .child("UpComingEvent")
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Slot slot = null;
                            for (DataSnapshot dt:snapshot.getChildren())
                                slot=dt.getValue(Slot.class);
                            venue.setText(slot.getVenue());
                            status.setText(slot.getStatus());
                            if(slot.getStatus().equals("Blocked"))
                                status.setTextColor(R.color.purple_200);
                            else
                                status.setTextColor(R.color.teal_700);
                            time.setText(slot.getStart_time()+"-"+slot.getEnd_time());
                            date.setText(slot.getDate());
                            cardView.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //You can pass "status"
                Toast.makeText(getContext(), "Sreyansh Your Task !!!!", Toast.LENGTH_SHORT).show();
            }
        });
        
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
