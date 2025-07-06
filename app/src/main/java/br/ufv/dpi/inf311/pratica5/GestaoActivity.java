package br.ufv.dpi.inf311.pratica5;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.ufv.dpi.inf311.pratica5.model.Local;
import br.ufv.dpi.inf311.pratica5.view.LocalAdapter;

public class GestaoActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Local> locais;
    BancoDados bd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestao);
        bd = BancoDados.getInstance();
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAnimation();
        recyclerView.setHasFixedSize(true);
        initLocais();
        recyclerView.setAdapter(new LocalAdapter(locais,getContext(),onClickLocal()));
    }

    private void initLocais(){
        locais = new ArrayList<>();
        //,"qtdVisitadas","latitude","longitude"
        Cursor c = bd.buscar("Checkin",new String[]{"Local","qtdVisitas","cat","latitude","longitude"},"","");

        while(c.moveToNext()){
            int local = c.getColumnIndex("Local");
            int qtd = c.getColumnIndex("qtdVisitas");
            int latitude = c.getColumnIndex("latitude");
            int longitude = c.getColumnIndex("longitude");
            int cat = c.getColumnIndex("cat");
            Local l = new Local(c.getString(local), c.getInt(qtd), c.getString(latitude), c.getString(longitude),null);
            String msg = String.format("Local: "+l);
            locais.add(l);
            Log.i("LOCATION",msg);
        }
        Log.i("LOCATION","Nº de loacais visitados: "+locais.size());
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
    private LocalAdapter.LocalOnClickListener onClickLocal(){
        return new LocalAdapter.LocalOnClickListener() {
            @Override
            public void onClickLocal(View view, int idx) {
                Local local = locais.get(idx);
              //  Toast.makeText(getContext(),"Local: "+local,Toast.LENGTH_LONG).show();
            }
        };
    }
    private Context getContext(){
        return this;
    }
}