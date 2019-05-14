package com.example.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

public class TimePickerFragment extends DialogFragment {
    private static final String ARG_TIME = "time";
    private static final String EXTRA_TIME = "com.example.criminalintent.time";
    private TimePicker mTimePicker;

    public static TimePickerFragment newInstance(Date time) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, time);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static Date getTime(Intent data) {
        return (Date) data.getSerializableExtra(EXTRA_TIME);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time,null);

        mTimePicker = view.findViewById(R.id.dialog_time_picker);
        Calendar calendar = Calendar.getInstance();
        assert getArguments() != null;
        Date time = (Date) getArguments().getSerializable(ARG_TIME);
        calendar.setTime(time);
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR);
        mTimePicker.setHour(hour);
        int minute = calendar.get(Calendar.MINUTE);
        mTimePicker.setMinute(minute);

        return new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setView(view)
                .setTitle(R.string.time_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int hour = mTimePicker.getHour();
                        int minute = mTimePicker.getMinute();
                        Date timeReturned = new GregorianCalendar(year, month, day, hour, minute).getTime();
                        sendResult(Activity.RESULT_OK, timeReturned);
                    }
                })
                .create();
    }

    private void sendResult(int resultCode, Date timeReturned) {
        if (null != getTargetFragment()) {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_TIME, timeReturned);
            getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
        }
    }
}
