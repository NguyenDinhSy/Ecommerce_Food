package com.example.ecommerce.ViewHolder;

import android.media.Rating;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Interface.ItemTouchListener;
import com.example.ecommerce.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {

    public TextView txtProductName, txtProductDescription,txtProductPrice;
    public ImageView imageView;
    public ItemTouchListener listener;
    public ImageView fav;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);


          imageView = (ImageView) itemView.findViewById(R.id.product_image);
          txtProductName = (TextView) itemView.findViewById(R.id.product_name);
            txtProductPrice = (TextView) itemView.findViewById(R.id.product_price);
          txtProductDescription = (TextView) itemView.findViewById(R.id.product_description);
          fav = (ImageView) itemView.findViewById(R.id.fav);

    }

    public void setItemTouchListener(ItemTouchListener listener){

        this.listener = listener;
    }



    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        listener.onTouch(view, getAdapterPosition(), false);

        return true;
    }
}
