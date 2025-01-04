package com.example.petcaremanager.dto;

import java.io.Serializable;

public class PetProfile  implements Serializable {

    private int id;
    private String name;
    private int age;
    private String breed;

    public PetProfile(int id, String name, int age, String breed) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.breed = breed;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getBreed() {
        return breed;
    }
}
