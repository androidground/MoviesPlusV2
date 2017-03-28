package com.eightbytestech.moviesplusv2.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.eightbytestech.moviesplusv2.R;

/**
 * Created by vishal on 28/3/17.
 */

public class ReviewViewHolder extends RecyclerView.ViewHolder {

    TextView authorTextView;
    TextView reviewTextView;

    public ReviewViewHolder(View itemView) {
        super(itemView);
        this.authorTextView = (TextView) itemView.findViewById(R.id.authorTextView);
        this.reviewTextView = (TextView) itemView.findViewById(R.id.reviewTextView);
    }
}
