package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView nhau, an_vat, com, chay;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);


        nhau = (ImageView) findViewById(R.id.nhau);
        an_vat = (ImageView) findViewById(R.id.an_vat);
        com = (ImageView) findViewById(R.id.com);
        chay = (ImageView) findViewById(R.id.chay);


        nhau.setOnTouchListener(new View.OnTouchListener() {
          @Override
          public boolean onTouch(View view, MotionEvent motionEvent) {

              Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
               intent.putExtra("category","nhau");
                startActivity(intent);

              return true;
          }
      });

        an_vat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category","an_vat");
                startActivity(intent);

                return true;
            }
        });
        com.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category","com");
                startActivity(intent);

                return true;
            }
        });
        chay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category","chay");
                startActivity(intent);

                return true;
            }
        });

    }


}
