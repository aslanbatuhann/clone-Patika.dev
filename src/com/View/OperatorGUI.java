package com.View;

import com.Helper.Config;
import com.Helper.Helper;
import com.model.Operator;
import com.model.User;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
    private JTextField fld_userID;
    private JButton btn_user_delete;
    private JTextField fld_srch_user_name;
    private JTextField flf_srch_user_uname;
    private JComboBox cmb_srch_usertype;
    private JButton btn_srch_user;
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
        mdl_user_list = new DefaultTableModel(){
            //method override ID column düzeltilmeye kapatıldı
            @Override
            public boolean isCellEditable(int row, int column) {
                if(column == 0)
                    return false;
                return super.isCellEditable(row, column);
            }
        };

        Object[] col_user_list = {"ID", "Ad Soyad", "Kullanıcı Adı", "Şifre", "Üyelik Tipi"};
        mdl_user_list.setColumnIdentifiers(col_user_list);

        row_user_list = new Object[col_user_list.length];

        loadUserModel();


        tbl_userlist.setModel(mdl_user_list);
        tbl_userlist.getTableHeader().setReorderingAllowed(false);

        //tablo üzerinden verileri güncellemek için eklenen kısım

        tbl_userlist.getModel().addTableModelListener(e -> {
            if(e.getType() == TableModelEvent.UPDATE){
                int user_id = Integer.parseInt(tbl_userlist.getValueAt(tbl_userlist.getSelectedRow(),0).toString());
                String user_name = tbl_userlist.getValueAt(tbl_userlist.getSelectedRow(),1).toString();
                String user_uname = tbl_userlist.getValueAt(tbl_userlist.getSelectedRow(),2).toString();
                String user_pass = tbl_userlist.getValueAt(tbl_userlist.getSelectedRow(),3).toString();
                String user_type = tbl_userlist.getValueAt(tbl_userlist.getSelectedRow(),4).toString();

                if(User.update(user_id,user_name,user_uname,user_pass,user_type)){
                    Helper.showMsg("success");
                    loadUserModel();
                }else{
                    Helper.showMsg("error");
                }
            }
        });


        btn_useradd.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_user_name) || Helper.isFieldEmpty(fld_username) || Helper.isFieldEmpty(fld_user_password)) {
                Helper.showMsg("fill");
            } else {
                String name = fld_user_name.getText();
                String userName = fld_username.getText();
                String password = fld_user_password.getText();
                String type = cmb_usertype.getSelectedItem().toString();

                if (User.add(name, userName, password, type)) {
                    Helper.showMsg("success");
                    loadUserModel();
                    fld_user_name.setText(null);
                    fld_username.setText(null);
                    fld_user_password.setText(null);
                }
            }
        });
        btn_user_delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Helper.isFieldEmpty(fld_userID)){
                    Helper.showMsg("fill");
                }else{
                    int userID = Integer.parseInt(fld_userID.getText());
                    if(User.delete(userID)){
                        Helper.showMsg("success");
                        loadUserModel();
                    }else{
                        Helper.showMsg("error");
                    }
                }
            }
        });


        btn_srch_user.addActionListener(e -> {
            String name = fld_srch_user_name.getText();
            String uname= flf_srch_user_uname.getText();
            String type = cmb_srch_usertype.getSelectedItem().toString();

            String query = User.searchQuery(name,uname,type);

            ArrayList<User> searchingUser = User.searchUserList(query);

            loadUserModel(searchingUser);

        });
    }

    public void loadUserModel() {

        DefaultTableModel clearModel = (DefaultTableModel) tbl_userlist.getModel();
        clearModel.setRowCount(0);
        for (User user : User.getList()) {

            int i = 0;
            row_user_list[i++] = user.getId();
            row_user_list[i++] = user.getName();
            row_user_list[i++] = user.getUserName();
            row_user_list[i++] = user.getPassword();
            row_user_list[i++] = user.getUserType();
            mdl_user_list.addRow(row_user_list);
        }
    }
    public void loadUserModel(ArrayList<User> List) {

        DefaultTableModel clearModel = (DefaultTableModel) tbl_userlist.getModel();
        clearModel.setRowCount(0);
        for (User user : List) {

            int i = 0;
            row_user_list[i++] = user.getId();
            row_user_list[i++] = user.getName();
            row_user_list[i++] = user.getUserName();
            row_user_list[i++] = user.getPassword();
            row_user_list[i++] = user.getUserType();
            mdl_user_list.addRow(row_user_list);
        }
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
