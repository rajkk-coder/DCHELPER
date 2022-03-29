package com.example.dchelper.scholar;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.dchelper.R;

import java.util.Calendar;

public class select_time extends AppCompatActivity {
    private ProgressBar progressbar;
    EditText editText1;
    EditText editText2;
    ImageView img1;
    ImageView img2;

    String startTime;
    String endTime;
    int start_time;
    int end_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time);

        editText1=findViewById(R.id.editTextTextPersonName);
        editText2=findViewById(R.id.editTextTextPersonName2);
        img1=findViewById(R.id.imageView_time);
        img2=findViewById(R.id.imageView2_time);
        editText1.setShowSoftInputOnFocus(false);
        editText2.setShowSoftInputOnFocus(false);

        //START TIME
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar=Calendar.getInstance();
                int hrs=calendar.get(Calendar.HOUR);
                int mn=calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog=new TimePickerDialog(select_time.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        if(hour<10) startTime="0" + hour;
                        else startTime=""+hour;
                        if(minute<10) startTime+=":0"+minute;
                        else startTime+=":"+minute;
                        editText1.setText(startTime);
                    }
                },hrs,mn,true);
                timePickerDialog.show();
            }
        });
        //START TIME

        //END TIME
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar=Calendar.getInstance();
                int hrs=calendar.get(Calendar.HOUR);
                int mn=calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog=new TimePickerDialog(select_time.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        if(hour<10) endTime="0" + hour;
                        else endTime=""+hour;
                        if(minute<10) endTime+=":0"+minute;
                        else endTime+=":"+minute;
                        editText2.setText(endTime);
                    }
                },hrs,mn,true);
                timePickerDialog.show();
            }
        });
        //END TIME

        Button OK=findViewById(R.id.button2);
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] token1=startTime.split(":");
                String[] token2=endTime.split(":");
                start_time=Integer.parseInt(token1[0])*60+Integer.parseInt(token1[1]);
                end_time=Integer.parseInt(token2[0])*60+Integer.parseInt(token2[1]);
                Toast.makeText(select_time.this, "select_time", Toast.LENGTH_SHORT).show();
                if(start_time<end_time && end_time-start_time<=60) {
                    Intent intent=new Intent(select_time.this, book_or_block.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(select_time.this, "Incorrect timing", Toast.LENGTH_SHORT).show();
                }
            }
        });
        progressbar=findViewById(R.id.progress_bar);
        progressbar.setProgress(75);
        progressbar.setMax(100);
    }
}