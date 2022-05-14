package edu.uwb.css533.homework;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class DatabaseConnection {
    //format is driver: provider://machine(ip or dns):port/database
    private String url = "jdbc:postgresql://localhost:5432/postgres";
    private String username = "postgres";
    private String password = "*****";
    Connection connection;

    public DatabaseConnection() {
    }
    public boolean logIn(String userName, String password){
        return false;
    }

    public void connect() {
        Connection result = null;
        try {
            result = DriverManager.getConnection(url, username, password);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        }
        connection = result;
        System.out.println("Connected to" + url);
    }

    public void generateToDoListsTable() {
        if (connection == null) {
            connect();
        }
        if (connection == null) {
            System.out.println(("Unable to connect."));
            return;
        }
        String sql = "CREATE TABLE Todo_lists (toDo_list_id VARCHAR ( 50 ) PRIMARY KEY, toDo_list_name VARCHAR ( 50 ) UNIQUE NOT NULL,toDo_list_type VARCHAR ( 50 ) NOT NULL, LastModifiedDate TIMESTAMP NOT NULL );";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            int rows = stmt.executeUpdate();
        } catch (SQLException e) {

        }
    }
    public void generateUsersInfoTable() {
        if (connection == null) {
            connect();
        }
        if (connection == null) {
            System.out.println(("Unable to connect."));
            return;
        }
        String sql = "CREATE TABLE Users_info (user_id VARCHAR ( 50 ) PRIMARY KEY, user_name VARCHAR ( 50 ) UNIQUE NOT NULL,user_password VARCHAR ( 50 ) NOT NULL, List_ids TEXT [] );";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            int rows = stmt.executeUpdate();
        } catch (SQLException e) {

        }
    }
    public void generateTasksTable(){
        if (connection == null) {
            connect();
        }
        if (connection == null) {
            System.out.println(("Unable to connect."));
            return;
        }
        String sql = "CREATE TABLE Task (task_id VARCHAR ( 50 ) PRIMARY KEY, task_name VARCHAR ( 50 ) UNIQUE NOT NULL,task_description VARCHAR ( 50 ), task_status VARCHAR ( 50 ), toDo_list_id VARCHAR ( 50 ), LastModifiedDate TIMESTAMP NOT NULL );";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            int rows = stmt.executeUpdate();
        } catch (SQLException e) {

        }
    }
    public boolean checkAccess(String user_id){
        return true;
    }
    public boolean addTask(String task_name, String task_description, String todolist_name, String user_name){
        boolean allowed = checkAccess(user_name);
        if(! allowed){
            System.out.println("Invalid user");
            return false;
        }
        if (connection == null) {
            connect();
        }
        if (connection == null) {
            System.out.println(("Unable to connect."));
            return false;
        }

        String sql = "INSERT INTO Task (task_id, task_name, task_description, task_status, toDo_list_id, LastModifiedDate) VALUES (?, ?, ?, ?, ?, ?);";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            String task_id = user_name+"_"+todolist_name+"_"+task_name;
            stmt.setString(1, task_id);
            stmt.setString(2, task_name);
            stmt.setString(3, task_description);
            stmt.setString(4, "Not Started");
            String toDo_list_id = user_name+"_"+todolist_name;
            stmt.setString(5, toDo_list_id);
            Date date = new Date();
            Timestamp ts = new Timestamp(date.getTime());
            stmt.setTimestamp(6, ts);
            int rows = stmt.executeUpdate();

        } catch (SQLException e) {
            return false;
        }
        return true;
    }
    public boolean deleteTask(String taskName, String listName,String userName){
        boolean allowed = checkAccess(userName);
        if(! allowed){
            System.out.println("Invalid user");
            return false;
        }
        if (connection == null) {
            connect();
        }
        if (connection == null) {
            System.out.println(("Unable to connect."));
            return false;
        }
        String task_id = userName+"_"+listName+"_"+taskName;

        String sql = "DELETE * FROM Task WHERE task_id= " + task_id+";";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            int rows = stmt.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public boolean deleteAllTasks(String user_name, String list_name){
        boolean allowed = checkAccess(user_name);
        if(! allowed){
            System.out.println("Invalid user");
            return false;
        }
        if (connection == null) {
            connect();
        }
        if (connection == null) {
            System.out.println(("Unable to connect."));
            return false;
        }
        String list_id = user_name+"_"+list_name;
        String sql = "DELETE * FROM Task WHERE toDo_list_id=" + list_id + ";";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
        }catch(SQLException e) {
            return false;
        }
        return true;
    }

    public boolean displayAllTaskNames(String user_name, String list_name){
        boolean allowed = checkAccess(user_name);
        if(! allowed){
            System.out.println("Invalid user");
            return false;
        }
        if (connection == null) {
            connect();
        }
        if (connection == null) {
            System.out.println(("Unable to connect."));
            return false;
        }
        String list_id = user_name+"_"+list_name;
        String sql_get_task_name = "SELECT task_name FROM Task WHERE toDo_list_id= " + list_id + ";";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql_get_task_name);
            if (rs == null){
                System.out.println("No data found.");
                return false;
            }
            System.out.println("Here is all your tasks");
            while (rs.next()) {
                System.out.println("Task Name: " + rs.getString("task_name"));
            }
        }catch(SQLException e){
            return false;
        }
        return true;
    }

    public boolean updateTaskContent(String user_name, String list_name,String content, String task_name){
        boolean allowed = checkAccess(user_name);
        if(! allowed){
            System.out.println("Invalid user");
            return false;
        }
        if (connection == null) {
            connect();
        }
        if (connection == null) {
            System.out.println(("Unable to connect."));
            return false;
        }
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        String task_id = user_name+"_"+list_name+"_"+task_name;
        String update_task_content_sql = "UPDATE Task SET task_description = " + content+ " LastModifiedDate = " + ts +" WHERE task_id ="+ task_id+";";
        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(update_task_content_sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updateTaskStatus(String user_id, String status, String task_id){
        boolean allowed = checkAccess(user_id);
        if(! allowed){
            System.out.println("Invalid user");
            return false;
        }
        if (connection == null) {
            connect();
        }
        if (connection == null) {
            System.out.println(("Unable to connect."));
            return false;
        }
        if(! (status.equals("Not Started") || status.equals("Completed" )||status.equals("In-Progress" ))){
            System.out.println("Task type can only be Completed, Not Started or In-Progress");
            return false;
        }
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        String update_task_status_sql = "UPDATE Task SET task_status = " + status+" LastModifiedDate = " + ts +" WHERE task_id ="+ task_id+";";
        try{
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(update_task_status_sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }



    public boolean updateListUserInfo(String userName, String add_or_delete, String new_list_id){
        String sql = "SELECT list_ids FROM Users_info WHERE user_id= " + userName + ";";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                List<String> result = (List<String>) rs.getArray("List_ids");
                if(add_or_delete.equals("add")){
                    result.add(new_list_id);
                }
                else {
                    if (result.isEmpty()){
                        System.out.println("no to-do list under the user");
                        return false;
                    }
                    result.remove(result.size() - 1);
                }
                String add_userInfo_sql = "UPDATE Users_info SET list_ids = " + result+" WHERE user_id ="+ userName+";";
                try{
                    Statement stmt_add_user_info = connection.createStatement();
                    ResultSet rs_add_user_info = stmt_add_user_info.executeQuery(add_userInfo_sql);
                } catch (SQLException e) {
                    return false;
                }
            }
        }catch(SQLException e){
            return false;
        }
        return true;
    }
    public boolean deleteAllLists(String userId){
        if (connection == null) {
            connect();
        }
        if (connection == null) {
            System.out.println(("Unable to connect."));
            return false;
        }
        String sql = "SELECT list_ids FROM Users_info WHERE user_id= " + userId + ";";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                List<String> result = (List<String>) rs.getArray("List_ids");
                if(result.isEmpty()){
                    System.out.println("no to-do lists under the user");
                    return false;
                }
                for (String s : result){
                    deleteList(userId,s);
                }
            }
        }catch(SQLException e) {
            return false;
        }
        return true;
    }


    public void addUser (String username, String password){

    }
    public boolean addList(String userName, String listName, String type) {
        if (connection == null) {
            connect();
        }
        if (connection == null) {
            System.out.println("Unable to connect.");
            return false;
        }
        if(!(type.equals("Group") || type.equals("Individual"))){
            System.out.println("task type should only be Group or Individual");
            return false;
        }
        String sql = "INSERT INTO To_do_lists (toDo_list_id, toDo_list_name, toDo_list_type, LastModifiedDate) VALUES (?, ?, ?, ?);";
        String list_id = userName+"_"+listName;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, list_id);
            stmt.setString(2, listName);
            stmt.setString(3, type);
            Date date = new Date();
            Timestamp ts = new Timestamp(date.getTime());
            stmt.setTimestamp(4, ts);
            int rows = stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;

        }
        boolean update = updateListUserInfo(userName,"add",list_id);
        if(update){
            return true;
        }
        else {
            return false;
        }
    }

    public boolean deleteList(String userName, String listName) {
        if (connection == null) {
            connect();
        }
        if (connection == null) {
            System.out.println(("Unable to connect."));
            return false;
        }
        String list_id = userName+"_"+listName;
        String sql = "DELETE * FROM To_do_lists WHERE toDo_list_id=" + list_id + ";";;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            int rows = stmt.executeUpdate();
        } catch (SQLException e) {
            return false;
        }

        boolean update = updateListUserInfo(userName,"delete",list_id);
        if(update){
            return true;
        }
        else {
            return false;
        }

    }


    public boolean displayAllListsNames(String user_id){
        List<String> resultList = new ArrayList<String>();
        if (connection == null) {
            connect();
        }
        if (connection == null) {
            System.out.println(("Unable to connect."));
            return false;
        }
        String sql_get_list_name = "SELECT list_ids FROM Users_info WHERE user_id=" + user_id + ";";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql_get_list_name);
            System.out.println("Here is all your to do lists");
            while (rs.next()) {
                resultList = (List<String>) rs.getArray("List_ids");
            }
        }catch(SQLException e){
            return false;
        }
        if(resultList.isEmpty()){
            System.out.println("There exits no lists");
            return false;
        }
        for (String a : resultList){
            String sql_get_name_type = "SELECT toDo_list_id, toDo_list_type FROM Todo_lists WHERE toDo_list_id=" + a + ";";
            try {
                Statement stmt_name_type = connection.createStatement();
                ResultSet rs_name_type = stmt_name_type.executeQuery(sql_get_name_type);
                System.out.println("Here is all your to do lists");
                while (rs_name_type.next()) {
                    System.out.println("Name: " + rs_name_type.getString("toDo_list_id") + " Type: " + rs_name_type.getString("toDo_list_type"));
                }
            }catch(SQLException e){
                return false;
            }
        }
        return true;
    }

}

