package org.acme;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import jakarta.persistence.Entity;


@Entity
public class Developer extends PanacheEntity {     //PanacheEntity; instead of having a repository


    public String name;     //getters and setters will be automatically generated
    public String favoriteFramework = "Quarkus";

    public String getFavoriteFramework(){
        return String.format("%s - Coding that sparks joy",favoriteFramework);
    }

    public void setName(String name){
        this.name=name.toLowerCase();
    }

    public String getName(){
        return name.toUpperCase();                      //this does not change the format of our data
    }
}
