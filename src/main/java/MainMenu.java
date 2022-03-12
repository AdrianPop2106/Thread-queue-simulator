import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {
    private JTextField n;
    private JTextField q;
    private JTextField max;
    private JTextField[] arrive;
    private JTextField[] serve;

    MainMenu(){
        this.setLocationRelativeTo(null);

        Font f=new Font("Serif",Font.BOLD,18);

        n=new JTextField(3);
        q=new JTextField(3);
        max=new JTextField(3);

        arrive=new JTextField[2];
        serve=new JTextField[2];
        arrive[0]=new JTextField(3);
        arrive[1]=new JTextField(3);
        serve[0]= new JTextField(3);
        serve[1]=new JTextField(3);

        JLabel clients=new JLabel("Clients");
        JLabel queues=new JLabel("Queues");
        JLabel maxSimulation=new JLabel("Simulation interval");

        JLabel arrival=new JLabel("Arrival");
        JLabel service=new JLabel("Service");
        JLabel[] separators=new JLabel[6];
        separators[0]=new JLabel("[");
        separators[0].setFont(f);
        separators[3]=new JLabel("[");
        separators[3].setFont(f);
        separators[1]=new JLabel(";");
        separators[1].setFont(f);
        separators[4]=new JLabel(";");
        separators[4].setFont(f);
        separators[2]=new JLabel("]");
        separators[2].setFont(f);
        separators[5]=new JLabel("]");
        separators[5].setFont(f);

        JButton submit=new JButton("Submit");
        submit.addActionListener(e -> {
            if(checkInputs()) {
                SimulationManager gen=new SimulationManager(Integer.parseInt(max.getText()),Integer.parseInt(arrive[1].getText()),
                        Integer.parseInt(arrive[0].getText()),Integer.parseInt(serve[1].getText()),Integer.parseInt(serve[0].getText()),
                        Integer.parseInt(q.getText()),Integer.parseInt(n.getText()),"result.txt");
                new Thread(gen).start();
                closeFrame();
            }
        });

        JPanel p1=new JPanel();
        p1.add(clients);
        p1.add(n);
        p1.add(Box.createRigidArea(new Dimension(3,1)));
        p1.add(queues);
        p1.add(q);
        p1.add(Box.createRigidArea(new Dimension(3,1)));
        p1.add(maxSimulation);
        p1.add(max);

        JPanel p2=new JPanel();
        p2.add(arrival);
        p2.add(separators[0]);
        p2.add(arrive[0]);
        p2.add(separators[1]);
        p2.add(arrive[1]);
        p2.add(separators[2]);
        p2.add(Box.createRigidArea(new Dimension(30,1)));
        p2.add(service);
        p2.add(separators[3]);
        p2.add(serve[0]);
        p2.add(separators[4]);
        p2.add(serve[1]);
        p2.add(separators[5]);

        JPanel pan=new JPanel();
        pan.add(Box.createRigidArea(new Dimension(1,30)));
        pan.add(p1);
        pan.add(p2);
        pan.add(submit);
        submit.setAlignmentX(Box.CENTER_ALIGNMENT);
        pan.add(Box.createRigidArea(new Dimension(1,10)));
        pan.setLayout(new BoxLayout(pan,BoxLayout.Y_AXIS));

        this.setContentPane(pan);
        this.setTitle("Multithreaded Queue:Input");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setSize(500,200);
        this.setVisible(true);
    }

    private boolean checkInputs(){
        if(n.getText().equals("") || q.getText().equals("") || max.getText().equals("") || arrive[0].getText().equals("")
        || arrive[1].getText().equals("") || serve[0].getText().equals("") || serve[1].getText().equals(""))
            return false;
        if(checkIncorrectString(n.getText()) || checkIncorrectString(q.getText()) || checkIncorrectString(max.getText()) ||
        checkIncorrectString(arrive[0].getText()) || checkIncorrectString(arrive[1].getText()) || checkIncorrectString(serve[0].getText())
        || checkIncorrectString(serve[1].getText()))
            return false;
        if(checkWrongValues())
            return false;
        return true;
    }

    private boolean checkIncorrectString(String s){
        for(int i=0;i<s.length();i++)
            if(s.charAt(i)<'0' && s.charAt(i)>'9')
                return true;
        return false;
    }

    private boolean checkWrongValues(){
        if(Integer.parseInt(arrive[1].getText())<Integer.parseInt(arrive[0].getText())
                || Integer.parseInt(serve[1].getText())<Integer.parseInt(serve[0].getText()))
            return true;
        return false;
    }

    public void closeFrame(){
        this.dispose();
    }

    public static void main(String[] args){
        new MainMenu();
    }
}
