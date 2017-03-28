package com.eightbytestech.moviesplusv2.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.eightbytestech.moviesplusv2.R;

public class TrailerViewHolder extends RecyclerView.ViewHolder {

    ImageButton trailerImageButton;

    public TrailerViewHolder(View itemView) {
        super(itemView);
        this.trailerImageButton = (ImageButton) itemView.findViewById(R.id.movieTrailer);
    }
}
