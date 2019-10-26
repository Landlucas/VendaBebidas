package br.feevale.vendabebidas;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class DrinkActivity extends AppCompatActivity {
    StoreDatabaseHelper db;
    Drink lastDrink;
    ListView listDrinks;
    DrinkListAdapter drinkAdapter;
    EditText newName, newVolume, newPrice;
    Spinner newIsAlcoholic;
    Button buttonAdd;
    Integer newIsAlchoholicNumber = 0;
    private boolean isAdd = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listDrinks      = (ListView) findViewById(R.id.listDrinks);
        newName         = (EditText) findViewById(R.id.name);
        newVolume       = (EditText) findViewById(R.id.volume);
        newIsAlcoholic  = (Spinner) findViewById(R.id.isAlcoholic);
        newPrice        = (EditText) findViewById(R.id.price);
        buttonAdd       = (Button) findViewById(R.id.buttonAdd);

        db = new StoreDatabaseHelper(this);
        drinkAdapter = new DrinkListAdapter(getBaseContext(), db);
        listDrinks.setAdapter(drinkAdapter);

        String spinnerOptions[] = {"É Alcoólico?","Não", "Sim"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newIsAlcoholic.setAdapter(adapter);

        newIsAlcoholic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i > 0) {
                    newIsAlchoholicNumber = i--;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d("ITEM","NOT");
            }
        });
        
        registerForContextMenu(listDrinks);
    }

    public void buttonAddClick(View v){
        if(isAdd) {
            lastDrink = new Drink();
            lastDrink.setName(newName.getText().toString());
            lastDrink.setVolume(Integer.parseInt(newVolume.getText().toString()));
            lastDrink.setAlcoholic(newIsAlchoholicNumber);
            lastDrink.setPrice(Double.parseDouble((newPrice.getText().toString())));
            Long cId = db.addDrink(lastDrink);
            lastDrink.setId(cId);
        } else {
            lastDrink.setName(newName.getText().toString());
            lastDrink.setVolume(Integer.parseInt(newVolume.getText().toString()));
            lastDrink.setAlcoholic(newIsAlchoholicNumber);
            lastDrink.setPrice(Double.parseDouble((newPrice.getText().toString())));
            db.updateDrink(lastDrink);
            isAdd = true;
            buttonAdd.setText(getResources().getString(R.string.add_drink));
        }
        drinkAdapter.notifyDataSetChanged();
        newName.setText("");
        newVolume.setText("");
        newIsAlcoholic.setSelection(0);
        newPrice.setText("");
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
                lastDrink = (Drink) listDrinks.getItemAtPosition(acmi.position);
                db.removeDrink(lastDrink);
                drinkAdapter.notifyDataSetChanged();
                Toast toast = Toast.makeText(getBaseContext(), "Removido " + lastDrink.getName(), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } catch (SQLiteConstraintException e){
                Toast toast = Toast.makeText(getBaseContext(), "Bebida não pode ser removida!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
        if (item.getItemId() == R.id.edit) {
            buttonAdd.setText(getResources().getString(R.string.edit_drink));
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            lastDrink = (Drink) listDrinks.getItemAtPosition(acmi.position);
            newName.setText(lastDrink.getName());
            newVolume.setText(lastDrink.getVolume().toString());
            newIsAlcoholic.setSelection(lastDrink.getAlcoholic()+1);
            newPrice.setText(lastDrink.getPrice().toString());
            isAdd = false;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(DrinkActivity.this, StoreMainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
