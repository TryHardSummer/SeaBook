package com.example.projecz;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class MainActivity extends AppCompatActivity {
    private Boolean StateExchange=null;
    private Boolean StateImageFst=false;
    private Boolean StateImageScd=false;
    private Boolean StateImageThd=false;

    private DatabaseReference mDataBaseUser;
    private DatabaseReference mDataBaseDeal;
    private FirebaseDatabase  mDataBase;
    private StorageReference mStorage;
    private String USER_KEY="User";
    private String DEAL_KEY="Deal";
    private String EMAIL_USER;
    private Uri uploadUriFirst=null;
    private Uri newUriFirst=null;
    private Uri uploadUriSecond=null;
    private Uri uploadUriThird=null;
    private StateAdapter stateAdapter;
    private String State;

    private ImageView Te,Fe,fst,scd,thd,ProfileAvatar;
    private TextView Name,Author, Gangrene,Script,Price;
    public String Address;


    private ImageView PhotoOfBook, PhotoOfBook2, PhotoOfBook3, NextPhotoB, BackPhotoB;
    private TextView NameBookCard,NameUpBookCard, ExchangeBookCard,GangreeBookCard, ScriptBookCard, Url1,Url2,Url3;

    ArrayList<Deal> deals=new ArrayList<Deal>();
    ListView DealsList;
    CollectionReference dealsdb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDataBase= FirebaseDatabase.getInstance();
        mDataBaseUser=mDataBase.getReference(USER_KEY);
        mDataBaseDeal=mDataBase.getReference(DEAL_KEY);
        mStorage= FirebaseStorage.getInstance().getReference("ImageDeals");
        EMAIL_USER=getIntent().getStringExtra("EmailUser");
        State="Main";
        ProfileAvatar=findViewById(R.id.ProfileAvatar);
        if(getIntent().getExtras().getString("State").equals("Search")){
            ComeBackToSearch();

        }
        else {
            ViewProfileMain();
        }
    }

    private Bitmap BitAvatar=null;
    public void ViewProfileMain() {
        TextView Name_,DealAm;
        Name_=findViewById(R.id.nameandsur);
        DealAm=findViewById(R.id.dealsamount);
        String emailik=getIntent().getStringExtra("EmailUser");
        Name_.setText(emailik);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference(USER_KEY);
        rootRef.orderByChild("Email").equalTo(emailik).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()) {
                    User user=ds.getValue(User.class);
                    String name=user.Name+" "+user.Surname.toString();

                    String d= "Количество сделок: "+user.AmountDeals;

                    if(user.ProfileImage!=null){
                        Picasso.get().load(user.ProfileImage).into(ProfileAvatar);
                    }
                    else{
                        ProfileAvatar.setImageResource(R.drawable.defaultava);
                        ProfileAvatar.setMaxWidth(70);
                        ProfileAvatar.setMaxHeight(70);
                    }
                    DealAm.setText(d);
                    Name_.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("error");
            }
        });


    }

    public void ViewProfile(View v){
        setContentView(R.layout.activity_main);
        ViewProfileMain();

    }
    private ListView ListEndDeals;
    private void setadapterenddeal(List<Deal> deals){
        ListEndDeals=findViewById(R.id.ListEndDeal);
        StateAdapter stateAdapter1=new StateAdapter(this,R.layout.list_item, deals);
        ListEndDeals.setAdapter(stateAdapter1);
    }
    public void ViewMyDeals(View v){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, MyDealsActivity.class);
        intent.putExtra("EmailUser",EMAIL_USER);
        startActivity(intent);
    }
    public void ViewMyEndDeals(View v){
        setContentView(R.layout.enddeals);
        State="MyEndDeals";
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("EndDeal");
        rootRef.orderByChild("Email").equalTo(EMAIL_USER).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Deal> endDeals=new ArrayList<>();
                for(DataSnapshot ds:snapshot.getChildren()){
                    Deal deal=ds.getValue(Deal.class);
                    endDeals.add(deal);
                }
                setadapterenddeal(endDeals);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void MySetting(View v){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, MySettings.class);
        intent.putExtra("EmailUser", EMAIL_USER);
        startActivity(intent);
        finish();
    }


    public void SetTrueEx(View v){
        StateExchange=true;
        Te.setImageResource(R.drawable.yes);
        Fe.setImageResource(R.drawable.no);
    }
    public void SetFalseEx(View v){
        StateExchange=false;
        Te.setImageResource(R.drawable.no);
        Fe.setImageResource(R.drawable.yes);
    }

    public void CreateDeal(View v){
        setContentView(R.layout.newdeal);
        Te= findViewById(R.id.yesexbutton);
        Fe=findViewById(R.id.noexbutton);
        fst=findViewById(R.id.firstpic);
        scd=findViewById(R.id.secpic);
        thd=findViewById(R.id.thirdpic);
        Name=findViewById(R.id.namebook);
        Author=findViewById(R.id.authorbook);
        Gangrene=findViewById(R.id.chanrebook);
        Script=findViewById(R.id.scriptbook);
        Price=findViewById(R.id.priceofbook);
        State="CreateDeal";

    }

    public void CompleteCreateDeal(View v) {
        if (!TextUtils.isEmpty(Name.getText().toString()) && !TextUtils.isEmpty(Author.getText().toString()) && !TextUtils.isEmpty(Gangrene.getText().toString()) && !TextUtils.isEmpty(Script.getText().toString())) {
            if(StateImageFst==true) {
                if (StateExchange == null) {
                    Toast.makeText(getApplicationContext(), "Заполните поле обмена!", Toast.LENGTH_SHORT).show();
                } else {
                    uploadImageFst();
                    setContentView(R.layout.mainpage);
                    getAddress(getIntent().getStringExtra("EmailUser"));
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "Прикрепите хотя бы одну картинку!", Toast.LENGTH_SHORT).show();
            }

        }
        else{
            Toast.makeText(getApplicationContext(),"Заполните обязательные поля!", Toast.LENGTH_SHORT).show();
        }
    }

        public void DownLoadFirstImage (View v){
            getImageFirst();
        }

        public void DownLoadSecondImage (View v){
            getImageSecond();
        }
        public void DownLoadThirdImage (View v){
            getImageThird();
        }


        private void getImageFirst () {
            Intent intentChooser = new Intent();
            intentChooser.setType("image/*");
            intentChooser.setAction(Intent.ACTION_GET_CONTENT);
            FirstActivityResultLauncher.launch(intentChooser);
        }
        public void getImageSecond () {
            Intent intentChooser = new Intent();
            intentChooser.setType("image/*");
            intentChooser.setAction(Intent.ACTION_GET_CONTENT);
            SecondActivityResultLauncher.launch(intentChooser);
        }
        public void getImageThird () {
            Intent intentChooser = new Intent();
            intentChooser.setType("image/*");
            intentChooser.setAction(Intent.ACTION_GET_CONTENT);
            ThirdActivityResultLauncher.launch(intentChooser);
        }

        ActivityResultLauncher<Intent> FirstActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        uploadUriFirst = result.getData().getData();
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            fst.setImageURI(result.getData().getData());
                            StateImageFst = true;
                            uploadUriFirst = result.getData().getData();
                        }

                    }
                });
        ActivityResultLauncher<Intent> SecondActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            scd.setImageURI(result.getData().getData());
                            StateImageScd = true;
                        }

                    }
                });
        ActivityResultLauncher<Intent> ThirdActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            thd.setImageURI(result.getData().getData());
                            StateImageThd = true;
                        }

                    }
                });

    private void uploadImageFst(){
        String imageName=System.currentTimeMillis()+"deal_image";
        final StorageReference mRef=mStorage.child(imageName);
        mRef.putFile(uploadUriFirst).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    List<Uri> uripic=new ArrayList<>();
                    List<Boolean> check=new ArrayList<>();
                    @Override
                    public void onSuccess(Uri uri) {
                        uripic.add(uri);
                        check.add(true);
                        uploadImageScd(uripic,check);
                    }
                });
            }
        });
    }
    private void uploadImageScd(List<Uri> e, List<Boolean> b){
        if(StateImageScd==false) {
            b.add(false);
            uploadImageThd(e,b);
        }
        else {
            String imageName = System.currentTimeMillis() + "deal_image";
            final StorageReference mRef = mStorage.child(imageName);
            mRef.putFile(uploadUriFirst).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            e.add(uri);
                            b.add(true);
                            uploadImageThd(e, b);
                        }
                    });
                }
            });
        }
    }
    private void uploadImageThd(List<Uri> e, List<Boolean> b){
        if (StateImageThd == false) {
            b.add(false);
            uploadCompleteDeal(e,b);
        }
        else {
            String imageName = System.currentTimeMillis() + "deal_image";
            final StorageReference mRef = mStorage.child(imageName);
            mRef.putFile(uploadUriFirst).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            e.add(uri);
                            b.add(true);
                            uploadCompleteDeal(e, b);
                        }
                    });
                }
            });
        }
    }
    private void uploadCompleteDeal(List<Uri> uri, List<Boolean> b){

                StateImageFst=false;
                StateImageScd=false;
                StateImageThd=false;
                if(!TextUtils.isEmpty(Price.getText().toString())){
                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("User");
                    String emailik=getIntent().getStringExtra("EmailUser").toString();
                    rootRef.orderByChild("Email").equalTo(emailik).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot ds:snapshot.getChildren()){
                                User user=ds.getValue(User.class);
                                Deal newDeal;
                                String s1=null,s2=null;
                                if(b.get(1)==true){
                                    s1=uri.get(1).toString();
                                }
                                if(b.get(2)==true){
                                    s2=uri.get(2).toString();
                                }
                                if(b.get(2)==true&&b.get(1)==false){
                                    s1=s2;
                                    s2=null;
                                }
                                String idDeal=System.currentTimeMillis()+"deal_id";
                                newDeal = new Deal(getIntent().getStringExtra("EmailUser"), Name.getText().toString(),
                                        Author.getText().toString(),
                                        Gangrene.getText().toString(), Script.getText().toString(), Price.getText().toString(), uri.get(0).toString() ,s1,s2,
                                        StateExchange,user.Adress,false,idDeal);
                                DatabaseReference mDataBaseDeal =FirebaseDatabase.getInstance().getReference("Deal");
                                mDataBaseDeal.push().setValue(newDeal);
                                Toast.makeText(getApplicationContext(),"Объявление добавлено!", Toast.LENGTH_SHORT).show();

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                else{
                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference(USER_KEY);
                    String emailik=getIntent().getStringExtra("EmailUser").toString();
                    rootRef.orderByChild("Email").equalTo(emailik).addValueEventListener(new ValueEventListener(){
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot ds:snapshot.getChildren()){
                                User user=ds.getValue(User.class);
                                Deal newDeal;
                                String s1=null,s2=null;
                                if(b.get(1)==true){
                                    s1=uri.get(1).toString();
                                }
                                if(b.get(2)==true){
                                    s2=uri.get(2).toString();
                                }
                                if(b.get(2)==true&&b.get(1)==false){
                                    s1=s2;
                                    s2=null;
                                }
                                String idDeal=System.currentTimeMillis()+"deal_id";
                                newDeal = new Deal(getIntent().getStringExtra("EmailUser"), Name.getText().toString(),
                                        Author.getText().toString(),
                                        Gangrene.getText().toString(), Script.getText().toString(), null, uri.get(0).toString() ,s1,s2,
                                        StateExchange,user.Adress,false,idDeal);
                                DatabaseReference mDataBaseDeal =FirebaseDatabase.getInstance().getReference("Deal");
                                mDataBaseDeal.push().setValue(newDeal);
                                Toast.makeText(getApplicationContext(),"Объявление добавлено!", Toast.LENGTH_SHORT).show();

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

    }


    private void ComeBackToSearch(){
        setContentView(R.layout.mainpage);
        DealsList=findViewById(R.id.listofbook);
        getAddress(getIntent().getStringExtra("EmailUser"));
    }
    public void ComeBackToSearchView(View v){
        setContentView(R.layout.mainpage);
        DealsList=findViewById(R.id.listofbook);
        getAddress(getIntent().getStringExtra("EmailUser"));
    }

    public void ViewMessages(View v){
        Intent intent=new Intent();
        intent.setClass(MainActivity.this, CreateMes.class);
        Bundle extras = new Bundle();
        extras.putString("EmailSender",EMAIL_USER);
        extras.putString("EmailGetter","");
        intent.putExtras(extras);
        startActivity(intent);
    }

    public void Search(View v)  {
        setContentView(R.layout.mainpage);
        DealsList=findViewById(R.id.listofbook);
        getAddress(getIntent().getStringExtra("EmailUser"));
    }
    Boolean ExchangeFilter=false;
    private ImageView IconExc;
    private EditText genref,otprice,doprice, namef,cityf;
    String genre="",priceot="",pricedo="",namefilter="",cityfilter="";
    public void filterDeals(View v){
        setContentView(R.layout.filter);
        ExchangeFilter=false;
        IconExc=findViewById(R.id.Exchangebutton);
        IconExc.setImageResource(R.drawable.no);
        genref=findViewById(R.id.enterGengre);
        otprice=findViewById(R.id.otPrice);
        doprice=findViewById(R.id.doPrice);
        namef=findViewById(R.id.enterNewBook);
        cityf=findViewById(R.id.enterCityFilter);
        genref.setText(genre);
        otprice.setText(priceot);
        doprice.setText(pricedo);
        namef.setText(namefilter);
        cityf.setText(cityfilter);
        if(ExchangeFilter==true) {
            ExchangeFilter = false;
            IconExc.setImageResource(R.drawable.no);
        }
        else{
            ExchangeFilter = true;
            IconExc.setImageResource(R.drawable.yes);
        }
    }


    public void ClearAllFilters(View v){
        genref.setText("");
        otprice.setText("");
        doprice.setText("");
        namef.setText("");
        cityf.setText("");
        ExchangeFilter = false;
        IconExc.setImageResource(R.drawable.no);
    }
    public void SetExchangeFilterClick(View v){
        if(ExchangeFilter==true) {
            ExchangeFilter = false;
            IconExc.setImageResource(R.drawable.no);
        }
        else{
            ExchangeFilter = true;
            IconExc.setImageResource(R.drawable.yes);
        }
    }
    public void AcceptFilter(View v){

        genre=genref.getText().toString();
        if(!genre.equals("")){
            if(genre.charAt(genre.length()-1)==' ')
                genre=genre.substring(0,genre.length()-1);
        }
        priceot=otprice.getText().toString();
        if(!priceot.equals("")){
            if(priceot.charAt(priceot.length()-1)==' ')
                priceot=priceot.substring(0,priceot.length()-1);
        }
        pricedo=doprice.getText().toString();
        if(!pricedo.equals("")){
            if(pricedo.charAt(pricedo.length()-1)==' ')
                pricedo=pricedo.substring(0,pricedo.length()-1);
        }
        namefilter=namef.getText().toString();
        if(!namefilter.equals("")){
            if(namefilter.charAt(namefilter.length()-1)==' ')
                namefilter=namefilter.substring(0,namefilter.length()-1);
        }
        cityfilter=cityf.getText().toString();
        if(!cityfilter.equals("")){
            if(cityfilter.charAt(cityfilter.length()-1)==' ')
                cityfilter=cityfilter.substring(0,cityfilter.length()-1);
        }
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Deal");
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Deal> filtersDeals=new ArrayList<>();
                for(DataSnapshot ds:snapshot.getChildren()){
                    Deal deal=ds.getValue(Deal.class);
                    if(
                            (genre.equals("")||deal.Gangree.equals(genre))
                            &&(priceot.equals("")||Integer.parseInt(deal.Price)>=Integer.parseInt(priceot))
                            &&(pricedo.equals("")||Integer.parseInt(deal.Price)<=Integer.parseInt(pricedo))
                            &&(namefilter.equals("")||deal.Name.equals(namefilter))
                            &&(cityfilter.equals("")||deal.Address.equals(cityfilter))
                            &&(ExchangeFilter==false||deal.Exchange==ExchangeFilter)
                    )
                    {
                        filtersDeals.add(deal);
                    }
                }
                setContentView(R.layout.mainpage);
                setadapter(filtersDeals);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getAddress(String mail){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("User");
        rootRef.orderByChild("Email").equalTo(mail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    User user=ds.getValue(User.class);
                    generateList(user.Adress);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void generateList(String City){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Deal");
        rootRef.orderByChild("Address").equalTo(City).addValueEventListener(new ValueEventListener() {
            ArrayList<Deal> AddressUser=new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    Deal deal=ds.getValue(Deal.class);
                    AddressUser.add(deal);
                }
                setadapter(AddressUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void setadapter(List<Deal> e){
        DealsList=findViewById(R.id.listofbook);
        if(deals.size()>0) {
            deals.clear();
        }
        for(int i=0;i<e.size();i++){
            deals.add(e.get(i));
        }
        stateAdapter=null;
        stateAdapter=new StateAdapter(this,R.layout.list_item, deals);
        DealsList.setAdapter(stateAdapter);
        stateAdapter.notifyDataSetChanged();
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Deal selectedDeal = (Deal)parent.getItemAtPosition(position);
                ViewSelectedCard(selectedDeal);
                stateAdapter.notifyDataSetChanged();
            }
        };
        DealsList.setOnItemClickListener(itemListener);
    }
    private int CountPhoto=0;
    private int PositionPhoto=1;
    private ImageView PhotoOfDealer;
    private TextView NameDealer,CountRev,FoneText;
    public void ViewSelectedCard(Deal selDeal){
        State="ViewDeal";
        setContentView(R.layout.cardbook);
        NameBookCard=findViewById(R.id.NameBookCard);
        NameUpBookCard=findViewById(R.id.NameUpBookCard);
        ExchangeBookCard=findViewById(R.id.ExchangeBookCard);
        GangreeBookCard=findViewById(R.id.GangreBookCard);
        ScriptBookCard=findViewById(R.id.ScriptBookCard);
        PhotoOfBook=findViewById(R.id.PhotoBookCard);
        PhotoOfDealer=findViewById(R.id.AvatarProd);
        NameDealer=findViewById(R.id.NameProd);
        CountRev=findViewById(R.id.ReviewsUser);
        FoneText=findViewById(R.id.ScriptFoneCard);


        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("User");
        rootRef.orderByChild("Email").equalTo(selDeal.Email).addValueEventListener(new ValueEventListener() {
            ArrayList<Deal> AddressUser=new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    User user=ds.getValue(User.class);
                    loadReview(user);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        NameBookCard.setText(selDeal.Name);
        //NameUpBookCard.setText(selDeal.Name);
        if(selDeal.Exchange==true){
            ExchangeBookCard.setText("Обмен: есть");
        }
        else{
            ExchangeBookCard.setText("Обмен: нет");
        }
        ScriptBookCard.setText("Описание: "+selDeal.Script);
        GangreeBookCard.setText("Жанр: "+selDeal.Gangree);
        ScriptBookCard.post(new Runnable() {
            @Override
            public void run() {
                //height is ready
                int height = ScriptBookCard.getHeight()+20;
                FoneText.getLayoutParams().height=height;
                //FoneText.getLayoutParams().width=500;
            }
        });
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

    public void generateList2(String Name){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Deal");
        rootRef.orderByChild("Name").equalTo(Name).addValueEventListener(new ValueEventListener() {
            ArrayList<Deal> NameDeals=new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    Deal deal=ds.getValue(Deal.class);
                    NameDeals.add(deal);
                }
                setadapter(NameDeals);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private ImageView writebut;
    private void loadReview(User user){
        NameDealer.setText(user.Name+" "+user.Surname);
        double stars=0;
        if(user.AverageRate==0){
            stars=0;
        }
        else{
            stars=user.AverageRate/user.CountRev;
        }
        int randomNum = ThreadLocalRandom.current().nextInt(3, 5 + 1);
        CountRev.setText(String.valueOf(randomNum)+" рейтинг");
        writebut=findViewById(R.id.writeButton);
        NameDealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this, ViewDealerProfile.class);
                intent.putExtra("EmailUser", user.Email);
                startActivity(intent);
            }
        });
        writebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this, CreateMes.class);
                intent.putExtra("EmailSender",EMAIL_USER);
                intent.putExtra("EmailGetter", user.Email);
                startActivity(intent);
            }
        });

    }
    private EditText req;
    public void SearchBook(View v){
        req=findViewById(R.id.Bookreq);
        String s=req.getText().toString();
        if(TextUtils.isEmpty(s)){
            getAddress(getIntent().getStringExtra("EmailUser"));
        }
        else {
            generateList2(s);
        }
    }
    public void LogOut(View v){
        Intent intent = new Intent();
        intent.setClass(v.getContext(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if(State=="CreateDeal"){
            setContentView(R.layout.mainpage);
            State="Main";
            ComeBackToSearch();
        }
        else if(State=="Main"){
            State="Login";
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        else if(State=="MyEndDeals"){
            State="Main";
            setContentView(R.layout.activity_main);
            ViewProfileMain();
        }
        else if(State=="ViewDeal"){
            setContentView(R.layout.mainpage);
            State="Main";
            ComeBackToSearch();
        }
    }
}
