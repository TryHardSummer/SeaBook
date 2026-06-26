package com.example.projecz;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class SetProfileImage extends AppCompatActivity
{
    private ImageView Image;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String URI= getIntent().getStringExtra("Uri");
        setContentView(R.layout.activity_main);
        Image=findViewById(R.id.ProfileAvatar);
        setImage(URI);
    }
    private void setImage(String s){
        Picasso.get().load(s).into(Image);
        killprocess();
    }
    private void killprocess(){
        finish();
    }
}
