package br.ufv.dpi.inf311.pratica5;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import br.ufv.dpi.inf311.pratica5.model.Local;
import br.ufv.dpi.inf311.pratica5.view.LocalAdapter;
import br.ufv.dpi.inf311.pratica5.view.LocalAdapterBasic;

public class RelatorioActivity extends AppCompatActivity {
    private BancoDados bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);
        bd = BancoDados.getInstance();
        List<Local> locaisVisitados = bd.getTodosLocaisVisitados();
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(new LocalAdapterBasic(locaisVisitados,this));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gestao_activity_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }
}