package com.merveyilmaz.bitkitanima;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profil#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profil extends Fragment {

    ImageButton ayarlarButton;
    ImageView profilFotoImage;
    Button gonderiEkleButton;
    TextView kullaniciadiiText;

    ProfilRecycleAdapter profilRecycleAdapter;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    ArrayList<String> imageFromFB;
    public ArrayAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public profil() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ayarlar.
     */
    // TODO: Rename and change types and number of parameters
    public static profil newInstance(String param1, String param2) {
        profil fragment = new profil();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profil, container, false);

        ayarlarButton = (ImageButton) view.findViewById(R.id.ayarlarButton);
        gonderiEkleButton = (Button) view.findViewById(R.id.gonderiEkleButton);
        kullaniciadiiText = (TextView) view.findViewById(R.id.kullaniciadiiText);
        profilFotoImage = (ImageView) view.findViewById(R.id.profilFotoImage);

        //firebase kullanabilmek için tanımlamalar yaptık.
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

        //arraylistimizi tanımladık
        imageFromFB=new ArrayList<>();

        //verileri çekmek için oluşturduğumuz fonksiyonu çağırdık.
        getDataFromFirestore();
        profilFoto();

        //profilde kullanıcı adı gözükmesi için emailimizi çağırdık.
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        String userEmail=firebaseUser.getEmail();

        if(userEmail.length() > 0) {
            //mailin ilk 5 hanesini kullanıcı adı olarak gösterdik.
            String emaililk5hane = userEmail.substring(0, 5);
            kullaniciadiiText.setText(emaililk5hane);
        }

        //recycle ımızı tanımladık
        RecyclerView recyclerViewProfil = (RecyclerView)  view.findViewById(R.id.recyclerViewProfil);
        recyclerViewProfil.setLayoutManager(new LinearLayoutManager(getActivity()));

        //arraylistimize kaydettiğimiz verileri oluşturduğumuz java clasına gönderiyoruz.
        profilRecycleAdapter=new ProfilRecycleAdapter(imageFromFB);
        //recycleview ile java clas ımızı bağdaştırıyoruz.
        recyclerViewProfil.setAdapter(profilRecycleAdapter);


        gonderiEkleButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent gecisYap1 = new Intent(getActivity(), gonderiEkle.class);
                startActivity(gecisYap1);
            }
        });

        ayarlarButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent gecisYap = new Intent(getActivity(), ayarlar.class);
                startActivity(gecisYap);
            }
        });

        return view;
    }

    public void profilFoto(){

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
                        Toast.makeText(getContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_SHORT).show();
                    }
                    if(queryDocumentSnapshots!=null){ //veriler alındıysa
                        for(DocumentSnapshot snapshot: queryDocumentSnapshots.getDocuments()){

                            //alınan değerleri kullanabilmek için hash map e kaydettik.
                            Map<String,Object> data=snapshot.getData();

                            //hash map a kaydettiğimiz verileri değişkenlere atadık.
                            String profilFotoDownloadUrl=(String) data.get("profilDownloadUrl");

                            //fotoğraflarımızı görüntüleyebilmek için picasso yapısını kullandık.
                            Picasso.get().load(profilFotoDownloadUrl).into(profilFotoImage);

                        }
                    }

                }
            });

    }


    public void getDataFromFirestore(){

        //Oluşturduğumuz Posts collection daki verileri çekmek için tanımlama yaptık.
        CollectionReference collectionReference=firebaseFirestore.collection("Posts");

        //Verileri firebaseden tarihe göre çeken fonksiyonumuzu yazdık.
        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if(e!=null){
                    //Verileri çekerken hata ile karşılaşırsak uyarı mesajı gösteriyor.
                    Toast.makeText(getActivity(),e.getLocalizedMessage().toString(),Toast.LENGTH_SHORT).show();
                }
                if(queryDocumentSnapshots!=null){ //veriler alındıysa
                    for(DocumentSnapshot snapshot: queryDocumentSnapshots.getDocuments()){

                        //alınan değerleri kullanabilmek için hash map e kaydettik.
                        Map<String,Object> data=snapshot.getData();

                        //hash map a kaydettiğimiz verileri değişkenlere atadık.
                        String downloadUrl=(String) data.get("downloadUrl");
                        String email=(String) data.get("Email");

                        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                        String userEmail=firebaseUser.getEmail();

                        //kullanıcı adına göre verileri filtreledik.
                        if(email.matches(userEmail.toString())){
                            //değişkenlerdeki değerleri kullanabilmek için arrayliste ekledik.
                            imageFromFB.add(downloadUrl);
                        }

                        //yaptığımız sonuçları kaydettik.
                        profilRecycleAdapter.notifyDataSetChanged();

                    }
                }

            }
        });
    }

}