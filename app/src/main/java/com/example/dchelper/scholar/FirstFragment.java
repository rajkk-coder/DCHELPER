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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.dchelper.R;
import com.example.dchelper.login.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


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
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Slot slot = null;
                            for (DataSnapshot dt:snapshot.getChildren())
                                slot=dt.getValue(Slot.class);
                            long difference_In_Time = 0;

                            assert slot != null;
                            if(slot.getStatus().equals("Blocked")){
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf
                                        = new SimpleDateFormat(
                                        "dd-MM-yyyy HH:mm:ss");
                                String end_date = sdf.format(new Date());
                                String start_date = slot.getCurrentDateAndTime();
                                try {
                                    Date d1 = sdf.parse(start_date);
                                    Date d2 = sdf.parse(end_date);
                                    assert d2 != null;
                                    assert d1 != null;
                                    difference_In_Time
                                            = d2.getTime() - d1.getTime();
                                }
                                catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if(difference_In_Time > 60*1000) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("scholars")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("UpComingEvent")
                                            .removeValue();
                                }
                                else {
                                    venue.setText(slot.getVenue());
                                    status.setText(slot.getStatus());
                                    status.setTextColor(R.color.purple_200);
                                    time.setText(slot.getStart_time() + "-" + slot.getEnd_time());
                                    date.setText(slot.getDate());
                                    cardView.setVisibility(View.VISIBLE);
                                }
                            }
                            else{
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf
                                        = new SimpleDateFormat(
                                        "dd-MM-yyyy HH:mm:ss");
                                String end_date = slot.getDate() +" "+ slot.getEnd_time() +":00";
                                String start_date = sdf.format(new Date());
                                try {
                                    Date d1 = sdf.parse(start_date);
                                    Date d2 = sdf.parse(end_date);
                                    assert d2 != null;
                                    assert d1 != null;
                                    difference_In_Time
                                            = d1.getTime() - d2.getTime();
                                }
                                catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if(difference_In_Time > 0) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("scholars")
                                            .child(user.getUid())
                                            .child("History")
                                    .push().setValue(slot);

                                    FirebaseDatabase.getInstance().getReference()
                                            .child("scholars")
                                            .child(user.getUid())
                                            .child("UpComingEvent").removeValue();
                                }
                                else {
                                    venue.setText(slot.getVenue());
                                    status.setText(slot.getStatus());
                                    status.setTextColor(R.color.teal_700);
                                    time.setText(slot.getStart_time() + "-" + slot.getEnd_time());
                                    date.setText(slot.getDate());
                                    cardView.setVisibility(View.VISIBLE);
                                }

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        cardView.setOnClickListener(view1 -> {
            //You can pass "status"
            FirebaseDatabase.getInstance().getReference()
                    .child("scholars")
                    .child(user.getUid())
                    .child("UpComingEvent")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @SuppressLint("ResourceAsColor")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Slot slot = null;
                                String path="";
                                for (DataSnapshot dt:snapshot.getChildren()){
                                    slot=dt.getValue(Slot.class);
                                    path= dt.getKey();
                                }
                                assert slot != null;
                                venue.setText(slot.getVenue());
                                status.setText(slot.getStatus());

                                if(slot.getStatus().equals("Blocked")){
                                    Intent intent=new Intent(getContext(),BookABlockedSlot.class);
                                    Bundle bundle=new Bundle();
                                    bundle.putString("date",slot.getDate());
                                    bundle.putString("start_time",slot.getStart_time());
                                    bundle.putString("end_time",slot.getEnd_time());
                                    bundle.putString("venue",slot.getVenue());
                                    bundle.putString("Mode","DC");
                                    bundle.putString("owner",slot.getOwner());
                                    bundle.putString("path",path);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                                else{
                                    Intent intent=new Intent(getContext(),CancelASlot.class);
                                    Bundle bundle=new Bundle();
                                    bundle.putString("date",slot.getDate());
                                    bundle.putString("start_time",slot.getStart_time());
                                    bundle.putString("end_time",slot.getEnd_time());
                                    bundle.putString("venue",slot.getVenue());
                                    bundle.putString("Mode","Dc");
                                    bundle.putString("owner",slot.getOwner());
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        });

        btn.setOnClickListener(view12 -> {
            Intent intent=new Intent(getContext(),managePanelMembers.class);
            startActivity(intent);
        });

        logout.setOnClickListener(view13 -> {
            signOut();
            startActivity(new Intent(getContext(),LoginActivity.class));
        });
        return view;
    }

    private void signOut() {
        String token="662739377428-a8iohjrun6k730qeg8d9jajers2h680k.apps.googleusercontent.com";
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(token)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(requireView().getContext(), gso);
        mGoogleSignInClient.signOut();
    }
}
