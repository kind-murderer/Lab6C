import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Creator
{
    //левый верхний край
    private double x_creat;
    private double y_creat;

    private static final int RADIUS = 42; //Тоже не синхронизирован с другими прямоугольниками, мб, и не надо

    //КОНСТРУКТОР
    public Creator(Field field)
    {
        x_creat = Math.random() * (field.getSize().getWidth() - 2 * RADIUS);
        y_creat = Math.random() * (field.getSize().getHeight() - 2 * RADIUS);
    }
    //Конец конструктора

    public double getX_creat() {
        return x_creat;
    }
    public double getY_creat() {
        return y_creat;
    }
    public static int getRADIUS() {
        return RADIUS;
    }

    //Метод прорисовки самого себя
    public void paint(Graphics2D canvas)
    {
        canvas.setColor(Color.WHITE);
        canvas.setPaint(Color.WHITE);
        Rectangle2D.Double creator = new Rectangle2D.Double(x_creat, y_creat, 2 * RADIUS, 2 * RADIUS);
        canvas.draw(creator);
        canvas.fill(creator);
    }
}
