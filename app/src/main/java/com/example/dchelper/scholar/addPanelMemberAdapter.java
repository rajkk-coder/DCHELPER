package com.example.dchelper.scholar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dchelper.R;
import com.example.dchelper.admin.faculty.Faculty;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class addPanelMemberAdapter extends FirebaseRecyclerAdapter<Faculty,addPanelMemberAdapter.addPanelMemberHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    ArrayList<String>panelList=new ArrayList<>();

    public addPanelMemberAdapter(@NonNull FirebaseRecyclerOptions<Faculty> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull addPanelMemberHolder holder, int position, @NonNull Faculty model) {
        holder.textView.setText(model.getName());
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("PanelMember").child("Raju").child("DC")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot data:snapshot.getChildren())
                                   if(data.exists()){
                                       panelList.add(data.getValue(String.class));
                                       Toast.makeText(view.getContext(), panelList.size(), Toast.LENGTH_SHORT).show();
                                   }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(holder.context, "lauda", Toast.LENGTH_SHORT).show();
                            }
                        });
             //-----   Toast.makeText(holder.context, ""+panelList.size(), Toast.LENGTH_SHORT).show();
//                String fname=model.getName();
//                int flag=0;
//                for(int i=0;i<panelList.size();i++){
//                    if(panelList.get(i).equals(fname)){
//                        flag=1;
//                        break;
//                    }
//                }
//                if (flag==0){
//                    FirebaseDatabase.getInstance().getReference()
//                            .child("PanelMember")
//                            .push().setValue(new PanelMember(fname,"DC","Sreyansh"))
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if(task.isSuccessful())
//                                Toast.makeText(holder.context, "Added successfully", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//                else{
//                    Toast.makeText(holder.context, "Memebr already exist", Toast.LENGTH_SHORT).show();
//                }
                panelList.clear();
            }
        });



//        holder.imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseDatabase.getInstance().getReference()
//                        .child("PanelMember")
//                        .child("xxxxx")
//                        .push().setValue()
//            }
//        });
    }

    @NonNull
    @Override
    public addPanelMemberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.add_panel_member_card,parent,false);
        return new addPanelMemberHolder(view);
    }



    class addPanelMemberHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageButton imageButton;
        private final Context context;
        public addPanelMemberHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.faculty_name_add_panel_member);
            imageButton=itemView.findViewById(R.id.c_box);
            context=itemView.getContext();
        }
    }
}
