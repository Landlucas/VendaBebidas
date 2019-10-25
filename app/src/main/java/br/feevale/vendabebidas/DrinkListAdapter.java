package br.feevale.vendabebidas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;

public class DrinkListAdapter extends BaseAdapter {
    StoreDatabase db;
    Context ctx;
    LayoutInflater inflater;

    public DrinkListAdapter(Context ctx, StoreDatabase db){
        inflater = LayoutInflater.from(ctx);
        this.db = db;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return db.getDrinks().size();
    }

    @Override
    public Object getItem(int i) {
        return db.getDrinks().get(i);
    }

    @Override
    public long getItemId(int i) {
        return db.getDrinks().get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = inflater.inflate(R.layout.drink_list_item, viewGroup, false);
        TextView name = (TextView) v.findViewById(R.id.name);
        TextView volume = (TextView) v.findViewById(R.id.volume);
        TextView isAlcoholic = (TextView) v.findViewById(R.id.isAlcoholic);
        TextView price = (TextView) v.findViewById(R.id.price);

        Drink d = db.getDrinks().get(i);
        name.setText(d.getName());
        volume.setText(d.getVolume().toString() + " ml");
        if(d.getAlcoholic() == 1) {
            isAlcoholic.setText(ctx.getResources().getString(R.string.alcoholic_drink));
        } else if(d.getAlcoholic() == 0) {
            isAlcoholic.setText(ctx.getResources().getString(R.string.not_alcoholic_drink));
        }
        DecimalFormat formater = new DecimalFormat("#.00");
        price.setText(ctx.getResources().getString(R.string.currency_prefix) + formater.format(d.getPrice()));

        return v;
    }
}
