package com.merveyilmaz.bitkitanima;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfilRecycleAdapter extends RecyclerView.Adapter<ProfilRecycleAdapter.PostHolder> {

    private ArrayList<String> imageList;

    public ProfilRecycleAdapter(ArrayList<String> imageList) {
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ProfilRecycleAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.receycler_profil,parent,false);

        return new ProfilRecycleAdapter.PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        //fotoğraflarımızı görüntüleyebilmek için picasso yapısını kullandık.
        Picasso.get().load(imageList.get(position)).into(holder.imageView);
    }


    @Override
    public int getItemCount() {

        //kaç tane postumuz olduğunu hesapladık.
        return imageList.size();
    }

    class PostHolder extends RecyclerView.ViewHolder{

        ImageView imageView;

        public PostHolder(@NonNull View itemView) {
            super(itemView);

            //recycle daki text ve image e ekleme yapabilmek için tanımladık.
            imageView=itemView.findViewById(R.id.reycyleImageProfil);

        }
    }
}
