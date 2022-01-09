package com.merveyilmaz.bitkitanima;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.UUID;

public class gonderiEkle extends AppCompatActivity {

    EditText yorumText;
    Button yukleButton;
    ImageView gonderiSec;
    Bitmap selectedImage;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    Uri imageData;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gonderi_ekle);

        yorumText=findViewById(R.id.yorumText);
        yukleButton=findViewById(R.id.yukleButton);
        gonderiSec=findViewById(R.id.gonderiSec);

        //firebase kullanabilmek için tanımlamalar yaptık.
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();


    }

    public void yukleButton(View view){

        //random uuıd tanımlaması yaptık.
        UUID uuıd=UUID.randomUUID();
        String imageName="image/"+uuıd+".jpg"; //fotoğrafımız ismini oluşturduk.

         if(imageData!=null){ //eğer image imiz boş değilse
            //firebase e fotoğrafımızı kaydetmek için fonksiyon yazdık.
            storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(gonderiEkle.this,"Fotoğraf yüklendi...",Toast.LENGTH_SHORT).show();

                    //fotoğrafımızı ekledik.
                    StorageReference newReference=firebaseStorage.getInstance().getReference(imageName);
                    newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            //fotoğrafı indirme linkini değişkene atadık.
                            String downloadUrl=uri.toString();

                            //mail bilgimizi ve yorumumuzu değişkene atadık.
                            FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                            String userEmail=firebaseUser.getEmail();
                            String comment=yorumText.getText().toString();

                            //bilgilerimizi firebase e kaydetmek için hash map yapısını kullandık.
                            HashMap<String,Object> postData=new HashMap<>();
                            postData.put("Email",userEmail);
                            postData.put("downloadUrl",downloadUrl);
                            postData.put("comment",comment);
                            postData.put("date", FieldValue.serverTimestamp());



                            firebaseFirestore.collection("Posts").add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                    Intent intent=new Intent(gonderiEkle.this,navigationBar.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(gonderiEkle.this,e.getLocalizedMessage().toString(),Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(gonderiEkle.this,e.getLocalizedMessage().toString(),Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    public void gonderiEkle(View view){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            //ilk erişimde izin ister.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else{
            //izin verilirse galeriye gider.
            Intent intentToGallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGallery,2);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==2 && resultCode==RESULT_OK && data!=null){
            imageData=data.getData(); //fotoğrafımızı image imize yükledik.
            try {
                //fotoğrafımızı bitmap yapısına kaydettik.
                if(Build.VERSION.SDK_INT>=28){
                    ImageDecoder.Source source=ImageDecoder.createSource(this.getContentResolver(),imageData);
                    selectedImage=ImageDecoder.decodeBitmap(source);
                    gonderiSec.setImageBitmap(selectedImage);
                }else{
                    selectedImage=MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageData);
                    gonderiSec.setImageBitmap(selectedImage);
                }

            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }
}