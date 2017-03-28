package com.eightbytestech.moviesplusv2.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.eightbytestech.moviesplusv2.R;
import com.eightbytestech.moviesplusv2.model.Review;
import com.eightbytestech.moviesplusv2.model.Trailer;
import com.eightbytestech.moviesplusv2.utility.ApiUtility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vishal on 28/3/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerViewHolder> {

    List<Trailer> trailerList;
    private LayoutInflater mLayouInflater;
    private Context mContext;

    public TrailerAdapter(Context context) {
        mContext = context;
        mLayouInflater = LayoutInflater.from(context);
    }

    public void setTrailerList(List<Trailer> trailers) {
        if ( trailers != null ) {
            this.trailerList = new ArrayList<>();
            this.trailerList.addAll(trailers);
            notifyDataSetChanged();
        }
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayouInflater.inflate(R.layout.trailer_list_content, parent, false);
        final TrailerViewHolder trailerViewHolder = new TrailerViewHolder(view);
        return trailerViewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        final Trailer trailer = trailerList.get(position);
        final String trailerIdString = trailer.key;
        ImageButton imageButton = holder.trailerImageButton;
        Picasso.with(mContext)
                .load(ApiUtility.YoutubeUtility.getPosterUrlFromTrailerId(trailerIdString))
                .error(R.drawable.trailer_thumbnail_placeholder)
                .into(imageButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(ApiUtility.YoutubeUtility.getTrailerUrlFromTrailerId(trailerIdString))));
                //Toast.makeText(mContext, "Selected trailer: " + trailer.name, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (trailerList == null) {
            return 0;
        } else {
            return trailerList.size();
        }
    }
}
