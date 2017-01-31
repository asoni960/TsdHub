package model;

/**
 * Created by abhinav on 18-08-2016.
 */
public class Ranking_ODI_data {
    public String name;
    public String rank,rating;
    public int country;

    public String getRank(){return rank;}
    public String getName(){return name;}
    public int getCountry(){return country;}
    public String getRating(){return rating;}

    public void setRank(String rank){this.rank=rank;}
    public void setName(String name){this.name=name;}
    public void setCountry(int country){this.country=country;}
    public void setRating(String rating){this.rating=rating;}
}
