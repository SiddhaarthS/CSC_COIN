package com.example.androidhive;

/**
 * Created by sesha on 13/3/15.
 */


/**
 * Created by sesha on 17/1/15.
 */


public class Actors {

    private String name;
    private String description;
    private String college;
    private String image;



    public Actors() {
        // TODO Auto-generated constructor stub
    }

    public Actors(String name, String description, String image,String college) {
        super();
        this.name = name;
        this.description = description;
        this.college=college;
        this.image = image;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }


    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public void setCollege(String college) {
        this.college = college;
    }
    public String getCollege() {
        return college;
    }



}
