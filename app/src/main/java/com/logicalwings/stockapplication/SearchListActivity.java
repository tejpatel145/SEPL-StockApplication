package com.logicalwings.stockapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.logicalwings.stockapplication.adapter.ChipListAdapter;
import com.logicalwings.stockapplication.adapter.SearchListAdapter;
import com.logicalwings.stockapplication.api.ApiClient;
import com.logicalwings.stockapplication.api.ApiInterFace;
import com.logicalwings.stockapplication.base.BaseActivity;
import com.logicalwings.stockapplication.model.AddTypeData;
import com.logicalwings.stockapplication.model.DeleteItemListener;
import com.logicalwings.stockapplication.model.RequestSearchData;
import com.logicalwings.stockapplication.model.ResponseSearchData;
import com.logicalwings.stockapplication.model.SearchData;
import com.logicalwings.stockapplication.model.StockData;
import com.logicalwings.stockapplication.model.UpdateDrumListener;
import com.logicalwings.stockapplication.monthpicker.MonthYearPickerDialog;
import com.logicalwings.stockapplication.utils.AppConstance;
import com.logicalwings.stockapplication.utils.AppUtils;
import com.logicalwings.stockapplication.utils.PaginationScrollListener;
import com.logicalwings.stockapplication.utils.PrefrenceConnector;
import com.logicalwings.stockapplication.utils.Toaster;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import logicalwings.android.com.lwmonthyearpiker.MonthPikerDialog;
import logicalwings.android.com.lwmonthyearpiker.listener.DateMonthDialogListener;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchListActivity extends BaseActivity implements DateMonthDialogListener {

    private static final int CODE_SEARCH_LIST = 201;
    private static final int REQUEST_UPDATE_DRUM = 101;
    private static final int REQUEST_SEARCH_DRUM = 102;
    private static final int REQUEST_ADD_DRUM = 103;
    private static final int PAGE_START = 1;
    private static final String KEY_STOCK_LIST = "StockList";
    private static final String KEY_STOCK_TYPE_LIST = "appdata";
    private static final String KEY_FILTER_TYPE_LIST = "filterTypeDataArrayList";
    private static final String KEY_FILTER_MONTH = "month";
    private static final String KEY_FILTER_YEAR = "year";
    private static final String KEY_FILTER_FORMATTED_DATE = "formattedDate";
    private static final String KEY_FILTER_CURRENT_PAGE = "currentPage";
    private static final String KEY_IS_LOADING = "isLoading";
    private static final String KEY_IS_LAST_PAGE = "isLastPage";
    private static final String KEY_STOCK_TYPE = "sStockType";
    private static final String KEY_STATUS = "status";

    private TextView tvNoData;
    private TextView tvMonth;
    private RecyclerView rvList;
    private RecyclerView rvChipView;
    private RadioButton rbInProgress;
    private RadioButton rbFinish;
    private SwipeRefreshLayout swipeContainer;
    private String formattedDate;
    private MonthPikerDialog mpd;
    private Toolbar toolbar;

    private AlertDialog.Builder alertDialogBuilder;
    private ArrayList<StockData> stockDataList;
    private SearchListAdapter adapter;
    private ChipListAdapter chipListAdapter;
    private RequestSearchData searchData;
    private SearchView searchView;
    private ImageView ivFilter;
    private int mYear = -1;
    private int mMonth = -1;
    private String sStockType;
    private int status = 0;
    private String sMonth;
    private boolean isLoading;
    private boolean isLastPage;
    private int currentPage = -1;

    private ArrayList<AddTypeData> filterTypeDataArrayList;

    private boolean doubleBackToExitPressedOnce;

    private BroadcastReceiver authentication_broadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AppConstance.DEBUG)
                Log.e(AppConstance.DEBUG_TAG, "SearchListActivity authentication broadcast call");
            clearData();
            Intent i = new Intent(SearchListActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

        receiver = new NetworkChangeReceiver();

        registerReceiver(receiver, filter);

        initData();

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_STOCK_TYPE_LIST)) {
                appData.setAddTypeDatas(savedInstanceState.<AddTypeData>getParcelableArrayList(KEY_STOCK_TYPE_LIST));
            }
            if (savedInstanceState.containsKey(KEY_STOCK_LIST)) {
                stockDataList = savedInstanceState.getParcelableArrayList(KEY_STOCK_LIST);
            }
            if (savedInstanceState.containsKey(KEY_FILTER_TYPE_LIST)) {
                filterTypeDataArrayList = savedInstanceState.getParcelableArrayList(KEY_FILTER_TYPE_LIST);
            }
            if (savedInstanceState.containsKey(KEY_STATUS)) {
                status = savedInstanceState.getInt(KEY_STATUS);
            }
            if (savedInstanceState.containsKey(KEY_FILTER_MONTH)) {
                mMonth = savedInstanceState.getInt(KEY_FILTER_MONTH);
            }
            if (savedInstanceState.containsKey(KEY_FILTER_YEAR)) {
                mYear = savedInstanceState.getInt(KEY_FILTER_YEAR);
            }
            if (savedInstanceState.containsKey(KEY_FILTER_CURRENT_PAGE)) {
                currentPage = savedInstanceState.getInt(KEY_FILTER_CURRENT_PAGE);
            }
            if (savedInstanceState.containsKey(KEY_IS_LOADING)) {
                isLoading = savedInstanceState.getBoolean(KEY_IS_LOADING);
            }
            if (savedInstanceState.containsKey(KEY_IS_LAST_PAGE)) {
                isLastPage = savedInstanceState.getBoolean(KEY_IS_LAST_PAGE);
            }
            if (savedInstanceState.containsKey(KEY_FILTER_FORMATTED_DATE)) {
                formattedDate = savedInstanceState.getString(KEY_FILTER_FORMATTED_DATE);
            }
            if (savedInstanceState.containsKey(KEY_STOCK_TYPE)) {
                sStockType = savedInstanceState.getString(KEY_STOCK_TYPE);
            }

        }

        initUi();

        setData();

        prepareFilterTypeData();

        if (filterTypeDataArrayList.size() > 0) {
            searchData.setSearchViewModelList(filterTypeDataArrayList);
        } else {
            updateUi();
        }

        if (stockDataList == null || stockDataList.size() == 0) {
            getSearchListData(true);
        } else {
            updateUi();
        }
    }

    private void prepareFilterTypeData() {
        filterTypeDataArrayList = new ArrayList<>();
        for (AddTypeData addTypeData : appData.getAddTypeDatas()) {
            if (addTypeData.getValue() != null && !addTypeData.getValue().isEmpty()) {
                filterTypeDataArrayList.add(addTypeData);
            }
        }
        searchData.setSearchViewModelList(filterTypeDataArrayList);
    }

    @Override
    public void initData() {

        super.initData();

        filterTypeDataArrayList = new ArrayList<>();
        searchData = new RequestSearchData();
        stockDataList = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        currentPage = PAGE_START;

        appData.setAddTypeDatas(AppUtils.createInProcessList(SearchListActivity.this));
    }

    @Override
    public void initUi() {
        super.initUi();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchView = findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(false);
        ivFilter = findViewById(R.id.ivFilter);

        tvNoData = findViewById(R.id.tvNoData);
        tvMonth = findViewById(R.id.tvMonth);
        swipeContainer = findViewById(R.id.swipeContainer);
        rvList = findViewById(R.id.rvList);
        rvChipView = findViewById(R.id.rvChipView);
        rbInProgress = findViewById(R.id.rbInProgress);
        rbFinish = findViewById(R.id.rbFinish);

        SimpleDateFormat df = new SimpleDateFormat("MMM-yyyy", Locale.getDefault());
        formattedDate = df.format(getCalender().getTime());
        tvMonth.setText(formattedDate);

        adapter = new SearchListAdapter(this, new UpdateDrumListener() {
            @Override
            public void onEdit(Object object) {
                Intent intent = new Intent(SearchListActivity.this, AddDrumActivity.class);
                intent.putExtra(AppConstance.DRUM_UPDATE_MODE_KEY, AppConstance.UPDATE_MODE);
                intent.putExtra(AppConstance.SELECTED_DRUM_KEY, (StockData) object);
                startActivityForResult(intent, REQUEST_UPDATE_DRUM);
            }

        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager llChipManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        rvList.setLayoutManager(linearLayoutManager);
        rvList.setItemAnimator(new DefaultItemAnimator());
        rvList.setAdapter(adapter);

        chipListAdapter = new ChipListAdapter(filterTypeDataArrayList, new DeleteItemListener() {
            @Override
            public void onDelete(int key) {
                for (int i = 0; i < filterTypeDataArrayList.size(); i++) {
                    if (filterTypeDataArrayList.get(i).getKey() == key) {
                        filterTypeDataArrayList.remove(i);
                    }
                }
                //Update AppData
                AddTypeData addTypeData = appData.getAddType(key);
                if (addTypeData != null) {
                    addTypeData.setValue(null);
                    addTypeData.setChecked(false);
                }

                if (status == 0) {
                    rbInProgress.setChecked(true);
                    sStockType = rbInProgress.getText().toString().toLowerCase().trim();
                } else {
                    rbFinish.setChecked(true);
                    sStockType = rbFinish.getText().toString().toLowerCase().trim();
                }

                searchData.setSearchViewModelList(filterTypeDataArrayList);
                searchData.setStatus(sStockType);
                resetAllPagingData();
                getSearchListData(true);
            }
        });
        rvChipView.setLayoutManager(llChipManager);
        rvChipView.setItemAnimator(new DefaultItemAnimator());
        rvChipView.setAdapter(chipListAdapter);

        tvMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openMonthPickerDialog();

            }
        });

        rbFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sStockType = rbFinish.getText().toString().toLowerCase().trim();
                status = 1;
                onRadioButtonClicked(view);
            }
        });
        rbInProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sStockType = rbInProgress.getText().toString().toLowerCase().trim();
                status = 0;
                onRadioButtonClicked(view);
            }
        });

        rvList.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {

            @Override
            protected void loadMoreItems() {

                isLoading = true;

                currentPage += 1;

                searchData.setPageIndex(currentPage);

                getSearchListData(true);
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (AppUtils.isOnline(SearchListActivity.this)) {
                    currentPage = PAGE_START;
                    if (stockDataList != null)
                        resetAllPagingData();
                    getSearchListData(false);
                } else {
                    Toaster.popInternetUnavailableToast(SearchListActivity.this);
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (filterTypeDataArrayList.size() > 0) {
                    for (int i = 0; i < filterTypeDataArrayList.size(); i++) {
                        if (filterTypeDataArrayList.get(i).getKey() == 0) {
                            filterTypeDataArrayList.remove(i);
                        }
                    }
                }
                filterTypeDataArrayList.add(new AddTypeData(query, 0, "Drum No", true));

                //Update AppData
                AddTypeData addTypeData = appData.getAddType(0);
                if (addTypeData != null) {
                    addTypeData.setValue(query);
                    addTypeData.setChecked(true);
                }

                searchData.setSearchViewModelList(filterTypeDataArrayList);
                resetAllPagingData();
                getSearchListData(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SearchListActivity.this, FilterActivity.class);
                i.putExtra(AppConstance.SELECTED_STOCKTYPE_KEY, sStockType);
                i.putExtra(AppConstance.SELECTED_STATUS_KEY, AppConstance.FILTER_DATA);
                startActivityForResult(i, REQUEST_SEARCH_DRUM);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(authentication_broadcast, new IntentFilter(AppConstance.AUTHENTICATION_CHANGE_BROADCAST));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(authentication_broadcast);
        unregisterReceiver(receiver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (appData != null && appData.getAddTypeDatas() != null)
            outState.putParcelableArrayList(KEY_STOCK_TYPE_LIST, appData.getAddTypeDatas());
        outState.putParcelableArrayList(KEY_STOCK_LIST, stockDataList);
        outState.putParcelableArrayList(KEY_FILTER_TYPE_LIST, filterTypeDataArrayList);
        outState.putString(KEY_FILTER_FORMATTED_DATE, formattedDate);
        outState.putString(KEY_STOCK_TYPE, sStockType);
        outState.putInt(KEY_FILTER_MONTH, mMonth);
        outState.putInt(KEY_FILTER_YEAR, mYear);
        outState.putInt(KEY_FILTER_CURRENT_PAGE, currentPage);
        outState.putInt(KEY_STATUS, status);
        outState.putBoolean(KEY_IS_LAST_PAGE, isLastPage);
        outState.putBoolean(KEY_IS_LOADING, isLoading);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_SEARCH_DRUM) {

                if (data != null) {
                    prepareFilterTypeData();
                    searchData.setSearchViewModelList(filterTypeDataArrayList);
                    resetAllPagingData();
                    searchView.setQuery("", false);
                    searchView.clearFocus();
                    getSearchListData(true);

                }
            }

            if (requestCode == REQUEST_UPDATE_DRUM) {
                resetAllPagingData();
                getSearchListData(true);
            }

            if (requestCode == REQUEST_ADD_DRUM) {

                resetAllPagingData();

                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);

                String format = "MMM-yyyy";
                SimpleDateFormat df = new SimpleDateFormat(format, Locale.getDefault());
                formattedDate = df.format(c.getTime());
                if (formattedDate != null) {

                    tvMonth.setText(formattedDate);
                    searchData.setDate(formattedDate);
                }

                getSearchListData(true);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(SearchListActivity.this, AddDrumActivity.class);
                startActivityForResult(intent, REQUEST_ADD_DRUM);
                break;

            case R.id.action_logout:
                logOutConfirmDialog();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;

        Toast.makeText(SearchListActivity.this, this.getResources().getString(R.string.exitMsg), Toast.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public void onDateMonth(Context context, int month, int year, String monthLabel) {
        if (context == null)
            return;
   //     Log.d(AppConstance.DEBUG_TAG, "onDateMonth() called with: context = [" + context + "], month = [" + month + "], year = [" + year + "], monthLabel = [" + monthLabel + "]");
        mMonth = month;
        mYear = year;
        tvMonth.setText(monthLabel);
        updateDate();
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
            Log.v(AppConstance.DEBUG_TAG, "SearchListActivity onTokenRefreshSuccess code: " + code);

        if (code == CODE_SEARCH_LIST) {
            if (AppUtils.isOnline(this)) {
                resetAllPagingData();
                getSearchListData(true);
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

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.rbFinish:
                if (checked) {
                    resetAllPagingData();
                    appData.setAddTypeDatas(AppUtils.createFinishList(SearchListActivity.this));
                    prepareFilterTypeData();
                    searchData.setStatus(sStockType);
                    getSearchListData(true);
                }
                break;
            case R.id.rbInProgress:
                if (checked) {

                    resetAllPagingData();
                    appData.setAddTypeDatas(AppUtils.createInProcessList(SearchListActivity.this));
                    prepareFilterTypeData();
                    searchData.setStatus(sStockType);
                    getSearchListData(true);
                }
                break;
        }
    }

    private void openMonthPickerDialog() {

        Calendar now = Calendar.getInstance();
        if (mpd == null) {
            mpd = MonthPikerDialog.newInstance(SearchListActivity.this, mYear, mMonth, R.color.colorPrimary);
        } else {
            mpd.initialize(SearchListActivity.this, mYear, mMonth, R.color.colorPrimary);
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

    private void updateDate() {

        searchData.setDate(tvMonth.getText().toString().trim());
        resetAllPagingData();
        getSearchListData(true);

    }

    public void setData() {

        sMonth = tvMonth.getText().toString().trim();

        if (rbInProgress.isChecked()) {
            sStockType = rbInProgress.getText().toString().toLowerCase().trim();
        }
        if (rbFinish.isChecked()) {
            sStockType = rbFinish.getText().toString().toLowerCase().trim();
        }

        searchData.setSearchViewModelList(filterTypeDataArrayList);
        searchData.setPageIndex(1);
        searchData.setPageSize(20);
        searchData.setSortType(-1);
        searchData.setSearchText("");
        searchData.setSortField("CreatedDate");
        searchData.setStatus(sStockType);
        searchData.setDate(sMonth);

    }

    public void getSearchListData(boolean isDisplayProgress) {

        if (isDisplayProgress)
            showProgressDialog(getString(R.string.please_wait));

    //    Log.d(AppConstance.DEBUG_TAG, "SearchListActivity getSearchListData : " + new Gson().toJson(searchData, RequestSearchData.class));

        ApiInterFace service = ApiClient.getClient(SearchListActivity.this);

//        Log.d("TESTP", "getSearchListData() called");
        service.getSearchList(searchData)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseSearchData>() {

                    @Override
                    public void onCompleted() {
//                        Log.d("TESTP", "onCompleted() called");
                        if (AppConstance.DEBUG)
                            Log.i(AppConstance.DEBUG_TAG, "onCompleted: ");

                        hideProgressDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
//                        Log.d("TESTP", "onError() called with: e = [" + e + "]");
//                        Log.i(AppConstance.DEBUG_TAG, "onError: " + e.getMessage());
                        swipeContainer.setRefreshing(false);
                        hideProgressDialog();
                        isLoading = false;
                        if (((HttpException) e).code() == AppConstance.LOGIN_TOKEN_EXPIRE_CODE) {
                            refreshToken(CODE_SEARCH_LIST);
                        }
                    }

                    @Override
                    public void onNext(ResponseSearchData responseSearchData) {

//                        Log.d("TESTP", "onNext() called with: responseSearchData = [" + responseSearchData + "]");

//                        Log.d(AppConstance.DEBUG_TAG, "onNext() called with: responseSearchData = [" + new Gson().toJson(responseSearchData) + "]");


                        if (getApplicationContext() == null)
                            return;

                        swipeContainer.setRefreshing(false);
                        hideProgressDialog();
                        switch (responseSearchData.getSuccess()) {

                            case 0:
                                if (responseSearchData.getData() != null &&
                                        responseSearchData.getData().getTotalPages() != null &&
                                        responseSearchData.getData().getTotalPages() == 0) {
                                    resetAllPagingData();
                                }
                                Toast.makeText(SearchListActivity.this, responseSearchData.getMessage(), Toast.LENGTH_LONG).show();
                                break;
                            case 1:
                                if (responseSearchData.getData() != null) {

                                    if (responseSearchData.getData().getList() != null &&
                                            responseSearchData.getData().getList().size() > 0) {
                                        stockDataList.addAll(responseSearchData.getData().getList());
                                    }
                                    if (currentPage >= responseSearchData.getData().getTotalPages()) {
                                        isLastPage = true;
                                    }
                                }
                                break;
                        }
                        if (AppConstance.DEBUG)
                            Log.e(AppConstance.DEBUG_TAG, "onNext: " + isLastPage + ":" + currentPage);
                        updateUi();
                        isLoading = false;
                        searchView.setQuery("", false);
                        searchView.clearFocus();
                    }
                });


    }

    private void resetAllPagingData() {
        isLastPage = false;
        isLoading = false;
        currentPage = PAGE_START;
        searchData.setPageIndex(currentPage);
        stockDataList.clear();

    }

    private void updateUi() {

        if (stockDataList.size() > 0) {
            tvNoData.setVisibility(View.GONE);
            rvList.setVisibility(View.VISIBLE);
        } else {
            rvList.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
            tvNoData.setText(getString(R.string.no_data_found));
        }

        adapter.setSearchDatas(stockDataList);
        adapter.notifyDataSetChanged();

        if (filterTypeDataArrayList.size() > 0) {
            rvChipView.setVisibility(View.VISIBLE);
        } else {
            rvChipView.setVisibility(View.GONE);
        }

        chipListAdapter.setFilterChips(filterTypeDataArrayList);
        chipListAdapter.notifyDataSetChanged();
    }

    public void logOutConfirmDialog() {

        alertDialogBuilder = new AlertDialog.Builder(SearchListActivity.this);
        alertDialogBuilder.setTitle("Do you want to logout?");
        alertDialogBuilder
                .setMessage("Click Yes to Logout!")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                clearData();
                                Intent intent = new Intent(SearchListActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

        alertDialogBuilder.show();

    }

    public void clearData() {

        PrefrenceConnector.clearAtLogout(this);
    }

}
