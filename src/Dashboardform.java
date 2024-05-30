import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dashboardform extends JDialog {
    private JPanel HelloBoardPanel;
    private JButton regystryButton;
    private JButton ButtonLogin;

    public Dashboardform(JFrame parent) {
        super(parent);
        setTitle("login");
        setContentPane(HelloBoardPanel);
        setSize(new Dimension(1280, 720));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        ButtonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginform loginform = new loginform(null);
                User user = loginform.user;
                if(user!=null){
                    MainMenuForm mainMenuForm = new MainMenuForm(null);
                    dispose();
                }else {
                    System.out.println("Отмена входа");
                }
            }
        });
        regystryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                RegystrationForm regystrationForm = new RegystrationForm(null);
                User user = regystrationForm.user;
                if (user == null){
                    Dashboardform dashboardform = new Dashboardform(null);
                } else {
                    Dashboardform dashboardform = new Dashboardform(null);
                }

            }
        });
        setVisible(true);
    }



}
