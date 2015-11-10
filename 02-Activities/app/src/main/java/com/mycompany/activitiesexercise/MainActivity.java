package com.mycompany.activitiesexercise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends CounterActivity {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setButtonText(getResources().getText(R.string.button_start).toString());
    }

    @Override
    public void doAction(View view)
    {
        super.doAction(view);
        Intent intent = new Intent(this, InceptionActivity.class);
        startActivity(intent);
    }
}

