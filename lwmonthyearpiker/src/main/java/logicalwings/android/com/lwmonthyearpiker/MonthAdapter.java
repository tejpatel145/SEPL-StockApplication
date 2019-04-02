package logicalwings.android.com.lwmonthyearpiker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by priyansh on 03/01/18.
 */

public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.MonthHolder> {

    private String[] months = new String[0];
    private OnSelectedListener listener;
    private int selectedItem = -1;
    private Context context;
    private int color;
    private boolean isDisplayFutureMonth;

    public MonthAdapter(Context context, boolean isDisplayFutureMonth, OnSelectedListener listener) {
        this.context = context;
        this.listener = listener;
        this.isDisplayFutureMonth = isDisplayFutureMonth;
        months = new DateFormatSymbols(Locale.ENGLISH).getShortMonths();

        if (selectedItem == -1) {
            Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            selectedItem = cal.get(Calendar.MONTH);
        }
    }

    @Override
    public MonthHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MonthHolder monthHolder = new MonthHolder(LayoutInflater.from(context).inflate(R.layout.item_view_month, parent, false));
        return monthHolder;
    }

    @Override
    public void onBindViewHolder(MonthHolder holder, int position) {
        holder.textViewMonth.setText(months[position]);
        holder.textViewMonth.setTextColor(selectedItem == position ? Color.WHITE : Color.BLACK);
        holder.itemView.setSelected(selectedItem == position ? true : false);
    }

    @Override
    public int getItemCount() {
        return months.length;
    }

    public void setLocale(Locale locale) {
        months = new DateFormatSymbols(locale).getShortMonths();
        notifyDataSetChanged();
    }

    public void setSelectedYear(int year) {


        if (!isDisplayFutureMonth) {
            return;
        }

        Calendar cal = Calendar.getInstance();

        if (year == cal.get(Calendar.YEAR)){

            String[] m = new DateFormatSymbols(Locale.ENGLISH).getShortMonths();

            months = new String[(cal.get(Calendar.MONTH)+ 1 )];

            for (int i = 0; i <= cal.get(Calendar.MONTH); i++) {
                months[i] = m[i];
            }

            selectedItem = months.length - 1;
        } else {

            months = new DateFormatSymbols(Locale.ENGLISH).getShortMonths();
        }

        notifyDataSetChanged();
    }

    public void setSelectedItem(int index) {
        if (index < 12 || index > -1) {
            selectedItem = index;
            notifyItemChanged(selectedItem);
        }
    }

    public void setBackgroundMonth(int color) {
        this.color = color;
    }

    public void setColor(int color) {
        this.color = color;
        notifyDataSetChanged();
    }

    public int getMonth() {
        return selectedItem;
    }

    public String getShortMonth() {
        if (selectedItem < 0 || selectedItem >= months.length)
            selectedItem = months.length-1;
        return months[selectedItem];
    }

    class MonthHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout layoutMain;
        TextView textViewMonth;

        public MonthHolder(View itemView) {
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
            notifyItemChanged(selectedItem);
            selectedItem = getAdapterPosition();
            notifyItemChanged(selectedItem);
            listener.onContentSelected(selectedItem);
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
        void onContentSelected(int month);
    }

    public int getStartDate() {
        return 1;
    }

    public int getEndDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, selectedItem + 1);
        cal.set(Calendar.DAY_OF_MONTH, selectedItem + 1);
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return maxDay;
    }
}
