package com.mycompany.activitiesexercise;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class InceptionActivity extends CounterActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setButtonText(getResources().getText(R.string.button_stop).toString());
        inception();
    }

    @Override
    public void doAction(View view)
    {
        super.doAction(view);
        finish();
    }

    private void inception()
    {
        final Context context = this;
        LinearLayout container = (LinearLayout) findViewById(R.id.container);
        Button button = new Button(this);
        button.setText(getResources().getText(R.string.button_start).toString());
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, InceptionActivity.class);
                startActivity(intent);
            }
        });
        container.addView(button);
    }
}