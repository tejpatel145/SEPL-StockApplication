package logicalwings.android.com.lwmonthyearpiker;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

import logicalwings.android.com.lwmonthyearpiker.listener.DateMonthDialogListener;

/**
 * Created by Pratik on 1/4/2018.
 */
public class MonthPikerDialog extends DialogFragment implements MonthAdapter.OnSelectedListener {

    private static final String KEY_YEAR = "MonthPikerDialog.YEAR";
    private static final String KEY_MONTH = "MonthPikerDialog.MONTH";
    private static final String KEY_MAX_YEAR = "MonthPikerDialog.MAX_YEAR";
    private static final String KEY_MIN_YEAR = "MonthPikerDialog.MIN_YEAR";
    private static final String KEY_COLOR_CODE = "MonthPikerDialog.COLOR_CODE";

    private TextView mTitleView;
    private TextView tvYear;
    private Button mPositiveButton;
    private Button mNegativeButton;
    private View contentView;

    private MonthAdapter monthAdapter;

    private DateMonthDialogListener dateMonthDialogListener;

    private int mYear;
    private int mMonth;
    private int minYear;
    private int maxYear;
    private int mColorCode;

    public static MonthPikerDialog newInstance(DateMonthDialogListener callBack, int year, int monthOfYear, int colorCode) {
        MonthPikerDialog ret = new MonthPikerDialog();
        ret.initialize(callBack, year, monthOfYear, colorCode);
        return ret;
    }

    public void initialize(DateMonthDialogListener callBack, int year, int monthOfYear, int colorCode) {
        dateMonthDialogListener = callBack;
        mYear= year;
        mMonth = monthOfYear;
        Calendar now = Calendar.getInstance();
        minYear = 2000;
        maxYear = now.get(Calendar.YEAR);
        mColorCode = colorCode;
    }

    public void setMinYear(int minYear) {
        this.minYear = minYear;
    }

    public void setMaxYear(int maxYear) {
        this.maxYear = maxYear;
    }

    public void setDateMonthDialogListener(DateMonthDialogListener dateMonthDialogListener) {
        this.dateMonthDialogListener = dateMonthDialogListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Activity activity = getActivity();

        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (savedInstanceState != null) {
            mYear = savedInstanceState.getInt(KEY_YEAR);
            mMonth = savedInstanceState.getInt(KEY_MONTH);
            maxYear = savedInstanceState.getInt(KEY_MAX_YEAR);
            minYear = savedInstanceState.getInt(KEY_MIN_YEAR);
            mColorCode = savedInstanceState.getInt(KEY_COLOR_CODE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        contentView = inflater.inflate(R.layout.month_picker_dialog, container, false);
        mTitleView = (TextView) contentView.findViewById(R.id.title);
        tvYear = (TextView) contentView.findViewById(R.id.text_year);

        Button next = (Button) contentView.findViewById(R.id.btn_next);
        next.setOnClickListener(nextButtonClick());

        Button previous = (Button) contentView.findViewById(R.id.btn_previous);
        previous.setOnClickListener(previousButtonClick());

        mPositiveButton = (Button) contentView.findViewById(R.id.btn_p);
        mNegativeButton = (Button) contentView.findViewById(R.id.btn_n);

        mPositiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateMonthDialogListener.onDateMonth(getActivity(), monthAdapter.getMonth(), mYear, mTitleView.getText().toString());
                dismiss();
            }
        });

        mNegativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        monthAdapter = new MonthAdapter(getActivity(),true, this);
        RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(monthAdapter);
        monthAdapter.setSelectedYear(mYear);

        setSelectedMonth(mMonth);

        tvYear.setText(String.valueOf(mYear));

        setColorTheme();

        return contentView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_YEAR, mYear);
        outState.putInt(KEY_MONTH, mMonth);
        outState.putInt(KEY_MAX_YEAR, maxYear);
        outState.putInt(KEY_MIN_YEAR, minYear);
        outState.putInt(KEY_COLOR_CODE, mColorCode);

    }

    public View.OnClickListener nextButtonClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mYear >= maxYear)
                    return;
                mYear++;
                tvYear.setText(String.valueOf(mYear));
                mTitleView.setText(monthAdapter.getShortMonth() + "-" + mYear);
                monthAdapter.setSelectedYear(mYear);
            }
        };
    }

    public View.OnClickListener previousButtonClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mYear <= minYear)
                    return;
                mYear--;
                tvYear.setText(String.valueOf(mYear));
                mTitleView.setText(monthAdapter.getShortMonth() + "-" + mYear);
                monthAdapter.setSelectedYear(mYear);
            }
        };
    }

    public void setSelectedMonth(int index) {
        monthAdapter.setSelectedItem(index);
        mTitleView.setText(monthAdapter.getShortMonth() + "-" + mYear);
    }
    public void show(FragmentManager manager, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commit();
    }

    @Override
    public void onContentSelected(int month) {
        mMonth = month;
        mTitleView.setText(monthAdapter.getShortMonth() + "-" + mYear);
    }

    private void setColorTheme() {
        if (mColorCode > 0) {

            LinearLayout linearToolbar = (LinearLayout) contentView.findViewById(R.id.linear_toolbar);
            linearToolbar.setBackgroundResource(mColorCode);
            monthAdapter.setBackgroundMonth(mColorCode);

            if (getActivity() != null) {

                mPositiveButton.setTextColor(ContextCompat.getColor(getActivity(), mColorCode));
                mNegativeButton.setTextColor(ContextCompat.getColor(getActivity(), mColorCode));
            }
        }

    }
}
