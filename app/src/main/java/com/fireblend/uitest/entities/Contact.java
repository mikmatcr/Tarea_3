package com.fireblend.uitest.entities;

/**
 * Created by Sergio on 8/20/2017.
 */

public class Contact {
    //Clase entidad para contener cada elemento de la lista, el cual representa un Contacto.
    
    public String name;
    public int age;
    public String email;
    public String phone;
    public String provincia;

    public Contact(String name, int age, String email, String phone, String provincia){
        this.name = name;
        this.age = age;
        this.email = email;
        this.phone = phone;
        this.provincia = provincia;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() { return phone; }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvincia() { return provincia; }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }



}
