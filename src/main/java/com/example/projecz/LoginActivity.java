package com.example.projecz;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.text.Normalizer;

public class LoginActivity extends AppCompatActivity {
    private EditText _login, _password;
    private TextView Nowemail,startb,t1,t2,t3,t4;
    private ImageView StartFone, v1,v2,v3,v4,v5,v6,v7,v8;
    private String EMAIL,PASSWORD;
    private FirebaseAuth mAuth;
    public static String adr;
    private ImageView g1,g2;
    private TextView h1,h2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mAuth=FirebaseAuth.getInstance();
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser nowUser=mAuth.getCurrentUser();
        if(nowUser==null){
            Nowemail.setVisibility(View.GONE);
            startb.setVisibility(View.GONE);
            StartFone.setVisibility(View.GONE);
            _login.setVisibility(View.VISIBLE);
            _password.setVisibility(View.VISIBLE);
            v1.setVisibility(View.VISIBLE);
            v2.setVisibility(View.VISIBLE);
            v3.setVisibility(View.VISIBLE);
            v4.setVisibility(View.VISIBLE);
            v5.setVisibility(View.VISIBLE);
            t1.setVisibility(View.VISIBLE);
            t2.setVisibility(View.VISIBLE);
            t3.setVisibility(View.VISIBLE);
            t4.setVisibility(View.GONE);
            v6.setVisibility(View.GONE);
            v7.setVisibility(View.GONE);
            v8.setVisibility(View.VISIBLE);
            Toast.makeText(this,"Зарегистрируйтесь в приложении!",Toast.LENGTH_SHORT).show();
        }
        else{
            Nowemail.setVisibility(View.VISIBLE);
            startb.setVisibility(View.VISIBLE);
            StartFone.setVisibility(View.VISIBLE);
            _login.setVisibility(View.GONE);
            _password.setVisibility(View.GONE);
            v1.setVisibility(View.GONE);
            v2.setVisibility(View.GONE);
            v3.setVisibility(View.GONE);
            v4.setVisibility(View.GONE);
            v5.setVisibility(View.GONE);
            t1.setVisibility(View.GONE);
            t2.setVisibility(View.GONE);
            t3.setVisibility(View.GONE);
            t4.setVisibility(View.VISIBLE);
            v6.setVisibility(View.VISIBLE);
            v7.setVisibility(View.VISIBLE);
            v8.setVisibility(View.GONE);
            String _nowemail="Вы вошли как:"+nowUser.getEmail();
            Nowemail.setText(_nowemail);
        }
    }

    private void init(){
        String l, p;
        _login = findViewById(R.id.login);
        _password = findViewById(R.id.password);
        l = _login.getText().toString();
        p = _password.getText().toString();
        Nowemail=findViewById(R.id.YourEmail);
        startb=findViewById(R.id.StartText);
        StartFone=findViewById(R.id.StartButton);
        v1=findViewById(R.id.imageView);
        v2=findViewById(R.id.imageView12);
        v3=findViewById(R.id.imageView13);
        v4=findViewById(R.id.imageView20);
        v5=findViewById(R.id.imageView22);
        t1=findViewById(R.id.textView3);
        t2=findViewById(R.id.textView);
        t3=findViewById(R.id.textView5);
        t4=findViewById(R.id.bLogOut);
        v6=findViewById(R.id.startButton2);
        v7=findViewById(R.id.SomeImageDes);
        v8=findViewById(R.id.knigi2);
    }
    public void GoToRegister(View v){
        setContentView(R.layout.registration);
        h1=findViewById(R.id.CreateVer);
        h2=findViewById(R.id.EmailVer);
        g1=findViewById(R.id.fone123);
        g2=findViewById(R.id.fone321);
        h1.setVisibility(View.VISIBLE);
        h2.setVisibility(View.INVISIBLE);
        g1.setVisibility(View.VISIBLE);
        g2.setVisibility(View.INVISIBLE);
    }
    public void GoToLogin(View v){
        setContentView(R.layout.login);
    }
    public void LogIn(View v){
        String email=_login.getText().toString();
        String password=_password.getText().toString();
        if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)){
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Вы успешно вошли в аккаунт!",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(v.getContext(), MainActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("EmailUser",mAuth.getCurrentUser().getEmail());
                        extras.putString("State","none");
                        intent.putExtras(extras);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Неправильный логин или пароль!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(),"Неправильное заполнение полей!",Toast.LENGTH_SHORT).show();
        }
    }
    public void Register(View v){
        EditText v1, v2, v3, v4, v5, v6, v7;
        v3 = findViewById(R.id.RegEma);
        v6 = findViewById(R.id.RegPas);
        v7 = findViewById(R.id.EnterPas);
        if (v6.getText().toString().equals(v7.getText().toString())) {
            String email=v3.getText().toString();
            String password=v6.getText().toString();
            if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)){
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            EMAIL=email;
                            PASSWORD=password;
                            SendEmailVer();
                            h1.setVisibility(View.INVISIBLE);
                            h2.setVisibility(View.VISIBLE);
                            g1.setVisibility(View.INVISIBLE);
                            g2.setVisibility(View.VISIBLE);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Ошибка регистрации!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else{
                Toast.makeText(this,"Пустые поля!",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this,"Пароли не совпадают!",Toast.LENGTH_SHORT).show();
        }
    }
    private void SendEmailVer(){
        FirebaseUser user= mAuth.getCurrentUser();
        assert user!=null;
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Проверьте вашу почту для подверждения Email",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Ошибка подверждения Email",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void CompleteRegister(View v){
        mAuth.getCurrentUser().reload();
        FirebaseUser user=mAuth.getCurrentUser();
        assert user!=null;
        if(user.isEmailVerified()==true) {
            Toast.makeText(getApplicationContext(), "Пожалуйста, заполниите оставшиеся поля!", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.anotherreg);
        }
        else{
            Toast.makeText(getApplicationContext(), "Пожалуйста, подтвердите Email", Toast.LENGTH_SHORT).show();
        }

    }
    public void onclickCompleteRegister(View v){
        TextView q1,q2,q3,q4;
        q1=findViewById(R.id.RegName);
        q2=findViewById(R.id.RegSur);
        q3=findViewById(R.id.RegAdr);
        q4=findViewById(R.id.RegPho);
        String Name=q1.getText().toString();
        String Surname=q2.getText().toString();
        String Adress=q3.getText().toString();
        String Phone=q4.getText().toString();
        if(Phone.length()==10) {
            Boolean temp=false;
            for(int i=0;i<Phone.length();i++) {
                if(Phone.charAt(i)>='0'&&Phone.charAt(i)<='9'){
                    continue;
                }
                else{
                    temp=true;
                }
            }
            if(temp==false) {
                DatabaseReference mDataBaseUser;
                mDataBaseUser = FirebaseDatabase.getInstance().getReference("User");
                if (!Adress.equals("")) {
                    if (Adress.charAt(Adress.length() - 1) == ' ')
                        Adress = Adress.substring(0, Adress.length() - 1);
                }
                String DefAva = "https://firebasestorage.googleapis.com/v0/b/seabook-a8adf.appspot.com/o/ImageDeals%2F1713798400849deal_image?alt=media&token=b815cfb6-6d29-4507-bbf2-5a33f13ad7d6";
                User newUser = new User(EMAIL, PASSWORD, Name, Surname, Adress, Phone, 0, DefAva, 0, 0);
                mDataBaseUser.push().setValue(newUser);
                Toast.makeText(getApplicationContext(), "Вы зарегистрировались!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(v.getContext(), MainActivity.class);
                Bundle extras = new Bundle();
                extras.putString("EmailUser", mAuth.getCurrentUser().getEmail());
                extras.putString("State", "none");
                intent.putExtras(extras);
                startActivity(intent);
            }
            else{
                Toast.makeText(getApplicationContext(), "Введите корректный номер телефона!", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Введите корректный номер телефона!", Toast.LENGTH_SHORT).show();
        }
    }
    public void LogOut(View v){
        FirebaseAuth.getInstance().signOut();
        Nowemail.setVisibility(View.GONE);
        startb.setVisibility(View.GONE);
        StartFone.setVisibility(View.GONE);
        _login.setVisibility(View.VISIBLE);
        _password.setVisibility(View.VISIBLE);
        v1.setVisibility(View.VISIBLE);
        v2.setVisibility(View.VISIBLE);
        v3.setVisibility(View.VISIBLE);
        v4.setVisibility(View.VISIBLE);
        v5.setVisibility(View.VISIBLE);
        t1.setVisibility(View.VISIBLE);
        t2.setVisibility(View.VISIBLE);
        t3.setVisibility(View.VISIBLE);
        t4.setVisibility(View.GONE);
        v6.setVisibility(View.GONE);
        v7.setVisibility(View.GONE);
        v8.setVisibility(View.VISIBLE);
    }
    public void LogInSavedUser(View v){
        mAuth.getCurrentUser().reload();
        FirebaseUser user=mAuth.getCurrentUser();
        assert user!=null;
        if(user.isEmailVerified()==false) {
            Intent intent = new Intent();
            intent.setClass(v.getContext(), MainActivity.class);
            Bundle extras = new Bundle();
            extras.putString("EmailUser", mAuth.getCurrentUser().getEmail());
            extras.putString("State", "none");
            intent.putExtras(extras);
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(), "Пожалуйста, подтвердите Email", Toast.LENGTH_SHORT).show();
        }
    }
}
