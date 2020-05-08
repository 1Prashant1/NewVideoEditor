package com.app.videoeditor;


public class CreatProfileModel {

    public String name;
    public String phoneno;
    public String email;
    public String friends;
    public String location;
    public String profilepic;
    public String created;



    public  CreatProfileModel(){

    }

    public CreatProfileModel(String name,  String phoneno, String email, String friends, String location, String profilepic, String created){
        this.name = name;
        this.phoneno = phoneno;
        this.email = email;
        this.friends = friends;
        this.location = location;
        this.profilepic = profilepic;
        this.created = created;
    }
    public String getUserName() {
        return name;
    }
    public String getUserPhoneno() {
        return phoneno;
    }
    public String getUserEmail() {
        return email;
    }
    public String getFriends() {
        return friends;
    }
    public String getLocation() {
        return location;
    }
    public String getProfilepic() {
        return profilepic;
    }
    public String getCreated() {
        return created;
    }
}



