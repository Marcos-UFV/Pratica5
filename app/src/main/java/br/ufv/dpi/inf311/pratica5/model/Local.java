package br.ufv.dpi.inf311.pratica5.model;

import java.io.Serializable;
import java.util.Objects;

public class Local implements Serializable {
    private String nome;
    private int qtdVisitas;
    private String latitude;
    private String longitude;
    private Categoria categoria;
    public Local(String nome, int qtdVisitas, String latitude, String longitude,Categoria categoria) {
        this.nome = nome;
        this.qtdVisitas = qtdVisitas;
        this.latitude = latitude;
        this.longitude = longitude;
        this.categoria = categoria;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQtdVisitas() {
        return qtdVisitas;
    }

    public void setQtdVisitas(int qtdVisitas) {
        this.qtdVisitas = qtdVisitas;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Local local = (Local) o;
        return Objects.equals(nome, local.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nome);
    }

    @Override
    public String toString() {
        return "Local{" +
                "nome='" + nome + '\'' +
                ", qtdVisitas=" + qtdVisitas +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", categoria=" + categoria +
                '}';
    }
}
