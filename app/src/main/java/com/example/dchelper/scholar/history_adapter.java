package com.example.dchelper.scholar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dchelper.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class history_adapter extends FirebaseRecyclerAdapter<Slot,history_adapter.history_Holder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public history_adapter(@NonNull FirebaseRecyclerOptions<Slot> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull history_adapter.history_Holder holder, int position, @NonNull Slot model) {
        holder.time.setText(model.getStart_time()+"-"+model.getEnd_time());
        holder.venue.setText(model.getVenue());
        holder.status.setText(model.getStatus());
        holder.date.setText(model.getDate());
    }

    @NonNull
    public history_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.history_card,parent,false);
        return new history_Holder(view);
    }

    class history_Holder extends RecyclerView.ViewHolder{

        TextView time,venue,status,date;
        private final Context context;
        public history_Holder(@NonNull View itemView) {
            super(itemView);
            context=itemView.getContext();
            time=itemView.findViewById(R.id.history_time);
            venue=itemView.findViewById(R.id.history_v_name);
            status=itemView.findViewById(R.id.history_status);
            date=itemView.findViewById(R.id.history_date);
        }
    }
}