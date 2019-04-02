package com.logicalwings.stockapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.logicalwings.stockapplication.base.BaseActivity;
import com.logicalwings.stockapplication.model.AddTypeData;
import com.logicalwings.stockapplication.utils.AppConstance;
import com.logicalwings.stockapplication.utils.AppUtils;
import com.logicalwings.stockapplication.utils.PrefrenceConnector;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FilterActivity extends BaseActivity {
    private static final String KEY_STOCK_TYPE_LIST = "typeList";
    private static final String KEY_SELECTED_POSITION = "selectedposition";
    private static final String KEY_FILTER_MONTH = "month";
    private static final String KEY_FILTER_DAY = "day";
    private static final String KEY_FILTER_YEAR = "year";
    private static final String KEY_FILTER_FORMATTED_DATE = "formattedDate";


    public static final int FILTER_DATE_KEY = 2;


    private Button btnOk;
    private Button btnCancel;

    private TextView tvTitle;

    private ImageView ivClose;

    private RecyclerView rvList;

    private int mYear = -1;
    private int mMonth = -1;
    private int mDay = -1;
    private FilterTypeAdapter adapter;
    private int selectedposition;

    private DatePickerDialog dpdDay;
    private String formattedDate;
    private String sDate;
    private String sStockType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        final IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

        receiver = new NetworkChangeReceiver();

        registerReceiver(receiver, filter);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        initData();

        if (savedInstanceState != null) {

            if (savedInstanceState.containsKey(KEY_STOCK_TYPE_LIST)) {
                appData.setAddTypeDatas(savedInstanceState.<AddTypeData>getParcelableArrayList(KEY_STOCK_TYPE_LIST));
            }
            if (savedInstanceState.containsKey(KEY_SELECTED_POSITION)) {
                selectedposition = savedInstanceState.getInt(KEY_SELECTED_POSITION);
            }
            if (savedInstanceState.containsKey(KEY_FILTER_MONTH)) {
                mMonth = savedInstanceState.getInt(KEY_FILTER_MONTH);
            }
            if (savedInstanceState.containsKey(KEY_FILTER_DAY)) {
                mDay = savedInstanceState.getInt(KEY_FILTER_DAY);
            }
            if (savedInstanceState.containsKey(KEY_FILTER_YEAR)) {
                mYear = savedInstanceState.getInt(KEY_FILTER_YEAR);
            }
            if (savedInstanceState.containsKey(KEY_FILTER_FORMATTED_DATE)) {
                formattedDate = savedInstanceState.getString(KEY_FILTER_FORMATTED_DATE);
            }
        }

        if (appData.getAddTypeDatas() == null || appData.getAddTypeDatas().size() == 0) {

            if (sStockType.equals("finish")) {

                appData.setAddTypeDatas(AppUtils.createFinishList(this));

            }
            if (sStockType.equals("inprocess")) {

                appData.setAddTypeDatas(AppUtils.createInProcessList(this));

            }

        }

        for (int i = 0; i < appData.getAddTypeDatas().size(); i++) {

            if (appData.getAddTypeDatas().get(i).getKey() == FILTER_DATE_KEY &&
                    appData.getAddTypeDatas().get(i).getValue() != null &&
                    !appData.getAddTypeDatas().get(i).getValue().isEmpty()) {
                sDate = appData.getAddTypeDatas().get(i).getValue();

                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
                Calendar save_date = Calendar.getInstance();

                Date myDate = null;
                try {
                    myDate = dateFormat.parse(sDate);
                    save_date.setTime(myDate);

                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }

                mDay = save_date.get(Calendar.DAY_OF_MONTH);
                mMonth = save_date.get(Calendar.MONTH);
                mYear = save_date.get(Calendar.YEAR);
                break;
            }
        }


        initUi();
    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();

        sStockType = intent.getStringExtra(AppConstance.SELECTED_STOCKTYPE_KEY);
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


    }

    @Override
    public void initUi() {

        super.initUi();

        tvTitle = findViewById(R.id.tvTitle);

        ivClose = findViewById(R.id.ivClose);

        rvList = findViewById(R.id.rvList);

        btnCancel = findViewById(R.id.btnCancel);

        btnOk = findViewById(R.id.btnOk);

        tvTitle.setText(getString(R.string.select_filter));


        adapter = new FilterTypeAdapter(appData.getAddTypeDatas(), new FilterTypeAdapter.CallBackListener() {
            @Override
            public void onSelect(AddTypeData addTypeData) {
                openDialogView(addTypeData);
            }

            @Override
            public void onClear(AddTypeData addTypeData) {

                addTypeData.setValue(null);
                addTypeData.setChecked(false);
                if (addTypeData.getKey() == FILTER_DATE_KEY) {
                    mYear = -1;
                    mMonth = -1;
                    mDay = -1;
                }

                adapter.notifyDataSetChanged();
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvList.setLayoutManager(mLayoutManager);
        rvList.setItemAnimator(new DefaultItemAnimator());
        rvList.setAdapter(adapter);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                finish();
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (appData !=null && appData.getAddTypeDatas() != null)
            outState.putParcelableArrayList(KEY_STOCK_TYPE_LIST, appData.getAddTypeDatas());

        outState.putInt(KEY_SELECTED_POSITION, selectedposition);
        outState.putString(KEY_FILTER_FORMATTED_DATE, formattedDate);
        outState.putInt(KEY_FILTER_MONTH, mMonth);
        outState.putInt(KEY_FILTER_DAY, mDay);
        outState.putInt(KEY_FILTER_YEAR, mYear);
    }

    private Calendar getCalender() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, mDay);
        calendar.set(Calendar.MONTH, mMonth);
        calendar.set(Calendar.YEAR, mYear);
        return calendar;
    }

    private void openDatePickerDialog(final AddTypeData addTypeData) {

        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;

                String format = "MM-dd-yyyy";
                SimpleDateFormat df = new SimpleDateFormat(format, Locale.getDefault());

                formattedDate = df.format(getCalender().getTime());

                if (formattedDate != null) {
                    addTypeData.setValue(formattedDate);
                    addTypeData.setChecked(true);
                    adapter.notifyDataSetChanged();
                }
            }
        };
        if (dpdDay == null) {

            dpdDay = DatePickerDialog.newInstance(onDateSetListener, mYear, mMonth, mDay);
        } else {

            dpdDay.initialize(onDateSetListener, mYear, mMonth, mDay);
        }

        Calendar now = Calendar.getInstance();
        dpdDay.setTitle(getString(R.string.select_day));
        Calendar minDate = Calendar.getInstance();
        minDate.set(Calendar.DAY_OF_MONTH, 1);
        minDate.set(Calendar.MONTH, 0);
        minDate.set(Calendar.YEAR, 2016);
        dpdDay.setMinDate(minDate);
        dpdDay.setMaxDate(now);
        dpdDay.show(getFragmentManager(), "DayDatepickerdialog");

    }

    private void openDialogView(AddTypeData addTypeData) {

        switch (addTypeData.getKey()) {

            case 0:
            case 1:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                openSearchByItem(addTypeData);
                break;
            case 2:
                openDatePickerDialog(addTypeData);
                break;

        }
    }

    private void openSearchByItem(final AddTypeData addTypeData) {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_layout, null);
        final TextInputEditText etDetail = alertLayout.findViewById(R.id.etDetail);
        final Button btnOk = alertLayout.findViewById(R.id.btnOk);
        final Button btnCancel = alertLayout.findViewById(R.id.btnCancel);
        final TextInputLayout input_layout_detail = alertLayout.findViewById(R.id.input_layout_detail);

        if (addTypeData.getKey() == 4) {
            etDetail.setInputType(InputType.TYPE_CLASS_NUMBER);
            etDetail.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        }

        etDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    input_layout_detail.setError(null);
                }
            }
        });

        if (addTypeData.getValue() != null && !addTypeData.getValue().isEmpty())
            etDetail.setText(addTypeData.getValue());

        input_layout_detail.setHint(addTypeData.getDetail());

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(String.format("Enter %s", addTypeData.getDetail() != null ? addTypeData.getDetail() : ""));
        builder.setView(alertLayout);
        builder.setCancelable(false);

        final AlertDialog dialog = builder.create();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = etDetail.getText().toString().trim();

                if (TextUtils.isEmpty(value)) {
                    input_layout_detail.setError("Please Enter " + addTypeData.getDetail());
                } else {
                    addTypeData.setValue(value);
                    addTypeData.setChecked(true);
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    public static class FilterTypeAdapter extends RecyclerView.Adapter<FilterTypeAdapter.ViewHolder> {

        public interface CallBackListener {
            public void onSelect(AddTypeData addTypeData);

            public void onClear(AddTypeData addTypeData);
        }

        private Context context;
        private List<AddTypeData> typeList;
        private final CallBackListener callBackListener;

        public FilterTypeAdapter(List<AddTypeData> list, CallBackListener callBackListener) {
            this.typeList = list;
            this.callBackListener = callBackListener;
        }

        public void setFilterData(List<AddTypeData> addTypeData) {
            this.typeList = addTypeData;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.item_filter_options, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

            final AddTypeData filterTypeData = typeList.get(position);

            if (typeList.get(position).getValue() == null ||
                    typeList.get(position).getValue().equals("")) {
                holder.ivClear.setVisibility(View.GONE);
            } else {
                holder.ivClear.setVisibility(View.VISIBLE);
            }

            holder.tvType.setText(filterTypeData.getDetail());

            holder.tvSelectedDetail.setText(filterTypeData.getValue());

            holder.isChecked.setChecked(filterTypeData.isChecked());

            holder.llMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (callBackListener != null) {
                        callBackListener.onSelect(filterTypeData);
                    }
                }
            });

            holder.ivClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (callBackListener != null) {
                        callBackListener.onClear(filterTypeData);
                    }
                }
            });


        }

        @Override
        public int getItemCount() {
            return typeList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private CheckBox isChecked;
            private TextView tvType;
            private TextView tvSelectedDetail;
            private ImageView ivClear;
            private LinearLayout llMain;
            private View viewSeprator;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                llMain = itemView.findViewById(R.id.llMain);
                viewSeprator = itemView.findViewById(R.id.viewSeprator);
                isChecked = itemView.findViewById(R.id.isChecked);
                ivClear = itemView.findViewById(R.id.ivClear);
                tvType = itemView.findViewById(R.id.tvType);
                tvSelectedDetail = itemView.findViewById(R.id.tvSelectedDetail);

            }
        }
    }


}
