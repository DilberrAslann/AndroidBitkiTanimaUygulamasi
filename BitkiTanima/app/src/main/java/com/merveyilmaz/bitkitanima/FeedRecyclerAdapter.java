package com.merveyilmaz.bitkitanima;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FeedRecyclerAdapter extends RecyclerView.Adapter<FeedRecyclerAdapter.PostHolder> {

    private ArrayList<String> imageList;
    private ArrayList<String> commentList;
    private ArrayList<String> emailList;
    private ArrayList<Timestamp> dateList;


    //yapıcı metot oluşturduk.
    public FeedRecyclerAdapter(ArrayList<String> imageList, ArrayList<String> commentList, ArrayList<String> emailList,ArrayList<Timestamp> dateList) {
        this.imageList = imageList;
        this.commentList = commentList;
        this.emailList = emailList;
        this.dateList = dateList;

    }


    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Eklediğimiz postları blog da gösterebilmek için tanımlama yaptık.
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.recycler_row,parent,false);

        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {

        //postta verilerimizi göstermek için holdera ekliyoruz.
        holder.commentText.setText(commentList.get(position));
        holder.emailText.setText("   "+emailList.get(position));
        holder.dateText.setText((dateList.get(position)).toDate().toString());

        //fotoğraflarımızı görüntüleyebilmek için picasso yapısını kullandık.
        Picasso.get().load(imageList.get(position)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {

        //kaç tane postumuz olduğunu hesapladık.
        return commentList.size();
    }

    class PostHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView emailText,commentText,dateText;

        public PostHolder(@NonNull View itemView) {
            super(itemView);

            //recycle daki text ve image e ekleme yapabilmek için tanımladık.
            imageView=itemView.findViewById(R.id.reycyleImage);
            emailText=itemView.findViewById(R.id.emailRecycleText);
            commentText=itemView.findViewById(R.id.commentRecycleText);
            dateText=itemView.findViewById(R.id.dateRecycleText);
        }
    }
}
