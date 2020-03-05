
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Field extends JPanel
{
    //Флаг преостановления движения
    private boolean paused;
    //Динамический список прыгающих мячей
    private ArrayList<BouncingBall> balls = new ArrayList<BouncingBall>(10);
    //Объект телепорта
    private Teleport Portal = null ;//йо =new Teleport только тогда, когда будет нарисовано в MainFrame, (в конструкторе телепорта требуется размер, а он 0,0 по умолчанию)
    private Destroyer myDestroyer = null;
    //Таймер отвечает за регулярную генерацию событий ActionEvent
    private Timer repaintTimer = new Timer(10, new ActionListener(){
        public void actionPerformed(ActionEvent ev)
        {
            repaint();
        }
    });

    //КОНСТРУКТОР
    public Field()
    {
        setBackground(Color.LIGHT_GRAY);

        repaintTimer.start();
    }
    //Конец констркутора
    //Унаследованный от JPanel метод
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D) g;
        //Прорисовка портла, если добавлен
        if(Portal != null) Portal.paint(canvas);
        if(myDestroyer != null) myDestroyer.paint(canvas);
        //Последрвательно запросить перерисовку от всех мячей из списка
        for(BouncingBall ball:balls)
        {
            ball.paint(canvas);
        }

    }
    //Метод добавления нового мяча
    public void addBall()
    {
        balls.add(new BouncingBall(this));
    }

    //Метод создания ТЕЛЕПОРТА
    public void createTeleport()
    {
        Portal = new Teleport(this);
    }
    public Teleport getTeleport()//в ball нужен *этот* порт
    { return Portal;}

    //Все то же с РАЗРУШИТЕЛЕМ, что и с Телепортом выше
    public void createDestroyer()
    { myDestroyer = new Destroyer(this);}
    public Destroyer getDestroyer()//в ball нужен *этот* порт
    { return myDestroyer;}

    //Синхронизированный метод(только один поток мб внутри)
    public synchronized void pause()
    { paused = true;}
    public synchronized void resume()
    {
        paused = false;
        notifyAll();
    }
    public synchronized void canMove(BouncingBall ball) throws InterruptedException
    {
       if(paused)
       { wait();} //если режим паузы включен, поток засыпает
    }

    public ArrayList<BouncingBall> getBalls()
    {
        return balls;
    }
}
