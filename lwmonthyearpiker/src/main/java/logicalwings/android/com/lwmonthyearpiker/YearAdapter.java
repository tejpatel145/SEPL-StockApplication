package logicalwings.android.com.lwmonthyearpiker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by priyansh on 03/01/18.
 */

public class YearAdapter extends RecyclerView.Adapter<YearAdapter.YearHolder> {

    private List<String> years;
    private OnSelectedListener listener;
    private Context context;
    private int color;
    private String selectedYear;

    public YearAdapter(Context context, OnSelectedListener listener, int minYear, int maxYear, int selectedYear) {
        this.context = context;
        this.listener = listener;
        this.years = new ArrayList<>();
        this.selectedYear = String.valueOf(selectedYear);
        while (minYear <= maxYear) {
            years.add(String.valueOf(minYear));
            minYear++;
        }
    }

    @Override
    public YearHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        YearHolder monthHolder = new YearHolder(LayoutInflater.from(context).inflate(R.layout.item_view_year, parent, false));
        return monthHolder;
    }

    @Override
    public void onBindViewHolder(YearHolder holder, int position) {
        holder.textViewMonth.setText(years.get(position));
        holder.textViewMonth.setTextColor(selectedYear.equals(years.get(position)) ? Color.WHITE : Color.BLACK);
        holder.itemView.setSelected(selectedYear.equals(years.get(position)));
    }

    @Override
    public int getItemCount() {
        return years.size();
    }

    public int getSelectedPosition() {
        if (years.contains(selectedYear)) {
            return years.indexOf(selectedYear);
        }
        return years.size() -1;
    }

    public void setSelectedYear(int selectedYear) {
        this.selectedYear = String.valueOf(selectedYear);
        notifyDataSetChanged();
    }

    public void setBackgroundMonth(int color) {
        this.color = color;
    }

    public void setColor(int color) {
        this.color = color;
        notifyDataSetChanged();
    }

    public int getYear() {
        return Integer.parseInt(selectedYear);
    }

    class YearHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout layoutMain;
        TextView textViewMonth;

        public YearHolder(View itemView) {
            super(itemView);
            layoutMain = (LinearLayout) itemView.findViewById(R.id.main_layout);
            textViewMonth = (TextView) itemView.findViewById(R.id.text_month);
            if (color != 0)
                setMonthBackgroundSelected(color);

            itemView.setClickable(true);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int selectedItem = getAdapterPosition();
            selectedYear = years.get(selectedItem);
            notifyDataSetChanged();
            listener.onContentSelected(Integer.valueOf(selectedYear));
        }

        private void setMonthBackgroundSelected(int color) {
            LayerDrawable layerDrawable = (LayerDrawable) ContextCompat.getDrawable(context, R.drawable.month_selected);
            GradientDrawable gradientDrawable = (GradientDrawable) layerDrawable.getDrawable(1);
            gradientDrawable.setColor(ContextCompat.getColor(context, color));
            layerDrawable.setDrawableByLayerId(1, gradientDrawable);

            StateListDrawable states = new StateListDrawable();
            states.addState(new int[]{android.R.attr.state_selected}, gradientDrawable);
            states.addState(new int[]{}, ContextCompat.getDrawable(context, R.drawable.month_default));
            layoutMain.setBackground(states);
        }
    }

    interface OnSelectedListener {
        void onContentSelected(int year);
    }
}
