package com.example.projecz;

public class User implements Cloneable{
    public String Email,Password,Name,Surname,Adress,Phone, ProfileImage;
    int AmountDeals,CountRev;
    double AverageRate;

    public User() {
    }

    public User(String email, String password, String name, String surname, String adress, String phone, int amountDeals, String profileImage,double averageRate, int countRev) {
        Email = email;
        Password = password;
        Name = name;
        Surname = surname;
        Adress = adress;
        Phone = phone;
        AmountDeals = amountDeals;
        ProfileImage=profileImage;
        AverageRate=averageRate;
        CountRev=countRev;
    }

}
