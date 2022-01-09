package com.merveyilmaz.bitkitanima;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link anasayfa#newInstance} factory method to
 * create an instance of this fragment.
 */
public class anasayfa extends Fragment {

    ImageView andirahnaImage,cevizImage,cayImage,cuhaImage,findikImage,haniftaImage,ihlamurImage,komarImage,kusburnuImage,ligarbaImage,manisakImage;
    ImageView karahindibaImage,karayemisImage,kardelenImage,kizilcikImage,uzumImage,moraImage,siklamenImage,incirImage,hurmaImage,vargitImage,zifinImage;
    GridLayout gridLayout;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public anasayfa() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment anasayfa.
     */
    // TODO: Rename and change types and number of parameters
    public static anasayfa newInstance(String param1, String param2) {
        anasayfa fragment = new anasayfa();
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
        View view =  inflater.inflate(R.layout.fragment_anasayfa, container, false);

        GridLayout gridLayout = (GridLayout) view.findViewById(R.id.gridLayout);

        //Imagelerimizi tanımladık
        andirahnaImage = (ImageView) view.findViewById(R.id.andirahnaImage);
        cevizImage = (ImageView) view.findViewById(R.id.cevizImage);
        cayImage = (ImageView) view.findViewById(R.id.cayImage);
        cuhaImage = (ImageView) view.findViewById(R.id.cuhaImage);
        findikImage = (ImageView) view.findViewById(R.id.findikImage);
        haniftaImage = (ImageView) view.findViewById(R.id.haniftaImage);
        ihlamurImage = (ImageView) view.findViewById(R.id.ihlamurImage);
        karahindibaImage = (ImageView) view.findViewById(R.id.karahindibaImage);
        karayemisImage = (ImageView) view.findViewById(R.id.karayemisImage);
        kardelenImage = (ImageView) view.findViewById(R.id.kardelenImage);
        kizilcikImage = (ImageView) view.findViewById(R.id.kizilcikImage);
        uzumImage = (ImageView) view.findViewById(R.id.uzumImage);
        komarImage = (ImageView) view.findViewById(R.id.komarImage);
        kusburnuImage = (ImageView) view.findViewById(R.id.kusburnuImage);
        ligarbaImage = (ImageView) view.findViewById(R.id.ligarbaImage);
        manisakImage = (ImageView) view.findViewById(R.id.manisakImage);
        moraImage = (ImageView) view.findViewById(R.id.moraImage);
        incirImage = (ImageView) view.findViewById(R.id.incirImage);
        siklamenImage = (ImageView) view.findViewById(R.id.siklamenImage);
        hurmaImage = (ImageView) view.findViewById(R.id.hurmaImage);
        vargitImage = (ImageView) view.findViewById(R.id.vargitImage);
        zifinImage = (ImageView) view.findViewById(R.id.zifinImage);

        //resimlere tıklandığında bitki bilgi sayfasına yönlendirdik.
        andirahnaImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),bitkiBilgi.class);
                intent.putExtra("bitkiAdi","Andirahna");
                startActivity(intent);

                /*Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.andirahna);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                Intent intent = new Intent(this, bitkiBilgi.class);
                intent.putExtra("resim", byteArray);
                startActivity(intent);*/
            }
        });
        cevizImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),bitkiBilgi.class);
                intent.putExtra("bitkiAdi","Ceviz");
                startActivity(intent);
            }
        });
        cayImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),bitkiBilgi.class);
                intent.putExtra("bitkiAdi","Cay");
                startActivity(intent);
            }
        });
        cuhaImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),bitkiBilgi.class);
                intent.putExtra("bitkiAdi","Cuha");
                startActivity(intent);
            }
        });
        findikImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),bitkiBilgi.class);
                intent.putExtra("bitkiAdi","Findik");
                startActivity(intent);
            }
        });
        haniftaImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),bitkiBilgi.class);
                intent.putExtra("bitkiAdi","Hanifta");
                startActivity(intent);
            }
        });
        ihlamurImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),bitkiBilgi.class);
                intent.putExtra("bitkiAdi","Ihlamur");
                startActivity(intent);
            }
        });
        karahindibaImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),bitkiBilgi.class);
                intent.putExtra("bitkiAdi","Karahindiba");
                startActivity(intent);
            }
        });
        karayemisImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),bitkiBilgi.class);
                intent.putExtra("bitkiAdi","Karayemis");
                startActivity(intent);
            }
        });
        kardelenImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),bitkiBilgi.class);
                intent.putExtra("bitkiAdi","Kardelen");
                startActivity(intent);
            }
        });
        kizilcikImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),bitkiBilgi.class);
                intent.putExtra("bitkiAdi","Kizilcik");
                startActivity(intent);
            }
        });
        uzumImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),bitkiBilgi.class);
                intent.putExtra("bitkiAdi","Kokulu Uzum");
                startActivity(intent);
            }
        });
        komarImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),bitkiBilgi.class);
                intent.putExtra("bitkiAdi","Komar");
                startActivity(intent);
            }
        });
        kusburnuImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),bitkiBilgi.class);
                intent.putExtra("bitkiAdi","Kusburnu");
                startActivity(intent);
            }
        });
        ligarbaImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),bitkiBilgi.class);
                intent.putExtra("bitkiAdi","Ligarba");
                startActivity(intent);
            }
        });
        manisakImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),bitkiBilgi.class);
                intent.putExtra("bitkiAdi","Manisak");
                startActivity(intent);
            }
        });
        moraImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),bitkiBilgi.class);
                intent.putExtra("bitkiAdi","Mora");
                startActivity(intent);
            }
        });
        incirImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),bitkiBilgi.class);
                intent.putExtra("bitkiAdi","Patlican Inciri");
                startActivity(intent);
            }
        });
        siklamenImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),bitkiBilgi.class);
                intent.putExtra("bitkiAdi","Siklamen");
                startActivity(intent);
            }
        });
        hurmaImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),bitkiBilgi.class);
                intent.putExtra("bitkiAdi","Trabzon Hurmasi");
                startActivity(intent);
            }
        });
        vargitImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),bitkiBilgi.class);
                intent.putExtra("bitkiAdi","Vargit");
                startActivity(intent);
            }
        });
        zifinImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),bitkiBilgi.class);
                intent.putExtra("bitkiAdi","Zifin");
                startActivity(intent);
            }
        });


        return view;
    }
}