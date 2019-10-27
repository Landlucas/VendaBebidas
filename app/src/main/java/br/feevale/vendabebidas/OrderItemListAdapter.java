package br.feevale.vendabebidas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;

public class OrderItemListAdapter extends BaseAdapter {
    StoreDatabaseHelper db;
    Context ctx;
    LayoutInflater inflater;
    Order order;

    public OrderItemListAdapter(Context ctx, StoreDatabaseHelper db, Order order) {
        inflater = LayoutInflater.from(ctx);
        this.db = db;
        this.ctx = ctx;
        this.order = order;
    }

    @Override
    public int getCount() {
        return db.getOrderItemsFromOrder(order).size();
    }

    @Override
    public Object getItem(int i) {
        return db.getOrderItemsFromOrder(order).get(i);
    }

    @Override
    public long getItemId(int i) {
        return db.getOrderItemsFromOrder(order).get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = inflater.inflate(R.layout.order_item_list_item, viewGroup, false);
        TextView drink = (TextView) v.findViewById(R.id.item_drink);
        TextView qty = (TextView) v.findViewById(R.id.item_qty);

        OrderItem item = db.getOrderItemsFromOrder(order).get(i);
        drink.setText(item.getDrink().getName());
        qty.setText(item.getQty().toString());

        return v;
    }
}
