package com.example.projecz;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.accessibilityservice.AccessibilityService;
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

public class StateAdapterChat extends ArrayAdapter<Message> {
    private LayoutInflater inflater;
    private int layout;
    private List<Message> messages;

    public StateAdapterChat(Context context, int resource, List<Message> messages) {
        super(context, resource, messages);
        this.messages = messages;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;
        notifyDataSetChanged();
            if(getItemViewType(position)==1)
                view = inflater.inflate(R.layout.messageofyou, parent, false);
            else{
                view = inflater.inflate(R.layout.messageofanother, parent, false);
            }
            TextView messag=view.findViewById(R.id.message_tv);
            TextView name=view.findViewById(R.id.message_date_tv);
            Message mes=messages.get(position);
            String mail;
            if(mes.Getter==""){
                mail=mes.Sender;
            }
            else{
                mail=mes.Getter;
            }
            messag.setText(mes.Message);
            name.setText(mes.Time);

        return view;
    }
    @Override
    public int getItemViewType(int position) {
        Message mes=messages.get(position);
        if(mes.Sender==""){
            return 1;
        }
        else if(mes.Getter==""){
            return 2;
        }
        return 3;
    }
}