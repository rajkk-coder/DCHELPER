package com.example.dchelper.scholar.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dchelper.R;
import com.example.dchelper.scholar.booking.Slot;
import com.example.dchelper.scholar.booking.select_time;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SlotAdapter extends RecyclerView.Adapter<SlotAdapter.SlotHolder> {
    private final Context context;
    private final ArrayList<Slot>slots;
    private FirebaseUser user;
    private String mode;

    public SlotAdapter(Context context, ArrayList<Slot> slots, String mode) {
        this.context = context;
        this.slots = slots;
        this.mode = mode;
        this.user=FirebaseAuth.getInstance().getCurrentUser();
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull SlotHolder holder, @SuppressLint("RecyclerView") int position){
        holder.startTime.setText(slots.get(position).getStart_time());
        holder.endTime.setText(slots.get(position).getEnd_time());
        if(slots.get(position).getStatus().equals("fna")){
            holder.slotStatus.setText("Faculty Not Available");
            holder.slotStatus.setTextSize(16);
            holder.slotStatus.setTextColor(Color.parseColor("#FF7F50"));
            holder.slotOwner.setVisibility(View.GONE);
        }else if(slots.get(position).getStatus().equals("free")){
            holder.slotStatus.setText(slots.get(position).getStatus());
            holder.slotStatus.setTextColor(Color.parseColor("#008000"));
            holder.slotOwner.setText(slots.get(position).getOwner());
        }
        else{
            holder.slotStatus.setText(slots.get(position).getStatus());
            holder.slotStatus.setTextColor(Color.parseColor("#0000FF"));
            holder.slotOwner.setText(slots.get(position).getOwner());
        }

        if(slots.get(position).getStatus().equals("free")){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                final boolean[] check = {false};
                @Override
                public void onClick(View view) {
                    check();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(check[0]){
                                Toast.makeText(context, "Your already scheduled a meet!!", Toast.LENGTH_SHORT).show();
                            }else {
                                Intent intent=new Intent(context, select_time.class);
                                Bundle bundle=new Bundle();
                                bundle.putString("venue",slots.get(position).getVenue());
                                bundle.putString("date",slots.get(position).getDate());
                                bundle.putString("slot_start_time",slots.get(position).getStart_time());
                                bundle.putString("slot_end_time",slots.get(position).getEnd_time());
                                bundle.putString("mode",mode);
                                intent.putExtras(bundle);
                                holder.context.startActivity(intent);
                            }
                        }
                    },500);

                }
                public void check(){
                    FirebaseDatabase.getInstance().getReference()
                            .child("scholars").child(user.getUid()).child("UpComingEvent")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists())
                                        check[0] =true;
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(context, "Something went wrong!!\nPlease try again...", Toast.LENGTH_SHORT).show();
                                }
                            });
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


    @Override
    public int getItemCount() {
        return slots.size();
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
