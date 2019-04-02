package com.logicalwings.stockapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.logicalwings.stockapplication.api.ApiClient;
import com.logicalwings.stockapplication.api.ApiInterFace;
import com.logicalwings.stockapplication.base.BaseActivity;
import com.logicalwings.stockapplication.model.BaseModel;
import com.logicalwings.stockapplication.model.StockData;
import com.logicalwings.stockapplication.utils.AppConstance;
import com.logicalwings.stockapplication.utils.AppUtils;
import com.logicalwings.stockapplication.utils.Toaster;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import logicalwings.android.com.lwmonthyearpiker.MonthPikerDialog;
import logicalwings.android.com.lwmonthyearpiker.listener.DateMonthDialogListener;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddDrumActivity extends BaseActivity implements DateMonthDialogListener {

    private static final int CODE_ADD_OR_UPDATE_DRUM = 202;

    private RadioButton rbInProgress;
    private RadioButton rbFinish;

    private TextInputLayout input_layout_date;
    private TextInputLayout input_layout_comment;
    private TextInputLayout input_layout_update;
    private TextInputLayout input_layout_stage;
    private TextInputLayout input_layout_condType;
    private TextInputLayout input_layout_location;

    private TextInputEditText etDrumNo;
    private TextInputEditText etCabelSize;
    private TextInputEditText etJobNo;
    private TextInputEditText etMeter;
    private TextInputEditText etRemark;
    private TextInputEditText etDate;
    private TextInputEditText etComment;
    private TextInputEditText etStage;
    private TextInputEditText etcondType;
    private TextInputEditText etLocation;
    private EditText etUpdate;
    private TextView tvMonth;

    private Button btnSave;
    private Button btnCancel;

    private MonthPikerDialog mpd;

    private StockData stockData;

    private String sDrumNo;
    private String sCableSize;
    private String sJobNo;
    private String sRemark;
    private String sStockType;
    private String sUpdate;
    private String sMonth;
    private String sCondType;
    private String sLocation;
    private int sMeter;
    private int selectedMode;
    private String formattedDate;
    private int mYear = -1;
    private int mMonth = -1;

    private BroadcastReceiver authentication_broadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AppConstance.DEBUG)
                Log.e(AppConstance.DEBUG_TAG, "AddDrumActivity authentication broadcast call");
            clearData();
            Intent i = new Intent(AddDrumActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drum);

        final IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

        receiver = new NetworkChangeReceiver();

        registerReceiver(receiver, filter);

        initData();

        initUi();

        if (selectedMode == 1) {

            updateUi();
        }

    }

    @Override
    public void initData() {
        super.initData();

        stockData = new StockData();

        selectedMode = getIntent().getIntExtra(AppConstance.DRUM_UPDATE_MODE_KEY, 0);

        if (selectedMode == AppConstance.UPDATE_MODE) {

            stockData = getIntent().getParcelableExtra(AppConstance.SELECTED_DRUM_KEY);

        } else {
            stockData = new StockData();
        }

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
    }

    @Override
    public void initUi() {
        super.initUi();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tvMonth = findViewById(R.id.tvMonth);
        if (selectedMode == AppConstance.UPDATE_MODE) {
            getSupportActionBar().setTitle(R.string.update_drum);
            tvMonth.setVisibility(View.GONE);
        } else {

            getSupportActionBar().setTitle(R.string.add_drum);
        }

        rbInProgress = findViewById(R.id.rbInProgress);
        rbFinish = findViewById(R.id.rbFinish);

        if (rbInProgress.isChecked()) {
            sStockType = rbInProgress.getText().toString().toLowerCase().trim();
        }
        if (rbFinish.isChecked()) {
            sStockType = rbFinish.getText().toString().toLowerCase().trim();
        }

        input_layout_date = findViewById(R.id.input_layout_date);
        input_layout_comment = findViewById(R.id.input_layout_comment);
        input_layout_update = findViewById(R.id.input_layout_update);
        input_layout_condType = findViewById(R.id.input_layout_condType);
        input_layout_stage = findViewById(R.id.input_layout_stage);
        input_layout_location = findViewById(R.id.input_layout_location);

        etDrumNo = findViewById(R.id.etDrumNo);
        etCabelSize = findViewById(R.id.etCabelSize);
        etJobNo = findViewById(R.id.etJobNo);
        etMeter = findViewById(R.id.etMeter);
        etRemark = findViewById(R.id.etRemark);
        etDate = findViewById(R.id.etDate);
        etComment = findViewById(R.id.etcomment);
        etUpdate = findViewById(R.id.etUpdate);
        etStage = findViewById(R.id.etStage);
        etcondType = findViewById(R.id.etcondType);
        etLocation = findViewById(R.id.etLocation);


        SimpleDateFormat df = new SimpleDateFormat("MMM-yyyy", Locale.getDefault());
        formattedDate = df.format(getCalender().getTime());
        tvMonth.setText(formattedDate);

        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);


        tvMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openMonthPickerDialog();

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateSetData();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(authentication_broadcast, new IntentFilter(AppConstance.AUTHENTICATION_CHANGE_BROADCAST));

    }

    @Override
    public void refreshToken(int code) {
        showProgressDialog(getString(R.string.please_wait));
        super.refreshToken(code);
    }

    @Override
    protected void onTokenRefreshSuccess(int code) {
        super.onTokenRefreshSuccess(code);
        if (AppConstance.DEBUG)
            Log.v(AppConstance.DEBUG_TAG, "AddDrumActivity onTokenRefreshSuccess code: " + code);

        if (code == CODE_ADD_OR_UPDATE_DRUM) {
            if (AppUtils.isOnline(this)) {
                addOrUpdateDrum(stockData);
            } else {
                Toaster.popInternetUnavailableToast(this);
            }

        }
    }

    @Override
    protected void onTokenRefreshFail(int code) {
        super.onTokenRefreshFail(code);
        hideProgressDialog();
    }

    @Override
    protected void onTokenRefreshBadResponse(int code) {
        super.onTokenRefreshBadResponse(code);
        hideProgressDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(authentication_broadcast);
        unregisterReceiver(receiver);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private void openMonthPickerDialog() {

        Calendar now = Calendar.getInstance();
        if (mpd == null) {
            mpd = MonthPikerDialog.newInstance(AddDrumActivity.this, mYear, mMonth, R.color.colorPrimary);
        } else {
            mpd.initialize(AddDrumActivity.this, mYear, mMonth, R.color.colorPrimary);
        }
        mpd.setMaxYear(now.get(Calendar.YEAR));
        mpd.setMinYear(2016);
        mpd.show(getFragmentManager(), "monthPicker");
    }

    private Calendar getCalender() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, mMonth);
        calendar.set(Calendar.YEAR, mYear);
        return calendar;
    }

    public void clearData() {
        etDrumNo.setText("");
        etCabelSize.setText("");
        etJobNo.setText("");
        etMeter.setText("");
        etRemark.setText("");
    }

    public void validateSetData() {

        int flag = 1;
        if (etDrumNo.getText().toString().trim().length() == 0) {
            etDrumNo.setError("Enter Drum Number");
            flag = 0;
        } else {
            sDrumNo = etDrumNo.getText().toString().trim();
        }

        if (flag == 0)
            return;

        if (selectedMode != AppConstance.UPDATE_MODE) {

            sCondType = etcondType.getText().toString().trim();


            if (etCabelSize.getText().toString().trim().length() == 0) {
                etCabelSize.requestFocus();
                etCabelSize.setError("Enter Cable Size");
                flag = 0;
            } else {
                sCableSize = etCabelSize.getText().toString().trim();
            }
            if (flag == 0)
                return;
        } else {
            sCableSize = etCabelSize.getText().toString().trim();
        }
        if (input_layout_update.getVisibility() == View.VISIBLE) {

            if (etUpdate.getText().toString().trim().length() == 0) {
                etUpdate.requestFocus();
                etUpdate.setError("Enter Update");
                flag = 0;
            } else {
                sUpdate = etUpdate.getText().toString().trim();
            }
            if (flag == 0)
                return;
        }

        sJobNo = etJobNo.getText().toString().trim();

        if (etMeter.getText().toString().trim().length() != 0)
            sMeter = Integer.parseInt(etMeter.getText().toString().trim());
        else
            sMeter = 0;

        sRemark = etRemark.getText().toString().trim();

        sLocation = etLocation.getText().toString().trim();

        if (rbInProgress.isChecked()) {
            sStockType = rbInProgress.getText().toString().toLowerCase().trim();
        }
        if (rbFinish.isChecked()) {
            sStockType = rbFinish.getText().toString().toLowerCase().trim();
        }

        sMonth = tvMonth.getText().toString().trim();

        StockData data = new StockData();
        if (selectedMode == 1) {
            if (stockData.getSrNo() != null) {
                data.setSrNo(stockData.getSrNo());
                data.setUpdate(sUpdate);
            }
        }
        data.setStockType(sStockType);
        data.setDrumNo(sDrumNo);
        data.setSize(sCableSize);
        data.setJobNo(sJobNo);
        data.setQtyMeter(sMeter);
        data.setLocation(sLocation);
        data.setRemark(sRemark);
        data.setMonth(sMonth);

        addOrUpdateDrum(data);
    }

    public void addOrUpdateDrum(StockData stockData) {

        showProgressDialog(getString(R.string.please_wait));

        Log.d(AppConstance.DEBUG_TAG, "AddDrumActivity addOrUpdateDrum : " + new Gson().toJson(stockData, StockData.class));

        ApiInterFace service = ApiClient.getClient(AddDrumActivity.this);

        service.addOrUpdateStock(stockData)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseModel>() {
                    @Override
                    public void onCompleted() {
                        if (AppConstance.DEBUG)
                            Log.i(AppConstance.DEBUG_TAG, "onCompleted: ");

                        hideProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (AppConstance.DEBUG)
                            Log.i(AppConstance.DEBUG_TAG, "onError: " + e.getMessage());
                        hideProgressDialog();
                        if (((HttpException) e).code() == AppConstance.LOGIN_TOKEN_EXPIRE_CODE) {
                            refreshToken(CODE_ADD_OR_UPDATE_DRUM);
                        }
                    }

                    @Override
                    public void onNext(BaseModel baseModel) {

                        if (baseModel != null) {

                            hideProgressDialog();

                            switch (baseModel.getSuccess()) {

                                case 0:

                                    Toast.makeText(AddDrumActivity.this, baseModel.getMessage(), Toast.LENGTH_SHORT).show();
                                    break;

                                case 1:
                                    Toast.makeText(AddDrumActivity.this, baseModel.getMessage(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    setResult(RESULT_OK, intent);
                                    finish();
                                    break;
                                default:
                                    Toast.makeText(AddDrumActivity.this, "Something went Wrong. \n Please Try Again Later", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    }
                });

    }

    public void updateUi() {

        if (stockData != null) {

            input_layout_date.setVisibility(View.VISIBLE);
            input_layout_comment.setVisibility(View.VISIBLE);
            input_layout_update.setVisibility(View.VISIBLE);
            input_layout_location.setVisibility(View.VISIBLE);

            if (stockData.getStockType().equals("inprocess")) {
                rbInProgress.setChecked(true);
                input_layout_stage.setVisibility(View.VISIBLE);
                input_layout_condType.setVisibility(View.VISIBLE);

            } else if (stockData.getStockType().equals("finish")) {
                rbFinish.setChecked(true);
            }


            etDrumNo.setEnabled(!(stockData.getSrNo() != null && stockData.getSrNo() > 0));

            //TODO TEJ As per they Rquire on 20190329

            /*etCabelSize.setEnabled(!(stockData.getSrNo() != null && stockData.getSrNo() > 0));
            etJobNo.setEnabled(!(stockData.getSrNo() != null && stockData.getSrNo() > 0));
            etMeter.setEnabled(!(stockData.getSrNo() != null && stockData.getSrNo() > 0));
            etLocation.setEnabled(!(stockData.getSrNo() != null && stockData.getSrNo() > 0));*/
            etRemark.setEnabled(!(stockData.getSrNo() != null && stockData.getSrNo() > 0));
            etDate.setEnabled(!(stockData.getSrNo() != null && stockData.getSrNo() > 0));
            etComment.setEnabled(!(stockData.getSrNo() != null && stockData.getSrNo() > 0));
            etStage.setEnabled(!(stockData.getSrNo() != null && stockData.getSrNo() > 0));
            etcondType.setEnabled(!(stockData.getSrNo() != null && stockData.getSrNo() > 0));

            etCabelSize.requestFocus();
            etCabelSize.post(new Runnable() {
                @Override
                public void run() {
                    etCabelSize.setSelection(etCabelSize.getText().toString().length());
                }
            });

            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            //"2018-07-24T13:49:00"
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

            if (stockData.getDate() != null) {

                Date date = null;
                try {
                    date = format.parse(stockData.getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Calendar calendar = Calendar.getInstance();

                if (date != null)
                    calendar.setTime(date);

                etDate.setText(sdf.format(calendar.getTime()));
            }

            if (stockData.getSrNo() != null && stockData.getSrNo() > 0) {
                btnSave.setText(getString(R.string.update));
            } else {
                btnSave.setText(getString(R.string.save));
            }

            if (stockData.getDrumNo() != null) {
                etDrumNo.setText(stockData.getDrumNo());
            }
            if (stockData.getSize() != null) {
                etCabelSize.setText(stockData.getSize());
            }
            if (stockData.getJobNo() != null) {
                etJobNo.setText(stockData.getJobNo());
            }
            if (stockData.getQtyMeter() != null) {
                etMeter.setText(String.valueOf(stockData.getQtyMeter()));
            }
            if (stockData.getRemark() != null) {
                etRemark.setText(stockData.getRemark());
            }
            if (stockData.getStage() != null) {

                etStage.setText(stockData.getStage());
            }
            if (stockData.getCondType() != null) {

                etcondType.setText(stockData.getCondType());
            }
            if (stockData.getComment() != null) {
                etComment.setText(stockData.getComment());
            }
            if (stockData.getUpdate() != null) {
                etUpdate.setText(stockData.getUpdate());
            }
            if (stockData.getLocation() != null) {
                etLocation.setText(stockData.getLocation());
            }

        } else {
            hideProgressDialog();
        }
    }

    @Override
    public void onDateMonth(Context context, int month, int year, String monthLabel) {
        if (context == null)
            return;
        mMonth = month;
        mYear = year;
        tvMonth.setText(monthLabel);
    }
}
