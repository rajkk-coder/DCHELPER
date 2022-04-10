package com.example.dchelper.scholar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dchelper.R;
import com.example.dchelper.scholar.booking.Slot;
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
        holder.status.setText("Completed");
        holder.date.setText(model.getDate());
        if(model.getMode().equals("CE")){
            holder.mode.setText("Comprehensive Exam");
        }
        else{
            holder.mode.setText("DC Meeting");
        }

    }

    @NonNull
    public history_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.history_card,parent,false);
        return new history_Holder(view);
    }

    class history_Holder extends RecyclerView.ViewHolder{

        TextView time,venue,status,date,mode;
        private final Context context;
        public history_Holder(@NonNull View itemView) {
            super(itemView);
            context=itemView.getContext();
            mode=itemView.findViewById(R.id.history_mode);
            time=itemView.findViewById(R.id.history_time);
            venue=itemView.findViewById(R.id.history_v_name);
            status=itemView.findViewById(R.id.history_status);
            date=itemView.findViewById(R.id.history_date);
        }
    }
}