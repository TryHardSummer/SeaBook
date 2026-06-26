package com.example.projecz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MySettings extends AppCompatActivity {

    private TextView Name,ConfirmText;
    private EditText hint;
    private Uri uploadUri;
    private int state=-1;
    private ImageView Avatar,ConfirmFone;
    private String USER_MAIL;
    private String State;
    private StorageReference mStorage;
    private Boolean StateImage=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        USER_MAIL=getIntent().getStringExtra("EmailUser");
        State="MainSet";
        mStorage= FirebaseStorage.getInstance().getReference("ImageAvatars");
        Avatar=findViewById(R.id.Avatar);
        ConfirmText=findViewById(R.id.ConfirmButton);
        ConfirmText.setVisibility(View.GONE);
        ConfirmFone=findViewById(R.id.Confirm);
        ConfirmFone.setVisibility(View.GONE);
        Avatar=findViewById(R.id.Avatar);
        setName();
    }
    private void setName(){
        Name=findViewById(R.id.NowName);
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("User");
        rootRef.orderByChild("Email").equalTo(USER_MAIL).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    User user=ds.getValue(User.class);
                    String nick=user.Name.toString()+" "+user.Surname.toString();
                    Name.setText(nick);
                    if(user.ProfileImage!=null) {
                        Picasso.get().load(user.ProfileImage).into(Avatar);
                    }
                    else{
                        Avatar.setImageResource(R.drawable.defaultava);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updatePhone(View v){
        State="EditFeild";
        setContentView(R.layout.editsomefield);
        hint=findViewById(R.id.newInfo);
        hint.setHint("Введите новый номер телефона");
        state=0;
    }
    public void updateMail(View v){
        State="EditFeild";
        setContentView(R.layout.editsomefield);
        hint=findViewById(R.id.newInfo);
        hint.setHint("Введите новую почту");
        state=1;
    }
    public void updateGeo(View v){
        State="EditFeild";
        setContentView(R.layout.editsomefield);
        hint=findViewById(R.id.newInfo);
        hint.setHint("Введите новый город");
        state=2;
    }
    public void saveChangeField(View v){
        if(!TextUtils.isEmpty(hint.getText().toString())) {
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("User");
            rootRef.orderByChild("Email").equalTo(USER_MAIL).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String key=ds.getKey();
                        String newField=hint.getText().toString();
                        if (state == 0) {
                            rootRef.child(key).child("Phone").setValue(newField);
                        } else if (state == 1) {
                           rootRef.child(key).child("Email").setValue(newField);
                        } else {
                            rootRef.child(key).child("Adress").setValue(newField);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            state=-1;
            setContentView(R.layout.settings);
            setName();
        }
        else{
            Toast.makeText(getApplicationContext(), "Заполните поле!", Toast.LENGTH_SHORT).show();
        }
    }
    public void Save(View v){
        Avatar=findViewById(R.id.Avatar);
        Intent intent = new Intent();
        intent.setClass(MySettings.this, MainActivity.class);
        intent.putExtra("EmailUser", USER_MAIL);
        startActivity(intent);
    }
    public void setNewAvatar(View v){
        getImage();

    }
    public void ConfirmAvatar(View v){
        getUriUploadImage();
        ConfirmText.setVisibility(View.GONE);
        ConfirmFone.setVisibility(View.GONE);
    }
    private void getUriUploadImage(){
        String imageName=System.currentTimeMillis()+"Avatar_image";
        final StorageReference mRef=mStorage.child(imageName);
        mRef.putFile(uploadUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UploadImage(uri);
                    }
                });
            }
        });
    }
    private void UploadImage(Uri uri){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("User");
        rootRef.orderByChild("Email").equalTo(USER_MAIL).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    String key=ds.getKey();
                    rootRef.child(key).child("ProfileImage").setValue(uri.toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getImage () {

        Intent intentChooser = new Intent();
        intentChooser.setType("image/*");
        intentChooser.setAction(Intent.ACTION_GET_CONTENT);
        ImageActivityResultLauncher.launch(intentChooser);
    }
    ActivityResultLauncher<Intent> ImageActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    uploadUri = result.getData().getData();
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Avatar.setImageURI(result.getData().getData());
                        uploadUri = result.getData().getData();
                        ConfirmText.setVisibility(View.VISIBLE);
                        ConfirmFone.setVisibility(View.VISIBLE);
                    }

                }
            });
    @Override
    public void onBackPressed() {
        if(State=="EditField"){
            setContentView(R.layout.settings);
            setName();
        }
        else if(State=="MainSet"){
            Intent intent = new Intent();
            intent.setClass(MySettings.this, MainActivity.class);
            Bundle extras = new Bundle();
            extras.putString("EmailUser",USER_MAIL);
            extras.putString("State","none");
            intent.putExtras(extras);
            startActivity(intent);
        }
    }
}
