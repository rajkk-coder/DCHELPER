package com.example.dchelper.scholar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onBindViewHolder(@NonNull SlotHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Slot model) {
        long difference_In_Time = 0;
        if(model.getStatus().equals("Blocked")) {
            SimpleDateFormat sdf
                    = new SimpleDateFormat(
                    "dd-MM-yyyy HH:mm:ss");
            String end_date = sdf.format(new Date());
            String start_date = model.getCurrentDateAndTime();
            try {
                Date d1 = sdf.parse(start_date);
                Date d2 = sdf.parse(end_date);
                difference_In_Time
                        = d2.getTime() - d1.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(difference_In_Time>20*1000){
            final int[] restrict={1};
            FirebaseDatabase.getInstance().getReference().child("slot")
                    .child(model.getDate())
                    .child(model.getVenue()).orderByChild("start_time")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<String> keys=new ArrayList<>();
                            String path="";
                            String new_slotStart_time = model.getStart_time();
                            String new_slotEnd_time=model.getEnd_time();
                            if(snapshot.exists()){
                                for(DataSnapshot dt:snapshot.getChildren()){
                                    Slot slot=dt.getValue(Slot.class);
                                    if(slot.getEnd_time().equals(model.getStart_time()) && slot.getOwner().equals("free")){
                                        keys.add(dt.getKey());
                                        new_slotStart_time=slot.getStart_time();
                                    }
                                    else if(slot.getStart_time().equals(model.getEnd_time()) && slot.getOwner().equals("free")){
                                        keys.add(dt.getKey());
                                        new_slotEnd_time=slot.getEnd_time();
                                    }
                                    if(slot.getStatus().equals("Booked") && slot.getOwner().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                        path=dt.getKey();
                                    }
                                }
                                for (int i=0;i<keys.size();i++)
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("slot").child(model.getDate()).child(model.getVenue()).child(keys.get(i)).removeValue();

                                //should be done only once

                                if(restrict[0] ==1){
                                    //Remove slot from the corresponding date and venue
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("slot").child(model.getDate()).child(model.getVenue()).child(path)
                                            .removeValue();
                                    //Add new free slot
                                    FirebaseDatabase.getInstance().getReference().child("slot").child(model.getDate()).child(model.getVenue())
                                            .push().setValue(new Slot("free",new_slotStart_time,new_slotEnd_time,model.getVenue(),model.getDate(),"free"));

                                    restrict[0] +=1;
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
        else{
            holder.startTime.setText(model.getStart_time());
            holder.endTime.setText(model.getEnd_time());
            holder.slotOwner.setText(model.getOwner());
            holder.slotStatus.setText(model.getStatus());
            if(model.getOwner().equals("free")){
                holder.slotOwner.setVisibility(View.GONE);
                holder.slotStatus.setTextSize(24);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(model.getOwner().equals("free")){
                    FirebaseDatabase.getInstance().getReference()
                            .child("scholars")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("UpComingEvent")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists())
                                        Toast.makeText(holder.context, "You can not book/block more than one slot at a time", Toast.LENGTH_SHORT).show();
                                    else {
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

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                }
            }
        });
        }
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
