package com.example.dchelper.scholar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dchelper.R;

public class slots extends AppCompatActivity {
    private ProgressBar progressbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slots);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            String sdate = bundle.getString("sdate");
            String edate = bundle.getString("edate");
            Toast.makeText(this, sdate, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, edate, Toast.LENGTH_SHORT).show();
        }
        Button btn=findViewById(R.id.button1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(slots.this , select_time.class);
                startActivity(intent);
            }
        });
        progressbar=findViewById(R.id.progress_bar);
        progressbar.setProgress(50);
        progressbar.setMax(100);
        Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
    }
}