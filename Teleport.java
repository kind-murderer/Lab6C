import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

public class Teleport
{
    //центры
    private double x_in;
    private double y_in;
    private double x_out;
    private double y_out;

    private static final int RADIUS = 42; //вот тут, кстати, не синхронизировано с ball, можно поместить это в field,
    // обозначить там "максимальный радиус круга, возможный на этом поле", мы все равно передаем field в BouncingBall
    //вообще, пусть будет 42, больше шансов влететь в поле
    private Field field;

    //КОНСТРУКТОР
    public Teleport(Field field)
    {
        this.field = field;

        x_in = 0; //пусть вход будет где-нибудь на левой стенке
        y_in = Math.random() * (field.getSize().getHeight() - 2 * RADIUS);

        x_out = field.getWidth() - 2*RADIUS; //пусть выход будет где-нибудь на левой стенке
        y_out = Math.random() * (field.getSize().getHeight() - 2 * RADIUS);
    }
    //Конец конструктора

    //геттеры
    public double getX_in() {
        return x_in;
    }
    public double getY_in() {
        return y_in;
    }
    public double getX_out() {
        return x_out;
    }
    public double getY_out() {
        return y_out;
    }
    public static int getRADIUS() {
        return RADIUS;
    }

    //Метод прорисовки самого себя
    public void paint(Graphics2D canvas)
    {
        canvas.setColor(Color.BLUE);
        canvas.setPaint(Color.BLUE);
        Rectangle2D.Double portal_1 = new Rectangle2D.Double(x_in, y_in, 2 * RADIUS, 2 * RADIUS);
        canvas.draw(portal_1);
        canvas.fill(portal_1);

        canvas.setColor(Color.ORANGE);
        canvas.setPaint(Color.ORANGE);
        Rectangle2D.Double portal_2 = new Rectangle2D.Double(x_out, y_out, 2 * RADIUS, 2 * RADIUS);
        canvas.draw(portal_2);
        canvas.fill(portal_2);
    }
}
