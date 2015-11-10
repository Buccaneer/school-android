package com.pieter_jan.game_2048;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by Pieter-Jan on 15/10/2015.
 */
public class Card extends CardView
{
    private TextView textView;
    private int number;
    private HashMap<Integer, Integer> colors;

    public Card(Context context)
    {
        super(context);
        initColors();
        init(context);
        setRadius(10.0F);
        setShadowPadding(0, 0, 0, 0);
        setPadding(4, 4, 4, 4);
    }

    private void init(Context context)
    {
        textView = new TextView(context);
        textView.setLayoutParams(new CardView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER));
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 32);
        addView(textView);
        //setBackground(getResources().getDrawable(R.drawable.border));
        setNumber(0);
    }

    private void initColors()
    {
        colors = new HashMap<>();
        colors.put(0, R.color.color_0);
        colors.put(2, R.color.color_2);
        colors.put(4, R.color.color_4);
        colors.put(8, R.color.color_8);
        colors.put(16, R.color.color_16);
        colors.put(32, R.color.color_32);
        colors.put(64, R.color.color_64);
        colors.put(128, R.color.color_128);
        colors.put(256, R.color.color_256);
        colors.put(512, R.color.color_512);
        colors.put(1024, R.color.color_1024);
        colors.put(2048, R.color.color_2048);
    }

    public int getNumber()
    {
        return number;
    }

    public void setNumber(int number)
    {
        this.number = number;
        update();
    }

    private void update()
    {
        textView.setText(number == 0 ? null : number + "");
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, number < 999 ? 32 : (number < 9999 ? 28 : (number < 99999 ? 22 : 16)));
        textView.setTextColor(getResources().getColor(number > 4 ? R.color.number_white : R.color.number_grey));
        Integer color = colors.get(number);
        if (color == null) color = R.color.color_2048;
        setCardBackgroundColor(getResources().getColor(color/*, getContext().getTheme()*/));
        //setBackground(getResources().getDrawable(R.drawable.rounded_bg));
    }
}
