package com.sagalasan.percentagetimer;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;


public class TimerDisplayActivity extends ActionBarActivity implements TimerData
{
    GregorianCalendar sDate, fDate, cDate;
    TextView txtStartDate, txtEndDate, txtStartTime, txtEndTime, txtPercent;
    ProgressBar barPercent;
    Long sDateInMillis, fDateInMillis, cDateInMillis;
    Timer timer;
    int timerInterval;
    double dateInterval, currentInterval, percentage;
    String percentString, timerName;

    TextView yearsNum, monthsNum, weeksNum, daysNum, hoursNum, minutesNum, secondsNum, millisecondsNum;
    TextView yearsDenum, monthsDenum, weeksDenum, daysDenum, hoursDenumm, minutesDenum, secondsDenum, millisecondsDenum;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_display);

        txtStartTime = (TextView) findViewById(R.id.start_time_tv);
        txtStartDate = (TextView) findViewById(R.id.start_date_tv);
        txtEndTime = (TextView) findViewById(R.id.end_time_tv);
        txtEndDate = (TextView) findViewById(R.id.end_date_tv);
        txtPercent = (TextView) findViewById(R.id.percent_text);
        barPercent = (ProgressBar) findViewById(R.id.percent_bar);

        createUnitStats();

        Intent intent = getIntent();
        timerName = intent.getStringExtra(TIMER_NAME);
        sDate = new GregorianCalendar();
        sDateInMillis = intent.getLongExtra(START_DATE, -1);
        sDate.setTimeInMillis(sDateInMillis);

        fDate = new GregorianCalendar();
        fDateInMillis = intent.getLongExtra(END_DATE, -1);
        fDate.setTimeInMillis(fDateInMillis);

        cDate = new GregorianCalendar();
        cDateInMillis = cDate.getTimeInMillis();

        txtStartDate.setText(returnDate(sDate));
        txtStartTime.setText(returnTime(sDate));
        txtEndDate.setText(returnDate(fDate));
        txtEndTime.setText(returnTime(fDate));

        setTotals();

        timerInterval = 1000;

        dateInterval = (double) (fDateInMillis - sDateInMillis);
        handler.postDelayed(runnable, 0);

    }

    private void createUnitStats()
    {
        yearsNum = (TextView) findViewById(R.id.years_num);
        monthsNum = (TextView) findViewById(R.id.months_num);
        weeksNum = (TextView) findViewById(R.id.weeks_num);
        daysNum = (TextView) findViewById(R.id.days_num);
        hoursNum = (TextView) findViewById(R.id.hours_num);
        minutesNum = (TextView) findViewById(R.id.minutes_num);
        secondsNum = (TextView) findViewById(R.id.seconds_num);
        millisecondsNum = (TextView) findViewById(R.id.milliseconds_num);

        yearsDenum = (TextView) findViewById(R.id.years_denum);
        monthsDenum = (TextView) findViewById(R.id.months_denum);
        weeksDenum = (TextView) findViewById(R.id.weeks_denum);
        daysDenum = (TextView) findViewById(R.id.days_denum);
        hoursDenumm = (TextView) findViewById(R.id.hours_denum);
        minutesDenum = (TextView) findViewById(R.id.minutes_denum);
        secondsDenum = (TextView) findViewById(R.id.seconds_denum);
        millisecondsDenum = (TextView) findViewById(R.id.milliseconds_denum);
    }

    private void setTotals()
    {
        millisecondsDenum.setText(Long.toString((fDateInMillis - sDateInMillis) / 1));
        secondsDenum.setText(Long.toString((fDateInMillis - sDateInMillis) / 1000));
        minutesDenum.setText(Long.toString((fDateInMillis - sDateInMillis) / 1000 / 60));
        hoursDenumm.setText(Long.toString((fDateInMillis - sDateInMillis) / 1000 / 60 /60));
        daysDenum.setText(String.format("%.4f", (float)(fDateInMillis - sDateInMillis) / 1000 / 60 / 60 / 24));
        weeksDenum.setText(String.format("%.4f", (float)(fDateInMillis - sDateInMillis) / 1000 / 60 / 60 / 24 / 7));
        monthsDenum.setText(String.format("%.4f", (float)(fDateInMillis - sDateInMillis) / 1000 / 60 / 60 / 24 / 30));
        yearsDenum.setText(String.format("%.4f", (float)(fDateInMillis - sDateInMillis) / 1000 / 60 / 60 / 24 / 365));
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable()
    {
        @Override
        public void run()
        {
            long ct = new GregorianCalendar().getTimeInMillis();
            currentInterval = (double) (ct - sDateInMillis);
            percentage = currentInterval / dateInterval * 100;
            txtPercent.setText(String.format("%.4f", percentage) + "%");
            barPercent.setProgress((int) percentage);

            millisecondsNum.setText(Long.toString((ct - sDateInMillis) / 1));
            secondsNum.setText(Long.toString((ct - sDateInMillis) / 1000));
            minutesNum.setText(Long.toString((ct - sDateInMillis) / 1000 / 60));
            hoursNum.setText(Long.toString((ct - sDateInMillis) / 1000 / 60 /60));
            daysNum.setText(String.format("%.4f", (float)(ct - sDateInMillis) / 1000 / 60 / 60 / 24));
            weeksNum.setText(String.format("%.4f", (float)(ct - sDateInMillis) / 1000 / 60 / 60 / 24 / 7));
            monthsNum.setText(String.format("%.4f", (float)(ct - sDateInMillis) / 1000 / 60 / 60 / 24 / 30));
            yearsNum.setText(String.format("%.4f", (float)(ct - sDateInMillis) / 1000 / 60 / 60 / 24 / 365));

            handler.postDelayed(this, 100);
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timer_display, menu);
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

    @Override
    public void onPause()
    {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        handler.postDelayed(runnable, 0);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        handler.postDelayed(runnable, 0);
    }
}
