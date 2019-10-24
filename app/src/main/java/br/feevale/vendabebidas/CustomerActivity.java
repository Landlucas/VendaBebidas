package br.feevale.vendabebidas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import br.feevale.databasesqlite.R;

public class CustomerActivity extends AppCompatActivity {
    StoreDatabase db;
    Customer lastCustomer;
    ListView listCustomers;
    CustomerListAdapter customerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        listCustomers = (ListView) findViewById(R.id.listCustomers);

        db = new StoreDatabase(this);
        customerAdapter = new CustomerListAdapter(getBaseContext(), db);
        listCustomers.setAdapter(customerAdapter);
    }
}
