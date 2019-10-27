package br.feevale.vendabebidas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

public class OrderItemsActivity extends AppCompatActivity {
    StoreDatabaseHelper db;
    Order order;
    OrderItem lastItem;
    OrderItemListAdapter orderItemAdapter;
    ListView listOrderItems;
    Spinner newDrink;
    EditText newQty;
    Button buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_items);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = StoreDatabaseHelper.getInstance(this);

        listOrderItems = (ListView) findViewById(R.id.listOrderItems);
        newDrink = (Spinner) findViewById(R.id.drink);
        newQty = (EditText) findViewById(R.id.qty);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);

        Intent intent = getIntent();
        order = db.getOrder(intent.getLongExtra("orderId", 0));

        orderItemAdapter = new OrderItemListAdapter(getBaseContext(), db, order);
        listOrderItems.setAdapter(orderItemAdapter);

        ArrayAdapter<Drink> drinkAdapter = new ArrayAdapter<Drink>(this, android.R.layout.simple_spinner_item, db.getDrinks());
        drinkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newDrink.setAdapter(drinkAdapter);
    }

    public void buttonAddClick(View v) {
        lastItem = new OrderItem();
        lastItem.setOrder(order);
        Drink d = (Drink) newDrink.getSelectedItem();
        lastItem.setDrink(d);
        lastItem.setQty(Integer.parseInt((newQty.getText().toString())));
        Long cId = db.addOrderItem(lastItem);
        lastItem.setId(cId);
        Double newTotal = (double) 0;
        for (OrderItem item: db.getOrderItemsFromOrder(order)) {
            newTotal += item.getDrink().getPrice() * item.getQty();
        }
        order.setTotal(newTotal);
        db.updateOrder(order);
        orderItemAdapter.notifyDataSetChanged();
        newDrink.setSelection(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
