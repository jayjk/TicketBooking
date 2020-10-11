package com.test.ticketbooking.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.test.ticketbooking.MovieDetails;
import com.test.ticketbooking.R;
import com.test.ticketbooking.models.MasterData;

import java.util.ArrayList;


public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.CandidateViewHolder> {

    Context context;
    ArrayList<MasterData> movieData;

    public MovieListAdapter(Context context, ArrayList<MasterData> articles) {
        this.context = context;
        this.movieData = articles;
    }

    @NonNull
    @Override
    public MovieListAdapter.CandidateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new  CandidateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListAdapter.CandidateViewHolder holder, final int position) {
        holder.tv_title.setText(movieData.get(position).getTitle());
        holder.tv_date.setText("Release Date: "+movieData.get(position).getRelease_date());
        holder.btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,context.getResources().getString(R.string.coming_soon),Toast.LENGTH_SHORT).show();
            }
        });

        RequestOptions rq = new RequestOptions().placeholder(android.R.drawable.stat_sys_download);

        if (movieData.get(position).getPoster_path()!=null)
        {
            String url ="https://image.tmdb.org/t/p/w500/"+movieData.get(position).getPoster_path();

            Glide.with(context)
                    .load(url)
                    .override(300, 400)
                    .apply(rq)
                    .into(holder.iv_img);
        }



        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent i = new Intent(context, MovieDetails.class);
                    i.setAction(Intent.ACTION_SEND);
                    i.putExtra("id",movieData.get(position));
                    context.startActivity(i);


            }
        });

    }


    @Override
    public int getItemCount() {
        return movieData.size();
    }

    public class CandidateViewHolder extends RecyclerView.ViewHolder{

        TextView tv_title,tv_date;
        ImageView iv_img;
        Button btn_book;
        View view;

        public CandidateViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);
            iv_img = itemView.findViewById(R.id.iv_img);
            tv_date =  itemView.findViewById(R.id.tv_date);

            btn_book = itemView.findViewById(R.id.btn_book);

            view = itemView;

        }
    }


}
