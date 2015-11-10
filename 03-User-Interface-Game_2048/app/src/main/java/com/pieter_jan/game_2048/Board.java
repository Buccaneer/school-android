package com.pieter_jan.game_2048;

import android.content.Context;
import android.util.Log;
import android.widget.GridLayout;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Pieter-Jan on 15/10/2015.
 */
public class Board extends GridLayout
{
    private Card[][] cardBoard;
    private int cardWidth;
    private Random rng;
    private Gson gson = new Gson();
    private int score;

    public Board(Context context)
    {
        this(context, null, 0);
    }

    public Board(Context context, String json, int score)
    {
        super(context);
        rng = new Random();
        this.score = score;
        initBoard(context, json);
    }

    public int getScore()
    {
        return score;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        cardWidth = w < h ? (w - 20) / 4 : (h - 20) / 4;
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                cardBoard[i][j].setMinimumWidth(cardWidth - 10);
                cardBoard[i][j].setMinimumHeight(cardWidth - 10);
                GridLayout.LayoutParams lp = new GridLayout.LayoutParams(GridLayout.spec(i), GridLayout.spec(j));
                lp.setMargins(5, 5, 5, 5);
                cardBoard[i][j].setLayoutParams(lp);
            }
        }
    }

    private void initBoard(Context context, String json)
    {
        //setBackground(getResources().getDrawable(R.drawable.border));
        //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //setLayoutParams(new GridView.LayoutParams(params));
        setRowCount(4);
        setColumnCount(4);
        setUseDefaultMargins(false);
        setPadding(10, 10, 10, 10);
        cardBoard = new Card[4][4];
        cardWidth = (getWidth() - 20) / 4;
        int[][] raw = json != null ? gson.fromJson(json, int[][].class) : null;
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                Card c = new Card(context);
                if (raw != null) c.setNumber(raw[i][j]);
                cardBoard[i][j] = c;
                addView(c, cardWidth, cardWidth);
            }
        }
        if (raw == null)
        {
            addRandom();
            addRandom();
        }
    }

    public void resetBoard()
    {
        score = 0;
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                cardBoard[i][j].setNumber(0);
            }
        }
        addRandom();
        addRandom();
    }

    private void addRandom()
    {
        ArrayList<int[]> list = new ArrayList<>();
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                if (cardBoard[i][j].getNumber() == 0)
                {
                    list.add(new int[]{i, j});
                }
            }
        }
        if (!list.isEmpty())
        {
            int[] randomSpot = list.get(rng.nextInt(list.size()));
            cardBoard[randomSpot[0]][randomSpot[1]].setNumber(rng.nextDouble() > 0.3 ? 2 : 4);
        }
    }

    public boolean canMove()
    {
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                if (cardBoard[i][j].getNumber() == 0)
                    return true;
                if (i - 1 >= 0 && cardBoard[i - 1][j].getNumber() == cardBoard[i][j].getNumber())
                    return true;
                if (j - 1 >= 0 && cardBoard[i][j - 1].getNumber() == cardBoard[i][j].getNumber())
                    return true;
                if (i + 1 < 4 && cardBoard[i + 1][j].getNumber() == cardBoard[i][j].getNumber())
                    return true;
                if (j + 1 < 4 && cardBoard[i][j + 1].getNumber() == cardBoard[i][j].getNumber())
                    return true;
            }
        }
        return false;
    }

    public boolean wonGame()
    {
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                if (cardBoard[i][j].getNumber() == 2048)
                    return true;
            }
        }
        return false;
    }

    public Board execute(Direction direction)
    {
        boolean move = false;
        boolean vertical = direction.equals(Direction.UP) || direction.equals(Direction.DOWN);
        for (int line = 0; line < 4; line++)
        {
            int previous = direction.start;
            int pos = direction.start;
            for (int element = direction.start; direction.check(element, direction.end); element = direction.next(element))
            {
                if (cardBoard[invertIfVertical(line, element, vertical)][invertIfVertical(element, line, vertical)].getNumber() != 0)
                {
                    if (previous != element && cardBoard[invertIfVertical(line, element, vertical)][invertIfVertical(element, line, vertical)].getNumber() == cardBoard[invertIfVertical(line, previous, vertical)][invertIfVertical(previous, line, vertical)].getNumber())
                    {
                        cardBoard[invertIfVertical(line, previous, vertical)][invertIfVertical(previous, line, vertical)].setNumber(cardBoard[invertIfVertical(line, previous, vertical)][invertIfVertical(previous, line, vertical)].getNumber() * 2);
                        cardBoard[invertIfVertical(line, element, vertical)][invertIfVertical(element, line, vertical)].setNumber(0);
                        score += cardBoard[invertIfVertical(line, previous, vertical)][invertIfVertical(previous, line, vertical)].getNumber();
                        previous = pos;
                        move = true;
                    }
                    else if (pos != element)
                    {
                        cardBoard[invertIfVertical(line, pos, vertical)][invertIfVertical(pos, line, vertical)].setNumber(cardBoard[invertIfVertical(line, element, vertical)][invertIfVertical(element, line, vertical)].getNumber());
                        cardBoard[invertIfVertical(line, element, vertical)][invertIfVertical(element, line, vertical)].setNumber(0);
                        previous = previous == pos ? previous : direction.next(previous);
                        pos = direction.next(pos);
                        move = true;
                    }
                    else
                    {
                        previous = previous == pos ? previous : direction.next(previous);
                        pos = direction.next(pos);
                    }
                }
            }
        }
        if (move)
        {
            addRandom();
        }
        return this;
    }

    private int invertIfVertical(int index1, int index2, boolean vertical)
    {
        return vertical ? index2 : index1;
    }

    public void print()
    {
        String output = "putput = \n";
        for (int row = 0; row < 4; row++)
        {
            for (int col = 0; col < 4; col++)
            {
                output += cardBoard[row][col].getNumber() + (col < 3 ? " | " : "");
            }
            output += "\n-------------\n";
        }
        output += "\n\n";
        Log.e("OUTPUT", output);
    }

    public String getJSON()
    {
        int[][] values = new int[4][4];
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                values[i][j] = cardBoard[i][j].getNumber();
        return gson.toJson(values);
    }

    public enum Direction
    {
        UP(new SmallerThanOrEquals(), new Increment(), 0, 3, 2),
        DOWN(new GreaterThanOrEquals(), new Decrement(), 3, 0, 1),
        LEFT(new SmallerThanOrEquals(), new Increment(), 0, 3, 2),
        RIGHT(new GreaterThanOrEquals(), new Decrement(), 3, 0, 1);

        private final Comparator comparator;
        private final Operation operation;
        public final int start;
        public final int end;
        public final int nextToEnd;

        Direction(Comparator comparator, Operation operation, int start, int end, int nextToEnd)
        {
            this.comparator = comparator;
            this.operation = operation;
            this.start = start;
            this.end = end;
            this.nextToEnd = nextToEnd;
        }

        public int next(int integer)
        {
            return operation.execute(integer);
        }

        public boolean check(int a, int b)
        {
            return comparator.compare(a, b);
        }
    }

    private interface Comparator
    {
        boolean compare(int a, int b);
    }

    private static class GreaterThanOrEquals implements Comparator
    {
        @Override
        public boolean compare(int a, int b)
        {
            return a >= b;
        }
    }

    private static class SmallerThanOrEquals implements Comparator
    {
        @Override
        public boolean compare(int a, int b)
        {
            return a <= b;
        }
    }

    private interface Operation
    {
        int execute(int integer);
    }

    private static class Increment implements Operation
    {
        @Override
        public int execute(int integer)
        {
            return ++integer;
        }
    }

    private static class Decrement implements Operation
    {
        @Override
        public int execute(int integer)
        {
            return --integer;
        }
    }
}
