package com.example.fiwareteste;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Devices implements Serializable {
    String id;
    String type;
    String commands;
    String Name;

    @SerializedName("Devices")
    String body;


    public Devices() {
    }

    public Devices(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCommands() {
        return commands;
    }

    public void setCommands(String commands) {
        this.commands = commands;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getName(String id) {
        if (this.id.equals(id)) {
            return Name;
        } else {
            return "";
        }
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", body='" + body + '\'' +
                '}' + '\n'+'\n' ;
    }
}
