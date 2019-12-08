package com.example.ecommerce.ViewHolder;

import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.ecommerce.Interface.ItemTouchListener;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecommerce.R;
public class CartViewHolder extends RecyclerView.ViewHolder  implements View.OnTouchListener {

    public TextView txtProductName, txtProductPrice, txtProductQuantity;
    private ItemTouchListener itemTouchListner;

    public CartViewHolder(View itemView)
    {
        super(itemView);

        txtProductName = itemView.findViewById(R.id.cart_product_name);
        txtProductPrice = itemView.findViewById(R.id.cart_product_price);
        txtProductQuantity = itemView.findViewById(R.id.cart_product_quantity);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        itemTouchListner.onTouch(view, getAdapterPosition(), false);

        return true;
    }

    public void setItemTouchListner(ItemTouchListener itemTouchListner) {
        this.itemTouchListner = itemTouchListner;
    }
}
