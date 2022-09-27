package com.gm3_developer.sem5_demo;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ConexionSQLite extends SQLiteOpenHelper {

    boolean estadoDelete = true;

    ArrayList<String> listaArticulos;
    ArrayList<Dto> articulosList;

    ArrayList<String> listaCate;
    ArrayList<Categoria> cateList;



    public ConexionSQLite(Context context) {
        super(context, "tiendita.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table categorias(id_cate integer not null primary key, nombrecategoria text, estadocategoria integer, fecharegistro datetime)");
        db.execSQL("create table productos(codigo integer not null primary key, descripcion text, precio real, id_cate integer, foreign key (id_cate) references categorias(id_cate))");

        db.execSQL("INSERT INTO categorias VALUES (1, 'Escritorio', 1, datetime('now','localtime')),(2,'Portatiles',1,datetime('now','localtime')),(3,'Accesorios',1,datetime('now','localtime'));");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists productos");
        db.execSQL("drop table if exists categorias");
        onCreate(db);
    }

    public SQLiteDatabase bd(){
        SQLiteDatabase bd = this.getWritableDatabase();
        return bd;
    }

    public boolean InserTradicional(Dto datos, Categoria cat){
        boolean estado = true;
        int resultado;

        Dto producto;

        try{
            int codigo = datos.getCodigo();
            String descripcion = datos.getDescripcion();
            double precio = datos.getPrecio();
            String nombreCate = cat.getNombrecategoria();

            Cursor fila = bd().rawQuery("select codigo from productos where codigo= '" + codigo + "'", null);
            Cursor fila2 = bd().rawQuery("select id_cate from categorias where nombrecategoria = '" + nombreCate + "'", null);

            if(fila.moveToFirst()==true){
                estado = false;
            }else if(fila2.moveToNext()){

                String SQL = "INSERT INTO productos \n" +
                        "(codigo,descripcion,precio, id_cate)\n" +
                        "VALUES \n" +
                        "('" + String.valueOf(codigo) + "', '" + descripcion + "', '" + String.valueOf(precio) + "', '" + String.valueOf(fila2.getInt(0)) + "');";

                bd().execSQL(SQL);
                bd().close();

                estado = true;
            }
        }catch(Exception e){
            estado = false;
            Log.e("Error. ", e.toString());
        }
        return estado;
    }

    public boolean insertarDatos(Dto datos){
        boolean estado = true;
        int resultado;
        ContentValues registro = new ContentValues();
        try{
            registro.put("codigo", datos.getCodigo());
            registro.put("descripcion", datos.getDescripcion());
            registro.put("precio", datos.getPrecio());

            Cursor fila = bd().rawQuery("select codigo from articulos where codigo='" + datos.getCodigo() + "'", null);

            if(fila.moveToFirst() == true){
                estado = false;
            }else{
                resultado = (int) bd().insert("articulos",null, registro);
                if(resultado > 0 )estado=true;
                else estado = false;
            }
        }catch(Exception e){
            estado = false;
            Log.e("Error.", e.toString());
        }
        return estado;
    }

    public boolean InsertRegister(Dto datos){
        boolean estado = true;
        int resultado;
        try{
            int codigo = datos.getCodigo();
            String descripcion = datos.getDescripcion();
            double precio = datos.getPrecio();

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String fecha1 = sdf.format(cal.getTime());

            Cursor fila = bd().rawQuery("select codigo from articulos where codigo='" + datos.getCodigo() + "'", null);
            if(fila.moveToFirst() == true){
                estado = false;
            }else{
                String SQL = "INSERT INTO articulos \n" +
                        "(codigo,descripcion,precio)\n" +
                        "VALUES\n" +
                        "(?,?,?);";

                bd().execSQL(SQL, new String []{String.valueOf(codigo), descripcion, String.valueOf(precio)});

                estado = true;
            }
        }catch (Exception e){
            estado = false;
            Log.e("Error.", e.toString());
        }
        return estado;
    }

    public boolean consultaCodigo(Dto datos, Categoria cat){
        boolean estado = true;
        int resultado;
        SQLiteDatabase bd = this.getWritableDatabase();
        try{
            int  codigo = datos.getCodigo();

            Cursor fila = bd.rawQuery("select codigo, descripcion, precio, id_cate from productos where codigo=" + codigo, null);
            if(fila.moveToFirst()){
                datos.setCodigo(Integer.parseInt(fila.getString(0)));
                datos.setDescripcion(fila.getString(1));
                datos.setPrecio(Double.parseDouble(fila.getString(2)));
                cat.setId_cate(Integer.parseInt(fila.getString(3)));
                estado = true;
            }else{
                estado = false;
            }
            bd.close();
        }catch (Exception e){
            estado = false;
            Log.e("Error.", e.toString());
        }
        return estado;
    }

    public boolean consultaArticulos(Dto datos, Categoria cat){
        boolean estado;
        int resultado;
        SQLiteDatabase bd = this.getReadableDatabase();
        try{
            int idCat = cat.getId_cate();
            String[] parametros = {String.valueOf(datos.getCodigo())};
            String[] campos = {"codigo","descripcion","precio", "id_cate"};
            Cursor fila = bd.query("productos",campos,"codigo=?",parametros,null, null, null);
            if(fila.moveToFirst()){
                datos.setCodigo(Integer.parseInt(fila.getString(0)));
                datos.setDescripcion(fila.getString(1));
                datos.setPrecio(Double.parseDouble(fila.getString(2)));
                estado = true;
            }else{
                estado = false;
            }

            fila.close();
            bd.close();
        }catch (Exception e){
            estado = false;
            Log.e("Error.", e.toString());
        }
        return estado;
    }


    public boolean consultarDescripcion(Dto datos, Categoria cat){
        boolean estado = true;
        int resultado;
        SQLiteDatabase bd = this.getWritableDatabase();
        try{
            String descripcion = datos.getDescripcion();
            Cursor fila = bd.rawQuery("select codigo, descripcion, precio from productos where descripcion='" + descripcion + "'", null);
            if(fila.moveToFirst()){
                datos.setCodigo(Integer.parseInt(fila.getString(0)));
                datos.setDescripcion(fila.getString(1));
                datos.setPrecio(Double.parseDouble(fila.getString(2)));
                estado = true;
            }else{
                estado = false;
            }
            bd.close();
        }catch (Exception e){
            estado = false;
            Log.e("Error.", e.toString());
        }
        return estado;
    }

    public boolean bajaCodigo(final Context context, final Dto datos, final Categoria cat){
        estadoDelete = true;
        try{
            int codigo = datos.getCodigo();
            Cursor fila = bd().rawQuery("select * from productos where codigo=" + codigo, null);
            if(fila.moveToFirst()){
                datos.setCodigo(Integer.parseInt(fila.getString(0)));
                datos.setDescripcion(fila.getString(1));
                datos.setPrecio(Double.parseDouble(fila.getString(2)));

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.ic_delete);
                builder.setTitle("Warning");
                builder.setMessage("¿Está seguro de borrar el registro? \nCódigo: " +
                        datos.getCodigo() + "\nDescripción: " + datos.getDescripcion());
                builder.setCancelable(false);
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int codigo = datos.getCodigo();
                        int cant = bd().delete("productos", "codigo=" + codigo, null);
                        if(cant > 0 ){
                            estadoDelete = true;
                            Toast.makeText(context, "Registro eliminado satisfactoriamente.", Toast.LENGTH_SHORT).show();
                        }else{
                            estadoDelete = false;
                        }
                        bd().close();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                Toast.makeText(context, "No hay resultados encontrados para la búsqueda especificada.", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            estadoDelete = false;
            Log.e("Error.", e.toString());
        }
        return estadoDelete;
    }

    public boolean modificar(Dto datos, Categoria cat){
        boolean estado = true;
        int resultado;
        SQLiteDatabase bd = this.getWritableDatabase();
        try{
            int codigo = datos.getCodigo();
            String descripcion = datos.getDescripcion();
            double precio = datos.getPrecio();
            String nombreCategoria = cat.getNombrecategoria();

            Cursor fila = bd().rawQuery("select id_cate from categorias where nombrecategoria = '" + nombreCategoria + "'", null);

            ContentValues registro = new ContentValues();
            registro.put("codigo", codigo);
            registro.put("descripcion", descripcion);
            registro.put("precio", precio);
            registro.put("id_cat", fila.getInt(0));

            int cant = (int) bd.update("productos", registro, "codigo=" + codigo, null);

            bd.close();
            if(cant>0)estado = true;
            else estado=false;
        }catch (Exception e){
            estado = false;
            Log.e("Error.", e.toString());
        }
        return estado;
    }


    public ArrayList<Dto> consultaListaArticulos(){
        SQLiteDatabase bd = this.getReadableDatabase();

        Dto productos = null;
        Categoria cat = null;
        articulosList = new ArrayList<Dto>();
        cateList = new ArrayList<Categoria>();

        try{
            Cursor fila = bd.rawQuery("select productos.codigo, productos.descripcion, productos.precio, categorias.id_cate, categorias.nombrecategoria from productos inner join categorias on productos.id_cate = categorias.id_cate", null);
            while(fila.moveToNext()){
                productos = new Dto();
                cat = new Categoria();
                productos.setCodigo(fila.getInt(0));
                productos.setDescripcion(fila.getString(1));
                productos.setPrecio(fila.getDouble(2));
                cat.setId_cate(fila.getInt(3));
                cat.setNombrecategoria(fila.getString(4));

                productos.setId_cate(cat.getId_cate());
                productos.setNombrecategoria(cat.getNombrecategoria());

                articulosList.add(productos);
                cateList.add(cat);

                Log.i("codigo", String.valueOf(productos.getCodigo()));
                Log.i("descripcion", productos.getDescripcion().toString());
                Log.i("precio", String.valueOf(productos.getPrecio()));
                Log.i("id_cate", String.valueOf(productos.getId_cate()));
            }
            obtenerListaArticulos();
        }catch (Exception e){
        }
        return articulosList;
    }

    public ArrayList<String> obtenerListaArticulos(){
        listaArticulos = new ArrayList<String>();
        listaArticulos.add("Seleccione");

        for (int i = 0; i < articulosList.size(); i++){
            listaArticulos.add(articulosList.get(i).getCodigo() + " ~ " + articulosList.get(i).getDescripcion());
        }
        return listaArticulos;
    }

    public ArrayList<Categoria> consultaListaCate(){
        SQLiteDatabase bd = this.getReadableDatabase();

        Categoria cate = null;
        cateList = new ArrayList<Categoria>();

        try{
            Cursor fila = bd.rawQuery("select id_cate, nombrecategoria, estadocategoria from categorias;", null);
            while(fila.moveToNext()){
                cate = new Categoria();
                cate.setId_cate(fila.getInt(0));
                cate.setNombrecategoria(fila.getString(1));
                cate.setEstadocategoria(fila.getInt(2));

                cateList.add(cate);

                Log.i("Id Categoria", String.valueOf(cate.getId_cate()));
                Log.i("Nombre de la categoria", cate.getNombrecategoria().toString());
                Log.i("Estado de la categoria", String.valueOf(cate.getEstadocategoria()));
            }
            obtenerListaCate();
        }catch (Exception e){
        }
        return cateList;
    }

    public ArrayList<String> obtenerListaCate(){
        listaCate = new ArrayList<String>();
        listaCate.add("Seleccione");

        for (int i = 0; i < cateList.size(); i++){
            listaCate.add(cateList.get(i).getNombrecategoria());
        }
        return listaCate;
    }

    public ArrayList<String> consultaListaArticulos1(){
        boolean estado = false;
        SQLiteDatabase bd = this.getReadableDatabase();

        Dto productos = null;
        Categoria cat = null;
        articulosList = new ArrayList<Dto>();
        cateList = new ArrayList<Categoria>();

        try{
            Cursor fila = bd.rawQuery("select productos.codigo, productos.descripcion, productos.precio, categorias.id_cate, categorias.nombrecategoria from productos inner join categorias on productos.id_cate = categorias.id_cate", null);
            while(fila.moveToNext()){
                productos = new Dto();
                cat = new Categoria();
                productos.setCodigo(fila.getInt(0));
                productos.setDescripcion(fila.getString(1));
                productos.setPrecio(fila.getDouble(2));
                cat.setId_cate(fila.getInt(3));
                cat.setNombrecategoria(fila.getString(4));
                Log.i("Id Categoria", String.valueOf(cat.getId_cate()));
                Log.i("Nombre de la categoría", cat.getNombrecategoria());

                productos.setId_cate(cat.getId_cate());
                productos.setNombrecategoria(cat.getNombrecategoria());



                articulosList.add(productos);
                cateList.add(cat);
            }

            listaArticulos = new ArrayList<String>();
            listaCate = new ArrayList<String>();


            for(int i=0; i<= articulosList.size(); i++){
                listaArticulos.add(articulosList.get(i).getCodigo() + " ~ " + articulosList.get(i).getDescripcion() + " ~ " + articulosList.get(i).getPrecio() + " ~ " + articulosList.get(i).getId_cate()+ " ~ " + articulosList.get(i).getNombrecategoria());
                /*listaCate.add(cateList.get(i).getId_cate() + " ~ " + cateList.get(i).getNombrecategoria());*/

                /*for(int j=0; j<=cateList.size();j++){
                    listaCate.add(cateList.get(j).getId_cate() + " ~ " + cateList.get(j).getNombrecategoria());
                }*/

            }


        }catch (Exception e){

        }
        return listaArticulos;
    }

}
