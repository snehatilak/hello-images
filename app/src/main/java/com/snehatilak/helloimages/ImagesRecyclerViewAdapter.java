package com.snehatilak.helloimages;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class ImagesRecyclerViewAdapter extends RecyclerView.Adapter<ImagesRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Image> imageList = new ArrayList<>();
    private Context context;

    public ImagesRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.images_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageCaptionView.setText(imageList.get(position).getCaption().toUpperCase());
        Glide.with(context).asBitmap().load(imageList.get(position).getImageUrl()).into(holder.imageView);
        holder.imageParentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ImageFullscreenActivity.class);
                intent.putExtra(v.getContext().getString(R.string.image_caption), imageList.get(position).getCaption());
                intent.putExtra(v.getContext().getString(R.string.image_url), imageList.get(position).getImageUrl());
                v.getContext().startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public void setImageList(ArrayList<Image> imageList) {
        this.imageList = imageList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private MaterialCardView imageParentView;
        private TextView imageCaptionView;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageParentView = itemView.findViewById(R.id.card_image_parent);
            imageCaptionView = itemView.findViewById(R.id.image_caption);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
