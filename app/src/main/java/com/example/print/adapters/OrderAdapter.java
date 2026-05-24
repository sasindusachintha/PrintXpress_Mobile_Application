package com.example.print.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.print.R;
import com.example.print.models.PrintOrder;
import com.example.print.utils.Base64Utils;
import com.google.android.material.button.MaterialButton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<PrintOrder> orders = new ArrayList<>();
    private OnOrderActionListener actionListener;

    public interface OnOrderActionListener {
        void onCancelOrder(PrintOrder order);
        void onRescheduleOrder(PrintOrder order);
    }

    public void setOnOrderActionListener(OnOrderActionListener listener) {
        this.actionListener = listener;
    }

    public void setOrders(List<PrintOrder> orders) {
        if (orders == null) {
            this.orders = new ArrayList<>();
        } else {
            this.orders = orders;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        PrintOrder order = orders.get(position);
        Context context = holder.itemView.getContext();

        holder.categoryName.setText(order.getCategory());
        holder.designName.setText(order.getDesignName());
        holder.status.setText(order.getStatus());
        holder.qty.setText(String.format(Locale.getDefault(), "Qty: %s", order.getQuantity()));

        // Status Styling
        if ("Pending".equalsIgnoreCase(order.getStatus())) {
            holder.status.setTextColor(Color.parseColor("#FF9800"));
            holder.actionsContainer.setVisibility(View.VISIBLE);
        } else if ("Processing".equalsIgnoreCase(order.getStatus())) {
            holder.status.setTextColor(Color.parseColor("#2196F3"));
            holder.actionsContainer.setVisibility(View.GONE);
        } else if ("Completed".equalsIgnoreCase(order.getStatus())) {
            holder.status.setTextColor(Color.parseColor("#4CAF50"));
            holder.actionsContainer.setVisibility(View.GONE);
        } else if ("Cancelled".equalsIgnoreCase(order.getStatus())) {
            holder.status.setTextColor(Color.RED);
            holder.actionsContainer.setVisibility(View.GONE);
        } else {
            holder.status.setTextColor(Color.BLACK);
            holder.actionsContainer.setVisibility(View.GONE);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
        String dateStr = sdf.format(new Date(order.getTimestamp()));
        if (order.getRescheduleDate() != null && !order.getRescheduleDate().isEmpty()) {
            dateStr += "\n(Rescheduled: " + order.getRescheduleDate() + ")";
        }
        holder.date.setText(dateStr);

        if (order.getImageBase64() != null && !order.getImageBase64().isEmpty()) {
            holder.image.setImageBitmap(Base64Utils.decode(order.getImageBase64()));
        } else {
            holder.image.setImageResource(R.drawable.ic_placeholder_image);
        }

        holder.btnCancel.setOnClickListener(v -> {
            if (actionListener != null) actionListener.onCancelOrder(order);
        });

        holder.btnReschedule.setOnClickListener(v -> {
            if (actionListener != null) actionListener.onRescheduleOrder(order);
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView categoryName, designName, status, date, qty;
        View actionsContainer;
        MaterialButton btnCancel, btnReschedule;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.order_design_image);
            categoryName = itemView.findViewById(R.id.order_category);
            designName = itemView.findViewById(R.id.order_design_name);
            status = itemView.findViewById(R.id.order_status);
            date = itemView.findViewById(R.id.order_date);
            qty = itemView.findViewById(R.id.order_qty);
            actionsContainer = itemView.findViewById(R.id.order_actions);
            btnCancel = itemView.findViewById(R.id.btn_cancel_order);
            btnReschedule = itemView.findViewById(R.id.btn_reschedule);
        }
    }
}
