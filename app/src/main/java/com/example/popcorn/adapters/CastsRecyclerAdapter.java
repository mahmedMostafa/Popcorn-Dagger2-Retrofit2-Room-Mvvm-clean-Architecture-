package com.example.popcorn.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.popcorn.adapters.viewholders.CastsViewHolder;
import com.example.popcorn.databinding.CastListItemBinding;
import com.example.popcorn.models.api.Cast;

import java.util.List;

public class CastsRecyclerAdapter extends RecyclerView.Adapter<CastsViewHolder> {

    private RequestManager requestManager;
    private List<Cast> casts;

    public CastsRecyclerAdapter(RequestManager requestManager, List<Cast> casts) {
        this.requestManager = requestManager;
        this.casts = casts;
    }

    @NonNull
    @Override
    public CastsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CastListItemBinding binding = CastListItemBinding.inflate(inflater, parent, false);
        return new CastsViewHolder(binding, requestManager);
    }

    @Override
    public void onBindViewHolder(@NonNull CastsViewHolder holder, int position) {
        holder.onBind(casts.get(position));
    }

    public void setCasts(List<Cast> casts){
        this.casts = casts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return casts.size();
    }
}
