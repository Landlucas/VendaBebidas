package br.feevale.vendabebidas;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class CustomerActivity extends AppCompatActivity {
    StoreDatabaseHelper db;
    Customer lastCustomer;
    ListView listCustomers;
    CustomerListAdapter customerAdapter;
    EditText newName, newEmail, newAddress;
    Button buttonAdd;
    private boolean isAdd = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listCustomers   = (ListView) findViewById(R.id.listCustomers);
        newName         = (EditText) findViewById(R.id.name);
        newEmail        = (EditText) findViewById(R.id.email);
        newAddress      = (EditText) findViewById(R.id.address);
        buttonAdd       = (Button) findViewById(R.id.buttonAdd);

        db = new StoreDatabaseHelper(this);
        customerAdapter = new CustomerListAdapter(getBaseContext(), db);
        listCustomers.setAdapter(customerAdapter);
        registerForContextMenu(listCustomers);
    }

    public void buttonAddClick(View v){
        if(isAdd) {
            lastCustomer = new Customer();
            lastCustomer.setName(newName.getText().toString());
            lastCustomer.setEmail(newEmail.getText().toString());
            lastCustomer.setAddress(newAddress.getText().toString());
            Long cId = db.addCustomer(lastCustomer);
            lastCustomer.setId(cId);
        } else {
            lastCustomer.setName(newName.getText().toString());
            lastCustomer.setEmail(newEmail.getText().toString());
            lastCustomer.setAddress(newAddress.getText().toString());
            db.updateCustomer(lastCustomer);
            isAdd = true;
            buttonAdd.setText("Adicionar cliente");
        }
        customerAdapter.notifyDataSetChanged();
        newName.setText("");
        newEmail.setText("");
        newAddress.setText("");
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
                lastCustomer = (Customer) listCustomers.getItemAtPosition(acmi.position);
                db.removeCustomer(lastCustomer);
                customerAdapter.notifyDataSetChanged();
                Toast toast = Toast.makeText(getBaseContext(), "Removido " + lastCustomer.getName(), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } catch (SQLiteConstraintException e){
                Toast toast = Toast.makeText(getBaseContext(), "Cliente não pode ser removido!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
        if (item.getItemId() == R.id.edit) {
            buttonAdd.setText("Editar cliente");
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            lastCustomer = (Customer) listCustomers.getItemAtPosition(acmi.position);
            newName.setText(lastCustomer.getName());
            newEmail.setText(lastCustomer.getEmail());
            newAddress.setText(lastCustomer.getAddress());
            isAdd = false;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(CustomerActivity.this, StoreMainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
