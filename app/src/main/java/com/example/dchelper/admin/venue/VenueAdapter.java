package com.example.dchelper.admin.venue;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dchelper.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

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
    protected void onBindViewHolder(@NonNull VenueHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Venue model) {
        holder.name.setText(model.getName());
        String str=model.getName();
        holder.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle= new Bundle();
                bundle.putString("venue",str);
                Intent intent=new Intent(view.getContext(),BookVenue.class);
                intent.putExtras(bundle);
                holder.context.startActivity(intent);
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus= DialogPlus.newDialog(holder.name.getContext())
                        .setContentHolder(new ViewHolder(R.layout.edit_venue))
                        .setExpanded(true,980)
                        .setGravity(Gravity.CENTER)
                        .create();
                View view1=dialogPlus.getHolderView();
                EditText name=view1.findViewById(R.id.editable_venue_name);
                EditText id=view1.findViewById(R.id.editableVenueId);
                name.setText(model.getName());
                id.setText(model.getId());
                ImageButton imageButton=view1.findViewById(R.id.permanent_delete_venue);
                ImageButton imageButton1=view1.findViewById(R.id.venue_save_changes);
                dialogPlus.show();
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseDatabase.getInstance().getReference().child("venueList")
                                .child(getRef(position).getKey())
                                .removeValue();
                        dialogPlus.dismiss();
                    }
                });
                imageButton1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String,Object> map=new HashMap<>();
                        map.put("id",id.getText().toString());
                        map.put("name",name.getText().toString());
                        FirebaseDatabase.getInstance().getReference().child("venueList")
                                .child(getRef(position).getKey())
                                .updateChildren(map);
                        Toast.makeText(id.getContext(), "Saved changes", Toast.LENGTH_SHORT).show();
                        dialogPlus.dismiss();
                    }
                });
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
        private final Context context;
        public VenueHolder(@NonNull View itemView) {
            super(itemView);
            context=itemView.getContext();
            name=itemView.findViewById(R.id.v_name);
            book=itemView.findViewById(R.id.v_book);
            edit=itemView.findViewById(R.id.v_edit);
        }
    }
}
