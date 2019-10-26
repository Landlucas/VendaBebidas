package br.feevale.vendabebidas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class StoreMainActivity extends AppCompatActivity {

    StoreDatabaseHelper db;
    Order lastOrder;
    ListView listOrders;
    OrderListAdapter orderAdapter;
    Spinner newOrderCustomer;
    Button buttonAdd;
    private boolean isAdd = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_main);

        listOrders          = (ListView) findViewById(R.id.listOrders);
        newOrderCustomer    = (Spinner) findViewById(R.id.customer);
        buttonAdd           = (Button) findViewById(R.id.buttonAdd);

        db = new StoreDatabaseHelper(this);
        orderAdapter = new OrderListAdapter(getBaseContext(), db);
        listOrders.setAdapter(orderAdapter);

        ArrayAdapter<Customer> customerAdapter = new ArrayAdapter<Customer>(this, android.R.layout.simple_spinner_item, db.getCustomers());
        customerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newOrderCustomer.setAdapter(customerAdapter);
    }

    public void buttonAddClick(View v) {
        lastOrder = new Order();
        Customer c = (Customer) newOrderCustomer.getSelectedItem();
        lastOrder.setCustomer( c );
        lastOrder.setTotal(new Double(1));
        Long cId = db.addOrder(lastOrder);
        lastOrder.setId(cId);
        orderAdapter.notifyDataSetChanged();
        newOrderCustomer.setSelection(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.customers){
            Intent intent = new Intent(this, CustomerActivity.class);
            startActivity(intent);
        }
        if(item.getItemId() == R.id.drinks){
            Intent intent = new Intent(this, DrinkActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
