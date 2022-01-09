package com.merveyilmaz.bitkitanima;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class hosegldiniz extends AppCompatActivity {

    Button giris;
    ImageView kayitKullaniciFoto;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosegldiniz);

        giris=findViewById(R.id.giris);
        kayitKullaniciFoto=findViewById(R.id.kayitKullaniciFoto);

        //Firebase özelliklerini kullanabilmek için tanımladık.
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        //verileri çekmek için oluşturduğumuz fonksiyonu çağırdık.
        getDataFromFirestore();

    }

    public void girisYapButton(View view){
        Intent gecisYap = new Intent(hosegldiniz.this, navigationBar.class);
        startActivity(gecisYap);
        finish();
    }

    public void getDataFromFirestore(){

        //Oluşturduğumuz ProfilFoto collection daki verileri çekmek için tanımlama yaptık.
        CollectionReference collectionReference=firebaseFirestore.collection("ProfilFoto");

        //mail bilgimizi  değişkene atadık.
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        String userEmail=firebaseUser.getEmail();

        //Verileri firebaseden emaile göre çeken fonksiyonumuzu yazdık.
        collectionReference.whereEqualTo("Email",userEmail).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if(e!=null){
                    //Verileri çekerken hata ile karşılaşırsak uyarı mesajı gösteriyor.
                    Toast.makeText(hosegldiniz.this,e.getLocalizedMessage().toString(),Toast.LENGTH_SHORT).show();
                }
                if(queryDocumentSnapshots!=null){ //veriler alındıysa
                    for(DocumentSnapshot snapshot: queryDocumentSnapshots.getDocuments()){

                        //alınan değerleri kullanabilmek için hash map e kaydettik.
                        Map<String,Object> data=snapshot.getData();

                        //hash map a kaydettiğimiz verileri değişkenlere atadık.
                        String profilFotoDownloadUrl=(String) data.get("profilDownloadUrl");

                        //fotoğraflarımızı görüntüleyebilmek için picasso yapısını kullandık.
                        Picasso.get().load(profilFotoDownloadUrl).into(kayitKullaniciFoto);

                    }
                }

            }
        });
    }
}