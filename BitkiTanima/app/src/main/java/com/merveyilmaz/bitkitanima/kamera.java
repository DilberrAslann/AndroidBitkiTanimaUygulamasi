package com.merveyilmaz.bitkitanima;

import android.Manifest;
import android.content.Intent;
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

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link kamera#newInstance} factory method to
 * create an instance of this fragment.
 */
public class kamera extends Fragment {

    ImageView fotoSecImage,kameraAcImage;
    Bitmap selectedImage;
    Uri imageData;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public kamera() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment kamera.
     */
    // TODO: Rename and change types and number of parameters
    public static kamera newInstance(String param1, String param2) {
        kamera fragment = new kamera();
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
        View view =  inflater.inflate(R.layout.fragment_kamera, container, false);

        //resimlerimizi tanımladık.
         fotoSecImage = (ImageView) view.findViewById(R.id.fotoSecImage);
         kameraAcImage = (ImageView) view.findViewById(R.id.kameraAcImage);

         //galeriden fotoğraf seçme butonuna tıklama özelliği verdik.
        fotoSecImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    //ilk erişimde izin ister.
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                }else{
                    //izin verilirse galeriye gider.
                    Intent intentToGallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intentToGallery,2);
                }

            }
        });
        //kamera açma butonuna tıklama özelliği verdik.
        kameraAcImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Resim çekme isteği ve activity başlatılıp id'si tanımlandı
                Intent kamera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(kamera,33);
            }
        });

        return view;

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


        if(requestCode==33){
            //kamera resultı

            //Çekilen resim id olarak bitmap şeklinde alındı ve imageview'e atandı
            Bitmap image=(Bitmap)data.getExtras().get("data");
            kameraAcImage.setImageBitmap(image);

        }
        else if(requestCode==2 && resultCode==RESULT_OK && data!=null){
            //galeri resultı

            //fotoğrafımızı image imize yükledik.
            imageData=data.getData();
            try {
                //fotoğrafımızı bitmap yapısına kaydettik. versiyon olarak 28 üstü ve altı olarak farklılık gösterdiği için ayırdık
                if(Build.VERSION.SDK_INT>=28){
                    ImageDecoder.Source source=ImageDecoder.createSource(getContext().getContentResolver(),imageData);
                    selectedImage=ImageDecoder.decodeBitmap(source);
                    fotoSecImage.setImageBitmap(selectedImage);
                }else{
                    selectedImage=MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),imageData);
                    fotoSecImage.setImageBitmap(selectedImage);
                }

            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }




}