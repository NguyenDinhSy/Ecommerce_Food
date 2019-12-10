package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.ecommerce.Model.Products;
import com.example.ecommerce.Model.Ratings;
import com.example.ecommerce.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

public class FoodDetailActivity extends AppCompatActivity implements RatingDialogListener {

    private Button addToCartButton;
    private FloatingActionButton btnRating;
    private RatingBar ratingBar;

    String foodID;

    DatabaseReference ratings;


    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice, productDescription, productName;
    private String productID = "", state = "Normal";

    
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        //MAPPING

        btnRating = (FloatingActionButton) findViewById(R.id.btn_rating);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        foodID = getIntent().getStringExtra("pid");
        productID = getIntent().getStringExtra("pid");

        addToCartButton = (Button) findViewById(R.id.pd_add_to_cart_button);
        numberButton = (ElegantNumberButton) findViewById(R.id.number_btn);
        productImage = (ImageView) findViewById(R.id.product_image_details);
        productName = (TextView) findViewById(R.id.product_name_details);
        productDescription = (TextView) findViewById(R.id.product_description_details);
        productPrice = (TextView) findViewById(R.id.product_price_details);

        ratings = FirebaseDatabase.getInstance().getReference().child("Rating");

        getProductDetails(productID);

        // Touch Listenner

        btnRating.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                showRatingDialog();


                return true;
            }
        });



        addToCartButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                if (state.equals("Order Placed") || state.equals("Order Shipped")){

                    Toast.makeText(FoodDetailActivity.this, "you can add purchase more products, once your order is shipped or confirmed.", Toast.LENGTH_LONG).show();

                }else {
                    addingToCartList();

                                   }
                return true;
            }
        });


        // GEt rangting
      //      getRatingFood(foodID);
    }

    // OUTSIDE ___________________________________________________________________________________________

//
//    private void getRatingFood(final String foodID){
//
//        com.google.firebase.database.Query foodRating =  ratings.orderByChild("pid").equalTo(foodID);
//
//        foodRating.addValueEventListener(new ValueEventListener() {
//            int count =0, sum = 0 ;
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot postsnapshot: dataSnapshot.getChildren()){
//
//                    Ratings  item = postsnapshot.getValue(Ratings.class);
//
//                    sum+= Integer.parseInt(item.getRatingValue());
//                    count++;
//
//                }
//                if (count != 0) {
//                    float average = sum / count;
//                    ratingBar.setRating(average);
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }



    private void showRatingDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
            .setNoteDescriptions(Arrays.asList("Very bad","Not good", "Quite Ok", "VEry good", "Excellent"))
            .setDefaultRating(1)
                .setTitle("Rare this good")
                .setDescription("Please select some stars and give feedback")
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimary)
                    .setHint("Please write your comment")
                .setHintTextColor(R.color.colorAccent)
                .setCommentTextColor(android.R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(FoodDetailActivity.this)
                .show();


    }

    @Override
    protected void onStart() {
        super.onStart();

       CheckOrderState();

    }

    private void addingToCartList()
    {
        String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("pname", productName.getText().toString());
        cartMap.put("price", productPrice.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", numberButton.getNumber());
        cartMap.put("discount", "");

        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Products").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful())
                        {
                            cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                                    .child("Products").child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if (task.isSuccessful())
                                            {
                                                Toast.makeText(FoodDetailActivity.this, "Added to Cart List.", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(FoodDetailActivity.this, Home2Activity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }

                    }
                });
    }


    private void getProductDetails(String productID) {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");


        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    Products products = dataSnapshot.getValue(Products.class);

                    productName.setText(products.getPname());
                    productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productImage);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void CheckOrderState()
    {
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String shippingState = dataSnapshot.child("state").getValue().toString();

                    if (shippingState.equals("shipped"))
                    {
                        state = "Order Shipped";
                    }
                    else if(shippingState.equals("not shipped"))
                    {
                        state = "Order Placed";
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onNeutralButtonClicked() {

    }

    // BUTTON SUBMIT


    @Override
    public void onPositiveButtonClicked(int value, String comments) {


        final Ratings rating = new Ratings( foodID, String.valueOf(value), Prevalent.currentOnlineUser.getPhone(), comments);

        ratings.child(Prevalent.currentOnlineUser.getPhone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(Prevalent.currentOnlineUser.getPhone()).exists()){
                    ratings.child(Prevalent.currentOnlineUser.getPhone()).removeValue();
                    ratings.child(Prevalent.currentOnlineUser.getPhone()).setValue(rating);

                }else {
                    ratings.child(Prevalent.currentOnlineUser.getPhone()).setValue(rating);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//      rat

    }
}
