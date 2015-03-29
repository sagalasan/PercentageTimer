package com.sagalasan.percentagetimer;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, TimerData
{
    Button btnStartDate, btnStartTime, btnEndDate, btnEndTime, btnStartTimer;
    TextView txtStartDate, txtStartTime, txtEndDate, txtEndTime, txtError;
    EditText editName;
    MyDBHandler dbHandler;

    GregorianCalendar sDate, fDate;

    private int mYear, mMonth, mDay, mHour, mMinute, mAmPm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler = new MyDBHandler(this, null, null, 1);

        btnStartDate = (Button) findViewById(R.id.start_date_button);
        btnStartTime = (Button) findViewById(R.id.start_time_button);
        btnEndDate = (Button) findViewById(R.id.end_date_button);
        btnEndTime = (Button) findViewById(R.id.end_time_button);
        btnStartTimer = (Button) findViewById(R.id.start_timer_button);

        txtStartDate = (TextView) findViewById(R.id.start_date_tv);
        txtStartTime = (TextView) findViewById(R.id.start_time_tv);
        txtEndDate = (TextView) findViewById(R.id.end_date_tv);
        txtEndTime = (TextView) findViewById(R.id.end_time_tv);
        txtError = (TextView) findViewById(R.id.name_error);

        editName = (EditText) findViewById(R.id.timer_name);

        btnStartDate.setOnClickListener(this);
        btnStartTime.setOnClickListener(this);
        btnEndDate.setOnClickListener(this);
        btnEndTime.setOnClickListener(this);

        sDate = new GregorianCalendar();
        fDate = new GregorianCalendar();

        fDate.set(Calendar.HOUR, fDate.get(Calendar.HOUR) + 1);

        txtStartDate.setText(returnDate(sDate));
        txtEndDate.setText(returnDate(fDate));
        txtStartTime.setText(returnTime(sDate));
        txtEndTime.setText(returnTime(fDate));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void onClick(View v)
    {
        if(v == btnStartDate || v == btnEndDate)
        {
            final boolean startDate;
            if(v == btnStartDate) startDate = true;
            else startDate = false;

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dpd = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener()
                    {
                        public void onDateSet(DatePicker view, int year, int month, int day)
                        {
                            if(startDate)
                            {
                                sDate.set(year, month, day);
                                txtStartDate.setText(returnDate(sDate));
                            }
                            else
                            {
                                fDate.set(year, month, day);
                                txtEndDate.setText(returnDate(fDate));
                            }
                        }
                    }, mYear, mMonth, mDay);
            dpd.show();
        }

        if(v == btnStartTime || v == btnEndTime)
        {
            final boolean startTime;
            if(v == btnStartTime) startTime = true;
            else startTime = false;

            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR);
            mMinute = c.get(Calendar.MINUTE);
            mAmPm = c.get(Calendar.AM_PM);

            TimePickerDialog tpd = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener()
                    {
                        public void onTimeSet(TimePicker view, int hour, int minute)
                        {
                            if(startTime)
                            {
                                sDate.set(Calendar.HOUR_OF_DAY, hour);
                                sDate.set(Calendar.MINUTE, minute);
                                txtStartTime.setText(returnTime(sDate));
                            }
                            else
                            {
                                fDate.set(Calendar.HOUR_OF_DAY, hour);
                                fDate.set(Calendar.MINUTE, minute);
                                txtEndTime.setText(returnTime(fDate));
                            }
                        }
                    }, mHour, mMinute, false);
            tpd.show();
        }
    }

    public void startTimer(View view)
    {
        if(dbHandler.exists(editName.getText().toString()))
        {
            txtError.setVisibility(View.VISIBLE);
            return;
        }
        txtError.setVisibility(View.INVISIBLE);
        Intent intent = new Intent(this, TimerDisplayActivity.class);

        Timers t = new Timers(editName.getText().toString(), sDate.getTimeInMillis(), fDate.getTimeInMillis());

        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        dbHandler.addTimer(t);

        intent.putExtra(TIMER_NAME, editName.getText());
        intent.putExtra(START_DATE, sDate.getTimeInMillis());
        intent.putExtra(END_DATE, fDate.getTimeInMillis());

        startActivity(intent);
    }

    String returnDate(GregorianCalendar c)
    {
        String result = "";
        int day = c.get(Calendar.DAY_OF_MONTH);
        result += (c.get(Calendar.MONTH) + 1);
        result += "/";
        if(day < 10)
        {
            result += "0" + day;
        }
        else
        {
            result += day;
        }
        result += "/" + c.get(Calendar.YEAR);
        return result;
    }

    String returnTime(GregorianCalendar c)
    {
        String result = "";
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        String amPm = "AM";
        if(hour > 12)
        {
            amPm = "PM";
            hour -= 12;
        }
        if(hour == 0)
        {
            hour = 12;
        }
        result += hour;
        result += ":";
        if(min < 10)
        {
            result += "0" + min;
        }
        else
        {
            result += min;
        }
        result += " " + amPm;
        return result;
    }

}