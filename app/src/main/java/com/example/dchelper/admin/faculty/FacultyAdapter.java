package com.example.dchelper.admin.faculty;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dchelper.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

public class FacultyAdapter extends FirebaseRecyclerAdapter<Faculty, FacultyAdapter.FacultyHolder>  {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FacultyAdapter(@NonNull FirebaseRecyclerOptions<Faculty> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FacultyHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Faculty model) {
        holder.name.setText(model.getName());
        holder.id.setText(model.getId());

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final DialogPlus dialogPlus= DialogPlus.newDialog(holder.name.getContext())
                        .setContentHolder(new ViewHolder(R.layout.activity_edit_faculty))
                        .setExpanded(true,1280)
                        .setGravity(Gravity.CENTER)
                        .create();
                View view1=dialogPlus.getHolderView();
                EditText name=view1.findViewById(R.id.editable_faculty_name);
                EditText id=view1.findViewById(R.id.editableFacultyId);
                ImageButton btn_save=view1.findViewById(R.id.save_changes);
                ImageButton btn_delete=view1.findViewById(R.id.permanent_delete_faculty);
                name.setText(model.getName());
                id.setText(model.getId());
                dialogPlus.show();
                btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseDatabase.getInstance().getReference()
                                .child("facultyList")
                                .child(getRef(position).getKey())
                                .removeValue();
                        dialogPlus.dismiss();
                    }
                });
                btn_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String,Object>map=new HashMap<>();
                        map.put("id",id.getText().toString());
                        map.put("name",name.getText().toString());
                        FirebaseDatabase.getInstance().getReference().child("facultyList")
                                .child(getRef(position).getKey())
                                .updateChildren(map);
                        dialogPlus.dismiss();
                    }
                });
            }

        });
    }

    @NonNull
    @Override
    public FacultyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.faculty_card,parent,false);
        return new FacultyHolder(view);
    }

    class FacultyHolder extends RecyclerView.ViewHolder{
        TextView name,id;
        ImageView edit;
        private final Context context;
        public FacultyHolder(@NonNull View itemView) {
            super(itemView);
            context=itemView.getContext();
            name=itemView.findViewById(R.id.user);
            id=itemView.findViewById(R.id.user_id);
            edit=itemView.findViewById(R.id.edit);
        }
    }
}
