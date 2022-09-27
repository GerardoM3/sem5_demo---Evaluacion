package com.gm3_developer.sem5_demo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText et_codigo, et_descripcion, et_precio, et_idCate;
    private Button btn_guardar, btn_consultar1, btn_consultar2, btn_eliminar, btn_actualizar;
    private TextView tv_resultado;

    private Spinner sp_cate;

    Categoria cate = new Categoria();

    boolean inputEt = false;
    boolean inputEd = false;
    boolean input1 = false;
    boolean inputIdCat = false;
    int resultadoInsert = 0;

    Modal ventanas = new Modal();
    ConexionSQLite conexion = new ConexionSQLite(this);
    Dto datos = new Dto();
    AlertDialog.Builder dialogo;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            new android.app.AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_close)
                    .setTitle("Warning")
                    .setMessage("¿Realmente desea salir?")
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            finishAffinity();
                        }
                    })
                    .show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setTitleTextColor(getResources().getColor(R.color.purple_500));
        toolbar.setTitleMargin(0,0,0,0);
        toolbar.setSubtitle("CRUD SQLite-2022");
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.purple_200));
        toolbar.setTitle("Gerardo Monroy");
        setSupportActionBar(toolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                confirmacion();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ventanas.Search(MainActivity.this);
            }
        });

        et_codigo = findViewById(R.id.et_codigo);
        et_descripcion = findViewById(R.id.et_descripcion);
        et_precio = findViewById(R.id.et_precio);
        /*et_idCate = findViewById(R.id.et_idCate);*/
        sp_cate = findViewById(R.id.sp_cate);
        btn_guardar = findViewById(R.id.btn_guardar);
        btn_consultar1 = findViewById(R.id.btn_consultar1);
        btn_consultar2 = findViewById(R.id.btn_consultar2);
        btn_eliminar = findViewById(R.id.btn_eliminar);
        btn_actualizar = findViewById(R.id.btn_actualizar);

        conexion.consultaListaCate();

        ArrayAdapter<CharSequence> adaptador = new ArrayAdapter(this, android.R.layout.simple_spinner_item, conexion.obtenerListaCate());
        sp_cate.setAdapter(adaptador);

        String senal = "";
        String codigo = "";
        String descripcion = "";
        String precio = "";
        String id_cate = "";

        try{
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            if(bundle != null){
                codigo = bundle.getString("codigo");
                senal = bundle.getString("senal");
                descripcion = bundle.getString("descripcion");
                precio = bundle.getString("precio");
                id_cate = bundle.getString("id_cate");

                if(senal.equals("1")){
                    et_codigo.setText(codigo);
                    et_descripcion.setText(descripcion);
                    et_precio.setText(precio);
                    sp_cate.getSelectedItem().toString().equals(id_cate);
                }
            }
        }catch(Exception e){

        }
    }

    private void confirmacion(){
        String mensaje = "¿Realmente desea salir?";
        dialogo = new AlertDialog.Builder(MainActivity.this);
        dialogo.setIcon(R.drawable.ic_close);
        dialogo.setTitle("Warning");
        dialogo.setMessage(mensaje);
        dialogo.setCancelable(false);
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogo, int id) {
                MainActivity.this.finish();
            }
        });
        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialogo.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.action_limpiar){
            et_codigo.setText(null);
            et_descripcion.setText(null);
            et_precio.setText(null);
            sp_cate.getSelectedItem().toString().isEmpty();
            return true;
        }else if(id == R.id.action_listaArticulos){
            Intent i = new Intent(this, ConsultaSpinner.class);
            startActivity(i);
            return true;
        }else if(id == R.id.action_listaArticulos1){
            Intent i = new Intent(this, ListViewArticulos.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void alta(View v){
        if(et_codigo.getText().toString().length()==0){
            et_codigo.setError("Campo obligatorio");
            inputEt = false;
        }else{
            inputEt = true;
        }

        if(et_descripcion.getText().toString().length()==0){
            et_descripcion.setError("Campo obligatorio");
            inputEd = false;
        }else{
            inputEd = true;
        }

        if(et_precio.getText().toString().length()==0){
            et_precio.setError("Campo obligatorio");
            input1 = false;
        }else{
            input1 = true;
        }

        if(sp_cate.getSelectedItem().equals(null)){
            inputIdCat = false;
        }else{
            inputIdCat = true;
        }

        if(inputEt && inputEd && input1 && inputIdCat){
            try{
                datos.setCodigo(Integer.parseInt(et_codigo.getText().toString()));
                datos.setDescripcion(et_descripcion.getText().toString());
                datos.setPrecio(Double.parseDouble(et_precio.getText().toString()));
                cate.setNombrecategoria(sp_cate.getSelectedItem().toString());

                if(conexion.InserTradicional(datos,cate)){
                    Toast.makeText(this, "Registro agregado satisfactoriamente!", Toast.LENGTH_SHORT).show();
                    limpiarDatos();
                }else{
                    Toast.makeText(getApplicationContext(), "Error. Ya existe un registro\n" + " Código:" + et_codigo.getText().toString(), Toast.LENGTH_LONG).show();
                    limpiarDatos();
                }
            }catch (Exception e){
                Toast.makeText(this, "Error. Ya existe.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void mensaje(String mensaje){
        Toast.makeText(this, "" + mensaje, Toast.LENGTH_SHORT).show();
    }

    public void limpiarDatos(){
        et_codigo.setText(null);
        et_descripcion.setText(null);
        et_precio.setText(null);
        sp_cate.getSelectedItem().toString().equals(null);
        et_codigo.requestFocus();
    }

    public void consultaporcodigo(View v){
        if(et_codigo.getText().toString().length()==0){
            et_codigo.setError("Campo obligatorio");
            inputEt = false;
        }else{
            inputEt = true;
        }

        if(inputEt){
            String codigo = et_codigo.getText().toString();
            datos.setCodigo(Integer.parseInt(codigo));
            if(conexion.consultaArticulos(datos, cate)){
                et_descripcion.setText(datos.getDescripcion());
                et_precio.setText(""+datos.getPrecio());
            }else{
                Toast.makeText(this, "No existe un artículo con dicho código", Toast.LENGTH_SHORT).show();
                limpiarDatos();
            }
        }else{
            Toast.makeText(this, "Ingrese el código del artículo a buscar.", Toast.LENGTH_SHORT).show();
        }
    }

    public void consultapordescripcion(View v){
        if(et_descripcion.getText().toString().length()==0){
            et_descripcion.setError("Campo obligatorio");
            inputEd = false;
        }else{
            inputEd = true;
        }

        if(inputEd){
            String descripcion = et_descripcion.getText().toString();
            datos.setDescripcion(descripcion);
            if(conexion.consultarDescripcion(datos, cate)){
                et_codigo.setText(""+datos.getCodigo());
                et_descripcion.setText(datos.getDescripcion());
                et_precio.setText(""+datos.getPrecio());
            }else{
                Toast.makeText(this, "No existe un artículo con dicha descripción", Toast.LENGTH_SHORT).show();
                limpiarDatos();
            }
        }else{
            Toast.makeText(this, "Ingrese la descripción del artículo a buscar.", Toast.LENGTH_SHORT).show();
        }
    }

    public void bajaporcodigo(View v){
        if(et_codigo.getText().toString().length()==0){
            et_codigo.setError("Campo obligatorio");
            inputEt = false;
        }else{
            inputEt = true;
        }

        if(inputEt){
            String cod = et_codigo.getText().toString();
            datos.setCodigo(Integer.parseInt(cod));
            if(conexion.bajaCodigo(MainActivity.this, datos, cate)){
                Toast.makeText(this, "Registro eliminado satisfactoriamente.", Toast.LENGTH_SHORT).show();
                limpiarDatos();
            }else{
                Toast.makeText(this, "No existe un artículo con dicho código.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void modificacion(View v){
        if(et_codigo.getText().toString().length()==0){
            et_codigo.setError("Campo obligatorio");
            inputEt = false;
        }else{
            inputEt = true;
        }

        if(inputEt){
            String cod = et_codigo.getText().toString();
            String descripcion = et_descripcion.getText().toString();
            double precio = Double.parseDouble(et_precio.getText().toString());
            String nombreCategoria = sp_cate.getSelectedItem().toString();

            datos.setCodigo(Integer.parseInt(cod));
            datos.setDescripcion(descripcion);
            datos.setPrecio(precio);
            cate.setNombrecategoria(nombreCategoria);
            if(conexion.modificar(datos, cate)){
                Toast.makeText(this, "Registro modificado correctamente", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "No se han encontrado resultados para la búsqueda especificada", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

