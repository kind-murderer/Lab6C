import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MainFrame extends JFrame
{
    private static final int WIDTH = 700; //размер окна приложения
    private static final int HEIGHT = 500;

    private JMenuItem pauseMenuItem;
    private JMenuItem resumeMenuItem;

    private Field field = new Field(); //Поле, по которому прыгают мячи

    //КОНСТРУКТОР
    public MainFrame()
    {
        super("Программирование и синхронизация потоков");
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        //Отцентрировать окно приложения на экране
        setLocation((kit.getScreenSize().width - WIDTH)/2, (kit.getScreenSize().height - HEIGHT)/2);
        //setExtendedState(MAXIMIZED_BOTH);

        //Создать меню
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu ballMenu = new JMenu("Мячи");
        Action addBallAction = new AbstractAction("Добавить мяч") {
            public void actionPerformed(ActionEvent event) {
                field.addBall();
                if(!pauseMenuItem.isEnabled() && !resumeMenuItem.isEnabled())
                {
                    //ни один из пунктов меню не являются доступными - сделать доступным "Паузу"
                    pauseMenuItem.setEnabled(true);
                }
            }
        };
        menuBar.add(ballMenu);
        ballMenu.add(addBallAction);

        JMenu controlMenu = new JMenu("Управление");
        menuBar.add(controlMenu);
        Action pauseAction = new AbstractAction("Приостановить движ") {
            public void actionPerformed(ActionEvent e) {
                field.pause();
                pauseMenuItem.setEnabled(false);
                resumeMenuItem.setEnabled(true);
            }
        };
        pauseMenuItem = controlMenu.add(pauseAction);
        pauseMenuItem.setEnabled(false);
        Action resumeAction = new AbstractAction("Возобновить движ") {
            public void actionPerformed(ActionEvent e) {
                field.resume();
                pauseMenuItem.setEnabled(true);
                resumeMenuItem.setEnabled(false);
            }
        };
        resumeMenuItem = controlMenu.add(resumeAction);
        resumeMenuItem.setEnabled(false);

        JButton buttonTeleportAndOther = new JButton("Добавить телепорт и др");
        buttonTeleportAndOther.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                field.createTeleport();
            }
        });
        Box hboxButtons = Box.createHorizontalBox();
        hboxButtons.add(buttonTeleportAndOther);
        //hboxButtons.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        menuBar.add(hboxButtons);
        //Добавить в центр граничной компановки поле Field
        getContentPane().add(field, BorderLayout.CENTER);
    } //Конец конструктора

    //Главный метод приложения
    public static void main(String[] args)
    {
        MainFrame frame = new MainFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
