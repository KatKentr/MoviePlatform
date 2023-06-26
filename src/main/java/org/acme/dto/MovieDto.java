package org.acme.dto;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.NotNull;


public class MovieDto {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private String director;


    @NotNull
    private String country;

    private double average;

    public MovieDto(){

    }

    public MovieDto(Long id, String title, String description, String director, String country) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.director= director;
        this.country=country;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setAverage(double average){this.average=average;}

    public double getAverage(){return average;}
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
