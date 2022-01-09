package com.merveyilmaz.bitkitanima;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    Button girisYapButon,kayitOlButon;
    EditText emailText,parolaText;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        girisYapButon    =   (Button) findViewById(R.id.girisYapButon);
        kayitOlButon    =   (Button) findViewById(R.id.kayitOlButon);
        emailText    =   findViewById(R.id.emailText);
        parolaText    =   findViewById(R.id.parolaText);


        //firebase kullanıcı işlemlerini kullanabilmek için tanımladık.
        firebaseAuth=FirebaseAuth.getInstance();

        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            //eğer kullanıcı daha önceden giriş yaptıysa otomatik giriş yapar.
            Intent gecisYap = new Intent(MainActivity.this, hosegldiniz.class);
            startActivity(gecisYap);
            finish();
        }

    }

    public void girisButton(View view){

        //text lerden email ve parolayı değişkenlere atadık.
        String email=emailText.getText().toString();
        String parola=parolaText.getText().toString();

        //eğer email ve parola firebase de kayıtlıysa giriş yapılır.
        firebaseAuth.signInWithEmailAndPassword(email,parola).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                Intent gecisYap = new Intent(MainActivity.this, hosegldiniz.class);
                startActivity(gecisYap);
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void kayitOlButton(View view){
        Intent gecisYap2 = new Intent(MainActivity.this, KayitOl.class);
        startActivity(gecisYap2);
        finish();
    }
}