package com.example.projecz;

import android.content.Context;
import android.text.TextUtils;
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

public class StateAdapter3 extends ArrayAdapter<Message> {
    private LayoutInflater inflater;
    private int layout;
    private List<Message> messages;

    public StateAdapter3(Context context, int resource, List<Message> messages) {
        super(context, resource, messages);
        this.messages = messages;
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

            Message mes=messages.get(position);
            String mail;
            if(mes.Sender==""){
                mail=mes.Getter;
            }
            else{
                mail=mes.Sender;
            }
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("User");
            rootRef.orderByChild("Email").equalTo(mail).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot ds:snapshot.getChildren()){
                        User user=ds.getValue(User.class);
                        Picasso.get().load(user.ProfileImage).into(photoView);
                        nameView.setText(user.Name+" "+user.Surname);
                        if(TextUtils.isEmpty(mes.Message)) {
                            priceView.setText("Пока нет сообщений!");
                        }
                        else{
                            priceView.setText(mes.Message);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        return view;
    }

}