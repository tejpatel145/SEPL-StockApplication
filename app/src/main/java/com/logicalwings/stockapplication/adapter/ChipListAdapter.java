package com.logicalwings.stockapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.logicalwings.stockapplication.R;
import com.logicalwings.stockapplication.model.AddTypeData;
import com.logicalwings.stockapplication.model.DeleteItemListener;

import java.util.List;

public class ChipListAdapter extends RecyclerView.Adapter<ChipListAdapter.ViewHolder> {

    private Context context;
    private List<AddTypeData> filterTypeDatas;
    private final DeleteItemListener deleteItemListener;

    public ChipListAdapter(List<AddTypeData> list, DeleteItemListener deleteItemListener) {
        this.filterTypeDatas = list;
        this.deleteItemListener = deleteItemListener;
    }

    public void setFilterChips(List<AddTypeData> filterTypeData) {
        this.filterTypeDatas = filterTypeData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_chip, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final AddTypeData filterTypeData = filterTypeDatas.get(position);

        if (filterTypeData.getValue() != null && !filterTypeData.getValue().isEmpty()) {

            switch (filterTypeData.getKey()) {
                case 0:
                    holder.tvChip.setText(filterTypeData.getDetail() + " : " + filterTypeData.getValue());
                    break;
                case 1:
                    holder.tvChip.setText(filterTypeData.getDetail() + " : " + filterTypeData.getValue());
                    break;
                case 2:
                    holder.tvChip.setText(filterTypeData.getDetail() + " : " + filterTypeData.getValue());
                    break;
                case 3:
                    holder.tvChip.setText(filterTypeData.getDetail() + " : " + filterTypeData.getValue());
                    break;
                case 4:
                    holder.tvChip.setText(filterTypeData.getDetail() + " : " + filterTypeData.getValue());
                    break;
                case 5:
                    holder.tvChip.setText(filterTypeData.getDetail() + " : " + filterTypeData.getValue());
                    break;
                case 6:
                    holder.tvChip.setText(filterTypeData.getDetail() + " : " + filterTypeData.getValue());
                    break;
                case 7:
                    holder.tvChip.setText(filterTypeData.getDetail() + " : " + filterTypeData.getValue());
                    break;
                case 8:
                    holder.tvChip.setText(filterTypeData.getDetail() + " : " + filterTypeData.getValue());
                    break;
                case 9:
                    holder.tvChip.setText(filterTypeData.getDetail() + " : " + filterTypeData.getValue());
                    break;
                case 10:
                    holder.tvChip.setText(filterTypeData.getDetail() + " : " + filterTypeData.getValue());
                    break;
            }
        }

        holder.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deleteItemListener != null) {
                    deleteItemListener.onDelete(filterTypeData.getKey());
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return filterTypeDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvChip;
        private ImageView ivClose;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChip = itemView.findViewById(R.id.tvChip);
            ivClose = itemView.findViewById(R.id.ivClose);
        }
    }
}
