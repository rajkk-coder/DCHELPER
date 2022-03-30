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

public class SlotAdapter extends FirebaseRecyclerAdapter<Slot,SlotAdapter.SlotHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public SlotAdapter(@NonNull FirebaseRecyclerOptions<Slot> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SlotHolder holder, int position, @NonNull Slot model) {
        holder.startTime.setText(model.getStart_time());
        holder.endTime.setText(model.getEnd_time());
        holder.slotOwner.setText(model.getOwner());
        holder.slotStatus.setText(model.getStatus());
    }

    @NonNull
    @Override
    public SlotHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.slot_card,parent,false);
        return new SlotHolder(view);
    }

    class SlotHolder extends RecyclerView.ViewHolder{

        TextView startTime,endTime,slotOwner,slotStatus;
        private final Context context;
        public SlotHolder(@NonNull View itemView) {
            super(itemView);
            context=itemView.getContext();
            startTime=itemView.findViewById(R.id.slot_start_time);
            endTime=itemView.findViewById(R.id.slot_end_time);
            slotOwner=itemView.findViewById(R.id.slot_owner);
            slotStatus=itemView.findViewById(R.id.slot_status);
        }
    }
}
