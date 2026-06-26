package com.example.projecz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class StateAdapter2 extends ArrayAdapter<Deal> {
    private LayoutInflater inflater;
    private int layout;
    private List<Deal> deals;
    private View.OnClickListener listener;

    public StateAdapter2(Context context, int resource, List<Deal> deals) {
        super(context, resource, deals);
        this.deals = deals;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;
        if(view==null) {
            view = inflater.inflate(this.layout, parent, false);

            ImageView photoView = view.findViewById(R.id.photobook);
            ImageView fone = view.findViewById(R.id.fone);
            TextView nameView = view.findViewById(R.id.namebookscr);
            TextView priceView = view.findViewById(R.id.price);
            ImageView DelBut=view.findViewById(R.id.deleteButton);

            Deal deal = deals.get(position);
            String s = null;
            s = deal.ImageFst;
            Picasso.get().load(s).into(photoView);
            nameView.setText(deal.Name);
            String endprice = "";
            if (deal.Price == null) {
                endprice = "Обмен";
            } else {
                if (deal.Exchange == false) {
                    endprice = deal.Price.toString() + " руб.";
                } else {
                    endprice = "Обмен / " + deal.Price.toString() + " руб.";
                }
            }
            priceView.setText(endprice);
            DelBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeleteDeal(deal);
                    deals.remove(position);

                }
            });

        }
        else {
            ImageView DelBut=view.findViewById(R.id.deleteButton);
            DelBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeleteDeal(deals.get(position));
                    deals.remove(position);
                }
            });
        }

        return view;
    }
    public void DeleteDeal(Deal deal){
        String ID_deal=deal.ID;
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Deal");
        rootRef.orderByChild("ID").equalTo(ID_deal).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    String key=ds.getKey();
                    rootRef.child(key).setValue(null);
                    AddToEndDeals(deal);
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void AddToEndDeals(Deal deal){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("EndDeal");
        rootRef.push().setValue(deal);
        DatabaseReference rootRef2 = FirebaseDatabase.getInstance().getReference("User");
        rootRef2.orderByChild("Email").equalTo(deal.Email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    User user=ds.getValue(User.class);
                    String key=ds.getKey();
                    int newAmountDeal;
                    if(user.AmountDeals==0) {
                        newAmountDeal = 1;
                        user.AmountDeals=newAmountDeal;
                    }
                    else{
                        newAmountDeal=user.AmountDeals+1;
                    }
                    rootRef2.child(key).child("AmountDeals").setValue(newAmountDeal);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}