package com.example.projecz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.List;

public class MyDealsActivity extends AppCompatActivity {

    private ListView myDeals;
    private List<Deal> Deals;
    private StateAdapter2 stateAdapter;
    private String EMAIL_USER;
    private String State;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mydeals);
        init();
        Deals=new ArrayList<>();
        GenerateList();
        State="MyDeals";
        EMAIL_USER=getIntent().getStringExtra("EmailUser");
    }
    public void GenerateList(){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Deal");
        String email=getIntent().getStringExtra("EmailUser");
        rootRef.orderByChild("Email").equalTo(email).addValueEventListener(new ValueEventListener() {
            ArrayList<Deal> AddressUser=new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    Deal deal=ds.getValue(Deal.class);
                    AddressUser.add(deal);
                }
                setAdapterMyDeals(AddressUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private  void setAdapterMyDeals(List<Deal> e){
        if (Deals.size()>0) Deals.clear();
        for(int i=0;i<e.size();i++){
            Deals.add(e.get(i));
        }
            stateAdapter = new StateAdapter2(this, R.layout.list_item2, Deals);
            myDeals.setAdapter(stateAdapter);


            AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Deal selectedDeal = (Deal) parent.getItemAtPosition(position);
                    ViewSelectedCard(selectedDeal);
                }
            };
            myDeals.setOnItemClickListener(itemListener);
    }



    private ImageView PhotoOfBook;
    private TextView NameBookCard,NameUpBookCard, ExchangeBookCard,GangreeBookCard, ScriptBookCard;
    private int CountPhoto=0;
    public void ViewSelectedCard(Deal selDeal){
        State="ViewMyDeal";
        setContentView(R.layout.cardbook);
        NameBookCard=findViewById(R.id.NameBookCard);
        NameUpBookCard=findViewById(R.id.NameUpBookCard);
        ExchangeBookCard=findViewById(R.id.ExchangeBookCard);
        GangreeBookCard=findViewById(R.id.GangreBookCard);
        ScriptBookCard=findViewById(R.id.ScriptBookCard);
        PhotoOfBook=findViewById(R.id.PhotoBookCard);

        NameBookCard.setText(selDeal.Name);
        NameUpBookCard.setText(selDeal.Name);
        if(selDeal.Exchange==true){
            ExchangeBookCard.setText("Обмен: есть");
        }
        else{
            ExchangeBookCard.setText("Обмен: нет");
        }
        ScriptBookCard.setText("Описание: "+selDeal.Script);
        GangreeBookCard.setText("Жанр: "+selDeal.Gangree);

        if(selDeal.ImageFst!=null){
            CountPhoto++;
        }
        if(selDeal.ImageScd!=null){
            CountPhoto++;
        }
        if(selDeal.ImageThd!=null){
            CountPhoto++;
        }
        Picasso.get().load(selDeal.ImageFst).into(PhotoOfBook);
        PhotoOfBook.setVisibility(View.VISIBLE);
    }
    private void init(){
        myDeals=findViewById(R.id.listofmydeals);
    }
    @Override
    public void onBackPressed() {
        if(State=="ViewMyDeal") {
            setContentView(R.layout.mydeals);
            init();
            Deals=new ArrayList<>();
            GenerateList();
            State="MyDeals";
        }
        else {
            Intent intent = new Intent();
            intent.setClass(MyDealsActivity.this, MainActivity.class);
            Bundle extras = new Bundle();
            extras.putString("EmailUser",EMAIL_USER);
            extras.putString("State","none");
            intent.putExtras(extras);
            startActivity(intent);
        }
    }
}
