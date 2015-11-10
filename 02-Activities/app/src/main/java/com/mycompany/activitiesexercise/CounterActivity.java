package com.mycompany.activitiesexercise;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Pieter-Jan on 8/11/2015.
 */
public class CounterActivity extends AppCompatActivity
{
    private int onCreateCount = 0;
    private int onStartCount = 0;
    private int onResumeCount = 0;
    private int onPauseCount = 0;
    private int onStopCount = 0;
    private int onRestartCount = 0;
    private int onDestroyCount = 0;

    private Button button;
    private TextView onCreate;
    private TextView onStart;
    private TextView onResume;
    private TextView onPause;
    private TextView onStop;
    private TextView onRestart;
    private TextView onDestroy;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.counters);
        Log.i(getLocalClassName(), "onCreate called");
        onCreateCount += 1;
        onCreate = (TextView) findViewById(R.id.oncreate);
        onCreate.setText(getString(R.string.on_create) + " " + onCreateCount);

        button = (Button) findViewById(R.id.button);

        onStart = (TextView) findViewById(R.id.onstart);
        onStart.setText(getString(R.string.on_start) + " " + onStartCount);
        onResume = (TextView) findViewById(R.id.onresume);
        onResume.setText(getString(R.string.on_resume) + " " + onResumeCount);
        onPause = (TextView) findViewById(R.id.onpause);
        onPause.setText(getString(R.string.on_pause) + " " + onPauseCount);
        onStop = (TextView) findViewById(R.id.onstop);
        onStop.setText(getString(R.string.on_stop) + " " + onStopCount);
        onRestart = (TextView) findViewById(R.id.onrestart);
        onRestart.setText(getString(R.string.on_restart) + " " + onRestartCount);
        onDestroy = (TextView) findViewById(R.id.ondestroy);
        onDestroy.setText(getString(R.string.on_destroy) + " " + onDestroyCount);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Log.i(getLocalClassName(), "onStart called");
        onStartCount += 1;
        onStart.setText(getString(R.string.on_start) + " " + onStartCount);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.i(getLocalClassName(), "onResume called");
        onResumeCount += 1;
        onResume.setText(getString(R.string.on_resume) + " " + onResumeCount);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        Log.i(getLocalClassName(), "onPause called");
        onPauseCount += 1;
        onPause.setText(getString(R.string.on_pause) + " " + onPauseCount);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        Log.i(getLocalClassName(), "onStop called");
        onStopCount += 1;
        onStop.setText(getString(R.string.on_stop) + " " + onStopCount);
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        Log.i(getLocalClassName(), "onRestart called");
        onRestartCount += 1;
        onRestart.setText(getString(R.string.on_restart) + " " + onRestartCount);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.i(getLocalClassName(), "onDestroy called");
        onDestroyCount += 1;
        onDestroy.setText(getString(R.string.on_destroy) + " " + onDestroyCount);
    }

    public void setButtonText(String text)
    {
        button.setText(text);
    }

    public void doAction(View view)
    {
    }

}
