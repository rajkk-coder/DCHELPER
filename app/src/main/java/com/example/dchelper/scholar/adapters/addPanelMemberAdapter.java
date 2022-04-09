package com.example.dchelper.scholar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dchelper.R;
import com.example.dchelper.admin.faculty.Faculty;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class addPanelMemberAdapter extends FirebaseRecyclerAdapter<Faculty,addPanelMemberAdapter.addPanelMemberHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public addPanelMemberAdapter(@NonNull FirebaseRecyclerOptions<Faculty> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull addPanelMemberHolder holder, int position, @NonNull Faculty model) {
        holder.textView.setText(model.getName());
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
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
