package model;

/**
 * Created by abhinav on 18-07-2016.
 */
public class Featured_Card_Data {
   public int Featured_image;
    public String Featured_title;
    public String Featured_timestamp;

 public Featured_Card_Data()
 {}

public Featured_Card_Data(int Featured_image, String Featured_title, String Featured_timestamp){
super();
 this.Featured_image= Featured_image;
 this.Featured_timestamp= Featured_timestamp;
 this.Featured_title=Featured_title;
}
 public int getImage(){return Featured_image;}
 public String getTitle(){return Featured_title;}
 public String getTimestamp(){return Featured_timestamp;}

 public void setImage(int image){this.Featured_image=Featured_image;}
 public void setCountry(String title){this.Featured_title=Featured_title;}
 public void setTimestamp(String timestamp){this.Featured_timestamp=Featured_timestamp;}
}

