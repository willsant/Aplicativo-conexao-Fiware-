package com.example.fiwareteste;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Usuario implements Serializable {

    String name;
    @SerializedName("usuarios")
    String email;
    String id;
    String senha;

    public Usuario() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", id='" + id + '\'' +
                ", senha='" + senha + '\'' +
                '}';
    }
}
