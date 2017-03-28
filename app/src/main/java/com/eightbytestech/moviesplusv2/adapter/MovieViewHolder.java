package com.eightbytestech.moviesplusv2.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.eightbytestech.moviesplusv2.R;

/**
 * Created by Home on 01/02/2017.
 */

public class MovieViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;

    public MovieViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.imageView);
    }
}
