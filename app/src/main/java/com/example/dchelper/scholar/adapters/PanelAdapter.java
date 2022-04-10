package com.example.dchelper.scholar.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dchelper.R;
import com.example.dchelper.admin.faculty.Faculty;
import com.example.dchelper.scholar.panelMembers.PanelMember;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PanelAdapter extends RecyclerView.Adapter<PanelAdapter.MemberViewHolder> {
    private final Context context;
    private final ArrayList<Faculty>faculties;
    private  FirebaseUser user;
    private String mode;

    public PanelAdapter(Context context,ArrayList<Faculty> faculties,String mode) {
        this.faculties = faculties;
        this.context = context;
        this.mode=mode;
        user=FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.add_panel_member_card,parent,false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Faculty faculty=faculties.get(position);
        holder.name.setText(faculty.getName());
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference myRef=FirebaseDatabase.getInstance().getReference().child("scholars")
                        .child(user.getUid()).child("PanelMember").child(mode);
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int flag=0;
                        for (DataSnapshot dt:snapshot.getChildren()){
                            PanelMember panelMember=dt.getValue(PanelMember.class);
                                if (flag==0 && panelMember.getFacultyName().equals(faculties.get(position).getName())){
                                    flag=1;
                                    holder.checkBox.setVisibility(View.VISIBLE);
                                    Toast.makeText(context, "Faculty already present", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                        }

                        if (flag==0){
                            PanelMember panelMember=new PanelMember(faculty.getName(),mode, user.getDisplayName());
                            FirebaseDatabase.getInstance().getReference()
                                    .child("scholars")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("PanelMember").child(mode)
                                    .push().setValue(panelMember).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(context, "Added Successfully", Toast.LENGTH_SHORT).show();
                                        holder.add.setVisibility(View.GONE);
                                        holder.add.setClickable(false);
                                        holder.checkBox.setVisibility(View.VISIBLE);

                                    }
                                    else
                                        Toast.makeText(context, "Something went Wrong!!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {

        return faculties.size();
    }

    public class MemberViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageButton add;
        CardView cardView;
        ImageView checkBox;
        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_raju);
            name=itemView.findViewById(R.id.faculty_name_add_panel_member);
            add=itemView.findViewById(R.id.c_box);
            checkBox=itemView.findViewById(R.id.check_box);
        }
    }
}
