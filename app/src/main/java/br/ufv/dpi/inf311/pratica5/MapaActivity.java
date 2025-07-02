package br.ufv.dpi.inf311.pratica5;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MapaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mapa_activity_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.item1){
            finish();
            return true;
        }

        if(id == R.id.item2){
            Intent intent = new Intent(this, GestaoActivity.class);
            startActivity(intent);
        }

        if(id == R.id.item3){
            Intent intent = new Intent(this, RelatorioActivity.class);
            startActivity(intent);
        }
        if(id == R.id.item4){
            //TODO: alterar exibição do mapa para normal
        }
        if(id == R.id.item5){
            //TODO: alterar exibição do mapa para híbidro
        }
        return super.onOptionsItemSelected(item);
    }
}