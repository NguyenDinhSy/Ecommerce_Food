package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView nhau, an_vat, com, chay;

    private Button LogoutBtn, CheckOrdersBtn, maintainProductsBtn;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);


        LogoutBtn = (Button) findViewById(R.id.admin_logout_btn);
        CheckOrdersBtn = (Button) findViewById(R.id.check_orders_btn);
        maintainProductsBtn = (Button) findViewById(R.id.maintain_btn);




        nhau = (ImageView) findViewById(R.id.nhau);
        an_vat = (ImageView) findViewById(R.id.an_vat);
        com = (ImageView) findViewById(R.id.com);
        chay = (ImageView) findViewById(R.id.chay);


//
//        maintainProductsBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view)
//            {
//                Intent intent = new Intent(AdminCategoryActivity.this, Home2Activity.class);
//                intent.putExtra("Admin", "Admin");
//                startActivity(intent);
//            }
//        });

        maintainProductsBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    Intent intent = new Intent(AdminCategoryActivity.this, Home2Activity.class);
                    intent.putExtra("Admin", "Admin");
                    startActivity(intent);
                return true;
                }
                return true;
            }
        });

//        LogoutBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view)
//            {
//                Intent intent = new Intent(AdminCategoryActivity.this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//                finish();
//            }
//        });

        LogoutBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    Intent intent = new Intent(AdminCategoryActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                return true;
                }
                return true;
            }
        });

//        CheckOrdersBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view)
//            {
//                Intent intent = new Intent(AdminCategoryActivity.this, AdminNewOrderActivity.class);
//                startActivity(intent);
//            }
//        });

        CheckOrdersBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminNewOrderActivity.class);
                startActivity(intent);
                return true;
            }
        });



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
