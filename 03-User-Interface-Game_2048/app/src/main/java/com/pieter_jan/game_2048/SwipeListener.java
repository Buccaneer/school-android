package com.pieter_jan.game_2048;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class SwipeListener implements View.OnTouchListener
{
    private final GestureDetectorCompat gestureDetector;

    public SwipeListener(Context context)
    {
        gestureDetector = new GestureDetectorCompat(context, new SwipeGestureListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        return gestureDetector.onTouchEvent(event);
    }

    public void onSwipeRight()
    {
    }

    public void onSwipeLeft()
    {
    }

    public void onSwipeTop()
    {
    }

    public void onSwipeBottom()
    {
    }

    private final class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener
    {
        private static final int SWIPE_DISTANCE_TRESHOLD = 100;
        private static final int SWIPE_VELOCITY_TRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e)
        {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
            boolean returnValue = false;

            float diffX = e2.getX() - e1.getX();
            float diffY = e2.getY() - e1.getY();

            if (Math.abs(diffX) > Math.abs(diffY))
            {
                if (Math.abs(diffX) > SWIPE_DISTANCE_TRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_TRESHOLD)
                {
                    if (diffX > 0)
                    {
                        onSwipeRight();
                    }
                    else
                    {
                        onSwipeLeft();
                    }
                    returnValue = true;
                }
            }
            else
            {
                if (Math.abs(diffY) > SWIPE_DISTANCE_TRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_TRESHOLD)
                {
                    if (diffY > 0)
                    {
                        onSwipeBottom();
                    }
                    else
                    {
                        onSwipeTop();
                    }
                    returnValue = true;
                }
            }
            return returnValue;
        }
    }
}