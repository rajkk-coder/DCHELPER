package com.example.dchelper.scholar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
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

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.dchelper.R;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class SecondFragment extends Fragment {
    private ProgressBar progressbar;
    EditText FromDate;
    EditText ToDate;
    ImageView fromDateCalender;
    ImageView toDateCalender;

    String startDate="";
    String endDate="";

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
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String[] token1=startDate.split("/");
                String[] token2=endDate.split("/");
                if(token1.length!=3 || token2.length!=3){
                    Toast.makeText(getContext(), "Provide a valid date", Toast.LENGTH_SHORT).show();
                }
                else if(Integer.parseInt(token1[2]) < Integer.parseInt(token2[2])){
                    Bundle bundle = new Bundle();
                    bundle.putString("sdate", startDate);
                    bundle.putString("edate", endDate);
                    //Number of dates
                    try {
                        String new_start_date=token1[1]+"/"+token1[0]+"/"+token1[2];
                        String new_end_date=token2[1]+"/"+token2[0]+"/"+token2[2];
                        SimpleDateFormat dates = new SimpleDateFormat("MM/dd/yyyy");
                        Date date1;
                        Date date2;
                        date1 = dates.parse(new_start_date);
                        date2 = dates.parse(new_end_date);
                        long difference = Math.abs(date1.getTime() - date2.getTime());
                        long differenceDates = difference / (24 * 60 * 60 * 1000);
                        String dayDifference = Long.toString(differenceDates);
                        bundle.putString("noOfDates",dayDifference);
                    }catch (Exception e){
                        Toast.makeText(getContext(), "gone wrong", Toast.LENGTH_SHORT).show();
                    }
                    //Number of dates

                    Intent intent=new Intent(getContext(), GetDateActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else if(Integer.parseInt(token1[2]) == Integer.parseInt(token2[2])){
                    if(Integer.parseInt(token1[1]) < Integer.parseInt(token2[1])){
                        Bundle bundle = new Bundle();
                        bundle.putString("sdate", startDate);
                        bundle.putString("edate", endDate);
                        //Number of dates
                        try {
                            String new_start_date=token1[1]+"/"+token1[0]+"/"+token1[2];
                            String new_end_date=token2[1]+"/"+token2[0]+"/"+token2[2];
                            SimpleDateFormat dates = new SimpleDateFormat("MM/dd/yyyy");
                            Date date1;
                            Date date2;
                            date1 = dates.parse(new_start_date);
                            date2 = dates.parse(new_end_date);
                            long difference = Math.abs(date1.getTime() - date2.getTime());
                            long differenceDates = difference / (24 * 60 * 60 * 1000);
                            String dayDifference = Long.toString(differenceDates);
                            bundle.putString("noOfDates",dayDifference);
                        }catch (Exception e){
                            Toast.makeText(getContext(), "gone wrong", Toast.LENGTH_SHORT).show();
                        }
                        //Number of dates
                        Intent intent=new Intent(getContext(), GetDateActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    else if(Integer.parseInt(token1[1]) == Integer.parseInt(token2[1])){
                        if(Integer.parseInt(token1[0]) <= Integer.parseInt(token2[0])){
                            Bundle bundle = new Bundle();
                            bundle.putString("sdate", startDate);
                            bundle.putString("edate", endDate);
                            //Number of dates
                        try {
                            String new_start_date=token1[1]+"/"+token1[0]+"/"+token1[2];
                            String new_end_date=token2[1]+"/"+token2[0]+"/"+token2[2];
                            SimpleDateFormat dates = new SimpleDateFormat("MM/dd/yyyy");
                            Date date1;
                            Date date2;
                            date1 = dates.parse(new_start_date);
                            date2 = dates.parse(new_end_date);
                            long difference = Math.abs(date1.getTime() - date2.getTime());
                            long differenceDates = difference / (24 * 60 * 60 * 1000);
                            String dayDifference = Long.toString(differenceDates);
                            bundle.putString("noOfDates",dayDifference);
                        }catch (Exception e){
                            Toast.makeText(getContext(), "gone wrong", Toast.LENGTH_SHORT).show();
                        }
                            //Number of dates
                            Intent intent=new Intent(getContext(), GetDateActivity.class);
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
