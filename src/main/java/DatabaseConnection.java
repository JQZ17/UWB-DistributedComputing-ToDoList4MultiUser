package edu.uwb.css533.homework;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class DatabaseConnection {
    //format is driver: provider://machine(ip or dns):port/database
    private String url = "jdbc:postgresql://localhost:5432/postgres";
    private String username = "postgres";
    private String password = "722713";
    private int connectionTries = 3;
    Connection connection;
    public Boolean isConnected() {
        int cnt = connectionTries;
        while (connection == null & cnt > 0) {
            connect();
            cnt--;
        }
        if (connection == null) {

            return false;
        }
        return true;

    }
    public DatabaseConnection() {
    }
    public boolean verify(String userName, String password){
        String[] splited = logIn(userName,password).split(" ");
        if(splited[0].equals("Successfully")){
            return true;
        }
        else{
            return false;
        }
    }
    public String logIn(String userName, String password){return "Successfully connected ";}

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

    public boolean generateToDoListsTable() {
        if (connection == null) {
            connect();
        }
        if (connection == null) {
            System.out.println(("Unable to connect."));
            return false;
        }
        String sql = "CREATE TABLE Todo_lists (toDo_list_id VARCHAR ( 500 ) PRIMARY KEY, toDo_list_name VARCHAR ( 500 ),toDo_list_type VARCHAR ( 500 ) NOT NULL, LastModifiedDate TIMESTAMP NOT NULL );";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            int rows = stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
    public boolean generateUsersInfoTable() {
        if (connection == null) {
            connect();
        }
        if (connection == null) {
            System.out.println(("Unable to connect."));
            return false;
        }
        String sql = "CREATE TABLE Users_info (user_id VARCHAR ( 500 ) PRIMARY KEY, user_name VARCHAR ( 500 ) UNIQUE NOT NULL,user_password VARCHAR ( 500 ) NOT NULL, List_ids TEXT [] );";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            int rows = stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
    public boolean generateTasksTable(){
        if (connection == null) {
            connect();
        }
        if (connection == null) {
            System.out.println(("Unable to connect."));
            return false;
        }
        String sql = "CREATE TABLE Task (task_id VARCHAR ( 500 ) PRIMARY KEY, task_name VARCHAR ( 500) UNIQUE NOT NULL,task_description VARCHAR ( 500), task_status VARCHAR ( 500 ), toDo_list_id VARCHAR ( 500 ), LastModifiedDate TIMESTAMP NOT NULL );";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            int rows = stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
    public boolean checkAccess(String username, String listid) {
        if (isConnected()) {
            String sql = "SELECT user_name, List_ids FROM Users_info WHERE user_name=?;";
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, username);

                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    Array listidsArray = rs.getArray(2);
                    String[] listids = (String[])listidsArray.getArray();
                    for (String s : listids) {
                        if (s.equals(listid)) {
                            System.out.println("User: " + username + " has access to list: " + listid);
                            return true;
                        }
                    }
                }
                return false;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            System.out.println( "Error: Unable to connect " + url);
            return false;
        }
    }
    public boolean grantAccess(String username, String listid) {
        if (isConnected()) {
            String sql = "SELECT List_ids FROM Users_info WHERE user_name=?;";
            String sqlUpdate = "UPDATE Users_info SET List_ids[?] = ? WHERE user_name=?;";
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, username);

                ResultSet rs = statement.executeQuery();
                int currentlistidLength = 0;
                if (rs.next()) {
                    Array listidsArray = rs.getArray(1);
                    String[] listids = (String[])listidsArray.getArray();
                    currentlistidLength = listids.length;
                    for (String s : listids) {
                        if (s.equals(listid)) {
                            System.out.println("User: " + username + "already had the access to list: " + listid);
                            return false;
                        }
                    }
                }
                System.out.println("User: " + username + "doesn't have the access to list: " + listid + ",granting access now!");
                PreparedStatement statement2 = connection.prepareStatement(sqlUpdate);
                statement2.setInt(1, currentlistidLength + 1);
                statement2.setString(2, listid);
                statement2.setString(3, username);

                statement2.executeUpdate();
                System.out.println("Grant access successfully");
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            System.out.println("Error: Unable to connect " + url);
            return false;
        }
    }

    public boolean addTask(String task_name, String task_description, String todolist_name, String user_name){
        String list_id = user_name + "_" + todolist_name;
        boolean allowed = checkAccess(user_name,list_id);
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
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean removeAccess(String username, String listid) {
        if (isConnected()) {
            String sql = "SELECT List_ids FROM Users_info WHERE user_name=?;";
            String sqlUpdate = "UPDATE Users_info SET List_ids = ? WHERE user_name=?;";
            try {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, username);

                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    Array listidsArray = rs.getArray(1);
                    String[] listids = (String[])listidsArray.getArray();
                    for (int i = 0; i < listids.length; i++) {
                        if (listids[i].equals(listid)) {
                            System.out.println("User: " + username + "currently have the access to list: " + listid + ", removing it!");
                            int removeIndex = i;
                            String[] copy = new String[listids.length - 1];
                            for (int j = 0; j < copy.length; j++){
                                if (j < removeIndex) {
                                    copy[j] = listids[j];
                                } else {
                                    copy[j] = listids[j + 1];
                                }
                            }
                            Array array = connection.createArrayOf("VARCHAR", copy);
                            PreparedStatement statement2 = connection.prepareStatement(sqlUpdate);
                            statement2.setArray(1, array);
                            statement2.setString(2, username);
                            statement2.executeUpdate();
                            System.out.println("Successfully removed the access");
                            return true;
                        }
                    }

                }
                System.out.println("User: " + username + "doesn't have the access to list: " + listid + ", so can't remove it");
                return false;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            System.out.println("Error: Unable to connect " + url);
            return false;
        }
    }

    public boolean deleteTask(String taskName, String listName,String userName){
        String list_id = userName+"_"+listName;
        boolean allowed = checkAccess(userName,list_id);
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
        String sql = "DELETE FROM Task WHERE task_id= ?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,task_id);
            int rows = stmt.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    private boolean deleteAccess(String user_name, String toDo_list_id) {
        return true;
    }

    public boolean deleteAllTasks(String user_name, String list_name){
        String list_id = user_name+"_"+list_name;
        boolean allowed = checkAccess(user_name,list_id);
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
        String sql = "DELETE FROM Task WHERE toDo_list_id=?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,list_id);
            stmt.executeUpdate();
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean displayAllTaskNames(String user_name, String list_name){
        String list_id = user_name+"_"+list_name;
        boolean allowed = checkAccess(user_name,list_id);
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
        String sql_get_task_name = "SELECT task_name FROM Task WHERE toDo_list_id=?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql_get_task_name);
            stmt.setString(1,list_id);
            ResultSet rs = stmt.executeQuery();
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
        String list_id = user_name+"_"+list_name;
        boolean allowed = checkAccess(user_name,list_id);
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
        String update_task_content_sql = "UPDATE Task SET task_description =?,LastModifiedDate =? WHERE task_id =?;";
        try{
            PreparedStatement stmt = connection.prepareStatement(update_task_content_sql);
            stmt.setString(1,content);
            stmt.setTimestamp(2,ts);
            stmt.setString(3,task_id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updateTaskStatus(String user_name, String list_name, String status, String task_name){
        String list_id = user_name+"_"+list_name;
        boolean allowed = checkAccess(user_name,list_id);
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
        String task_id = user_name+"_"+list_name+"_"+task_name;
        String update_task_status_sql = "UPDATE Task SET task_status =?,LastModifiedDate =? WHERE task_id =?;";
        try{
            PreparedStatement stmt = connection.prepareStatement(update_task_status_sql);
            stmt.setString(1,status);
            stmt.setTimestamp(2,ts);
            stmt.setString(3,task_id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
        String sql = "SELECT list_ids FROM Users_info WHERE user_id=?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Array result = rs.getArray(1);
                String[] result_2 = (String[]) result.getArray();
                if(result_2.length == 0){
                    System.out.println("no to-do lists under the user");
                    return false;
                }
                for (String s : result_2){
                    String[] splited =s.split("_");
                    String listName = splited[1];
                    deleteList(userId,listName);
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
        String sql = "INSERT INTO Todo_lists (toDo_list_id, toDo_list_name, toDo_list_type, LastModifiedDate) VALUES (?, ?, ?, ?);";
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
        boolean update = grantAccess(userName,list_id);
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
        String sql = "DELETE FROM Todo_lists WHERE toDo_list_id=?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1,list_id);
            int rows = stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        boolean update = removeAccess(userName,list_id);
        if(update){
            return true;
        }
        else {
            System.out.println("update info table failed");
            return false;
        }

    }

    public boolean displayAllListsNames(String user_id){
        String[] result = {};
        if (connection == null) {
            connect();
        }
        if (connection == null) {
            System.out.println(("Unable to connect."));
            return false;
        }
        String sql_get_list_name = "SELECT list_ids FROM Users_info WHERE user_id=?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql_get_list_name);
            stmt.setString(1,user_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Array resultList = rs.getArray(1);
                result = (String[]) resultList.getArray();
            }
        }catch(SQLException e){
            return false;
        }
        if(result.length == 0){
            System.out.println("There exits no lists");
            return false;
        }
        for (int i =0; i<result.length;i++){
            String sql_get_name_type = "SELECT toDo_list_id, toDo_list_type FROM Todo_lists WHERE toDo_list_id=?;";
            try {
                PreparedStatement stmt_name_type = connection.prepareStatement(sql_get_name_type);
                stmt_name_type.setString(1,result[i]);
                ResultSet rs_name_type = stmt_name_type.executeQuery();
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
