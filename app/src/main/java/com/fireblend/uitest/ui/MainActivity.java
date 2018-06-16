package com.fireblend.uitest.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fireblend.uitest.AddContact;
import com.fireblend.uitest.Preferencias;
import com.fireblend.uitest.R;
import com.fireblend.uitest.bd.Contactosbd;
import com.fireblend.uitest.bd.DatabaseHelper;
import com.fireblend.uitest.bd.UsuariosSQLiteHelper;
import com.fireblend.uitest.entities.Contact;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.Where;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity {


    RecyclerView list;
    ArrayList<Contactosbd> datos;
    Button addcontac;
    AdaptadorContactos adaptador;
    DatabaseHelper helper;

    Button preferencias;
    TextView name;
    TextView age;
    TextView phone;
    TextView email;
    TextView provincia;



    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = (TextView)findViewById(R.id.name);
        age = (TextView)findViewById(R.id.age);
        phone = (TextView)findViewById(R.id.phone);
        email = (TextView)findViewById(R.id.email);
        provincia= (TextView)findViewById(R.id.provincia);



        list = (RecyclerView) findViewById(R.id.lista_contactos);


        list.setHasFixedSize(true);
        //Dar estructura al RecyclerView

        //list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //list.setLayoutManager(new GridLayoutManager(this,2));
        list.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));



        //traemos los datos nuevos del Activity AddContact
        //Bundle nuevo = getIntent().getExtras();

        //Cargamos los datos
        //setupList();
       /* try {
            cargardatos();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }*/

        try {
            mostrar();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }


        adaptador = new AdaptadorContactos(datos);


        //Mostrar mensaje al tocar un dato
        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = list.getChildAdapterPosition(view);
                if(pos != RecyclerView.NO_POSITION){
                    Contactosbd clickedDataItem = datos.get(pos);
                    Toast.makeText(view.getContext(), "Hola " + clickedDataItem.nombre, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, DeleteContact.class);
                    String ID = Integer.toString(clickedDataItem.id);
                    String mensaje = ID;

                    intent.putExtra("ID", mensaje);


                    startActivity(intent);

                }

            }
        });

        list.setAdapter(adaptador);



        //Ir al nuevo Activity
        addcontac = (Button)findViewById(R.id.add_contact);
        addcontac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddContact.class);

                startActivity(intent);
            }
        });

        //Ir a preferencias
        preferencias = (Button)findViewById(R.id.preferencias);
        preferencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Preferencias.class);
                startActivity(intent);
            }
        });



        //Si hay datos nuevos se cargan al Arreglo
        /*if(nuevo != null) {
            datos.add(0, new Contact(nuevo.getString("Nombre"), nuevo.getInt("Edad"), nuevo.getString("Email"), nuevo.getString("Telefono"), nuevo.getString("Provincia")));
            adaptador.notifyItemInserted(1);
        }*/

    }

    @Override
    protected void onResume() {

        cargarpreferencias();

        super.onResume();

    }


    //Metodo para cargar preferencias
    private void cargarpreferencias() {

        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        String lista = sharedPrefs.getString("lista","No hay");

        switch (lista){
            case "Lista":
                list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            break;
            case "Columna":

                list.setLayoutManager(new GridLayoutManager(this,2));
                break;
        }



        /*String letra = sharedPrefs.getString("letra", "No hay");
        switch(letra){
            case "6":
                name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 6);
                age.setTextSize(TypedValue.COMPLEX_UNIT_SP, 6);
                email.setTextSize(TypedValue.COMPLEX_UNIT_SP, 6f);
                phone.setTextSize(TypedValue.COMPLEX_UNIT_SP, 6f);
                provincia.setTextSize(TypedValue.COMPLEX_UNIT_SP, 6f);
                break;
            case "8":
                name.setTextSize(8);
                age.setTextSize(8);
                email.setTextSize(8);
                phone.setTextSize(8);
                provincia.setTextSize(8);
                break;
            case "10":
                name.setTextSize(10);
                age.setTextSize(10);
                email.setTextSize(10);
                phone.setTextSize(10);
                provincia.setTextSize(10);
                break;
            case "12":
                name.setTextSize(12);
                age.setTextSize(12);
                email.setTextSize(12);
                phone.setTextSize(12);
                provincia.setTextSize(12);
                break;
            case "16":
                name.setTextSize(16);
                age.setTextSize(16);
                email.setTextSize(16);
                phone.setTextSize(16);
                provincia.setTextSize(16);
                break;
            case "No hay":
                break;
                default:
                    name.setTextSize(12);
                    age.setTextSize(12);
                    email.setTextSize(12);
                    phone.setTextSize(12);
                    provincia.setTextSize(12);
                    break;
        }*/

    }


    private void cargardatos() throws java.sql.SQLException {
        DatabaseHelper helper = new DatabaseHelper(this);
        Dao<Contactosbd, Integer> usuarioDao = null;
        try {
            usuarioDao = helper.getUsuarioDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Contactosbd contacto = new Contactosbd();
        contacto.nombre = "Sergio2";
        contacto.edad = 12;
        contacto.correo = "miiiii@gmail.com";
        contacto.telefono = "123456";
        contacto.provincia = "San";
        try {
            usuarioDao.create(contacto);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public void mostrar() throws java.sql.SQLException {

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
            List<Contactosbd> contacto = usuarioDao.queryForAll();
            datos = new ArrayList<Contactosbd>(contacto);

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }





    /*private void setupList() {
        datos = new ArrayList<Contact>();
            datos.add(new Contact("Sergio", 28, "sergiome@gmail.com", "88854764", "San José"));
            datos.add(new Contact("Andres", 1, "alex@gmail.com", "88883644", "Puntarenas"));
            datos.add(new Contact("Andrea", 2, "andrea@gmail.com", "98714764", "Puntarenas"));
            datos.add(new Contact("Fabian", 3, "fabian@gmail.com", "12345678", "Puntarenas"));
            datos.add(new Contact("Ivan", 4, "ivan@gmail.com", "87654321", "Puntarenas"));
            datos.add(new Contact("Gabriela", 5, "gabriela@gmail.com", "09871234", "Puntarenas"));
            datos.add(new Contact("Alex", 6, "sergiome@gmail.com", "43215678", "Puntarenas"));
    }*/



    /*private void setupList() {
        final List<Contact> contacts = new ArrayList();
        Bundle datos = getIntent().getExtras();
        if (datos == null) {
            //Lista ejemplo con datos estaticos. Normalmente, estos serían recuperados de una BD o un REST API.
            contacts.add(new Contact("Sergio", 28, "sergiome@gmail.com", "88854764", "San José"));
            contacts.add(new Contact("Andres", 1, "alex@gmail.com", "88883644", "Puntarenas"));
            contacts.add(new Contact("Andrea", 2, "andrea@gmail.com", "98714764", "Puntarenas"));
            contacts.add(new Contact("Fabian", 3, "fabian@gmail.com", "12345678", "Puntarenas"));
            contacts.add(new Contact("Ivan", 4, "ivan@gmail.com", "87654321", "Puntarenas"));
            contacts.add(new Contact("Gabriela", 5, "gabriela@gmail.com", "09871234", "Puntarenas"));
            contacts.add(new Contact("Alex", 6, "sergiome@gmail.com", "43215678", "Puntarenas"));
        }else {
            //Lista ejemplo con datos estaticos. Normalmente, estos serían recuperados de una BD o un REST API.
            contacts.add(new Contact("Sergio", 28, "sergiome@gmail.com", "88854764", "San José"));
            contacts.add(new Contact("Andres", 1, "alex@gmail.com", "88883644", "Puntarenas"));
            contacts.add(new Contact("Andrea", 2, "andrea@gmail.com", "98714764", "Puntarenas"));
            contacts.add(new Contact("Fabian", 3, "fabian@gmail.com", "12345678", "Puntarenas"));
            contacts.add(new Contact("Ivan", 4, "ivan@gmail.com", "87654321", "Puntarenas"));
            contacts.add(new Contact("Gabriela", 5, "gabriela@gmail.com", "09871234", "Puntarenas"));
            contacts.add(new Contact("Alex", 6, "sergiome@gmail.com", "43215678", "Puntarenas"));
            contacts.add(new Contact("Michael", 6, "sergiome@gmail.com", "43215678", "Puntarenas"));
            contacts.add(new Contact(datos.getString("Nombre"), datos.getInt("Edad"), datos.getString("Email"), datos.getString("Telefono"), datos.getString("Provincia")));
        }
        //Le asignamos a la lista un nuevo BaseAdapter, implementado a continuación
        list.setAdapter(new BaseAdapter() {
            @Override
            //Retorna el numero de elementos en la lista.
            public int getCount() {
                return contacts.size();
            }
            @Override
            //Retorna el elemento que pertenece a la posición especificada.
            public Object getItem(int position) {
                return contacts.get(position);
            }
            @Override
            //Devuelve un identificador único para cada elemento de la lista.
            //En nuestro caso, basta con devolver la posición del elemento en la lista.
            public long getItemId(int position) {
                return position;
            }
            @Override
            //Devuelve la vista que corresponde a cada elemento de la lista
            public View getView(int position, View convertView, ViewGroup parent) {
                //LayoutInflater inflater = getLayoutInflater();
                //View row = inflater.inflate(R.layout.contact_item, parent, false);
                View row = convertView;
                if (row == null){
                    LayoutInflater inflater = getLayoutInflater();
                    row = inflater.inflate(R.layout.contact_item, parent, false);
                }
                TextView name, age, phone, email, provincia;
                name = (TextView) row.findViewById(R.id.name);
                age = (TextView) row.findViewById(R.id.age);
                phone = (TextView) row.findViewById(R.id.phone);
                email = (TextView) row.findViewById(R.id.email);
                provincia = (TextView) row.findViewById(R.id.provincia);
                Button btn = (Button) row.findViewById(R.id.row_btn);
                //Basandonos en la posicion provista en este metodo, proveemos los datos
                //correctos para este elemento.
                final int pos = position;
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "Hola, "+contacts.get(pos).name, Toast.LENGTH_SHORT).show();
                    }
                });
                name.setText(contacts.get(position).name);
                age.setText(contacts.get(position).age+"");
                phone.setText(contacts.get(position).phone);
                email.setText(contacts.get(position).email);
                provincia.setText(contacts.get(position).provincia);
                return row;
            }
        });
    }*/
}
