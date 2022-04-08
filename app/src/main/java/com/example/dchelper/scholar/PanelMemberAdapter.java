package com.example.dchelper.scholar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dchelper.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class PanelMemberAdapter extends FirebaseRecyclerAdapter<PanelMember,PanelMemberAdapter.PanelMemberHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    FirebaseUser user;
    public PanelMemberAdapter(@NonNull FirebaseRecyclerOptions<PanelMember> options) {
        super(options);
        user= FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    protected void onBindViewHolder(@NonNull PanelMemberHolder holder, @SuppressLint("RecyclerView") int position, @NonNull PanelMember model) {
        holder.textView.setText(model.getFacultyName());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("scholars")
                        .child(user.getUid()).child("PanelMember").child("DC")
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
