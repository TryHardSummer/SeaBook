package com.example.projecz;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateMes extends AppCompatActivity {
    private String USER_MAIL, State;
    private StateAdapter3 stateAdapter;
    private StateAdapterChat stateAdapterChat;
    private ListView ListMes;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String res=getIntent().getExtras().getString("EmailGetter");
        if(TextUtils.isEmpty(res)==true){
            State="Chats";
            setContentView(R.layout.messages);
            ListMes=findViewById(R.id.MessageList);
            USER_MAIL=getIntent().getStringExtra("EmailSender");
            DatabaseReference mDataBaseMes = FirebaseDatabase.getInstance().getReference("Messages");
            mDataBaseMes.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<Message> res=new ArrayList<>();
                    ArrayList<String> del=new ArrayList<>();
                    for(DataSnapshot ds:snapshot.getChildren()){
                        Message mes=ds.getValue(Message.class);
                        if(mes.Getter.toString().equals(USER_MAIL)){
                            Boolean check=false;
                            int Index=-1;
                            for(int i=0;i<del.size();i++){
                                if(del.get(i).equals(mes.Sender)){
                                    check=true;
                                    Index=i;
                                }
                            }
                            if(check==true){
                                for(int i=0;i<res.size();i++){
                                    if(res.get(i).Sender==del.get(Index)){
                                        res.get(i).Message=mes.Message;
                                    }
                                }
                            }
                            else {
                                mes.Getter = "";
                                String newUser = mes.Sender;
                                del.add(newUser);
                                res.add(mes);
                            }
                        }
                        else if(mes.Sender.toString().equals(USER_MAIL)){
                            Boolean check=false;
                            int Index=-1;
                            for(int i=0;i<del.size();i++){
                                if(del.get(i).equals(mes.Getter)){
                                    check=true;
                                    Index=i;
                                }
                            }
                            if(check==true){
                                for(int i=0;i<res.size();i++){
                                    if(res.get(i).Getter==del.get(Index)){
                                        res.get(i).Message=mes.Message;
                                    }
                                }
                            }
                            else {
                                mes.Sender = "";
                                String newUser = mes.Getter;
                                del.add(newUser);
                                res.add(mes);
                            }
                        }
                    }
                    setadapter(res);

                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
        else{
            String S=getIntent().getStringExtra("EmailSender");
            String G=getIntent().getStringExtra("EmailGetter");
            USER_MAIL=getIntent().getStringExtra("EmailSender");
            DatabaseReference mDataBaseMes = FirebaseDatabase.getInstance().getReference("Messages");
            Date d1=new Date();
            Message message=new Message(S,G,null, d1.toString());
            mDataBaseMes.push().setValue(message);
            message.Sender="";
            State="Chat";
            ViewMessages(message);
        }
    }
    public void setadapter(List<Message> e){
        stateAdapter=new StateAdapter3(this,R.layout.list_item, e);
        ListMes.setAdapter(stateAdapter);
        stateAdapter.notifyDataSetChanged();
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Message selectedMes=(Message) parent.getItemAtPosition(position);
                ViewMessages(selectedMes);
            }
        };
        ListMes.setOnItemClickListener(itemListener);
    }
    private ListView chat;
    private ImageView SendButton;
    private TextView NameSendUser;
    private void ViewMessages(Message m){
        setContentView(R.layout.chatusers);
        State="Chat";
        chat=findViewById(R.id.ChatsMessages);
        NameSendUser=findViewById(R.id.NameOfMessage);
        String mail;
        if(m.Getter==""){
            mail=m.Sender;
        }
        else{
            mail=m.Getter;
        }
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("User");
        rootRef.orderByChild("Email").equalTo(mail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    User user=ds.getValue(User.class);
                    String nameandsurname=user.Name+" "+user.Surname;
                    NameSendUser.setText(nameandsurname);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        NewMes = findViewById(R.id.messageofuser);
        SendButton=findViewById(R.id.SendMessageBut);
        SendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newMessage=NewMes.getText().toString();
                Date d1=new Date();
                Message message=new Message(USER_MAIL, mail, newMessage, d1.toString());
                DatabaseReference mDataBaseMes = FirebaseDatabase.getInstance().getReference("Messages");
                mDataBaseMes.push().setValue(message);
                stateAdapterChat=null;
                ViewMessages(m);
            }
        });


        DatabaseReference mDataBaseMes = FirebaseDatabase.getInstance().getReference("Messages");
        mDataBaseMes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Message> messages=new ArrayList<>();
                for(DataSnapshot ds:snapshot.getChildren()){
                    Message ms=ds.getValue(Message.class);
                    if(ms.Getter!=""&&ms.Getter.equals(USER_MAIL) && ms.Sender.equals(mail)&&!TextUtils.isEmpty(ms.Message)){
                        ms.Getter="";
                        messages.add(ms);
                    }
                    else if(ms.Sender!=""&&ms.Sender.equals(USER_MAIL) && ms.Getter.equals(mail)&&!TextUtils.isEmpty(ms.Message)){
                        ms.Sender="";
                        messages.add(ms);
                    }
                }
                setAdapterToChat(messages);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setAdapterToChat(List<Message> ms){
        stateAdapterChat=new StateAdapterChat(this, R.layout.messageofyou, ms);
        chat.setAdapter(stateAdapterChat);
        stateAdapterChat.notifyDataSetChanged();
    }
    public void gotoProfile(View view){
        Intent intent=new Intent();
        intent.setClass(CreateMes.this, MainActivity.class);
        intent.putExtra("EmailUser", USER_MAIL);
        startActivity(intent);
    }
    public void gotoSearch(View view){
        Intent intent=new Intent();
        intent.setClass(CreateMes.this, MainActivity.class);
        Bundle extras = new Bundle();
        extras.putString("EmailUser",USER_MAIL);
        extras.putString("State","Search");
        intent.putExtras(extras);
        startActivity(intent);
    }
    private TextView NewMes;
    @Override
    public void onBackPressed() {
        if(State=="Chat"){
            State="Chats";
            setContentView(R.layout.messages);
            ListMes=findViewById(R.id.MessageList);
            DatabaseReference mDataBaseMes = FirebaseDatabase.getInstance().getReference("Messages");
            mDataBaseMes.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<Message> res=new ArrayList<>();
                    ArrayList<String> del=new ArrayList<>();
                    for(DataSnapshot ds:snapshot.getChildren()){
                        Message mes=ds.getValue(Message.class);
                        if(mes.Getter.toString().equals(USER_MAIL)){
                            Boolean check=false;
                            int Index=-1;
                            for(int i=0;i<del.size();i++){
                                if(del.get(i).equals(mes.Sender)){
                                    check=true;
                                    Index=i;
                                }
                            }
                            if(check==true){
                                for(int i=0;i<res.size();i++){
                                    if(res.get(i).Sender==del.get(Index)){
                                        res.get(i).Message=mes.Message;
                                    }
                                }
                            }
                            else {
                                mes.Getter = "";
                                String newUser = mes.Sender;
                                del.add(newUser);
                                res.add(mes);
                            }
                        }
                        else if(mes.Sender.toString().equals(USER_MAIL)){
                            Boolean check=false;
                            int Index=-1;
                            for(int i=0;i<del.size();i++){
                                if(del.get(i).equals(mes.Getter)){
                                    check=true;
                                    Index=i;
                                }
                            }
                            if(check==true){
                                for(int i=0;i<res.size();i++){
                                    if(res.get(i).Getter==del.get(Index)){
                                        res.get(i).Message=mes.Message;
                                    }
                                }
                            }
                            else {
                                mes.Sender = "";
                                String newUser = mes.Getter;
                                del.add(newUser);
                                res.add(mes);
                            }
                        }
                    }
                    setadapter(res);

                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else if(State=="Chats"){
            Intent intent=new Intent();
            intent.setClass(CreateMes.this, MainActivity.class);
            intent.putExtra("EmailUser", USER_MAIL);
            startActivity(intent);

        }
    }

}
