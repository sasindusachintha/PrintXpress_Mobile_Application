package com.example.print.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.print.R;
import com.example.print.models.SavedDesign;
import com.example.print.utils.Base64Utils;
import java.util.ArrayList;
import java.util.List;

public class SavedDesignAdapter extends RecyclerView.Adapter<SavedDesignAdapter.DesignViewHolder> {

    private List<SavedDesign> designs = new ArrayList<>();
    private OnDesignClickListener listener;

    public interface OnDesignClickListener {
        void onDesignClick(SavedDesign design);
    }

    public SavedDesignAdapter(OnDesignClickListener listener) {
        this.listener = listener;
    }

    public void setDesigns(List<SavedDesign> designs) {
        this.designs = designs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DesignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_saved_design, parent, false);
        return new DesignViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DesignViewHolder holder, int position) {
        SavedDesign design = designs.get(position);
        holder.name.setText(design.getName());
        if (design.getImageBase64() != null && !design.getImageBase64().isEmpty()) {
            holder.image.setImageBitmap(Base64Utils.decode(design.getImageBase64()));
        }
        holder.itemView.setOnClickListener(v -> listener.onDesignClick(design));
    }

    @Override
    public int getItemCount() {
        return designs.size();
    }

    static class DesignViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;

        public DesignViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.iv_saved_design);
            name = itemView.findViewById(R.id.tv_saved_design_name);
        }
    }
}
