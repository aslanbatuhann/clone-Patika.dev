package com.View;

import com.Helper.Config;
import com.Helper.Helper;
import com.model.Operator;
import com.model.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends JFrame {
    private JPanel wrapper;
    private JPanel wrappertop;
    private JPanel wrapperbot;
    private JTextField fld_user_uname;
    private JButton btn_login;
    private JPasswordField fld_user_pass;

    public LoginGUI() {
        add(wrapper);
        setSize(600, 600);
        setLocation(Helper.screenCentrePoint("x", getSize()), Helper.screenCentrePoint("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);


        btn_login.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_user_uname) || Helper.isFieldEmpty(fld_user_pass)) {
                Helper.showMsg("fill");
            } else {
                User user = User.getFetch(fld_user_uname.getText(), fld_user_pass.getText());
                if (user == null) {
                    Helper.showMsg("Kullanıcı Bulunamadı.");
                } else {
                    switch (user.getUserType()){
                        case "operator":
                            OperatorGUI operatorGUI = new OperatorGUI((Operator) user);
                            break;
                        case "educator":
                            EducatorGUI educatorGUI = new EducatorGUI();
                            break;
                        case "student":
                            StudentGUI studentGUI = new StudentGUI();
                            break;
                    }
                    dispose();
                }
            }

        });
    }

    public static void main(String[] args) {
        Helper.setLayout();
        LoginGUI loginGUI = new LoginGUI();
    }
}
