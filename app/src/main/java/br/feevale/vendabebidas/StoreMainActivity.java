package br.feevale.vendabebidas;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
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
        db = StoreDatabaseHelper.getInstance(this);

        listOrders = (ListView) findViewById(R.id.listOrders);
        newOrderCustomer = (Spinner) findViewById(R.id.customer);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);

        orderAdapter = new OrderListAdapter(getBaseContext(), db);
        listOrders.setAdapter(orderAdapter);
        registerForContextMenu(listOrders);

        ArrayAdapter<Customer> customerAdapter = new ArrayAdapter<Customer>(this, android.R.layout.simple_spinner_item, db.getCustomers());
        customerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newOrderCustomer.setAdapter(customerAdapter);
    }

    public void buttonAddClick(View v) {
        lastOrder = new Order();
        Customer c = (Customer) newOrderCustomer.getSelectedItem();
        lastOrder.setCustomer(c);
        lastOrder.setTotal((double) 0);
        Long cId = db.addOrder(lastOrder);
        lastOrder.setId(cId);
        newOrderCustomer.setSelection(0);
        Intent intent = new Intent(this, OrderItemsActivity.class);
        intent.putExtra("orderId", lastOrder.getId());
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu,
                                    View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_crud, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.remove) {
            try {
                AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                lastOrder = (Order) listOrders.getItemAtPosition(acmi.position);
                db.removeOrder(lastOrder);
                orderAdapter.notifyDataSetChanged();
                Toast toast = Toast.makeText(getBaseContext(), "Removida venda para " + lastOrder.getCustomer().getName(), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } catch (SQLiteConstraintException e) {
                Toast toast = Toast.makeText(getBaseContext(), "Venda não pode ser removida!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
        if (item.getItemId() == R.id.edit) {
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            lastOrder = (Order) listOrders.getItemAtPosition(acmi.position);
            Intent intent = new Intent(this, OrderItemsActivity.class);
            intent.putExtra("orderId", lastOrder.getId());
            startActivity(intent);
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.customers) {
            Intent intent = new Intent(this, CustomerActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.drinks) {
            Intent intent = new Intent(this, DrinkActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        orderAdapter.notifyDataSetChanged();
    }
}
