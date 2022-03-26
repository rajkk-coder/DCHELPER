package com.example.dchelper.admin.faculty;

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

public class FacultyAdapter extends FirebaseRecyclerAdapter<Faculty, FacultyAdapter.FacultyHolder> {


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
    protected void onBindViewHolder(@NonNull FacultyHolder holder, int position, @NonNull Faculty model) {
        holder.name.setText(model.getName());
        holder.id.setText(model.getId());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(FacultyListActivity.CO, "To be done", Toast.LENGTH_SHORT).show();
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
        public FacultyHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.user);
            id=itemView.findViewById(R.id.user_id);
            edit=itemView.findViewById(R.id.edit);
        }
    }
}
