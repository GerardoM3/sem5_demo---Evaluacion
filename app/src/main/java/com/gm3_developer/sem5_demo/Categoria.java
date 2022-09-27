package com.gm3_developer.sem5_demo;

import java.io.Serializable;

public class Categoria implements Serializable {
    int id_cate;
    String nombrecategoria;
    int estadocategoria;

    public Categoria() {
    }

    public Categoria(int id_cate, String nombrecategoria, int estadocategoria) {
        this.id_cate = id_cate;
        this.nombrecategoria = nombrecategoria;
        this.estadocategoria = estadocategoria;
    }

    public int getId_cate() {
        return id_cate;
    }

    public String getNombrecategoria() {
        return nombrecategoria;
    }

    public int getEstadocategoria() {
        return estadocategoria;
    }

    public void setId_cate(int id_cate) {
        this.id_cate = id_cate;
    }

    public void setNombrecategoria(String nombrecategoria) {
        this.nombrecategoria = nombrecategoria;
    }

    public void setEstadocategoria(int estadocategoria) {
        this.estadocategoria = estadocategoria;
    }
}
