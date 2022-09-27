package com.gm3_developer.sem5_demo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Modal {

    Dialog myDialog;
    AlertDialog.Builder dialogo;
    boolean validaInput = false;
    String codigo;
    String descripcion;
    String precio;
    String id_cate;

    SQLiteDatabase bd = null;
    Dto datos = new Dto();
    Categoria cate = new Categoria();

    public void Search(final Context context){
        myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.ventana1);
        myDialog.setTitle("Search");
        myDialog.setCancelable(false);
        final ConexionSQLite conexion = new ConexionSQLite(context);
        final EditText et_cod = myDialog.findViewById(R.id.et_cod);
        Button btn_buscar = myDialog.findViewById(R.id.btn_buscar);
        TextView tv_close = myDialog.findViewById(R.id.tv_close);
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });

        btn_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_cod.getText().toString().length()==0){
                    validaInput = false;
                    et_cod.setError("Campo obligatorio");
                }else{
                    validaInput = true;
                }

                if(validaInput){
                    String cod = et_cod.getText().toString();
                    datos.setCodigo(Integer.parseInt(cod));
                    if(conexion.consultaCodigo(datos, cate)){
                        codigo = String.valueOf(datos.getCodigo());
                        descripcion = datos.getDescripcion();
                        precio = String.valueOf(datos.getPrecio());
                        id_cate = String.valueOf(cate.getId_cate());
                        String action;
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("senal","1");
                        intent.putExtra("codigo", codigo);
                        intent.putExtra("descripcion", descripcion);
                        intent.putExtra("precio", precio);
                        intent.putExtra("id_cate", id_cate);
                        context.startActivity(intent);

                        myDialog.dismiss();
                    }else{
                        Toast.makeText(context, "No se ha encontrado resultados para la búsqueda especificada.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "No ha especificado lo que desea buscar.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

}
