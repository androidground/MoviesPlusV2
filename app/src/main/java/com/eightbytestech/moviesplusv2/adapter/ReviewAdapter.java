package com.eightbytestech.moviesplusv2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eightbytestech.moviesplusv2.R;
import com.eightbytestech.moviesplusv2.model.Movie;
import com.eightbytestech.moviesplusv2.model.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vishal on 28/3/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder> {

    List<Review> reviewList;
    private LayoutInflater mLayouInflater;
    private Context mContext;

    public ReviewAdapter(Context context) {
        mContext = context;
        mLayouInflater = LayoutInflater.from(context);
    }

    public void setReviewList(List<Review> reviews) {
        if ( reviews != null ) {
            this.reviewList = new ArrayList<>();
            this.reviewList.addAll(reviews);
            notifyDataSetChanged();
        }
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayouInflater.inflate(R.layout.review_list_content, parent, false);
        final ReviewViewHolder reviewViewHolder = new ReviewViewHolder(view);
        return reviewViewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.authorTextView.setText(review.author);
        holder.reviewTextView.setText(review.content);
    }

    @Override
    public int getItemCount() {
        if (reviewList == null) {
            return 0;
        } else {
            return reviewList.size();
        }
    }
}
