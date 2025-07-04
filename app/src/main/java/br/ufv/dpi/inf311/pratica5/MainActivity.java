package br.ufv.dpi.inf311.pratica5;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import br.ufv.dpi.inf311.pratica5.model.Categoria;
import br.ufv.dpi.inf311.pratica5.model.Local;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private static final int FINE_PERMISSION_CODE = 12;
    private LocationManager lm;
    private Criteria criteria;
    private String provider;
    private TextView latTextView;
    private TextView longTextView;
    Location currentLocation;
    BancoDados bd;
    private Map<Integer,Categoria> categorias;
    private MaterialAutoCompleteTextView localTextView;
    private MaterialAutoCompleteTextView autoCompleteTextView;
    private Set<Local> locaisVisitados;
    private MaterialButton btnCheckIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        latTextView = findViewById(R.id.latitude);
        longTextView = findViewById(R.id.longitude);
        btnCheckIn = findViewById(R.id.btnCheckIn);
        btnCheckIn.setOnClickListener(onClickCheckIn());
        bd = BancoDados.getInstance();
        criaCategorias();
        criaLocaisVisitados();

        criteria = new Criteria();
        PackageManager packageManager = getPackageManager();
        boolean hasGPS = packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
        if(hasGPS){
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
        }else{
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        }
    }
    private void criaLocaisVisitados(){
        locaisVisitados = new HashSet<>();
        //,"qtdVisitadas","latitude","longitude"
        Cursor c = bd.buscar("Checkin",new String[]{"Local","qtdVisitas","cat","latitude","longitude"},"","");

        while(c.moveToNext()){
            int local = c.getColumnIndex("Local");
            int qtd = c.getColumnIndex("qtdVisitas");
            int latitude = c.getColumnIndex("latitude");
            int longitude = c.getColumnIndex("longitude");
            int cat = c.getColumnIndex("cat");
            Categoria categoria = categorias.get(c.getInt(cat));
            Local l = new Local(c.getString(local), c.getInt(qtd), c.getString(latitude), c.getString(longitude),categoria);
            locaisVisitados.add(l);
            String msg = String.format("Local: "+l);
            Log.i("LOCATION",msg);
        }
        Log.i("LOCATION","Nº de loacais visitados: "+locaisVisitados.size());
    }

    private void criaCategorias(){
        Cursor c = bd.buscar("Categoria",new String[]{"idCategoria","nome"},"","");
        categorias = new HashMap<>();
        while(c.moveToNext()){
            int id = c.getColumnIndex("idCategoria");
            int nome = c.getColumnIndex("nome");
            categorias.put(c.getInt(id),new Categoria(c.getInt(id),c.getString(nome)));
        }
        Log.i("LOCATION","Nº de categorias: "+categorias.size());
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestLocationPermission();
        provider = lm.getBestProvider(criteria,true);
        if(provider != null){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            lm.requestLocationUpdates(provider,5000,0,this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<String> categoriasList = new ArrayList<>();
        for(Categoria c: categorias.values()){
            categoriasList.add(c.getNome());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, categoriasList);
        autoCompleteTextView = findViewById(R.id.autoCompleteView);
        autoCompleteTextView.setAdapter(arrayAdapter);
        localTextView = findViewById(R.id.localAutoComplete);
        List<String> locais  = new ArrayList<>();
        for(Local l:locaisVisitados){
            locais.add(l.getNome());
        }
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, locais);
        localTextView.setAdapter(stringArrayAdapter);
    }

    public View.OnClickListener onClickCheckIn(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomeLocal = localTextView.getText().toString();
                String categoria = autoCompleteTextView.getText().toString();
                if(currentLocation == null || nomeLocal.isEmpty() || categoria.isEmpty()){
                    Toast.makeText(getContext(),"Valores não podem ser nulos",Toast.LENGTH_LONG).show();
                }else{
                    String lat = latTextView.getText().toString();
                    String longt = longTextView.getText().toString();
                    Categoria cat = null;
                    for(Categoria c: categorias.values()){
                        if(c.getNome().equals(categoria)){
                            cat = c;
                        }
                    }
                    Local local = new Local(nomeLocal, 1, lat, longt,cat);
                    ContentValues cv = new ContentValues();
                    if(!locaisVisitados.contains(local)){
                        cv.put("Local",local.getNome());
                        cv.put("qtdVisitas",local.getQtdVisitas());
                        cv.put("cat",local.getCategoria().getId());
                        cv.put("latitude",local.getLatitude());
                        cv.put("longitude",local.getLongitude());
                        bd.inserir("Checkin",cv);
                    }else{
                        Iterator<Local> iterator = locaisVisitados.iterator();
                        while (iterator.hasNext()){
                            Local next = iterator.next();
                            if(local.equals(next)){
                                local.setQtdVisitas(next.getQtdVisitas() + 1);
                                break;
                            }
                        }
                        String where = String.format("Local = '%s'",local.getNome());
                        cv.put("qtdVisitas",local.getQtdVisitas());
                        bd.atualizar("Checkin",cv,where);
                    }
                    finish();
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        };
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
            if(currentLocation != null){
                Intent intent = new Intent(this, MapaActivity.class);
                intent.putExtra("latitude",currentLocation.getLatitude());
                intent.putExtra("longitude",currentLocation.getLongitude());
                startActivity(intent);
            }else{
                Toast.makeText(getContext(),"Aguarde a definição da localização",Toast.LENGTH_LONG).show();
            }
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

    @Override
    public void onLocationChanged(@NonNull Location location) {
        currentLocation = location;
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        DecimalFormat df = new DecimalFormat("0.#######");
        latTextView.setText(df.format(latitude));
        longTextView.setText(df.format(longitude));

        Log.i("LOCATION","Latitude: "+latitude+" Longitude: "+longitude);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, int deviceId) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId);
        switch (requestCode){
            case FINE_PERMISSION_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    updateCurrentLocation();
                }
        }
    }
    public void requestLocationPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_COARSE_LOCATION)){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},FINE_PERMISSION_CODE);
            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},FINE_PERMISSION_CODE);
            }
        }else{
            updateCurrentLocation();
        }
    }
    private void updateCurrentLocation(){
        if(currentLocation != null){
            double latitude = currentLocation.getLatitude();
            double longitude = currentLocation.getLongitude();

            DecimalFormat df = new DecimalFormat("0.#######");
            latTextView.setText(df.format(latitude));
            longTextView.setText(df.format(longitude));
        }
    }
    private Context getContext(){
        return this;
    }

    @Override
    protected void onDestroy() {
        lm.removeUpdates(this);
        super.onDestroy();
    }
}