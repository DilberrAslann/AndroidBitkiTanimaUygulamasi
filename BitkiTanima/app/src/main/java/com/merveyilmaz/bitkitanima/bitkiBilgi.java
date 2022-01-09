package com.merveyilmaz.bitkitanima;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class bitkiBilgi extends AppCompatActivity {

    ImageButton geriiButton;
    ImageView bitkiImage;
    TextView bitkiAdiText,bitkiBilgiText;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;

    String bitkiAdi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitki_bilgi);

        bitkiImage=findViewById(R.id.bitkiImage);
        bitkiBilgiText=findViewById(R.id.bitkiBilgiText);
        bitkiAdiText=findViewById(R.id.bitkiAdiText);

        bitkiAdi = getIntent().getExtras().getString("bitkiAdi");
        bitkiAdiText.setText(bitkiAdi);

        geriiButton=findViewById(R.id.geriiButton);
        geriiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gecisYap = new Intent(bitkiBilgi.this, bahcem.class);
                startActivity(gecisYap);
            }
        });

        //Firebase özelliklerini kullanabilmek için tanımladık.
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();

        //verileri çekmek için oluşturduğumuz fonksiyonu çağırdık.
        getDataFromFirestore();

    }


    public void getDataFromFirestore(){

        //Oluşturduğumuz BitkiBilgi collection daki verileri çekmek için tanımlama yaptık.
        CollectionReference collectionReference=firebaseFirestore.collection("BitkiBilgi");

        //Verileri firebaseden bitkiAdına göre bitki bilgilerini çeken  fonksiyonumuzu yazdık.
        collectionReference.whereEqualTo("bitkiAdi",bitkiAdi).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if(e!=null){
                    //Verileri çekerken hata ile karşılaşırsak uyarı mesajı gösteriyor.
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_SHORT).show();
                }
                if(queryDocumentSnapshots!=null){ //veriler alındıysa
                    for(DocumentSnapshot snapshot: queryDocumentSnapshots.getDocuments()){

                        //alınan değerleri kullanabilmek için hash map e kaydettik.
                        Map<String,Object> data=snapshot.getData();

                            //hash map a kaydettiğimiz verileri değişkenlere atadık.
                            String genelBilgi=(String) data.get("genelBilgi");
                            String dikimZamanı=(String) data.get("dikimZamanı");
                            String hasatZamanı=(String) data.get("hasatZamanı");
                            String budamaZamanı=(String) data.get("budamaZamanı");
                            String türleri=(String) data.get("türleri");
                            String faydaları=(String) data.get("faydaları");
                            String kullanıldığıYerler=(String) data.get("kullanıldığıYerler");
                            String bitkiFoto=(String) data.get("bitkiFotoUrl");

                            //fotoğraflarımızı görüntüleyebilmek için picasso yapısını kullandık.
                            Picasso.get().load(bitkiFoto).into(bitkiImage);

                            //değişkenlerdeki verileri text de gösterdik.
                            bitkiBilgiText.setText("Genel bilgi:\n\n"+genelBilgi+"\n\n"+"Dikim Zamanı:\n\n"+dikimZamanı+"\n\n"+"Hasat Zamanı:\n\n"+hasatZamanı+"\n\n"+"Budama Zamanı:\n\n"+budamaZamanı+"\n\n"+"Türleri:\n\n"+türleri+"\n\n"+"Faydaları:\n\n"+faydaları+"\n\n"+"Kullanıldığı yerler:\n\n"+kullanıldığıYerler+"\n\n");
                    }
                }

            }
        });
    }
}