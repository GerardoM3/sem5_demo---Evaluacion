package com.gm3_developer.sem5_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ConsultaSpinner extends AppCompatActivity {
    private Spinner sp_options;
    private TextView tv_cod, tv_descripcion, tv_precio, tv_IdCate, tv_NombreCate;

    ConexionSQLite conexion = new ConexionSQLite(this);
    Dto datos = new Dto();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_spinner);

        sp_options = findViewById(R.id.sp_options);

        tv_cod = findViewById(R.id.tv_cod);
        tv_descripcion = findViewById(R.id.tv_descripcion);
        tv_precio = findViewById(R.id.tv_precio);
        tv_IdCate = findViewById(R.id.tv_IdCate);
        tv_NombreCate = findViewById(R.id.tv_NombreCate);

        conexion.consultaListaArticulos();

        ArrayAdapter<CharSequence> adaptador = new ArrayAdapter(this, android.R.layout.simple_spinner_item, conexion.obtenerListaArticulos());
        sp_options.setAdapter(adaptador);

        sp_options.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position!=0){
                    tv_cod.setText("Código: " + conexion.consultaListaArticulos().get(position-1).getCodigo());
                    tv_descripcion.setText("Descripción: " + conexion.consultaListaArticulos().get(position-1).getDescripcion());
                    tv_precio.setText("Precio: " + String.valueOf(conexion.consultaListaArticulos().get(position-1).getPrecio()));
                    tv_IdCate.setText("ID Categoría: " + String.valueOf(conexion.consultaListaArticulos().get(position-1).getId_cate()));
                    tv_NombreCate.setText("Categoría: " + conexion.consultaListaArticulos().get(position-1).getNombrecategoria());
                }else{
                    tv_cod.setText("Código: ");
                    tv_descripcion.setText("Descripción: ");
                    tv_precio.setText("Precio: ");
                    tv_IdCate.setText("ID Categoría: ");
                    tv_NombreCate.setText("Categoría: ");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
