package org.acme.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="movies")
public class Movie  {      //In case of extending the PanacheEntity: We are not creating getters and setters and we are not providing an @Id: Panache feature
                                                 //By extending PanacheEntity, we’re using the Active Record persistence pattern instead of a DAO. This means that all persistence methods are blended with our own Entity.
  @Id
  @GeneratedValue                  //entity identifier
   private Long id;

   @NotNull
   private String title;                          //Instance fields would be set to public if we extended the panacheEntity

   @NotNull
   private String description;

   @NotNull
   private String director;

   @NotNull
   private String country;


   @Column(name="rates_number")
   private int ratesCount;

   @Column(name="rates_sum")
   private int sum;

   @Transient              //not mapped to db
   private double average;


    @OneToMany(
            mappedBy = "movie"
    )
    @JsonIgnore
    private List<UserMovie> users=new ArrayList<>();



    public Movie(){

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



   //getters and setters for the list of UserMovie instances

    public List<UserMovie> getUsers() {
        return users;
    }

    public void setUsers(List<UserMovie> users) {
        this.users = users;
    }

    public int getRatesCount() {
        return ratesCount;
    }

    public void setRatesCount(int ratesCount) {
        this.ratesCount = ratesCount;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public double getAverage(){   //calculate average rating of movie

       if (ratesCount!=0) {

             average=sum/ratesCount;
       } else {

           average=0.0;

       }

       return average;
    }

    public synchronized void incrementRateCount(){

        setRatesCount(getRatesCount()+1);
    }

    public synchronized void decrementRateCount(){

        if (getRatesCount()>0){
            setRatesCount(getRatesCount()-1);
        }

    }

    public synchronized void addToRatesSum(int rate){

        setSum(getSum()+rate);

    }

    public synchronized void removeRateFromSum(int rate){

        if (getSum()>=rate){
            setSum(getSum()-rate);
        }

    }



}
