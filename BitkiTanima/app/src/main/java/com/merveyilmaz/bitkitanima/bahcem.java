package com.merveyilmaz.bitkitanima;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link bahcem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class bahcem extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ListView bahcemListView;
    ArrayAdapter<String> adapter;
    ArrayList<String> bitkiAdi;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public bahcem() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment bahcem.
     */
    // TODO: Rename and change types and number of parameters
    public static bahcem newInstance(String param1, String param2) {
        bahcem fragment = new bahcem();
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

        View view =  inflater.inflate(R.layout.fragment_bahcem, container, false);

        //Kamerada okutulan bitkileri bahçem bölümünde göstermek için listview oluşturduk.
        bahcemListView=(ListView) view.findViewById(R.id.bahcemListView);

        //Listview a bitki adlarını ekleyebilmek için arraylist tanımladık.
        bitkiAdi=new ArrayList<String>();
        bitkiAdi.add("   Bitki Adi");
        bitkiAdi.add("   Bitki Adi");
        bitkiAdi.add("   Bitki Adi");
        bitkiAdi.add("   Bitki Adi");
        bitkiAdi.add("   Bitki Adi");
        bitkiAdi.add("   Bitki Adi");
        bitkiAdi.add("   Bitki Adi");
        bitkiAdi.add("   Bitki Adi");
        bitkiAdi.add("   Bitki Adi");
        bitkiAdi.add("   Bitki Adi");
        bitkiAdi.add("   Bitki Adi");
        bitkiAdi.add("   Bitki Adi");

        //Arraylist ile listview ımızı bağdaştırmak için adapter kullandık.
        adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, bitkiAdi);
        bahcemListView.setAdapter(adapter);

        //Listviewımıza tıklanabilirlik için fonksiyon tanımladık.
        bahcemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();

                Intent gecisYap = new Intent(getActivity(), bitkiBilgi.class);
                startActivity(gecisYap);

            }
        });
        return view;


    }
}