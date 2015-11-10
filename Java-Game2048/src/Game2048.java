/**
 *
 * @author Pieter-Jan
 */
public class Game2048
{
    int grid[][] =
    {
        {
            8, 2, 2, 4
        },
        {
            0, 0, 0, 2
        },
        {
            2, 0, 0, 2
        },
        {
            2, 2, 2, 2
        }
    };

    public static void main(String[] args)
    {
        Game2048 game = new Game2048();
        game.print();
        game.execute(Direction.UP);
        game.print();
    }

    public void execute(Direction direction)
    {
        boolean invert = direction.equals(Direction.UP) || direction.equals(Direction.DOWN);
        // loop over lines in direction
        for (int line = 0; line < 4; line++)
        {
            int previous = direction.start;
            int pos = direction.start;
            for (int element = direction.start; direction.check(element, direction.end); element = direction.next(element))
            {
                if (grid[invert(line, element, invert)][invert(element, line, invert)] != 0)
                {
                    if (previous != element && grid[invert(line, element, invert)][invert(element, line, invert)] == grid[invert(line, previous, invert)][invert(previous, line, invert)])
                    {
                        grid[invert(line, previous, invert)][invert(previous, line, invert)] *= 2;
                        grid[invert(line, element, invert)][invert(element, line, invert)] = 0;
                        previous = pos;
                    }
                    else if (pos != element)
                    {
                        grid[invert(line, pos, invert)][invert(pos, line, invert)] = grid[invert(line, element, invert)][invert(element, line, invert)];
                        grid[invert(line, element, invert)][invert(element, line, invert)] = 0;
                        previous = previous == pos ? previous : direction.next(previous);
                        pos = direction.next(pos);
                    }
                    else
                    {
                        previous = previous == pos ? previous : direction.next(previous);
                        pos = direction.next(pos);
                    }
                }
            }
        }
    }

    private int invert(int index1, int index2, boolean invert)
    {
        return invert ? index2 : index1;
    }

    public void print()
    {
        String output = new String();
        for (int row = 0; row < 4; row++)
        {
            for (int col = 0; col < 4; col++)
            {
                output += grid[row][col] + (col < 3 ? " | " : "");
            }
            output += "\n-------------\n";
        }
        output += "\n\n";
        System.out.print(output);
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

        public boolean check(int a)
        {
            return comparator.compare(a, end);
        }
    }

    private interface Comparator
    {
        public boolean compare(int a, int b);
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
        public int execute(int integer);
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

    // legacy
    public void up()
    {
        // loop over columns
        for (int col = 0; col < 4; col++)
        {
            // loop over rows
            for (int row = 0; row < 4; row++)
            {
                // check if value is 0
                if (grid[col][row] == 0)
                {
                    // loop over all remaining rows and shift all values one position to the left
                    for (int i = row; i < 3; i++)
                    {
                        grid[col][i] = grid[col][i + 1];
                    }
                    grid[col][3] = 0;
                }
                // check if value at row is same as value at next row
                if (row + 1 < 4 && grid[col][row] == grid[col][row + 1])
                {
                    // double value at current row and remove value at next row
                    grid[col][row] *= 2;
                    grid[col][row + 1] = 0;
                }
            }
        }
    }
}
