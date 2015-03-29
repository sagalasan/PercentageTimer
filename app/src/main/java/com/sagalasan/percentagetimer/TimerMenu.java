package com.sagalasan.percentagetimer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.FileOutputStream;
import java.util.ArrayList;


public class TimerMenu extends ActionBarActivity implements AdapterView.OnItemClickListener, TimerData
{
    private ListView lv;
    private ArrayList<String> timerNames;
    private ArrayList<Timers> timers;
    private ArrayAdapter<String> arrayAdapter;
    MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_menu);

        lv = (ListView) findViewById(R.id.timer_list);
        timerNames = new ArrayList<String>();
        timers = new ArrayList<Timers>();

        dbHandler = new MyDBHandler(this, null, null, 1);

        popArrayLists();

        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                timerNames
        );
        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                jump(position);
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                AlertDialog diaBox = AskOption(position, id);
                diaBox.show();
                return true;
            }
        });
    }

    private void popArrayLists()
    {
        timers.clear();
        timers = dbHandler.returnTimers();

        for(int i = 0; i < timers.size(); i++)
        {
            timerNames.add(timers.get(i).get_timername());
        }
    }

    private void jump(int position)
    {
        String timerName = timerNames.get(position);
        Timers temp;
        long sDate;
        long fDate;
        Intent intent = new Intent();

        temp = returnTimerForIntent(timers, timerName);
        sDate = temp.get_startdate();
        fDate = temp.get_enddate();

        intent.setClass(this, TimerDisplayActivity.class);
        intent.putExtra(TIMER_NAME, timerName);
        intent.putExtra(START_DATE, sDate);
        intent.putExtra(END_DATE, fDate);

        startActivity(intent);
    }

    private void deleteTimer(int position)
    {
        dbHandler.deleteTimer(timerNames.get(position));
        runOnUiThread(run);
    }

    Runnable run = new Runnable() {
        @Override
        public void run() {
            timerNames.clear();
            popArrayLists();
            arrayAdapter.notifyDataSetChanged();
            lv.invalidateViews();
            lv.refreshDrawableState();
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timer_menu, menu);
        return super.onCreateOptionsMenu(menu);
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
        else if(id == R.id.action_new)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private Timers returnTimerForIntent(ArrayList<Timers> t, String name)
    {
        for(int i = 0; i < t.size(); i++)
        {
            if(t.get(i).get_timername().equals(name))
            {
                return t.get(i);
            }
        }
        return null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private AlertDialog AskOption(final int position, final long id)
    {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setIcon(R.drawable.abc_ic_voice_search_api_mtrl_alpha)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteTimer(position);
                        dialog.dismiss();
                    }

                })

                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        runOnUiThread(run);
    }

    @Override
    public void onRestart()
    {
        super.onResume();
        runOnUiThread(run);
    }
}
