package com.pokemonworld.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

/**
 * Created by J. Paulo on 05/05/2017.
 */

public class Pokemon implements Serializable {
    @Expose
    String name;
    @Expose
    String weight;
    @Expose
    String height ;
    @Expose
    String base_experience ;
    @Expose
    int id ;
    @Expose
    List<Tipo> types;
    @Expose
    List<Form> forms;
    @Expose
    int level;
    @Expose
    int life;
    @Expose
    int agility;
    @Expose
    int force;
    @Expose
    String photo;

    public Pokemon(String name, String weight, String height, String base_experience, int id, List<Tipo> types, List<Form> forms, int level, int life, int agility, int force, String photo) {
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.base_experience = base_experience;
        this.id = id;
        this.types = types;
        this.forms = forms;
        this.level = level;
        this.life = life;
        this.agility = agility;
        this.force = force;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getBase_experience() {
        return base_experience;
    }

    public void setBase_experience(String base_experience) {
        this.base_experience = base_experience;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Tipo> getTypes() {
        return types;
    }

    public void setTypes(List<Tipo> types) {
        this.types = types;
    }

    public List<Form> getForms() {
        return forms;
    }

    public void setForms(List<Form> forms) {
        this.forms = forms;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getAgility() {
        return agility;
    }
    public void setAgility(int agility) {
        this.agility = agility;
    }

    public int getForce() {
        return force;
    }

    public void setForce(int force) {
        this.force = force;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
