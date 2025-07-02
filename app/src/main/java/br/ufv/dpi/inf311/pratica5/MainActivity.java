package br.ufv.dpi.inf311.pratica5;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onResume() {
        super.onResume();
        String[] categories = getResources().getStringArray(R.array.categories);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, categories);
        MaterialAutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoCompleteView);
        autoCompleteTextView.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.item1){
            Intent intent = new Intent(this, MapaActivity.class);
            startActivity(intent);
        }

        if(id == R.id.item2){
            Intent intent = new Intent(this, GestaoActivity.class);
            startActivity(intent);
        }

        if(id == R.id.item3){
            Intent intent = new Intent(this, RelatorioActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}