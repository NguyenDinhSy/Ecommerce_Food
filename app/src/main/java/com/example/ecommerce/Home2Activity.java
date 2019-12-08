package com.example.ecommerce;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.example.ecommerce.Database.Database;
import com.example.ecommerce.Model.Products;
import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class Home2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference ProductsRef;
    private DatabaseReference FavoriteRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    //Database localDB;

    private String type = "";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);

        //local database
     //   localDB = new Database(this);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null)
        {
            type = getIntent().getExtras().get("Admin").toString();
        }

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        FavoriteRef = FirebaseDatabase.getInstance().getReference().child("Favorite");

        Paper.init(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                Intent intent = new Intent(Home2Activity.this, CartActivity.class);
                startActivity(intent);


                return true;
            }
        });



        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =(NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView =   navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);

        userNameTextView.setText(Prevalent.currentOnlineUser.getName());

        Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);


        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }


    @Override
    protected void onStart() {

        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(ProductsRef, Products.class)
                        .build();


        final FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @SuppressLint("ClickableViewAccessibility")
                    @Override
                    protected void onBindViewHolder(@NonNull final ProductViewHolder holder, final int position, @NonNull final Products model) {

                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Giá ="+ model.getPrice()+"$");
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        //caution

                        //FavoriteRef = FirebaseDatabase.getInstance().getReference().child("Favorite");


                        if (!model.getPid()

                                .equals(FavoriteRef.child(Prevalent.currentOnlineUser.getPhone()).child("pid"))
                        )  {
                            holder.fav.setImageResource(R.drawable.ic_favorite_border_black_24dp);

                        }
                        else {

                             holder.fav.setImageResource(R.drawable.ic_favorite_black_24dp);
                        }


                        holder.fav.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {

                                HashMap<String, Object> userMap = new HashMap<>();




                            if (!model.getPid()

                                    .equals(FavoriteRef.child(Prevalent.currentOnlineUser.getPhone()).child("pid"))
                                       ) {


                                userMap.put("pid1", model.getPid());


                               FavoriteRef.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);
                           //     String s = "product 1";

                                FavoriteRef.child(Prevalent.currentOnlineUser.getPhone()).child("pid").child(model.getPid());
                                FavoriteRef.child(Prevalent.currentOnlineUser.getPhone()).push().child(model.getPid());


                                holder.fav.setImageResource(R.drawable.ic_favorite_black_24dp);

                            }else {

                                FavoriteRef.child(Prevalent.currentOnlineUser.getPhone()).removeValue();
                                holder.fav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            }


                                return true;
                            }
                        });

                        holder.imageView.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {

                                Intent intent = new Intent(Home2Activity.this, FoodDetailActivity.class);
                                intent.putExtra("pid", model.getPid());
                                startActivity(intent);
                                return true;
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;

                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.home2, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();


//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_cart) {

            Intent intent =  new Intent(Home2Activity.this, CartActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_search) {


            Intent intent =  new Intent(Home2Activity.this, SearchProductsActivity.class);
            startActivity(intent);
        }

        else if (id == R.id.nav_categories) {


        } else if (id == R.id.nav_settings) {


            Intent intent =  new Intent(Home2Activity.this, SettingsActivity.class);
            startActivity(intent);



        } else if (id == R.id.nav_logout) {
            Paper.book().destroy();

            Intent intent = new Intent(Home2Activity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
