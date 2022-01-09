package com.merveyilmaz.bitkitanima;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ayarlar extends AppCompatActivity {

    ListView ayarlarListView;
    ArrayList<String> ayarlarArray;
    ArrayAdapter<String> adapter;
    ImageButton geriButton;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayarlar);

        //Kullanıcı işlemleri için firebasei aktif hale getirdik.
        firebaseAuth=FirebaseAuth.getInstance();

        //Ayarlarımızı ekleyebileceğimiz bir liste yapısı oluşturduk bunu da burada tanımladık.
        ayarlarListView=(ListView) findViewById(R.id.ayarlarListView);

        //Listview ımıza ayar ekleyebilmek için arraylist oluşturduk.
        ayarlarArray=new ArrayList<String>();
        ayarlarArray.add("   Profil Fotoğrafı Ekle");
        ayarlarArray.add("   Çıkış Yap");


        //Arraylistimizdeki ayarları listview ımızla bağdaştırdık.
        adapter = new  ArrayAdapter<String>(this.getBaseContext(), android.R.layout.simple_list_item_1, android.R.id.text1, ayarlarArray);
        ayarlarListView.setAdapter(adapter);

        //Listview ımıza tıklanabilirlik fonksiyonu tanımladık.
        ayarlarListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(ayarlar.this, ((TextView) view).getText(), Toast.LENGTH_SHORT).show();

                //Listviewımızda tıkladığımız bölümün ne iş yapacağını aşağıda if else ile kontrol ettik.
                if(((TextView) view).getText()=="   Çıkış Yap"){

                    firebaseAuth.signOut(); //Kullanıcımızın uygulamadan çıkış yapmasını sağlayan firebase yapısı tanımladık.

                    Intent gecisYap1 = new Intent(ayarlar.this, MainActivity.class);
                    startActivity(gecisYap1);
                    finish();

                }
                else if(((TextView) view).getText()=="   Profil Fotoğrafı Ekle"){

                    Intent gecisYap1 = new Intent(ayarlar.this, profilFotoSec.class);
                    startActivity(gecisYap1);
                    finish();

                }

            }
        });

        geriButton=(ImageButton) findViewById(R.id.geriButton);
        geriButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gecisYap = new Intent(ayarlar.this, profil.class);
                startActivity(gecisYap);
            }
        });

    }
}