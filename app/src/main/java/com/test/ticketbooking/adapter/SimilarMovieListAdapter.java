package com.test.ticketbooking.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.test.ticketbooking.MovieDetails;
import com.test.ticketbooking.R;
import com.test.ticketbooking.models.MasterData;

import java.util.ArrayList;


public class SimilarMovieListAdapter extends RecyclerView.Adapter<SimilarMovieListAdapter.CandidateViewHolder> {

    Context context;
    ArrayList<MasterData> movieData;

    public SimilarMovieListAdapter(Context context, ArrayList<MasterData> articles) {
        this.context = context;
        this.movieData = articles;
    }

    @NonNull
    @Override
    public SimilarMovieListAdapter.CandidateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_similar_movie, parent, false);
        return new  CandidateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimilarMovieListAdapter.CandidateViewHolder holder, final int position) {
        holder.tv_title.setText(movieData.get(position).getTitle());

        RequestOptions rq = new RequestOptions().placeholder(android.R.drawable.stat_sys_download);

        if (movieData.get(position).getPoster_path()!=null)
        {
            String url ="https://image.tmdb.org/t/p/w500/"+movieData.get(position).getPoster_path();

            Glide.with(context)
                    .load(url)
                    .override(500, 600)
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

        TextView tv_title;
        ImageView iv_img;
        View view;

        public CandidateViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);
            iv_img = itemView.findViewById(R.id.iv_img);
            view = itemView;

        }
    }


}
