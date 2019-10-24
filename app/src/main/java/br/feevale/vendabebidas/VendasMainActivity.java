package br.feevale.vendabebidas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import br.feevale.databasesqlite.R;

public class VendasMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendas_main);
    }

    // Cria um options menu. este é o menu que fica associado à barra superior
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Operação chamada pelo menu da barra superior
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.customers){
            Intent intent = new Intent(this, CustomerActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
