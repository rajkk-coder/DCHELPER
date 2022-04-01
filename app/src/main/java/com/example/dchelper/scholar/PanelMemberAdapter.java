package com.example.dchelper.scholar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dchelper.R;
import com.example.dchelper.admin.faculty.FacultyAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.Holder;

public class PanelMemberAdapter extends FirebaseRecyclerAdapter<PanelMember,PanelMemberAdapter.PanelMemberHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PanelMemberAdapter(@NonNull FirebaseRecyclerOptions<PanelMember> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PanelMemberHolder holder, int position, @NonNull PanelMember model) {
        holder.textView.setText(model.getFacultyName());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference()
                        .child("PanelMember")
                        .child(getRef(position).getKey())
                        .removeValue();
            }
        });
        
    }

    @NonNull
    @Override
    public PanelMemberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_display_panel_member_card,parent,false);
        return new PanelMemberHolder(view);
    }

    class PanelMemberHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        private final Context context;
        public PanelMemberHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.faculty_name_panel_member);
            imageView=itemView.findViewById(R.id.panel_delete);
            context=itemView.getContext();
        }
    }
}