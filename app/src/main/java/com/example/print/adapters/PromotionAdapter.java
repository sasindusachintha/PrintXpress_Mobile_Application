package com.example.print.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.print.R;
import com.example.print.models.Promotion;
import java.util.ArrayList;
import java.util.List;

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.PromoViewHolder> {

    private List<Promotion> promotions = new ArrayList<>();

    public void setPromotions(List<Promotion> promotions) {
        this.promotions = promotions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PromoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_promotion, parent, false);
        return new PromoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PromoViewHolder holder, int position) {
        Promotion promo = promotions.get(position);
        holder.title.setText(promo.getTitle());
        // Using local drawable resource instead of Base64
        if (promo.getImageResId() != 0) {
            holder.image.setImageResource(promo.getImageResId());
        } else {
            holder.image.setImageResource(R.drawable.ic_placeholder_image);
        }
    }

    @Override
    public int getItemCount() {
        return promotions.size();
    }

    static class PromoViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;

        public PromoViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.promo_image);
            title = itemView.findViewById(R.id.promo_title);
        }
    }
}
