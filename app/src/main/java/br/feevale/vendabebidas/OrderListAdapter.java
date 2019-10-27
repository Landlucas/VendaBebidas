package br.feevale.vendabebidas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;

public class OrderListAdapter extends BaseAdapter {
    StoreDatabaseHelper db;
    Context ctx;
    LayoutInflater inflater;

    public OrderListAdapter(Context ctx, StoreDatabaseHelper db){
        inflater = LayoutInflater.from(ctx);
        this.db = db;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return db.getOrders().size();
    }

    @Override
    public Object getItem(int i) {
        return db.getOrders().get(i);
    }

    @Override
    public long getItemId(int i) {
        return db.getOrders().get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = inflater.inflate(R.layout.order_list_item, viewGroup, false);
        TextView customer = (TextView) v.findViewById(R.id.customer);
        TextView total = (TextView) v.findViewById(R.id.total);

        Order o = db.getOrders().get(i);
        customer.setText(o.getCustomer().getName());
        DecimalFormat formater = new DecimalFormat("0.00");
        total.setText(ctx.getResources().getString(R.string.currency_prefix) + " " + formater.format(o.getTotal()));

        return v;
    }
}
