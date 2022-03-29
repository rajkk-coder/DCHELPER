package com.example.dchelper.scholar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dchelper.R;

public class book_or_block extends AppCompatActivity {
    Button block;
    Button book;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_or_block);
        block=findViewById(R.id.button4);
        book=findViewById(R.id.button3);
        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(book_or_block.this , ScholarDashboardActivity.class);
                Toast.makeText(book_or_block.this, "block", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(book_or_block.this , ScholarDashboardActivity.class);
                Toast.makeText(book_or_block.this, "book", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }
}