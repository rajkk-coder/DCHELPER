package com.example.dchelper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class Schedule extends AppCompatActivity {

    private int current_progress=0;
    private ProgressBar progressbar;
    private Button startProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        progressbar=findViewById(R.id.progessbar);
        startProgress =findViewById(R.id.start);
        startProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current_progress=current_progress + 20;
                progressbar.setProgress(current_progress);
                progressbar.setMax(100);
            }
        });
    }
}