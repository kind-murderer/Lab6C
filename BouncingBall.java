import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class BouncingBall implements Runnable
{
    //макс радиус, скорость и мин радиус, которые можетиметь мяч
    private static final int MAX_RADIUS = 40;
    private static final int MIN_RADIUS = 3;
    private static final int MAX_SPEED = 15;

    private Field field;
    private int radius;
    private Color color;

    //Текущие координаты мяча
    private double x;
    private double y;

    //Вертикальная и горизонтальная компонента скорости
    private int speed;
    private double speedX;
    private double speedY;

    //КОНСТРУКТОР
    public BouncingBall (Field field)
    {
        //необходимо иметь ссылку на поле, по которому прыгает мяч,
        //чтобы отслеживать выход за его пределы
        this.field = field;
        //радиус случайного мяча
        radius = new Double(Math.random()*(MAX_RADIUS - MIN_RADIUS)).intValue() + MIN_RADIUS;
        //расчет скорости
        speed = new Double(Math.round(5 * MAX_SPEED / radius)).intValue();
        if (speed > MAX_SPEED)
        {
            speed = MAX_SPEED;
        }
        //начальное направление скорости случайно
        //угол от 0 до 2PI
        double angle = Math.random() * 2 * Math.PI;
        speedX = 3 * Math.cos(angle);
        speedY = 3 * Math.sin(angle);
        //Цвет мяча случаен
        color = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
        //Начальное положение случайно
        x = Math.random() * (field.getSize().getWidth() - 2 * radius) + radius;
        y = Math.random() * (field.getSize().getWidth() - 2 * radius) + radius;;
        //Создаем новый экземпляр потока, передавая аргументом
        //ссылку на класс, реализующий Runnable(т.е. на bouncingball)
        Thread thisThread = new Thread(this);
        //Запускаем поток
        thisThread.start();
    } //Конец конструктора
    //Метод run() исполняется внутри потока. Когда он завершвет работу,
    //то завершается и поток
    public void run()
    {
        try
        {
            //крутим бесконечный цикл, пока нас не прервут
            while(true)
            {
                //Синхронизация потоков на самом объекте поля
                //Если движение разрешено - управление будет возаращено в метод
                //В противном случае - активный поток заснет
                field.canMove(this);
                if(x + speedX <= radius) //отскакиваем от  левой стенки, если врезаемся в нее
                {
                    speedX = -speedX;
                    x = radius;
                }
                else
                    if(x + speedX >= field.getWidth() - radius) //от правой стенки
                    {
                        speedX = -speedX;
                        x = new Double(field.getWidth() - radius).intValue();
                    }
                    else
                        if (y + speedY <= radius)
                        {
                            //System.out.println("I was here");
                            speedY = -speedY;
                            y = radius;
                        }
                        else
                            if (y + speedY >= field.getHeight() - radius)
                            {
                                speedY = -speedY;
                                y = new Double(field.getHeight()-radius).intValue();
                            }
                            else //просто смещаемся
                            {
                                x += speedX;
                                y += speedY;
                            }
                            Thread.sleep(16-speed);
            }
        } catch (InterruptedException ex)
        {
            System.out.println("Нас прервали."); // и ничего не делаем больше
        }
    }

    //Метод прорисовки самого себя
    public void paint(Graphics2D canvas)
    {
        canvas.setColor(color);
        canvas.setPaint(color);
        Ellipse2D.Double ball = new Ellipse2D.Double(x - radius, y - radius, 2 * radius, 2 * radius);
        canvas.draw(ball);
        canvas.fill(ball);
    }
}
