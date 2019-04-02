package logicalwings.android.com.lwmonthyearpiker;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
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

import logicalwings.android.com.lwmonthyearpiker.listener.DateYearDialogListener;

/**
 * Created by Pratik on 1/4/2018.
 */
public class YearPikerDialog extends DialogFragment implements YearAdapter.OnSelectedListener {

    private static final String KEY_YEAR = "YearPikerDialog.YEAR";
    private static final String KEY_MAX_YEAR = "YearPikerDialog.MAX_YEAR";
    private static final String KEY_MIN_YEAR = "YearPikerDialog.MIN_YEAR";
    private static final String KEY_COLOR_CODE = "YearPikerDialog.COLOR_CODE";

    private TextView tvYear;
    private Button mPositiveButton;
    private Button mNegativeButton;
    private View contentView;

    private YearAdapter yearAdapter;

    private DateYearDialogListener dateYearDialogListener;

    private int mYear;
    private int minYear;
    private int maxYear;
    private int mColorCode;


    public static YearPikerDialog newInstance(DateYearDialogListener callBack, int year, int colorCode) {
        YearPikerDialog ret = new YearPikerDialog();
        ret.initialize(callBack, year, colorCode);
        return ret;
    }

    public void initialize(DateYearDialogListener callBack, int year, int colorCode) {
        dateYearDialogListener = callBack;
        mYear= year;
        Calendar now = Calendar.getInstance();
        minYear = 2016;
        maxYear = now.get(Calendar.YEAR);
        mColorCode = colorCode;
    }

    public void setMinYear(int minYear) {
        this.minYear = minYear;
    }

    public void setMaxYear(int maxYear) {
        this.maxYear = maxYear;
    }

    public void setDateYearDialogListener(DateYearDialogListener dateYearDialogListener) {
        this.dateYearDialogListener = dateYearDialogListener;
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
            maxYear = savedInstanceState.getInt(KEY_MAX_YEAR);
            minYear = savedInstanceState.getInt(KEY_MIN_YEAR);
            mColorCode = savedInstanceState.getInt(KEY_COLOR_CODE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        contentView = inflater.inflate(R.layout.year_picker_dialog, container, false);
        tvYear = (TextView) contentView.findViewById(R.id.title);

        mPositiveButton = (Button) contentView.findViewById(R.id.btn_p);
        mNegativeButton = (Button) contentView.findViewById(R.id.btn_n);

        mPositiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateYearDialogListener.onDateYear(getActivity(), yearAdapter.getYear());
                dismiss();
            }
        });

        mNegativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        yearAdapter = new YearAdapter(getActivity(),this, minYear, maxYear, mYear);
        RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(yearAdapter);

        tvYear.setText(String.valueOf(mYear));

        setColorTheme();

        return contentView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_YEAR, mYear);
        outState.putInt(KEY_MAX_YEAR, maxYear);
        outState.putInt(KEY_MIN_YEAR, minYear);
        outState.putInt(KEY_COLOR_CODE, mColorCode);

    }

    @Override
    public void onContentSelected(int year) {
        mYear = year;
        tvYear.setText(String.valueOf(mYear));
    }

    public void show(FragmentManager manager, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commit();
    }

    public void setColorTheme() {

        if (mColorCode > 0) {
            LinearLayout linearToolbar = (LinearLayout) contentView.findViewById(R.id.linear_toolbar);
            linearToolbar.setBackgroundResource(mColorCode);
            yearAdapter.setBackgroundMonth(mColorCode);
        }

        if (getActivity() != null) {

            mPositiveButton.setTextColor(ContextCompat.getColor(getActivity(), mColorCode));
            mNegativeButton.setTextColor(ContextCompat.getColor(getActivity(), mColorCode));
        }
    }
}
