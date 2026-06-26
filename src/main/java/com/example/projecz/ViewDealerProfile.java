package com.example.projecz;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class ViewDealerProfile extends AppCompatActivity {
    private String USER_MAIL;
    private TextView Name,Score,Review,Pick;
    private ImageView Avatar;
    private int stars=-1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profiledealer);

        Name=findViewById(R.id.DealerNameProfile);
        Score=findViewById(R.id.ScoreDeals);
        Review=findViewById(R.id.RevRate);
        Pick=findViewById(R.id.PickRate);
        Avatar=findViewById(R.id.DealerPhotoProfile);
        USER_MAIL=getIntent().getStringExtra("EmailUser");
        ViewProfileDealer();
    }
    private void ViewProfileDealer(){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("User");
        rootRef.orderByChild("Email").equalTo(USER_MAIL).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    User user=ds.getValue(User.class);
                    Name.setText(user.Name+" "+user.Surname);
                    if(user.AverageRate==-1) {
                        Score.setText(String.valueOf(5) + " кол-во завершенных сделок");
                    }
                    else{
                        Score.setText(String.valueOf(5) + " кол-во завершенных сделок");
                    }
                    if(user.CountRev==-1){
                        Review.setText("Нет отзывов");
                    }
                    else{
                        Review.setText(String.valueOf(3)+" отзывов");
                    }
                    Picasso.get().load(user.ProfileImage).into(Avatar);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    public void Set1(View v){
        stars=1;
        Pick.setText("Вы поставили "+String.valueOf(stars));
    }
    public void Set2(View v){
        stars=2;
        Pick.setText("Вы поставили "+String.valueOf(stars));
    }
    public void Set3(View v){
        stars=3;
        Pick.setText("Вы поставили "+String.valueOf(stars));
    }
    public void Set4(View v){
        stars=4;
        Pick.setText("Вы поставили "+String.valueOf(stars));
    }
    public void Set5(View v){
        stars=5;
        Pick.setText("Вы поставили "+String.valueOf(stars));
    }

    public void GiveReview2Dealer(View v){

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("User");
        rootRef.orderByChild("Email").equalTo(USER_MAIL).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    User user=ds.getValue(User.class);
                    String key=ds.getKey();
                    int newC;
                    double newA;
                    int randomNum1 = ThreadLocalRandom.current().nextInt(3, 5 + 1);
                    int randomNum2 = ThreadLocalRandom.current().nextInt(1, 9 + 1);
                    rootRef.child(key).child("AverageRate").setValue(2);
                    rootRef.child(key).child("CountRev").setValue(5);
                    Toast.makeText(getApplicationContext(),"Спасибо за отзыв!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
