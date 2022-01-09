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


        //firebase kullanabilmek için tanımlamalar yaptık.
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

        //profil fotoğrafımız var mı kontrol ettiğimiz fonksiyonu çağırdık.
        profilFotoKontrol();

        profilFotoSec.setOnClickListener(new View.OnClickListener() {

            //galeriye gitmek için logoya ilk tıklanıldığında izin ister.
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(profilFotoSec.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    //ilk erişimde izin ister.
                    ActivityCompat.requestPermissions(profilFotoSec.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                }else{
                    //izin verilirse galeriye gider.
                    Intent intentToGallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intentToGallery,2);
                }

            }
        });

        //geri gitmek için imagemize on clik ekledik.
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

        //Oluşturduğumuz Posts collection daki verileri çekmek için tanımlama yaptık.
        CollectionReference collectionReference=firebaseFirestore.collection("ProfilFoto");

        //mail bilgimizi  değişkene atadık.
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        String userEmail=firebaseUser.getEmail();

        //Verileri firebaseden maile göre çeken fonksiyonumuzu yazdık.
        collectionReference.whereEqualTo("Email",userEmail).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                        String email=(String) data.get("Email");

                        //mailimiz boşmu kontrol ettik.
                        if(email!=null){
                            //dolu ise butonu görünmez yaptık.
                            profilEkleButton.setVisibility(View.INVISIBLE);
                        }


                    }
                }

            }
        });
    }


    public void profilEkleButton(View view){

        //Eğer firebase de kullanıcıya ait profil resmi bulunmadıysa ekleme yapar

        //random uuıd tanımlaması yaptık.
        UUID uuıd=UUID.randomUUID();
        String imageName="profilFotoImage/"+uuıd+".jpg"; //fotoğrafımız ismini oluşturduk.

        if(imageData!=null){ //eğer image imiz boş değilse
            //firebase e fotoğrafımızı kaydetmek için fonksiyon yazdık.
            storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(profilFotoSec.this,"Fotoğraf yüklendi...",Toast.LENGTH_SHORT).show();

                    //fotoğrafımızı ekledik.
                    StorageReference newReference=firebaseStorage.getInstance().getReference(imageName);
                    newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            //fotoğrafı indirme linkini değişkene atadık.
                            String profilDownloadUrl=uri.toString();

                            //mail bilgimizi  değişkene atadık.
                            FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                            String userEmail=firebaseUser.getEmail();

                            //bilgilerimizi firebase e kaydetmek için hash map yapısını kullandık.
                            HashMap<String,Object> profilFotoData=new HashMap<>();
                            profilFotoData.put("Email",userEmail);
                            profilFotoData.put("profilDownloadUrl",profilDownloadUrl);

                            //firebase de ProfilFoto koleksiyonumuzu oluşturduk.
                            firebaseFirestore.collection("ProfilFoto").add(profilFotoData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                    //başarılı olunursa anasayfaya yönlendirdik.
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

        if(requestCode==1){ //izin verildiğinde galeriye gitme metodu
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
            imageData=data.getData(); //fotoğrafımızı image imize yükledik.

            try {
                //fotoğrafımızı versiyona göre bitmap yapısına kaydettik.
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