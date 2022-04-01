package com.example.dchelper.scholar;

import android.content.Context;
<<<<<<< HEAD
=======
import android.content.Intent;
import android.os.Bundle;
>>>>>>> origin/master
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
<<<<<<< HEAD
        holder.slotStatus.setText(model.getStatus());
=======
        if(!model.getOwner().equals("free"))
            holder.slotStatus.setText(model.getStatus());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(model.getOwner().equals("free")){
                    Intent intent=new Intent(view.getContext(),select_time.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("venue",model.getVenue());
                    bundle.putString("date",model.getDate());
                    bundle.putString("slot_start_time",model.getStart_time());
                    bundle.putString("slot_end_time",model.getEnd_time());
                    bundle.putString("reference",getRef(position).getKey());
                    intent.putExtras(bundle);
                    holder.context.startActivity(intent);
                }
            }
        });
>>>>>>> origin/master
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
