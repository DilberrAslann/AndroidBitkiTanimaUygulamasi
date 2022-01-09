package com.merveyilmaz.bitkitanima;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link blog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class blog extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    FeedRecyclerAdapter feedRecyclerAdapter;

    ArrayList<String> commentFromFB;
    ArrayList<String> imageFromFB;
    ArrayList<String> emailFromFB;
    ArrayList<Timestamp> dateFromFB;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public blog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment blog.
     */
    // TODO: Rename and change types and number of parameters
    public static blog newInstance(String param1, String param2) {
        blog fragment = new blog();
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
        View view =  inflater.inflate(R.layout.fragment_blog, container, false);

        //Firebaseden çektiğimiz verileri kullanabilmek için arraylist tanımladık.
        commentFromFB=new ArrayList<>();
        imageFromFB=new ArrayList<>();
        emailFromFB=new ArrayList<>();
        dateFromFB=new ArrayList<>();

        //Firebase özelliklerini kullanabilmek için tanımladık.
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        //verileri çekmek için oluşturduğumuz fonksiyonu çağırdık.
        getDataFromFirestore();

        //recycle ımızı tanımladık
        RecyclerView recyclerView=(RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //arraylistimize kaydettiğimiz verileri oluşturduğumuz java clasına gönderiyoruz.
        feedRecyclerAdapter=new FeedRecyclerAdapter(imageFromFB,commentFromFB,emailFromFB,dateFromFB);
        //recycleview ile java clas ımızı bağdaştırıyoruz.
        recyclerView.setAdapter(feedRecyclerAdapter);

        return view;
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
                        String comment=(String) data.get("comment");
                        String email=(String) data.get("Email");
                        String downloadUrl=(String) data.get("downloadUrl");
                        Timestamp date=(Timestamp) data.get("date");

                        if(email.length() > 0) {
                            //mailin ilk 5 hanesini kullanıcı adı olarak gösterdik.
                            String emaililk5hane = email.substring(0, 5);
                            emailFromFB.add(emaililk5hane);
                        }

                        //değişkenlerdeki değerleri kullanabilmek için arrayliste ekledik.
                        commentFromFB.add(comment);
                        imageFromFB.add(downloadUrl);
                        dateFromFB.add(date);

                        /*System.out.println(comment);
                        System.out.println(kullaiciAdi);
                        System.out.println(downloadUrl);*/

                        //yaptığımız sonuçları kaydettik.
                        feedRecyclerAdapter.notifyDataSetChanged();
                    }
                }

            }
        });
    }
}