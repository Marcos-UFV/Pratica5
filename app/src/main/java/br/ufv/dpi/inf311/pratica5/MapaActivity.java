package br.ufv.dpi.inf311.pratica5;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import br.ufv.dpi.inf311.pratica5.model.Categoria;
import br.ufv.dpi.inf311.pratica5.model.Local;

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap map;
    private LatLng latLng;
    private BancoDados bd;
    private List<Local> locaisVisitados;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        bd = BancoDados.getInstance();
        criaLocaisVisitados();
        Intent intent = getIntent();
        double latitude = intent.getDoubleExtra("latitude", 0);
        double longitude = intent.getDoubleExtra("longitude",0);
        latLng = new LatLng(latitude,longitude);
        Log.i("MAP","Recebeu - Lat: "+latitude+"Long: "+longitude);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    private void criaLocaisVisitados(){
        Cursor c = bd.buscar("Checkin c, Categoria ca", new String[]{"c.Local local", "c.qtdVisitas visitas", "c.latitude latitude", "c.longitude longitude", "ca.idCategoria idCategoria", "ca.nome nomeCategoria"}, "c.cat = ca.idCategoria", null);
        locaisVisitados = new ArrayList<>();
        while(c.moveToNext()){
            int local = c.getColumnIndex("local");
            int qtd = c.getColumnIndex("visitas");
            int latitude = c.getColumnIndex("latitude");
            int longitude = c.getColumnIndex("longitude");

            int id = c.getColumnIndex("idCategoria");
            int nome = c.getColumnIndex("nomeCategoria");
            Categoria categoria = new Categoria(c.getInt(id), c.getString(nome));
            Local l = new Local(c.getString(local), c.getInt(qtd), c.getString(latitude), c.getString(longitude), categoria);
            locaisVisitados.add(l);
            Log.i("MAP","Categoria buscada - ID: "+c.getInt(id)+"Nome: "+c.getString(nome));
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
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
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
        if(id == R.id.item5){
            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.addMarker(new MarkerOptions()
                .position(latLng));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,19));
        Log.i("MAP","O mapa está pronto!");
        desenharLocaisVisitados();
    }
    private void desenharLocaisVisitados(){
        Log.i("MAP","Desenhando locais visitados!");
        for(Local l:locaisVisitados){
            String latitude = l.getLatitude().replace(",",".");
            String longitude = l.getLongitude().replace(",",".");
            latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
            Categoria categoria = l.getCategoria();
            String snippet = String.format("Categoria: %s Visitas: %d",categoria.getNome(),l.getQtdVisitas());
            map.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(l.getNome()))
                    .setSnippet(snippet);
        }
        Log.i("MAP","Locais visitados desenhados!");
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}