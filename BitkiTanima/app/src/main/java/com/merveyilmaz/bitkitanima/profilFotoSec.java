package com.merveyilmaz.bitkitanima;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class profilFotoSec extends AppCompatActivity {

    Button profilEkleButton;
    ImageView profilFotoSec,profilFotoGeriButton;

    Bitmap selectedImage;
    Uri imageData;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_foto_sec);

        profilEkleButton=findViewById(R.id.profilEkleButton);
        profilFotoSec=findViewById(R.id.profilFotoSec);
        profilFotoGeriButton=findViewById(R.id.profilFotoGeriButton);


        //firebase kullanabilmek i??in tan??mlamalar yapt??k.
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

        //profil foto??raf??m??z var m?? kontrol etti??imiz fonksiyonu ??a????rd??k.
        profilFotoKontrol();

        profilFotoSec.setOnClickListener(new View.OnClickListener() {

            //galeriye gitmek i??in logoya ilk t??klan??ld??????nda izin ister.
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(profilFotoSec.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    //ilk eri??imde izin ister.
                    ActivityCompat.requestPermissions(profilFotoSec.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                }else{
                    //izin verilirse galeriye gider.
                    Intent intentToGallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intentToGallery,2);
                }

            }
        });

        //geri gitmek i??in imagemize on clik ekledik.
        profilFotoGeriButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent=new Intent(profilFotoSec.this,navigationBar.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });




    }

    public void profilFotoKontrol(){

        //Olu??turdu??umuz Posts collection daki verileri ??ekmek i??in tan??mlama yapt??k.
        CollectionReference collectionReference=firebaseFirestore.collection("ProfilFoto");

        //mail bilgimizi  de??i??kene atad??k.
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        String userEmail=firebaseUser.getEmail();

        //Verileri firebaseden maile g??re ??eken fonksiyonumuzu yazd??k.
        collectionReference.whereEqualTo("Email",userEmail).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if(e!=null){

                    //Verileri ??ekerken hata ile kar????la????rsak uyar?? mesaj?? g??steriyor.
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_SHORT).show();


                }

                if(queryDocumentSnapshots!=null){ //veriler al??nd??ysa
                    for(DocumentSnapshot snapshot: queryDocumentSnapshots.getDocuments()){

                        //al??nan de??erleri kullanabilmek i??in hash map e kaydettik.
                        Map<String,Object> data=snapshot.getData();

                        //hash map a kaydetti??imiz verileri de??i??kenlere atad??k.
                        String email=(String) data.get("Email");

                        //mailimiz bo??mu kontrol ettik.
                        if(email!=null){
                            //dolu ise butonu g??r??nmez yapt??k.
                            profilEkleButton.setVisibility(View.INVISIBLE);
                        }


                    }
                }

            }
        });
    }


    public void profilEkleButton(View view){

        //E??er firebase de kullan??c??ya ait profil resmi bulunmad??ysa ekleme yapar

        //random uu??d tan??mlamas?? yapt??k.
        UUID uu??d=UUID.randomUUID();
        String imageName="profilFotoImage/"+uu??d+".jpg"; //foto??raf??m??z ismini olu??turduk.

        if(imageData!=null){ //e??er image imiz bo?? de??ilse
            //firebase e foto??raf??m??z?? kaydetmek i??in fonksiyon yazd??k.
            storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(profilFotoSec.this,"Foto??raf y??klendi...",Toast.LENGTH_SHORT).show();

                    //foto??raf??m??z?? ekledik.
                    StorageReference newReference=firebaseStorage.getInstance().getReference(imageName);
                    newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            //foto??raf?? indirme linkini de??i??kene atad??k.
                            String profilDownloadUrl=uri.toString();

                            //mail bilgimizi  de??i??kene atad??k.
                            FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                            String userEmail=firebaseUser.getEmail();

                            //bilgilerimizi firebase e kaydetmek i??in hash map yap??s??n?? kulland??k.
                            HashMap<String,Object> profilFotoData=new HashMap<>();
                            profilFotoData.put("Email",userEmail);
                            profilFotoData.put("profilDownloadUrl",profilDownloadUrl);

                            //firebase de ProfilFoto koleksiyonumuzu olu??turduk.
                            firebaseFirestore.collection("ProfilFoto").add(profilFotoData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                    //ba??ar??l?? olunursa anasayfaya y??nlendirdik.
                                    Intent intent=new Intent(profilFotoSec.this,navigationBar.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(profilFotoSec.this,e.getLocalizedMessage().toString(),Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(profilFotoSec.this,e.getLocalizedMessage().toString(),Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==1){ //izin verildi??inde galeriye gitme metodu
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Intent intentToGallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery,2);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==2 && resultCode==RESULT_OK && data!=null){
            imageData=data.getData(); //foto??raf??m??z?? image imize y??kledik.

            try {
                //foto??raf??m??z?? versiyona g??re bitmap yap??s??na kaydettik.
                if(Build.VERSION.SDK_INT>=28){
                    ImageDecoder.Source source=ImageDecoder.createSource(profilFotoSec.this.getContentResolver(),imageData);
                    selectedImage=ImageDecoder.decodeBitmap(source);
                    profilFotoSec.setImageBitmap(selectedImage);
                }else{
                    selectedImage=MediaStore.Images.Media.getBitmap(profilFotoSec.this.getContentResolver(),imageData);
                    profilFotoSec.setImageBitmap(selectedImage);
                }

            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }
}