package com.vincent.psm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vincent.psm.R;
import com.vincent.psm.data.Cart;

import java.util.ArrayList;

import static com.vincent.psm.data.DataHelper.Comma;

public class CartListAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<Cart> carts;

    public CartListAdapter(Context context, ArrayList<Cart> carts) {
        layoutInflater = LayoutInflater.from(context);
        this.carts = carts;
    }

    @Override
    public int getCount() {
        return carts.size();
    }

    @Override
    public Object getItem(int position) {
        return carts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null)
            view = layoutInflater.inflate(R.layout.lst_cart, parent, false);

        TextView txtCartName = view.findViewById(R.id.txtCartName);
        TextView txtTotal = view.findViewById(R.id.txtTotal);
        TextView txtDate = view.findViewById(R.id.txtDate);
        TextView txtSalesName = view.findViewById(R.id.txtSales);

        txtCartName.setText(carts.get(position).getCartName());
        txtTotal.setText("$ " + Comma(String.valueOf(carts.get(position).getTotal())));
        //txtDate.setText(carts.get(position).getCartName());
        txtSalesName.setText(carts.get(position).getSalesName());

        return view;
    }
}