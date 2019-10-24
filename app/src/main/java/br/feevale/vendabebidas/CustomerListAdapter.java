package br.feevale.vendabebidas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import br.feevale.databasesqlite.R;

public class CustomerListAdapter extends BaseAdapter {
    VendasDatabase db;
    Context ctx;
    LayoutInflater inflater;

    public CustomerListAdapter(Context ctx, VendasDatabase db){
        inflater = LayoutInflater.from(ctx);
        this.db = db;
    }

    @Override
    public int getCount() {
        return db.getCustomers().size();
    }

    @Override
    public Object getItem(int i) {
        return db.getCustomers().get(i);
    }

    @Override
    public long getItemId(int i) {
        return db.getCustomers().get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = inflater.inflate(R.layout.customer_list_item, viewGroup, false);
        TextView name = (TextView) v.findViewById(R.id.name);
        TextView email = (TextView) v.findViewById(R.id.email);
        TextView address = (TextView) v.findViewById(R.id.address);

        Customer c = db.getCustomers().get(i);
        name.setText(c.getName());
        email.setText(c.getEmail());
        address.setText(c.getAddress());

        return v;
    }
}
