package com.fireblend.uitest.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.fireblend.uitest.R;
import com.fireblend.uitest.bd.Contactosbd;
import com.fireblend.uitest.bd.DatabaseHelper;
import com.j256.ormlite.dao.Dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class DeleteContact extends AppCompatActivity {

    TextView TxtNombre;
    TextView TxtEdad;
    TextView TxtTelefono;
    TextView TxtEmail;
    TextView provincias;
    Button boton_eliminar;
    Button boton_exportar;
    RelativeLayout fondo_layout;



    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_contact);

        TxtNombre = (TextView)findViewById(R.id.TxtNombre);
        TxtEdad = (TextView)findViewById(R.id.TxtEdad);
        TxtEmail = (TextView)findViewById(R.id.TxtEmail);
        TxtTelefono= (TextView)findViewById(R.id.TxtTelefono);
        provincias = (TextView)findViewById(R.id.provincias);



        try {
            mostrar();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        boton_eliminar = (Button)findViewById(R.id.boton_eliminar);
        fondo_layout =(RelativeLayout) findViewById(R.id.layout_contact);
        cargarpreferencias();
        boton_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminar();

            }
        });

        boton_exportar = (Button)findViewById(R.id.boton_exportar);
        boton_exportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Revisar si se tiene Permiso:
                int permissionCheck = ContextCompat.checkSelfPermission(DeleteContact.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if(permissionCheck == PackageManager.PERMISSION_GRANTED){
                    try {
                        continuar();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    askForPermission();
                }

            }
        });
    }

    private void cargarpreferencias() {

        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);


        boolean quitar_borrar = sharedPrefs.getBoolean("quitar_borrar", false);
        String prueba = "";
        if (quitar_borrar == false){
            prueba = "Falso";

        }else{
            prueba = "Si";
            boton_eliminar.setVisibility(View.INVISIBLE);
        }
        String fondo = sharedPrefs.getString("fondo", "No hay");
        switch(fondo){
            case "Red":
                fondo_layout.setBackgroundColor(Color.RED);
            break;
            case "Blue":
                fondo_layout.setBackgroundColor(Color.BLUE);
                break;
            case "White": fondo_layout.setBackgroundColor(Color.WHITE);
            break;

        }

    }

    //Creando y Accediendo a Archivos:
    private void continuar() throws IOException {
        Toast.makeText(this, "Tenemos Permiso!", Toast.LENGTH_SHORT).show();
        //Creo la carpeta
        File dir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "Prueba");

        if (!dir.mkdirs()) {
            Log.e(TAG, "Directory not created");

        }
        String archivo = "PruebaMike.txt"; //nombre del archivo
        //Creación del archivo
        File file = new File(dir,archivo);
        file.createNewFile();
        llenararchivo(archivo, file);
    }

    private  void llenararchivo(String Filename, File file){
        //traemos los datos nuevos del Activity AddContact
        String mensaje = getIntent().getStringExtra("ID");
        int numMensaje = Integer.parseInt(mensaje);
        DatabaseHelper helper = new DatabaseHelper(this);
        Dao<Contactosbd, Integer> usuarioDao = null;
        try {
            usuarioDao = helper.getUsuarioDao();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        //Generamos un filtro y obtenemos la lista resultado
        //List<Contactosbd> datos = null;
        try {

            Contactosbd contactos = usuarioDao.queryForId(numMensaje);

            TxtNombre.setText(contactos.nombre);
            String name = TxtNombre.getText().toString();
            TxtEdad.setText(String.valueOf(contactos.edad));
            String edad = TxtEdad.getText().toString();
            TxtEmail.setText(contactos.correo);
            String correo = TxtEmail.getText().toString();
            TxtTelefono.setText(contactos.telefono);
            String telefono = TxtTelefono.getText().toString();
            provincias.setText(contactos.provincia);
            String provincia = provincias.getText().toString();

            //String Filename = "PruebaMike.txt";
            String campos = name + "," + edad + "," + correo + "," + telefono + "," + provincia;

            OutputStreamWriter bf = new OutputStreamWriter(new FileOutputStream(file));
            bf.write(campos);
            bf.close();




        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Mostramos los datos del contacto
    public void mostrar() throws java.sql.SQLException {
        //traemos los datos nuevos del Activity AddContact
        String mensaje = getIntent().getStringExtra("ID");
        int numMensaje = Integer.parseInt(mensaje);
        DatabaseHelper helper = new DatabaseHelper(this);
        Dao<Contactosbd, Integer> usuarioDao = null;
        try {
            usuarioDao = helper.getUsuarioDao();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        //Generamos un filtro y obtenemos la lista resultado
        //List<Contactosbd> datos = null;
        try {

            Contactosbd contactos = usuarioDao.queryForId(numMensaje);

            TxtNombre.setText(contactos.nombre);
            TxtEdad.setText(String.valueOf(contactos.edad));
            TxtEmail.setText(contactos.correo);
            TxtTelefono.setText(contactos.telefono);
            provincias.setText(contactos.provincia);



        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    private void eliminar(){
        final DatabaseHelper helper = new DatabaseHelper(this);
        //traemos los datos nuevos del Activity AddContact
        String mensaje = getIntent().getStringExtra("ID");
        int numMensaje = Integer.parseInt(mensaje);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Está seguro de borrar el contacto?")
                .setTitle("Confirmacion")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //traemos los datos nuevos del Activity AddContact
                        String mensaje = getIntent().getStringExtra("ID");
                        int numMensaje = Integer.parseInt(mensaje);

                        Dao<Contactosbd, Integer> usuarioDao = null;
                        try {
                            usuarioDao = helper.getUsuarioDao();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (java.sql.SQLException e) {
                            e.printStackTrace();
                        }


                        Contactosbd contacto = new Contactosbd();
                        contacto.id = numMensaje;
                        try {
                            usuarioDao.delete(contacto);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (java.sql.SQLException e) {
                            e.printStackTrace();
                        }

                        dialog.cancel();

                        Toast.makeText(DeleteContact.this, "Se borró el contacto",
                                Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(DeleteContact.this,MainActivity.class);
                        startActivity(intent);

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("Dialogos", "Confirmacion Cancelada.");
                        dialog.cancel();
                    }
                });
        Dialog dialog = builder.create();
        dialog.show();

    }

    private static final int PERM_CODE = 100;
    public void askForPermission(){
        ActivityCompat.requestPermissions(DeleteContact.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERM_CODE);
    }

    //Manejando la Respuesta del Usuario:

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED){
            try {
                continuar();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
        }
    }

    //Verificación de Disponibilidad para Escritura:
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    //Verificación de Disponibilidad para Lectura:
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }


    }



