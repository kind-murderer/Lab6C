import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Destroyer
{
    //левый верхний край
    private double x_destr;
    private double y_destr;

    private static final int RADIUS = 42; //Тоже не синхронизирован с другими прямоугольниками, мб, и не надо

    //КОНСТРУКТОР
    public Destroyer(Field field)
    {
        x_destr = Math.random() * (field.getSize().getWidth() - 2 * RADIUS);
        y_destr = Math.random() * (field.getSize().getHeight() - 2 * RADIUS);
    }
    //Конец конструктора
    public double getX_destr() {
        return x_destr;
    }
    public double getY_destr() {
        return y_destr;
    }
    public static int getRADIUS() {
        return RADIUS;
    }

    //Метод прорисовки самого себя
    public void paint(Graphics2D canvas)
    {
        canvas.setColor(Color.DARK_GRAY);
        canvas.setPaint(Color.DARK_GRAY);
        Rectangle2D.Double destruct = new Rectangle2D.Double(x_destr, y_destr, 2 * RADIUS, 2 * RADIUS);
        canvas.draw(destruct);
        canvas.fill(destruct);
    }
}
