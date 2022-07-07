package com.model;

import com.Helper.DBConnector;
import com.Helper.Helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class User {
    private int id;
    private String name;
    private String userName;
    private String password;
    private String userType;

    public User() {
    }

    public User(int id, String name, String userName, String password, String userType) {
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.password = password;
        this.userType = userType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public static ArrayList<User> getList() {
        ArrayList<User> userArrayList = new ArrayList<>();
        String query = "SELECT * FROM user";
        User obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();

            ResultSet resultSet = st.executeQuery(query);
            while (resultSet.next()) {
                obj = new User();
                obj.setId(resultSet.getInt("id"));
                obj.setName(resultSet.getString("name"));
                obj.setUserName(resultSet.getString("username"));
                obj.setPassword(resultSet.getString("password"));
                obj.setUserType(resultSet.getString("usertype"));

                userArrayList.add(obj);
            }
            st.close();
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userArrayList;
    }


    public static boolean add(String name, String userName, String password, String userType) {

        String query = "INSERT INTO user (name,username,password,usertype) VALUES (?,?,?,?)";
        User findUser = User.getFetch(userName);

        if (findUser != null) {
            Helper.showMsg("Farklı Kullanıcı adı giriniz.");
            return false;
        }

        boolean key = true;
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, name);
            pr.setString(2, userName);
            pr.setString(3, password);
            pr.setString(4, userType);
            key = pr.executeUpdate() != -1;
            pr.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        return key;
    }

    public static User getFetch(String userName) {
        User obj = null;
        String query = "SELECT * FROM user WHERE username = ?";

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, userName);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                obj = new User();
                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("name"));
                obj.setUserName(rs.getString("username"));
                obj.setPassword(rs.getString("password"));
                obj.setUserType(rs.getString("usertype"));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

    public static User getFetch(int id) {
        User obj = null;
        String query = "SELECT * FROM user WHERE id = ?";

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                obj = new User();
                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("name"));
                obj.setUserName(rs.getString("username"));
                obj.setPassword(rs.getString("password"));
                obj.setUserType(rs.getString("usertype"));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

    //login işlemi için
    public static User getFetch(String uName, String password) {
        User obj = null;
        String query = "SELECT * FROM user WHERE username = ? AND password = ?";

        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, uName);
            pr.setString(2, password);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                switch (rs.getString("usertype")) {
                    case "operator":
                        obj = new Operator();
                        break;
                    default:
                        obj = new User();
                }

                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("name"));
                obj.setUserName(rs.getString("username"));
                obj.setPassword(rs.getString("password"));
                obj.setUserType(rs.getString("usertype"));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }


    public static boolean delete(int id) {
        String query = "DELETE FROM user WHERE id = ?";
        ArrayList<Course> courseArrayList = Course.getListByUser(id);
        for (Course course : courseArrayList) {
            Course.delete(course.getId());
        }

        boolean key = true;
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);

            key = pr.executeUpdate() != -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return key;
    }


    public static boolean update(int id, String name, String uname, String pass, String type) {
        String query = "UPDATE user SET name = ? , username= ?,password = ?,usertype=? WHERE  id = ?";
        //kullanıcı adı güncellemesini sorgulamak için eklenen kısım
        User findUser = User.getFetch(uname);
        if (findUser != null && findUser.getId() != id) {
            Helper.showMsg("Bu kullanıcı adı daha önceden eklenmis.");
            return false;
        }
       /* ArrayList<String> usertype = new ArrayList<String>();
        usertype.add("operator");
        usertype.add("student");
        usertype.add("educator");
        User findtype = User.getFetch(uname);
        if (findtype != null && findtype.getUserType() != usertype.get(0) && findtype.getUserType() != usertype.get(1) && findtype.getUserType() != usertype.get(2)) {
            Helper.showMsg("yanlış tip seçimi yaptınız");
            return false;
        }*/

        boolean key = true;
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, name);
            pr.setString(2, uname);
            pr.setString(3, pass);
            pr.setString(4, type);
            pr.setInt(5, id);
            key = pr.executeUpdate() != -1;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return key;

    }

    public static ArrayList<User> searchUserList(String query) {
        ArrayList<User> userArrayList = new ArrayList<>();
        User obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();

            ResultSet resultSet = st.executeQuery(query);
            while (resultSet.next()) {
                obj = new User();
                obj.setId(resultSet.getInt("id"));
                obj.setName(resultSet.getString("name"));
                obj.setUserName(resultSet.getString("username"));
                obj.setPassword(resultSet.getString("password"));
                obj.setUserType(resultSet.getString("usertype"));

                userArrayList.add(obj);
            }
            st.close();
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userArrayList;
    }

    public static String searchQuery(String name, String uname, String type) {
        String query = "SELECT * FROM user WHERE username LIKE '%{{uname}}%' AND name LIKE '%{{name}}%'";
        query = query.replace("{{uname}}", uname);
        query = query.replace("{{name}}", name);
        if (!type.isEmpty()) {
            query += "AND usertype ='{{type}}'";
            query = query.replace("{{type}}", type);
        }
        return query;
    }
}
