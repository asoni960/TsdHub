package model;

/**
 * Created by abhinav on 01-08-2016.
 */
public class Ranking_Team_Data {
    public String rank, country, match, point, rating;

    public String getRank(){return rank;}
    public String getCountry(){return country;}
    public String getMatch(){return match;}
    public String getPoint(){return point;}
    public String getRating(){return rating;}

    public void setRank(String rank){this.rank=rank;}
    public void setCountry(String country){this.country=country;}
    public void setMatch(String match){this.match=match;}
    public void setPoint(String point){this.point=point;}
    public void setRating(String rating){this.rating=rating;}
}
