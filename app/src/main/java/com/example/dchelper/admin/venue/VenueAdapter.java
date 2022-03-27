package com.example.dchelper.admin.venue;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dchelper.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class VenueAdapter extends FirebaseRecyclerAdapter<Venue, VenueAdapter.VenueHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public VenueAdapter(@NonNull FirebaseRecyclerOptions<Venue> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull VenueHolder holder, int position, @NonNull Venue model) {
        holder.name.setText(model.getName());
        holder.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(FacultyListActivity.CO, "To be done", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public VenueHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.venue_card,parent,false);
        return new VenueHolder(view);
    }

    class VenueHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView book;
        ImageButton edit;
        public VenueHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.v_name);
            book=itemView.findViewById(R.id.v_book);
            edit=itemView.findViewById(R.id.v_edit);
        }
    }
}
