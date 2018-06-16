package com.fireblend.uitest.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fireblend.uitest.R;
import com.fireblend.uitest.bd.Contactosbd;
import com.fireblend.uitest.entities.Contact;

import java.util.ArrayList;
import java.util.List;


/*
El ViewHolder es importante en RecyclerView porque nos ayuda tomar todos los elementos que más adelante serán personalizados,
ósea un contenerdor vacío que nos servirá para no tener que estar creando nuevas vistars cada vez que cambia un elemento, sino que
reutilizará una vista. Con esto se consigue mejorar el procesamiento de las listas, con más fluidez y mejor experiencia para el usuario.


 */
public class AdaptadorContactos extends RecyclerView.Adapter<AdaptadorContactos.ContactosViewHolder> implements View.OnClickListener{
    private ArrayList<Contactosbd> datos;
    private View.OnClickListener listener;




    public AdaptadorContactos(ArrayList<Contactosbd> datos) {

        this.datos = datos;
    }

    @Override
    public ContactosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);
        itemView.setOnClickListener(this);

        ContactosViewHolder tvh = new ContactosViewHolder(itemView);

        return tvh;

    }
    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }


    public class ContactosViewHolder extends RecyclerView.ViewHolder {
        //TextView name, age, phone, email, provincia;
        TextView nombre, edad, telefono, correo, provincia;

        public ContactosViewHolder(View itemView) {
            super(itemView);


            nombre = (TextView) itemView.findViewById(R.id.name);
            edad = (TextView) itemView.findViewById(R.id.age);
            telefono = (TextView) itemView.findViewById(R.id.phone);
            correo = (TextView) itemView.findViewById(R.id.email);
            provincia = (TextView) itemView.findViewById(R.id.provincia);
        }
        public void bindContact(Contactosbd c) {
            nombre.setText(c.nombre);
            edad.setText(String.valueOf(c.edad));
            telefono.setText(c.telefono);
            correo.setText(c.correo);
            provincia.setText(c.provincia);

        }
    }

    @Override
    public void onBindViewHolder(ContactosViewHolder holder, int position) {
        Contactosbd item = datos.get(position);

        holder.bindContact(item);



    }

    @Override
    public int getItemCount() {

        return datos.size();
    }


}
