package com.View;

import com.Helper.Config;
import com.Helper.Helper;
import com.model.Course;
import com.model.Operator;
import com.model.Patika;
import com.model.User;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
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
    private JPanel pnl_patika_list;
    private JScrollPane scrll_patika_list;
    private JTable tbl_patika_list;
    private JPanel pnl_patika_add;
    private JLabel lbl_patika_add;
    private JTextField fld_patika_addname;
    private JButton btn_patika_add;
    private JPanel pnl_course_list;
    private JScrollPane scrl_course_list;
    private JTable tbl_course_list;
    private JPanel pnl_course_add;
    private JTextField fld_course_name;
    private JTextField fld_course_lang;
    private JButton btn_course_add;
    private JComboBox cmb_course_patika;
    private JComboBox cmb_course_user;
    private DefaultTableModel mdl_user_list;
    private Object[] row_user_list;
    private DefaultTableModel mdl_patika_list;
    private Object[] row_patika_list;
    private JPopupMenu patikaMenu;
    private DefaultTableModel mdl_course_list;
    private Object[] row_course_list;

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
        mdl_user_list = new DefaultTableModel() {
            //method override ID column düzeltilmeye kapatıldı
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0)
                    return false;
                return super.isCellEditable(row, column);
            }
        };

        Object[] col_user_list = {"ID", "Ad Soyad", "Kullanıcı Adı", "Şifre", "Üyelik Tipi"};
        mdl_user_list.setColumnIdentifiers(col_user_list);

        row_user_list = new Object[col_user_list.length];

        loadUserModel();


        tbl_userlist.setModel(mdl_user_list);
        //Columnları sabitleme
        tbl_userlist.getTableHeader().setReorderingAllowed(false);

        //tablo üzerinden verileri güncellemek için eklenen kısım

        tbl_userlist.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int user_id = Integer.parseInt(tbl_userlist.getValueAt(tbl_userlist.getSelectedRow(), 0).toString());
                String user_name = tbl_userlist.getValueAt(tbl_userlist.getSelectedRow(), 1).toString();
                String user_uname = tbl_userlist.getValueAt(tbl_userlist.getSelectedRow(), 2).toString();
                String user_pass = tbl_userlist.getValueAt(tbl_userlist.getSelectedRow(), 3).toString();
                String user_type = tbl_userlist.getValueAt(tbl_userlist.getSelectedRow(), 4).toString();

                if (User.update(user_id, user_name, user_uname, user_pass, user_type)) {
                    Helper.showMsg("success");
                    loadUserModel();
                } else {
                    Helper.showMsg("error");
                }
            }
        });
        //modelUserList
        //modelPatikaList
        //** patikalar tablosunda popup menu işlemleri
        patikaMenu = new JPopupMenu();
        JMenuItem updateMenu = new JMenuItem("Güncelle");
        JMenuItem deleteMenu = new JMenuItem("Delete");
        patikaMenu.add(updateMenu);
        patikaMenu.add(deleteMenu);

        updateMenu.addActionListener(e -> {
            int select_id = Integer.parseInt(tbl_patika_list.getValueAt(tbl_patika_list.getSelectedRow(), 0).toString());
            UpdatePatikaGUI updatePatikaGUI = new UpdatePatikaGUI(Patika.getFetch(select_id));
            updatePatikaGUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadPatikaModel();
                }
            });
        });

        deleteMenu.addActionListener(e -> {
            if (Helper.confirm("sure")) {
                int select_id = Integer.parseInt(tbl_patika_list.getValueAt(tbl_patika_list.getSelectedRow(), 0).toString());
                if (Patika.delete(select_id)) {
                    Helper.showMsg("success");
                    loadPatikaModel();
                } else {
                    Helper.showMsg("error");
                }
            }

        });
        //** patikalar tablosunda popup menu işlemleri

        mdl_patika_list = new DefaultTableModel();
        Object[] col_patika_list = {"ID", "Patika Adı"};
        mdl_patika_list.setColumnIdentifiers(col_patika_list);

        row_patika_list = new Object[col_patika_list.length];
        loadPatikaModel();

        tbl_patika_list.setModel(mdl_patika_list);
        //**popup menu tabloya eklendi
        tbl_patika_list.setComponentPopupMenu(patikaMenu);

        //Columnları sabitleme
        tbl_patika_list.getTableHeader().setReorderingAllowed(false);
        //Columnların boyutunu degiştirme
        tbl_patika_list.getColumnModel().getColumn(0).setMaxWidth(100);
        //patika tablosunda mousela seçme işlemi
        tbl_patika_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                int selected_row = tbl_patika_list.rowAtPoint(point);
                tbl_patika_list.setRowSelectionInterval(selected_row, selected_row);
            }
        });
        //modelPatikaList

        //modelCourseList
        mdl_course_list = new DefaultTableModel();
        Object[] col_courseList = {"ID", "Course Name", "Programming language", "Patika", "Educator"};
        mdl_course_list.setColumnIdentifiers(col_courseList);
        row_course_list = new Object[col_courseList.length];
        loadCourseList();

        tbl_course_list.setModel(mdl_course_list);
        tbl_course_list.getColumnModel().getColumn(0).setMaxWidth(75);
        tbl_course_list.getTableHeader().setReorderingAllowed(false);

        //modelCourseList
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
                if (Helper.isFieldEmpty(fld_userID)) {
                    Helper.showMsg("fill");
                } else {
                    //silme işleminde emin misiniz ?
                    if (Helper.confirm("sure")) {
                        int userID = Integer.parseInt(fld_userID.getText());
                        if (User.delete(userID)) {
                            Helper.showMsg("success");
                            loadUserModel();
                        } else {
                            Helper.showMsg("error");
                        }
                    }
                }
            }
        });


        btn_srch_user.addActionListener(e -> {
            String name = fld_srch_user_name.getText();
            String uname = flf_srch_user_uname.getText();
            String type = cmb_srch_usertype.getSelectedItem().toString();

            String query = User.searchQuery(name, uname, type);

            ArrayList<User> searchingUser = User.searchUserList(query);

            loadUserModel(searchingUser);

        });
        btn_logout.addActionListener(e -> {
            dispose();
        });
        btn_patika_add.addActionListener(e -> {
            if (Helper.isFieldEmpty(fld_patika_addname)) {
                Helper.showMsg("fill");
            } else {
                if (Patika.add(fld_patika_addname.getText())) {
                    Helper.showMsg("success");
                    loadPatikaModel();
                    fld_patika_addname.setText(null);
                } else {
                    Helper.showMsg("error");
                }
            }
        });
    }

    private void loadCourseList() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_course_list.getModel();
        clearModel.setRowCount(0);
        int i;
        for (Course obj : Course.getList()) {
            i = 0;
            row_course_list[i++] = obj.getId();
            row_course_list[i++] = obj.getName();
            row_course_list[i++] = obj.getLang();
            row_course_list[i++] = obj.getPatika().getName();
            row_course_list[i++] = obj.getEducator().getName();
                    mdl_course_list.addRow(row_course_list);
        }
    }

    private void loadPatikaModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_patika_list.getModel();
        clearModel.setRowCount(0);
        int i;
        for (Patika obj : Patika.getList()) {
            i = 0;

            row_patika_list[i++] = obj.getId();
            row_patika_list[i++] = obj.getName();
            mdl_patika_list.addRow(row_patika_list);


        }
    }

    public void loadUserModel() {

        DefaultTableModel clearModel = (DefaultTableModel) tbl_userlist.getModel();
        clearModel.setRowCount(0);
        int i;
        for (User user : User.getList()) {

            i = 0;
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
