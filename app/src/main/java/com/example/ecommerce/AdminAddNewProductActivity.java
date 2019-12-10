package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {

    private ProgressDialog loadingBar;
    private String CategoryName, Description, Price, PName, saveCurrentDate, saveCurrentTime;
    private Button AddNewProductButton;
    private ImageView InputProductImage;
    private static final int GalleryPick = 1;
    private Uri ImageUri;

    private String productRandomKey, downloadImageurl;
    private StorageReference ProductImageRef;
    private DatabaseReference ProductsRef;
    private EditText InputProductName, InputProductPrice, InputProductDescription;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        // Find id
        CategoryName = getIntent().getExtras().get("category").toString();
        ProductImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");


        //Toast.makeText(this, CategoryName, Toast.LENGTH_SHORT).show();
        AddNewProductButton = (Button) findViewById(R.id.add_new_product);
        InputProductName = (EditText) findViewById(R.id.product_name);
        InputProductPrice = (EditText) findViewById(R.id.product_price);
        InputProductDescription = (EditText) findViewById(R.id.product_description);
        InputProductImage = (ImageView) findViewById(R.id.select_product_image);
        loadingBar = new ProgressDialog(this);


        // Select Image
        InputProductImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    OpenGallery();
                    return  true;
                }

                return true;
                }


        });

        AddNewProductButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    ValidateProductData();
                return true;
                }
                return true;
            }
        });


    }

    //CHOOSE IMAGE
    private void OpenGallery() {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    // RESULT ( REQUEST , RESULT, DATA)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick &&   resultCode == RESULT_OK  && data != null){

            ImageUri = data.getData();
            InputProductImage.setImageURI(ImageUri);
        }
    }

    private void ValidateProductData(){

        Description = InputProductDescription.getText().toString();
        Price = InputProductPrice.getText().toString();
        PName = InputProductName.getText().toString();

        if (ImageUri == null ){
            Toast.makeText(this,"Product image is mandatory." ,Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(Description)){
            Toast.makeText(this,"Please write description ",Toast.LENGTH_SHORT).show();

        }else if (TextUtils.isEmpty(Price)){
            Toast.makeText(this,"Please write Price ",Toast.LENGTH_SHORT).show();

        }else if (TextUtils.isEmpty(PName)){
            Toast.makeText(this,"Please write Product Name",Toast.LENGTH_SHORT).show();

        }else {

            StoreProductInformation();
        }
    }

    private void StoreProductInformation() {

     //LOADING BAR
        loadingBar.setTitle("Add new product");
        loadingBar.setMessage("Please wait, while we are adding the new product");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

    // DATE FORMAT
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;
    // IMAGE URL STORAGE & UPLOAD
        final StorageReference filePath = ProductImageRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask =filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String message = e.toString();
                Toast.makeText(AdminAddNewProductActivity.this,"Error"+ message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(AdminAddNewProductActivity.this, "Image uploaded Successfully..", Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask= uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful()){

                            throw task.getException();

                        }
                        downloadImageurl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                            if (task.isSuccessful()){
                                downloadImageurl = task.getResult().toString();

                                Toast.makeText(AdminAddNewProductActivity.this, "got Image Url successful", Toast.LENGTH_SHORT).show();

                                SaveProductInfoToDatabase();
                            }
                    }
                });
            }
        });
    }



    private void SaveProductInfoToDatabase() {

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid",productRandomKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("description",Description);
        productMap.put("image",downloadImageurl);
        productMap.put("category",CategoryName);
        productMap.put("price",Price);
        productMap.put("pname",PName);

        ProductsRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){

                            Intent intent = new Intent(AdminAddNewProductActivity.this,AdminCategoryActivity.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(AdminAddNewProductActivity.this, "Product is added successfully! ", Toast.LENGTH_SHORT).show();

                        }else {

                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminAddNewProductActivity.this, "Error: "+ message, Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }
}
