package com.example.dchelper.scholar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.dchelper.R;

import java.util.Calendar;
import java.util.Objects;

public class SecondFragment extends Fragment {
    private ProgressBar progressbar;
    EditText FromDate;
    EditText ToDate;
    ImageView fromDateCalender;
    ImageView toDateCalender;

    String startDate;
    String endDate;

    public SecondFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_second, container, false);

        FromDate=view.findViewById(R.id.From_date);
        ToDate=view.findViewById(R.id.To_date);
        fromDateCalender=view.findViewById(R.id.from_date_calender);
        toDateCalender=view.findViewById(R.id.To_date_calender);
        FromDate.setShowSoftInputOnFocus(false);
        ToDate.setShowSoftInputOnFocus(false);


        //DATE
        fromDateCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calender=Calendar.getInstance();
                int year= calender.get(Calendar.YEAR);
                int month=calender.get(Calendar.MONTH);
                int day=calender.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month=month+1;
                        if(day<10) startDate="0"+day;
                        else startDate=""+day;
                        if(month<10) startDate+="/0"+month;
                        else startDate += "/"+month;
                        startDate+="/"+year;
                        FromDate.setText(startDate);
                        Toast.makeText(getContext(), "startdate", Toast.LENGTH_SHORT).show();
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
        //DATE

        //DATE
        toDateCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calender=Calendar.getInstance();
                int year= calender.get(Calendar.YEAR);
                int month=calender.get(Calendar.MONTH);
                int day=calender.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month=month+1;
                        if(day<10) endDate="0"+day;
                        else endDate=""+day;
                        if(month<10) endDate+="/0"+month;
                        else endDate += "/"+month;
                        endDate+="/"+year;
                        ToDate.setText(endDate);
                        Toast.makeText(getContext(), "enddate", Toast.LENGTH_SHORT).show();
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
        //DATE

        Button btn=view.findViewById(R.id.proceed);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] token1=startDate.split("/");
                String[] token2=endDate.split("/");
//                if(token1.length==3){
//                    try{
//                        int a=Integer.parseInt(token1[0]);
//                        int b=Integer.parseInt(token1[1]);
//                        int c=Integer.parseInt(token1[2]);
//                    }catch (Exception e){
//                        Toast.makeText(getContext(), (CharSequence) e, Toast.LENGTH_SHORT).show();
//                    }
//                }
//                else{
//                    Toast.makeText(getContext(), "invalid start date", Toast.LENGTH_SHORT).show();
//                }
//                if(token2.length==3){
//                    try{
//                        int a=Integer.parseInt(token2[0]);
//                        int b=Integer.parseInt(token2[1]);
//                        int c=Integer.parseInt(token2[2]);
//                    }catch (Exception e){
//                        Toast.makeText(getContext(), (CharSequence) e, Toast.LENGTH_SHORT).show();
//                    }
//                }
//                else{
//                    Toast.makeText(getContext(), "invalid start date", Toast.LENGTH_SHORT).show();
//                }
                if(Integer.parseInt(token1[2]) < Integer.parseInt(token2[2])){
                    Bundle bundle = new Bundle();
                    bundle.putString("sdate", startDate);
                    bundle.putString("edate", endDate);
                    Intent intent=new Intent(getContext(), slots.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else if(Integer.parseInt(token1[2]) == Integer.parseInt(token2[2])){
                    if(Integer.parseInt(token1[1]) < Integer.parseInt(token2[1])){
                        Bundle bundle = new Bundle();
                        bundle.putString("sdate", startDate);
                        bundle.putString("edate", endDate);
                        Intent intent=new Intent(getContext(), slots.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    else if(Integer.parseInt(token1[1]) == Integer.parseInt(token2[1])){
                        if(Integer.parseInt(token1[0]) <= Integer.parseInt(token2[0])){
                            Bundle bundle = new Bundle();
                            bundle.putString("sdate", startDate);
                            bundle.putString("edate", endDate);
                            Intent intent=new Intent(getContext(), slots.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(getContext(), "Invalid date range", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getContext(), "Invalid date range", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getContext(), "Invalid date range", Toast.LENGTH_SHORT).show();
                }

            }
        });

        progressbar=view.findViewById(R.id.progress_bar);
        progressbar.setProgress(25);
        progressbar.setMax(100);
        return view;
    }

}
