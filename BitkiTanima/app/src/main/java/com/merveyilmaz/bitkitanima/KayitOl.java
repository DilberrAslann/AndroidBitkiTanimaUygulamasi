package com.merveyilmaz.bitkitanima;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class KayitOl extends AppCompatActivity {

    Button giris;
    EditText kayitEmailText,kayitParolaText;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ol);

        giris=findViewById(R.id.giris);
        kayitEmailText=findViewById(R.id.kayitEmailText);
        kayitParolaText=findViewById(R.id.kayitParolaText);


        //firebase kullanıcı özelliklerini kullanabilmek için tanımlama yaptık.
        firebaseAuth=FirebaseAuth.getInstance();


    }

    public void kullaniciBilgiKaydetButton(View view){

        String Email=kayitEmailText.getText().toString();
        String parola=kayitParolaText.getText().toString();

        if(Email.matches("")){
            Toast.makeText(KayitOl.this,"E-mail adresinizi boş bırakmayınız..",Toast.LENGTH_SHORT).show();
        }else if(parola.matches("")){
            Toast.makeText(KayitOl.this,"Parolanızı boş bırakmayınız..",Toast.LENGTH_SHORT).show();
        }

        //eğer email ve parola text lere girildiyse firebase de hesap oluşturuluyor.
        firebaseAuth.createUserWithEmailAndPassword(Email,parola).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(KayitOl.this,"Kullanıcı oluşturuldu...",Toast.LENGTH_SHORT).show();
                Intent gecisYap = new Intent(KayitOl.this, hosegldiniz.class);
                startActivity(gecisYap);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(KayitOl.this,e.getLocalizedMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void girisButton(View view){
        Intent gecisYap = new Intent(KayitOl.this, MainActivity.class);
        startActivity(gecisYap);
        finish();
    }
}