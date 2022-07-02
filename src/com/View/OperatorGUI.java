package com.View;

import com.Helper.Config;
import com.Helper.Helper;
import com.model.Operator;
import com.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OperatorGUI extends JFrame {

    private JPanel wrapper;
    private JTabbedPane top_operator;
    private JLabel lbl_welcome;
    private JPanel pnl_top;
    private JButton btn_logout;
    private JPanel pnl_userList;
    private JScrollPane scrl_userlist;
    private JTable tbl_userlist;
    private JPanel pnl_userform;
    private JTextField fld_user_name;
    private JTextField fld_username;
    private JTextField fld_user_password;
    private JComboBox cmb_usertype;
    private JButton btn_useradd;
    private DefaultTableModel mdl_user_list;
    private Object[] row_user_list;

    private final Operator operator;

    public OperatorGUI(Operator operator) {
        this.operator = operator;


        add(wrapper);
        setSize(1000, 500);


        setLocation(Helper.screenCentrePoint("x", getSize()), Helper.screenCentrePoint("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);

        lbl_welcome.setText("Hoşgeldin : " + operator.getName());

        //modelUserList
        mdl_user_list = new DefaultTableModel();
        Object[] col_user_list = {"ID", "Ad Soyad", "Kullanıcı Adı", "Şifre", "Üyelik Tipi"};
        mdl_user_list.setColumnIdentifiers(col_user_list);

        for (User user : User.getList()) {

            Object[] row = new Object[col_user_list.length];
            row[0]=user.getId();
            row[1]=user.getName();
            row[2]=user.getUserName();
            row[3]=user.getPassword();
            row[4]=user.getUserType();
            mdl_user_list.addRow(row);
        }

        tbl_userlist.setModel(mdl_user_list);
        tbl_userlist.getTableHeader().setReorderingAllowed(false);


        btn_useradd.addActionListener(e -> {
                if(Helper.isFieldEmpty(fld_user_name) || Helper.isFieldEmpty(fld_username) || Helper.isFieldEmpty(fld_user_password) ){
                    Helper.showMsg("fill");
                }else {
                    String name = fld_user_name.getText();
                    String userName= fld_username.getText();
                    String password=fld_user_password.getText();
                    String type =cmb_usertype.getSelectedItem().toString();

                    if(User.add(name,userName,password,type)){
                        Helper.showMsg("success");
                    }else {
                        Helper.showMsg("error");
                    }
                }
        });
    }

    public static void main(String[] args) {
        Helper.setLayout();
        Operator op = new Operator();
        op.setId(1);
        op.setName("Batuhan Aslan");
        op.setPassword("12345");
        op.setUserType("operator");
        op.setUserName("Batuhan");


        OperatorGUI opGUI = new OperatorGUI(op);
    }
}
