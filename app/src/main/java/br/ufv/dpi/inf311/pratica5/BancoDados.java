package br.ufv.dpi.inf311.pratica5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import br.ufv.dpi.inf311.pratica5.model.Categoria;
import br.ufv.dpi.inf311.pratica5.model.Local;

public class BancoDados {
    protected SQLiteDatabase db;
    private final String NOME_BANCO = "pratica5_bd";
    private static BancoDados INSTANCE;
    private final String[] SCRIPT = new String[]{
            "CREATE TABLE Checkin (Local TEXT PRIMARY KEY, qtdVisitas INTEGER "+
            "NOT NULL, cat INTEGER NOT NULL, latitude TEXT NOT NULL,"+
            "longitude TEXT NOT NULL, CONSTRAINT fkey0 FOREIGN KEY (cat)"+
            "REFERENCES Categoria (idCategoria));",
            "CREATE TABLE Categoria (idCategoria INTEGER PRIMARY KEY " +
            "AUTOINCREMENT, nome TEXT NOT NULL);",
            "INSERT INTO Categoria (nome) VALUES ('Restaurante');",
            "INSERT INTO Categoria (nome) VALUES ('Bar');",
            "INSERT INTO Categoria (nome) VALUES ('Cinema');",
            "INSERT INTO Categoria (nome) VALUES ('Universidade');",
            "INSERT INTO Categoria (nome) VALUES ('Estádio');",
            "INSERT INTO Categoria (nome) VALUES ('Parque');",
            "INSERT INTO Categoria (nome) VALUES ('Outros');"
    };
    private BancoDados(){
        Context ctx = MyApp.getAppContext();
        db = ctx.openOrCreateDatabase(NOME_BANCO,Context.MODE_PRIVATE,null);
        Cursor c = buscar("sqlite_master", null, "type = 'table'", "");
        if(c.getCount() == 1){
            for (String s : SCRIPT) {
                db.execSQL(s);
            }
            Log.i("BANCO_DADOS", "Criou tabelas do banco e as populou.");
        }
        c.close();
        Log.i("BANCO_DADOS", "Abriu conexão com o banco.");
    }
    public static BancoDados getInstance(){
        if(INSTANCE == null){
            INSTANCE = new BancoDados();
        }
        return INSTANCE;
    }
    public Cursor buscar(String tabela, String colunas[], String where, String orderBy) {
        Cursor c;
        if(!where.equals(""))
            c = db.query(tabela, colunas, where, null, null, null, orderBy);
        else
            c = db.query(tabela, colunas, null, null, null, null, orderBy);
        Log.i("BANCO_DADOS", "Realizou uma busca e retornou [" + c.getCount() + "] registros.");
        return c;
    }
    public List<Local> getTodosLocaisVisitados(){
        List<Local> locaisVisitados = new ArrayList<>();
        Cursor c = INSTANCE.buscar("Checkin",new String[]{"Local","qtdVisitas","cat","latitude","longitude"},"","qtdVisitas desc");
        while(c.moveToNext()){
            int local = c.getColumnIndex("Local");
            int qtd = c.getColumnIndex("qtdVisitas");
            int latitude = c.getColumnIndex("latitude");
            int longitude = c.getColumnIndex("longitude");
            Local l = new Local(c.getString(local), c.getInt(qtd), c.getString(latitude), c.getString(longitude),null);
            locaisVisitados.add(l);
            String msg = String.format("Local: "+l);
            Log.i("LOCATION",msg);
        }
        Log.i("LOCATION","Nº de loacais visitados: "+locaisVisitados.size());
        return locaisVisitados;
    }
    public List<Local> getTodosLocaisVisitadosComCategorias(){
        Cursor c = INSTANCE.buscar("Checkin c, Categoria ca", new String[]{"c.Local local", "c.qtdVisitas visitas", "c.latitude latitude", "c.longitude longitude", "ca.idCategoria idCategoria", "ca.nome nomeCategoria"}, "c.cat = ca.idCategoria", null);
        List<Local> locaisVisitados = new ArrayList<>();
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
        return locaisVisitados;
    }
    public long inserir(String tabela, ContentValues valores) {
        long id = db.insert(tabela, null, valores);
        Log.i("BANCO_DADOS", "Cadastrou registro com o id [" + id + "]");
        return id;
    }
    public int atualizar(String tabela, ContentValues valores, String where) {
        int count = db.update(tabela, valores, where, null);
        Log.i("BANCO_DADOS", "Atualizou [" + count + "] registros");
        return count;
    }

    public int deletar(String tabela, String[] where) {
        int count = db.delete(tabela, "Local = ?", where);
        Log.i("BANCO_DADOS", "Deletou [" + count + "] registros");
        return count;
    }

    public void abrir() {
        Context ctx = MyApp.getAppContext();
        db = ctx.openOrCreateDatabase(NOME_BANCO, Context.MODE_PRIVATE, null);
        Log.i("BANCO_DADOS", "Abriu conexão com o banco.");
    }
    public void fechar() {
        if (db != null && db.isOpen()) {
            db.close();
            Log.i("BANCO_DADOS", "Fechou conexão com o Banco.");
        }
    }
}
