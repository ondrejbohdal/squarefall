import java.awt.*;

public class Square {
    private int num;
    private Color col;

    public Square(int maxnum) {
        num = 1 + (int)(Math.random() * maxnum);
        if (num % 2==1)
            col=Color.green;
        else
            col=Color.orange;
    }

    public Color getColor() {
        return col;
    }

    public int getNumber() {
        return num;
    }
}


