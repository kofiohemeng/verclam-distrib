package com.verclamdistrib.woocomerce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.verclamdistrib.woocomerce.R;

import java.util.List;

import com.verclamdistrib.woocomerce.fragments.Checkout;
import com.verclamdistrib.woocomerce.fragments.My_Cart;
import com.verclamdistrib.woocomerce.models.coupons_model.CouponDetails;


/**
 * DemoCouponsListAdapter is the adapter class of ListView holding List of DemoCoupons in Checkout
 **/

public class DemoCouponsListAdapter extends BaseAdapter {

    Context context;
    private My_Cart my_cart;
    Checkout checkout;
    private List<CouponDetails> couponsList;

    private LayoutInflater layoutInflater;


    public DemoCouponsListAdapter(Context context, List<CouponDetails> couponsList, My_Cart my_cart) {
        this.context = context;
        this.my_cart = my_cart;
        this.couponsList = couponsList;

        layoutInflater = LayoutInflater.from(context);
    }
    
    public DemoCouponsListAdapter(Context context, List<CouponDetails> couponsList, Checkout checkout) {
        this.context = context;
        this.checkout = checkout;
        this.couponsList = couponsList;
        
        layoutInflater = LayoutInflater.from(context);
    }
    
    //********** Returns the total number of items in the data set represented by this Adapter *********//
    
    @Override
    public int getCount() {
        return couponsList.size();
    }
    
    
    //********** Returns the item associated with the specified position in the data set *********//
    
    @Override
    public Object getItem(int position) {
        return couponsList.get(position);
    }
    
    
    //********** Returns the item id associated with the specified position in the list *********//
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    
    
    //********** Returns a View that displays the data at the specified position in the data set *********//
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_demo_coupon, parent, false);

            holder = new ViewHolder();

            holder.use_coupon = (Button) convertView.findViewById(R.id.use_coupon_btn);
            holder.coupon_code = (TextView) convertView.findViewById(R.id.coupon_code);
            holder.coupon_type = (TextView) convertView.findViewById(R.id.coupon_type);
            holder.coupon_amount = (TextView) convertView.findViewById(R.id.coupon_amount);
            holder.coupon_details = (TextView) convertView.findViewById(R.id.coupon_details);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }



        holder.coupon_code.setText("Code : "+ couponsList.get(position).getCode());
        holder.coupon_type.setText("Type : "+ couponsList.get(position).getDiscountType());
        holder.coupon_amount.setText("Discount : "+ couponsList.get(position).getAmount());
        holder.coupon_details.setText(couponsList.get(position).getDescription());


        holder.use_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                my_cart.setCouponCode(couponsList.get(position).getCode());

            }
        });


        return convertView;
    }
    
    
    
    /********** Custom ViewHolder provides a direct reference to each of the Views within a Data_Item *********/
    
    static class ViewHolder {
        private Button use_coupon;
        private TextView coupon_code, coupon_type, coupon_amount, coupon_details;

    }

}

