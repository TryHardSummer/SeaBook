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

import java.util.ArrayList;
import java.util.List;

public class StateAdapter extends ArrayAdapter<Deal> {
    private LayoutInflater inflater;
    private int layout;
    private List<Deal> deals;

    public StateAdapter(Context context, int resource, List<Deal> deals) {
        super(context, resource, deals);
        this.deals = deals;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;
        notifyDataSetChanged();
        if(view==null) {
            view = inflater.inflate(this.layout, parent, false);

            ImageView photoView = view.findViewById(R.id.photobook);
            ImageView fone = view.findViewById(R.id.fone);
            TextView nameView = view.findViewById(R.id.namebookscr);
            TextView priceView = view.findViewById(R.id.price);

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

        }

        return view;
    }

}