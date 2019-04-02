package com.logicalwings.stockapplication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.logicalwings.stockapplication.R;
import com.logicalwings.stockapplication.model.StockData;
import com.logicalwings.stockapplication.model.UpdateDrumListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {

    private Context context;
    private List<StockData> stockData;
    private final UpdateDrumListener updateDrumListener;

    public SearchListAdapter(Context context, UpdateDrumListener updateDrumListener) {
        this.context = context;
        this.stockData = new ArrayList<>();
        this.updateDrumListener = updateDrumListener;
    }

    public void setSearchDatas(List<StockData> searchDatas) {
        this.stockData.clear();
        this.stockData.addAll(searchDatas);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_search_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchListAdapter.ViewHolder viewHolder, final int position) {
        final StockData data = stockData.get(position);
        if (data.getDrumNo() != null && data.getDrumNo().trim().length() > 0) {

            String deviceName = data.getDrumNo();
            String[] fNameArray = deviceName.split(" ");
            StringBuilder capitalString = new StringBuilder();

            for (String s : fNameArray) {
                String upperCase = s.substring(0, 1).toUpperCase() + s.substring(1);
                capitalString.append(upperCase + " ");
            }

            viewHolder.tvDrumNo.setText(capitalString.toString());
        }
        viewHolder.tvSize.setText(data.getSize() != null && !data.getSize().isEmpty() ? data.getSize() : context.getString(R.string.na));
        String sTime = "";

        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        //"2018-07-24T13:49:00"
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

        if (data.getDate() != null) {

            Date date = null;
            try {
                date = format.parse(data.getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();

            if (date != null)
                calendar.setTime(date);

            sTime = (sdf.format(calendar.getTime()));
        }
        viewHolder.tvMeter.setText(data.getQtyMeter() != null ? String.valueOf(data.getQtyMeter()) : context.getString(R.string.na));
        viewHolder.tvUpdate.setText(data.getUpdate() != null && !data.getUpdate().isEmpty() ? data.getUpdate() : context.getString(R.string.na));
        if (data.getUpdate() != null && !data.getUpdate().isEmpty()) {
            viewHolder.llParent.setBackgroundResource(R.drawable.item_updateed_background);
            viewHolder.llDrumno.setBackgroundColor(Color.parseColor("#3CB371"));
         //   viewHolder.llParent.setPadding(10,10,10,10);
        }else {
            viewHolder.llParent.setBackgroundResource(R.drawable.item_background);
            viewHolder.llDrumno.setBackgroundColor(Color.parseColor("#9aa3ab"));
        //    viewHolder.llParent.setPadding(10,10,10,10);
        }

        viewHolder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (updateDrumListener != null) {
                    updateDrumListener.onEdit(data);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return stockData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llParent;
        LinearLayout llDrumno;
        TextView tvDrumNo;
        TextView tvSize;
        TextView tvDate;
        TextView tvJobno;
        TextView tvMeter;
        TextView tvUpdate;
        ImageView ivEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            llParent = itemView.findViewById(R.id.llParent);
            llDrumno = itemView.findViewById(R.id.llDrumno);
            tvDrumNo = itemView.findViewById(R.id.tvDrumNo);
            tvSize = itemView.findViewById(R.id.tvSize);
            tvMeter = itemView.findViewById(R.id.tvMeter);
            tvUpdate = itemView.findViewById(R.id.tvUpdate);
            ivEdit = itemView.findViewById(R.id.ivEdit);
        }
    }
}
