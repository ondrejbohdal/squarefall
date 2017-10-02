import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Stack;
import java.util.Vector;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class MainClass extends JFrame implements ActionListener, KeyListener {
    JLabel task1, task2, scorelb,timelb;
    int maxnum, tasknum, gameon, score, oldsize,time,seconds,posx,posy;
    String halloffame = "";
    String sqtask="";
    Stack<Square> column1 = new Stack<Square>();
    Stack<Square> column2 = new Stack<Square>();
    Stack<Square> column3 = new Stack<Square>();
    int [] taskchoice = new int [2];
    Vector<Stack<Square>> columns = new Vector<Stack<Square>>();
    Vector<Player> players = new Vector<Player>();
    javax.swing.Timer timer,tm;
    JMenuBar menuBar;
    JMenu menu;
    JMenuItem menuItem1, menuItem2, menuItem3;
    Square sq;
    Boolean enabledkeys=true;
    String [] svkvocab = {"Nová hra", "Návod", "Sieň slávy",
                          "<html>Úloha:<br>Polož padajúci štvorec na štvorec,<br>aby ich súčet bol</html>",
                          "Vyhral si!  Ale ak sa chceš dostať do siene slávy, skús znova :-).", "rovný ", "menší ako ","väčší ako ",
                          "<html>1. Štvorec posúvaš šípkami.<br>"
                          + "2. Ak stlačíš šípku Dole, štvorec spadne rovno dole.<br>"
                          + "3. Padajúci štvorec padne každých niekoľko sekúnd o jedno políčko.<br>"
                          + "4. Cieľom hry je zbaviť sa všetkých štvorcov.<br>"
                          + "5. Dva štvorce zmiznú ak ich súčet spĺňa zadanie.<br>"
                          + "7. Ak v nejakom rade nie je miesto na nový štvorec, hra končí.</html>",
                          "Prehral si! Môžeš však skúsiť znova.",
                          "Na spustenie hry choď do menu, a stlač tlačidlo Nová hra.",
                          "Vitaj!","Meno","Skóre","Čas","Naozaj?","<html>Naozaj chceš začať novú hru?<br>"
                          + "Tvoja aktuálna hra sa potom stratí.</html>","Áno","Nie",
                          "Naozaj chceš ukončiť hru?","Nastavenia","1 až 10","1 až 20","1 až 50","Zvoľ, aké veľké chceš mať čísla v hre.",
                          "Meno","Skóre","Čas [min]","Rýchlo","Stredne","Pomaly","Ako rýchlo má padať štvorec?","Čas (s)",
                          "<html>Vyhral si! Toto ale nie je všetko!<br> Dostal si sa do siene slávy.<br> Zadaj svoje meno, prosím.</html>","Prehra","Výhra"
                         };
    String [] engvocab = {"New Game","Rules","Hall of Fame",
                          "<html>Task:<br>Put the falling block <br>on one of the blocks <br>so that their sum is</html>",
                          "You have won! But if you want to get into the hall of fame, try again :-).","equal to ","smaller than ","bigger than ",
                          "<html>1. You can move the block using arrows.<br>"
                          + "2. If you use down-arrow the block will fall down.<br>"
                          + "3. The block falls down every several seconds by one step.<br>"
                          + "4. The aim of this game is to get rid of all blocks.<br>"
                          + "5. The two blocks will disappear if their sum fulfills the task.<br>"
                          + "7. If there is no space for the falling block in one of the rows, you lose the game.</html>",
                          "You have lost! But you can try again.",
                          "For playing the game, go into the menu and click on the button New Game.",
                          "Welcome!","Name","Score","Time","Are you sure?","<html>Do you really want to start a new game?<br>"
                          + "Your current game will be lost.</html>","Yes","No","Do you really want to end the game?",
                          "Settings","1 to 10","1 to 20","1 to 50", "Choose how big numbers you want to have in the game.",
                          "Name","Score","Time [min]","Quickly","Medium","Slowly","How quickly should the square fall?","Time (s)",
                          "<html>You have won! But this is not all!<br> You have got into the hall of fame.<br> Enter your name please.</html>","Defeat","Victory"
                         };

    String [] vocab = new String [svkvocab.length];

    public MainClass() {
        super("Squarefall");
        setSize(750, 750);
        setLayout(null);

        //Create the menu bar.
        Object[] options = {"English",
                            "Slovenčina"
                           };

        int n = JOptionPane.showOptionDialog(this,
                                             "Please choose your language. "
                                             + "Zvoľ si svoj jazyk, prosím.",
                                             "Language/Jazyk",
                                             JOptionPane.YES_NO_CANCEL_OPTION,
                                             JOptionPane.QUESTION_MESSAGE,
                                             null,
                                             options,
                                             options[0]);
        if (n == 1)
            for (int i = 0; i < vocab.length; i++)
                vocab[i] = svkvocab[i];
        else
            for (int i = 0; i < vocab.length; i++)
                vocab[i] = engvocab[i];


        menuBar = new JMenuBar();

        //Build the first menu.
        menu = new JMenu("Menu");
        menuBar.add(menu);

        //a group of JMenuItems
        menuItem1 = new JMenuItem(vocab[0]);//""New Game"
        menuItem1.addActionListener(this);
        menu.add(menuItem1);

        menuItem2 = new JMenuItem(vocab[1]);// "Rules"
        menuItem2.addActionListener(this);
        menu.add(menuItem2);

        menuItem3 = new JMenuItem(vocab[2]);// "Hall of Fame"
        menuItem3.addActionListener(this);
        menu.add(menuItem3);

        setJMenuBar(menuBar);
        task1 = newlab(50, 50, 250, 100, vocab[3]);

        //"<html>Task:<br>Put the falling block <br>on one of the blocks <br>so that their sum is</html>"
        //change it to 120 - the height
        task1.setFont(new Font("Arial", Font.BOLD, 20));

        task2 = newlab(50, 150, 250, 30,"");
        task2.setFont(new Font("Arial", Font.BOLD, 20));

        scorelb = newlab(50, getHeight()-200, 150, 30, vocab[13] + ": 0");
        scorelb.setFont(new Font("Arial", Font.BOLD, 20));

        timelb = newlab(50, getHeight()-250, 150, 30, vocab[32] + ": 0");
        timelb.setFont(new Font("Arial", Font.BOLD, 20));

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {}
            public void windowClosing(WindowEvent e) {
                if (gameon == 1) {
                    Object[] options2 = {vocab[17], vocab[18]};//yes, no
                    int u = JOptionPane.showOptionDialog(MainClass.this,
                                                         vocab[19],//do you really want to end game?
                                                         vocab[15],//are you sure?
                                                         JOptionPane.YES_NO_CANCEL_OPTION,
                                                         JOptionPane.QUESTION_MESSAGE,
                                                         null,
                                                         options2,
                                                         options2[0]);
                    if (u == 0) {
                        createFile("Players.txt");
                        System.exit(0);
                    }
                    else
                        ;
                }
                else {
                    createFile("Players.txt");
                    System.exit(0);
                }

            }
        });

        timer = new Timer(0, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {

                oldsize=columns.elementAt(posx).size();

                if (posy==columns.elementAt(posx).size() && columns.elementAt(posx).size()!=0)

                {
                    switch (taskchoice[0]) {
                    case 0:  {
                        if ((sq.getNumber()+columns.get(posx).peek().getNumber())==taskchoice[1])
                            columns.elementAt(posx).pop();
                        else columns.elementAt(posx).add(sq);
                    }
                    break;
                    case 1:  {
                        if ((sq.getNumber()+columns.get(posx).peek().getNumber())<taskchoice[1])
                            columns.elementAt(posx).pop();
                        else columns.elementAt(posx).add(sq);
                    }
                    break;
                    case 2:  {
                        if ((sq.getNumber()+columns.get(posx).peek().getNumber())>taskchoice[1])
                            columns.elementAt(posx).pop();
                        else columns.elementAt(posx).add(sq);
                    }
                    break;
                    default:
                        break;
                    }

                    score++;
                    scorelb.setText(vocab[13] +": "+(score));
                    repaint();
                    if ((column1.size()+column2.size()+column3.size())==0) {
                        gameon=0;
                        sq = new Square(maxnum);
                        posx=1;
                        posy=6;
                        timer.stop();
                        tm.stop();
                        if (players.size()==10) {
                            if (score<players.elementAt(9).getScore())
                            {   //ask for the name+other operations + find the suitable position
                                //JOptionPane.showMessageDialog(MainClass.this, vocab[4]);
                                //the same as below
                                String plname = (String)JOptionPane.showInputDialog(
                                                    MainClass.this,
                                                    vocab[33],
                                                    vocab[35],
                                                    JOptionPane.PLAIN_MESSAGE,
                                                    null,
                                                    null,
                                                    null);

                                int k=0;
                                for (k=0; k<players.size(); k++)
                                {
                                    if (score<players.elementAt(k).getScore()) {
                                        break;
                                    }
                                    if (score==players.elementAt(k).getScore())
                                    {
                                        while (score==players.elementAt(k).getScore() && k<players.size())
                                        {
                                            if (time<=players.elementAt(k).getTime())
                                            {
                                                break;
                                            }
                                            k++;
                                        }
                                        break;
                                    }
                                }

                                players.add(k, new Player(plname, score, time));
                                players.remove(10);

                            }
                            else if (score==players.elementAt(9).getScore())
                            {
                                if (time<=players.elementAt(9).getTime())
                                {
                                    String plname = (String)JOptionPane.showInputDialog(
                                                        MainClass.this,
                                                        vocab[33],
                                                        vocab[35],
                                                        JOptionPane.PLAIN_MESSAGE,
                                                        null,
                                                        null,
                                                        null);

                                    int k=0;
                                    for (k=0; k<players.size(); k++)
                                    {
                                        if (score<players.elementAt(k).getScore()) {
                                            break;
                                        }
                                        if (score==players.elementAt(k).getScore())
                                        {
                                            while (score==players.elementAt(k).getScore() && k<players.size())
                                            {
                                                if (time<=players.elementAt(k).getTime())
                                                {
                                                    break;
                                                }
                                                k++;
                                            }
                                            break;
                                        }
                                    }

                                    players.add(k, new Player(plname, score, time));
                                }
                                else JOptionPane.showMessageDialog(MainClass.this, vocab[4],vocab[35],JOptionPane.INFORMATION_MESSAGE);
                            }
                            else JOptionPane.showMessageDialog(MainClass.this, vocab[4],vocab[35],JOptionPane.INFORMATION_MESSAGE);
                        }
                        else {
                            //we just insert the new player
                            //ask for name - input dialog + all other things
                            String plname = (String)JOptionPane.showInputDialog(
                                                MainClass.this,
                                                vocab[33],
                                                vocab[35],
                                                JOptionPane.PLAIN_MESSAGE,
                                                null,
                                                null,
                                                null);


                            int k=0;
                            for (k=0; k<players.size(); k++)
                            {
                                if (score<players.elementAt(k).getScore()) {
                                    break;
                                }
                                if (score==players.elementAt(k).getScore())
                                {
                                    while (score==players.elementAt(k).getScore() && k<players.size())
                                    {
                                        if (time<=players.elementAt(k).getTime())
                                        {
                                            break;
                                        }
                                        k++;
                                    }
                                    break;
                                }
                            }

                            players.add(k, new Player(plname, score, time));

                        }

                    }
                    if (columns.elementAt(0).size()>6||columns.elementAt(1).size()>6||columns.elementAt(2).size()>6) {
                        gameon=0;
                        timer.stop();
                        tm.stop();
                        JOptionPane.showMessageDialog(MainClass.this, vocab[9],vocab[34],JOptionPane.WARNING_MESSAGE);
                    }
                    if (gameon==1)
                    {
                        timer.stop();
                        sq = new Square(maxnum);
                        posx=1;
                        posy=6;
                        timer.setInitialDelay(seconds);
                        timer.setDelay(seconds);
                        taskchoice = task();
                        if (taskchoice[0] == 0)
                        {
                            task2.setText(vocab[5] + Integer.toString(taskchoice[1]));//"equal to "
                            sqtask="= ";
                        }
                        if (taskchoice[0] == 1)
                        {
                            task2.setText(vocab[6] + Integer.toString(taskchoice[1]));//"smaller than "
                            sqtask="< ";
                        }
                        if (taskchoice[0] == 2)
                        {
                            task2.setText(vocab[7] + Integer.toString(taskchoice[1]));//"bigger than "
                            sqtask="> ";
                        }
                        sqtask=sqtask+taskchoice[1];
                        enabledkeys=true;
                        timer.start();
                    }
                }
                else if (oldsize==0 && posy==0)
                {
                    columns.elementAt(posx).add(sq);
                    sq = new Square(maxnum);
                    posx=1;
                    posy=6;
                    score++;
                    scorelb.setText(vocab[13] +": "+(score));
                    taskchoice = task();
                    if (taskchoice[0] == 0)
                    {
                        task2.setText(vocab[5] + Integer.toString(taskchoice[1]));//"equal to "
                        sqtask="= ";
                    }
                    if (taskchoice[0] == 1)
                    {
                        task2.setText(vocab[6] + Integer.toString(taskchoice[1]));//"smaller than "
                        sqtask="< ";
                    }
                    if (taskchoice[0] == 2)
                    {
                        task2.setText(vocab[7] + Integer.toString(taskchoice[1]));//"bigger than "
                        sqtask="> ";
                    }
                    sqtask=sqtask+taskchoice[1];
                    enabledkeys=true;
                    timer.stop();
                    timer.setInitialDelay(seconds);
                    timer.setDelay(seconds);
                    timer.start();
                }
                else posy--;
                repaint();
            }
        });

        tm = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                time++;
                timelb.setText(vocab[32] + ": " + time);
            }
        });
        addKeyListener(this);
    }

    public void drawCenteredString(String s, int xp, int yp, Graphics g) {
        FontMetrics fm = g.getFontMetrics();
        int x = xp+(80 - fm.stringWidth(s)) / 2;
        int y = yp+(fm.getAscent() + (80 - (fm.getAscent() + fm.getDescent())) / 2);
        g.drawString(s, x, y);
    }

    public void drawFallSquare(Graphics g) {
        if (sq == null)
            return;
        g.setColor(sq.getColor());
        g.fillRect(getWidth()-330+(posx+1)*10+80*posx, getHeight()-30-10*(posy+1)-80*(posy+1), 80, 80);
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        drawCenteredString(Integer.toString(sq.getNumber()), getWidth()-330+(posx+1)*10+80*posx, getHeight()-30-10*(posy+1)-80*(posy+1),g);
    }

    public void drawTaskSquare(Graphics g) {
        if (sqtask=="")
            return;
        if (taskchoice[1]%2==0) g.setColor(Color.orange);
        else g.setColor(Color.green);
        g.fillRect(110, 270, 80, 80);
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        drawCenteredString(sqtask, 110, 270,g);
    }

    void readhallplayers(String fileName) {
        //JFrame frame = new JFrame(""); /*this frame would be used in case of a problem with a file with data*/
        String name = "";
        int score = 0;
        int time = 0;
        try {
            /*we use try statement because it is possible there would be a problem with the file with data about accounts*/
            BufferedReader glasses = new BufferedReader(new FileReader (fileName));
            String line;
            int row = 1;
            //numoftopplayers = Integer.parseInt(glasses.readLine().trim());
            line = glasses.readLine();/*here we read and store a new line from the file with data*/
            while (line != null) {/*here we save all accounts into a vector*/
                if (row % 3 == 1)
                    name=line;
                if (row % 3 == 2)
                    score = Integer.parseInt(line);
                if (row % 3 == 0) {
                    time = Integer.parseInt(line);
                    players.add(new Player(name, score, time));
                }
                line = glasses.readLine();
                row++;
            }
            glasses.close();
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage() + " Problem with file " + fileName, "Error message", JOptionPane.ERROR_MESSAGE);
            /*this frame would appear if there was a problem with the file*/
        }
    }


    private void createFile(String fileName) {
        //JFrame frame = new JFrame("");/*this frame would be used in case of a problem with a file with data*/
        PrintWriter pen;
        try {
            pen = new PrintWriter(new FileOutputStream(fileName));
            //pen.println(numoftopplayers);
            for (int i = 0; i < players.size(); i++) {
                pen.println(players.elementAt(i).getName());
                pen.println(players.elementAt(i).getScore());
                pen.println(players.elementAt(i).getTime());
            }
            pen.flush();
            pen.close();
        }
        catch(IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage() + " Problem with file " + fileName, "Error message", JOptionPane.ERROR_MESSAGE);
            /*this frame would appear if there was a problem with the file*/
        }
    }

    void drawbackrect(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(getWidth()-330, getHeight()-670, 280, 650);
    }

    void drawsquares(Graphics g) {
        for (int i = 0; i < 3; i++)
            for (int k = 0; k < columns.get(i).size(); k++) {
                g.setColor(columns.get(i).get(k).getColor());
                g.fillRect(getWidth()-330+(i+1)*10+80*i, getHeight()-30-10*(k+1)-80*(k+1), 80, 80);
                g.setColor(Color.white);
                g.setFont(new Font("Arial", Font.BOLD, 20));
                drawCenteredString(Integer.toString(columns.get(i).get(k).getNumber()), getWidth()-330+(i+1)*10+80*i, getHeight()-30-10*(k+1)-80*(k+1),g);
            }
    }

    int [] task() {
        //what if it is full now?
        int [] taskchoice = new int [2];
        int block;
        try {
            block = columns.elementAt((int)(Math.random()*3)).peek().getNumber();
        }
        catch (Exception e) {
            if (columns.elementAt(0).size() != 0)
                block = columns.elementAt(0).peek().getNumber();
            else if (columns.elementAt(1).size() != 0)
                block = columns.elementAt(1).peek().getNumber();
            else
                block = columns.elementAt(2).peek().getNumber();
        }

        //if there was not any of them containing a number, we would have already won
        int typeoftask = (int)(Math.random()*3);
        if (typeoftask == 0) {
            int tasknumEqual = sq.getNumber()+block;
            taskchoice[0] = 0;
            taskchoice[1] = tasknumEqual;
        }
        if (typeoftask == 1) {
            int tasknumSmaller = sq.getNumber()+block+1;
            taskchoice[0] = 1;
            taskchoice[1] = tasknumSmaller;
        }
        if (typeoftask == 2) {
            int tasknumBigger = sq.getNumber()+block-1;
            taskchoice[0] = 2;
            taskchoice[1] = tasknumBigger;
        }

        return taskchoice;
    }

    JLabel newlab(int x, int y, int width, int height, String str) {
        /*in this function we create a new label, and set all the things necessary for using it
         * we use it because writing for statements is more lengthy than writing just one statement*/
        JLabel l = new JLabel(str);
        l.setBounds(x, y, width,height);
        l.setVisible(true);
        this.getContentPane().add(l);
        return l;
    }

    int mxask() {
        Object[] options = {vocab[21], vocab[22], vocab[23]};//"1 to 10","1 to 20","1 to 50"
        int n = JOptionPane.showOptionDialog(this,
                                             vocab[24],//choose how big the numbers should be
                                             vocab[20],//settings
                                             JOptionPane.YES_NO_OPTION,
                                             JOptionPane.QUESTION_MESSAGE,
                                             null,     //do not use a custom Icon
                                             options,  //the titles of buttons
                                             options[0]);
        if (n == 0)
            return 10;
        if (n == 1)
            return 20;
        else
            return 50;
    }


    int secask()
    {
        Object[] options = {vocab[28],vocab[29],vocab[30]};//quickly medium slow
        int n = JOptionPane.showOptionDialog(this,
                                             vocab[31],//choose how big the numbers should be
                                             vocab[20],//settings
                                             JOptionPane.YES_NO_OPTION,
                                             JOptionPane.QUESTION_MESSAGE,
                                             null,     //do not use a custom Icon
                                             options,  //the titles of buttons
                                             options[0]);
        if (n==0) return 2000;
        if (n==1) return 3000;
        else return 4000;
    }

    public void actionPerformed(ActionEvent e) {
        //one of the two buttons - language
        if (e.getSource() == menuItem1) {
            if (gameon == 1) {
                Object[] options = {vocab[17], vocab[18]};//yes, no
                int n = JOptionPane.showOptionDialog(this,
                                                     vocab[16],//do you really want to start a new game? the current game will be lost.
                                                     vocab[15],//are you sure?
                                                     JOptionPane.YES_NO_CANCEL_OPTION,
                                                     JOptionPane.QUESTION_MESSAGE,
                                                     null,
                                                     options,
                                                     options[0]);
                if (n == 0) {
                    timer.stop();
                    tm.stop();
                    score = 0;
                    time=0;
                    scorelb.setText(vocab[13] + ": " + score);
                    maxnum = mxask();
                    seconds=secask();
                    timer.setInitialDelay(seconds);
                    timer.setDelay(seconds);
                    timer.start();
                    tm.start();
                    sq = new Square(maxnum);
                    column1.removeAllElements();
                    column2.removeAllElements();
                    column3.removeAllElements();
                    posx = 1;
                    posy = 6;
                    for (int i = 0; i < 3 ; i++) {
                        column1.add(new Square(maxnum));
                        column2.add(new Square(maxnum));
                        column3.add(new Square(maxnum));
                    }
                    gameon = 1;
                    enabledkeys=true;
                    taskchoice = task();

                    if (taskchoice[0] == 0)
                    {
                        task2.setText(vocab[5] + Integer.toString(taskchoice[1]));//"equal to "
                        sqtask="= ";
                    }
                    if (taskchoice[0] == 1)
                    {
                        task2.setText(vocab[6] + Integer.toString(taskchoice[1]));//"smaller than "
                        sqtask="< ";
                    }
                    if (taskchoice[0] == 2)
                    {
                        task2.setText(vocab[7] + Integer.toString(taskchoice[1]));//"bigger than "
                        sqtask="> ";
                    }
                    sqtask=sqtask+taskchoice[1];
                    repaint();
                }
                else
                    ;
            }
            else {
                score = 0;
                time=0;
                scorelb.setText(vocab[13] + ": " + score);
                maxnum = mxask();
                seconds=secask();
                timer.setInitialDelay(seconds);
                timer.setDelay(seconds);
                timer.start();
                tm.start();
                sq = new Square(maxnum);
                column1.removeAllElements();
                column2.removeAllElements();
                column3.removeAllElements();
                posx = 1;
                posy = 6;
                for (int i = 0; i < 3 ; i++) {
                    column1.add(new Square(maxnum));
                    column2.add(new Square(maxnum));
                    column3.add(new Square(maxnum));
                }
                gameon = 1;
                enabledkeys=true;
                taskchoice = task();
                if (taskchoice[0] == 0)
                {
                    task2.setText(vocab[5] + Integer.toString(taskchoice[1]));//"equal to "
                    sqtask="= ";
                }
                if (taskchoice[0] == 1)
                {
                    task2.setText(vocab[6] + Integer.toString(taskchoice[1]));//"smaller than "
                    sqtask="< ";
                }
                if (taskchoice[0] == 2)
                {
                    task2.setText(vocab[7] + Integer.toString(taskchoice[1]));//"bigger than "
                    sqtask="> ";
                }
                sqtask=sqtask+taskchoice[1];
                repaint();
            }
        }

        if (e.getSource() == menuItem2)
            JOptionPane.showMessageDialog(this, vocab[8], vocab[1], JOptionPane.INFORMATION_MESSAGE);

        if (e.getSource() == menuItem3) {
            //we put the players - names, scores and times into string halloffame, which we print then in the joptionpane
            //we use for it html tables
            //one line is written like this <tr>  <td>Jill</td>  <td>Smith</td>   <td>50</td></tr>
            halloffame="<html><table>";
            //halloffame=halloffame+"f";//here we add the main things
            //<b><i>
            //</i></b>
            String seconds;
            halloffame = halloffame + "<tr><td></td><td><b><i>" + vocab[25]+"</i></b></td><td><b><i>" +
                         vocab[26] + "</i></b></td><td><b><i>" + vocab[27] + "</i></b></td></tr>";
            for (int i = 0; i < players.size(); i++) {
                if ((players.elementAt(i).getTime() % 60) < 10)
                    seconds = "0" + (players.elementAt(i).getTime() % 60);
                else seconds = "" + (players.elementAt(i).getTime() % 60);
                halloffame = halloffame + "<tr><td>"  + (i+1) + ".</td>   <td>" + players.elementAt(i).getName() +
                             "</td>   <td>" + players.elementAt(i).getScore() + "</td>   <td>" +
                             (players.elementAt(i).getTime() / 60) + ":" + seconds + "</td></tr>";
            }
            halloffame = halloffame + "</table></html>";
            JOptionPane.showMessageDialog(this, halloffame, vocab[2], JOptionPane.PLAIN_MESSAGE);
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        drawbackrect(g);
        drawsquares(g);
        drawFallSquare(g);
        drawTaskSquare(g);
    }

    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {}

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            if (gameon == 1 && posx < 2 && columns.get(posx+1).size() < posy+1 && enabledkeys==true) { //here we check whether we can move to the right
                posx++;
                repaint();
            }

        if (e.getKeyCode() == KeyEvent.VK_LEFT)
            if (gameon == 1 && posx > 0 && columns.get(posx-1).size() < posy+1 && enabledkeys==true) {//here we check whether we can move to the left
                posx--; //we move
                repaint();
            }

        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (gameon == 1 && columns.get(posx).size() < posy+1 && enabledkeys==true) {
                timer.stop();
                enabledkeys=false;
                timer.setInitialDelay(0);
                timer.setDelay(50);
                timer.start();
            }
        }
    }


    public static void main(String [] args) {
        MainClass window = new MainClass();
        window.readhallplayers("Players.txt");
        window.score = 0;
        window.setVisible(true);
        window.maxnum = 20;
        window.columns.add(window.column1);
        window.columns.add(window.column2);
        window.columns.add(window.column3);
        window.posx = 1;
        window.posy = 6;
        window.sq = new Square(window.maxnum);
        for (int i = 0; i < 3; i++) {
            window.column1.add(new Square(window.maxnum));
            window.column2.add(new Square(window.maxnum));
            window.column3.add(new Square(window.maxnum));
        }
        window.taskchoice = window.task();
        if (window.taskchoice[0] == 0)
        {
            window.task2.setText(window.vocab[5] + Integer.toString(window.taskchoice[1]));//"equal to "
            window.sqtask="= ";
        }
        if (window.taskchoice[0] == 1)
        {
            window.task2.setText(window.vocab[6] + Integer.toString(window.taskchoice[1]));//"smaller than "
            window.sqtask="< ";
        }
        if (window.taskchoice[0] == 2)
        {
            window.task2.setText(window.vocab[7] + Integer.toString(window.taskchoice[1]));//"bigger than "
            window.sqtask="> ";
        }
        window.sqtask=window.sqtask+window.taskchoice[1];
        window.repaint();
        JOptionPane.showMessageDialog(window, window.vocab[10], window.vocab[11], JOptionPane.INFORMATION_MESSAGE);
    }

}
