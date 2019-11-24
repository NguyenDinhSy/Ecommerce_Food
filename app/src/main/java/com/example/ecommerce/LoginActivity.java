package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.annotation.SuppressLint;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.ecommerce.Model.Users;
import com.example.ecommerce.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText InputPhoneNumber, InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;


    private  String parentDbName = "Users";
    private CheckBox chkBoxRememberMe;

    //Admin
    private TextView AdminLink, NotAdminLink;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        InputPhoneNumber = (EditText) findViewById(R.id.login_phone_number_input);
        InputPassword = (EditText) findViewById(R.id.login_password_input);
        LoginButton = (Button) findViewById(R.id.login_btn);
        loadingBar = new ProgressDialog(this);
        AdminLink = (TextView) findViewById(R.id.admin_panel_link);
        NotAdminLink = (TextView) findViewById(R.id.not_admin_panel_link);


        chkBoxRememberMe = (CheckBox) findViewById(R.id.remember_me_chkb);
        Paper.init(this);



        LoginButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                LoginUser();
                return true;
            }
        });


        AdminLink.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                LoginButton.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
                return true;
            }
        });
        NotAdminLink.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                LoginButton.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
                return true;
            }
        });

    }
    private void LoginUser(){

        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();


         if(TextUtils.isEmpty(phone)){

            Toast.makeText(this, "Please write your phone number..",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){

            Toast.makeText(this, "Please write your password..",Toast.LENGTH_SHORT).show();
        }else {
             loadingBar.setTitle("Login account");
             loadingBar.setMessage("Please waiting while we are check  ");
             loadingBar.setCanceledOnTouchOutside(false);
             loadingBar.show();


             AllowAccessToAccount(phone,password);
         }
    }
    private void AllowAccessToAccount(final String phone, final String password){

        if(chkBoxRememberMe.isChecked()){
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(parentDbName).child(phone).exists()){

                    Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

                    if(usersData.getPhone().equals(phone)){

                        if(usersData.getPassword().equals(password)){

                            if (parentDbName.equals("Admins")){



                            Toast.makeText(LoginActivity.this, "Logged in Admin, Successfully",Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                            startActivity(intent);

                            }
                            else if(parentDbName.equals("Users")){

                                Toast.makeText(LoginActivity.this, "Logged in, Successfully",Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, Home2Activity.class);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);

                            }

                        }else {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Password is incorrect",Toast.LENGTH_SHORT).show();

                        }

                    }



                }else {

                    Toast.makeText(LoginActivity.this, "Account"+phone+" do not exists",Toast.LENGTH_SHORT).show();
                    Toast.makeText(LoginActivity.this, parentDbName+ " not exist", Toast.LENGTH_SHORT).show();

                    loadingBar.dismiss();


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
