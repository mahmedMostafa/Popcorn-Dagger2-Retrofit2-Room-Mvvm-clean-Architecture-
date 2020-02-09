package com.example.popcorn.adapters.viewholders;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.popcorn.databinding.CastListItemBinding;
import com.example.popcorn.models.api.Cast;

public class CastsViewHolder extends RecyclerView.ViewHolder {

    private RequestManager requestManager;
    private CastListItemBinding binding;

    public CastsViewHolder(CastListItemBinding binding , RequestManager requestManager) {
        super(binding.getRoot());
        this.binding = binding;
        this.requestManager = requestManager;
    }

    public void onBind(Cast cast){
        requestManager.load(cast.getProfilePath()).into(binding.castImageView);
        binding.castCharTextView.setText(cast.getCharacter());
        binding.castNameTextView.setText(cast.getName());
    }
}
