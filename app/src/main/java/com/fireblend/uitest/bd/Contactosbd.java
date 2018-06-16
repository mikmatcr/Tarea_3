package com.fireblend.uitest.bd;

import android.widget.EditText;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "contactos")
public class Contactosbd {


    @DatabaseField(generatedId = true, columnName = "ID",canBeNull = false)
    public int id;

    @DatabaseField(columnName = "nombre",canBeNull = false)
    public String nombre;

    @DatabaseField(columnName = "edad",canBeNull = false)
    public int edad;

    @DatabaseField(columnName = "correo",canBeNull = false)
    public String correo;

    @DatabaseField(columnName = "telefono",canBeNull = false)
    public String telefono;

    @DatabaseField(columnName = "provincia",canBeNull = false)
    public String provincia;

    public Contactosbd() {}

}