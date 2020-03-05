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

    //Если зашел в creator, то снова сдублироваться он сможет только после выхода из него
    // (иначе там будут создаваться копии, копии копий и т.д. пока все не покинут область)
    private boolean flag_untouchable;

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
        y = Math.random() * (field.getSize().getHeight() - 2 * radius) + radius;
        //флаг
        flag_untouchable = false;//по умолчанию
        //Создаем новый экземпляр потока, передавая аргументом
        //ссылку на класс, реализующий Runnable(т.е. на bouncingball)
        Thread thisThread = new Thread(this);
        //Запускаем поток
        thisThread.start();
    } //Конец конструктора
    //КОНСТРУКТОРА КОПИРОВАНИЯ
    public BouncingBall (Field field, BouncingBall b)
    {
        this.field = field;

        radius = b.radius;
        speed = b.speed;

        speedX = b.speedX;
        speedY = b.speedY;

        color = b.color;

        x = b.x;
        y = b.y;

        flag_untouchable = b.flag_untouchable;

        Thread thisThread = new Thread(this);

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
                else if(x + speedX >= field.getWidth() - radius) //от правой стенки
                    {
                        speedX = -speedX;
                        x = new Double(field.getWidth() - radius).intValue();
                    }
                    else if (y + speedY <= radius)
                        {
                            speedY = -speedY;
                            y = radius;
                        }
                        else if (y + speedY >= field.getHeight() - radius)
                            {
                                speedY = -speedY;
                                y = new Double(field.getHeight()-radius).intValue();
                            }
                            else if((field.getTeleport() != null) && //ПОПАДАНИЕ В ТЕЛЕПОРТ
                        (x + speedX - radius >= field.getTeleport().getX_in()) &&
                        (x + speedX + radius <= field.getTeleport().getX_in() + field.getTeleport().getRADIUS() * 2) &&
                        (y + speedY - radius >= field.getTeleport().getY_in()) &&
                        (y + speedY + radius <= field.getTeleport().getY_in() + field.getTeleport().getRADIUS() * 2) )
                            {
                                x = field.getTeleport().getX_out() + field.getTeleport().getRADIUS() ;
                                y = field.getTeleport().getY_out() + field.getTeleport().getRADIUS() ;
                            }
                            else if((field.getDestroyer() != null) &&
                        (x + speedX - radius >= field.getDestroyer().getX_destr()) &&
                        (x + speedX + radius <= field.getDestroyer().getX_destr() + field.getDestroyer().getRADIUS() * 2) &&
                        (y + speedY - radius >= field.getDestroyer().getY_destr()) &&
                        (y + speedY + radius <= field.getDestroyer().getY_destr() + field.getDestroyer().getRADIUS() * 2))
                            {
                                synchronized (field.getBalls()) {
                                field.getBalls().remove(this);
                                }
                            }
                            else if((field.getCreator() != null) &&
                        (x + speedX - radius >= field.getCreator().getX_creat()) &&
                        (x + speedX + radius <= field.getCreator().getX_creat() + field.getCreator().getRADIUS() * 2) &&
                        (y + speedY - radius >= field.getCreator().getY_creat()) &&
                        (y + speedY + radius <= field.getCreator().getY_creat() + field.getCreator().getRADIUS() * 2))
                            {
                                if(flag_untouchable == false)
                                {
                                    flag_untouchable = true; //не трогаем больше(и дубликат тоже будет с тем же флагом пока что)
                                    BouncingBall duplicate = new BouncingBall(field, this);
                                    double angle = Math.random() * 2 * Math.PI;
                                    duplicate.speedX = 3 * Math.cos(angle);
                                    duplicate.speedY = 3 * Math.sin(angle);
                                    synchronized (field.getBalls()){
                                    field.getBalls().add(duplicate);
                                    }
                                    //двигаемся дальше тоже
                                    x += speedX;
                                    y += speedY;
                                } else //заглянуть на будущий шаг, мб уже пора снять флаг (прям перед выходом т.е.)
                                {
                                    x += speedX;
                                    y += speedY;
                                    if ((x + speedX - radius <= field.getCreator().getX_creat()) ||
                                            (x + speedX + radius >= field.getCreator().getX_creat() + field.getCreator().getRADIUS() * 2) ||
                                            (y + speedY - radius <= field.getCreator().getY_creat()) ||
                                            (y + speedY + radius >= field.getCreator().getY_creat() + field.getCreator().getRADIUS() * 2))
                                    {
                                        flag_untouchable = false;
                                    }
                                }
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
