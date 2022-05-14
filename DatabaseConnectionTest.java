package edu.uwb.css533.homework;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.sql.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/*
 *******The steps for storing Junit:
 * When first open the test file, the IntelliJ system inform users to import and configure Junit,
 * when users agree the import and configure terms, the IntelliJ would automatically import and
 * configure Junit for users
 *
 ********How to run the tests:
 * click the green start button next to "class DatabaseConnectionTest" would test all test units. or
 * click the green start button next to each test unit.
 * considering the connection to database, these test cases have to execute in order, one by one
 * */
class DatabaseConnectionTest {
    DatabaseConnection databaseConnection;

    public DatabaseConnectionTest() {
        this.databaseConnection = new DatabaseConnection();
        databaseConnection.addUser("me","123");
   }


    @Test
    void addList() {
        /*normal execution*/
        Assert.assertTrue(databaseConnection.addList("me","Apple","individual"));
        /*error condition 1: duplicate lists*/
        Assert.assertFalse(databaseConnection.addList("me","Apple","individual"));
        /*error condition 2: duplicate lists*/
        Assert.assertFalse(databaseConnection.addList("me","Apple","ok"));
    }

    @Test
    void deleteList() {
        /*normal execution*/
        Assert.assertTrue(databaseConnection.deleteList("me","Apple"));
        /*error condition 1: delete not exist to-do list*/
        Assert.assertFalse(databaseConnection.deleteList("me","Banana"));
    }

    @Test
    void deleteAllLists() {
        databaseConnection.addList("me","Apple","individual");
        /*normal execution*/
        Assert.assertTrue(databaseConnection.deleteAllLists("me"));
        /*error condition 1: delete all lists when there is no to-do lists under the specific user*/
        Assert.assertFalse(databaseConnection.deleteAllLists("me"));
    }

    @Test
    void displayAlListsNames() {
        /*error condition 1: display all lists when there is no to-do lists under a specific user*/
        Assert.assertFalse(databaseConnection.displayAllListsNames("me"));
        databaseConnection.addList("me","Apple","individual");
        /*normal execution*/
        Assert.assertTrue(databaseConnection.displayAllListsNames("me"));
    }

    @Test
    void addTask() {
        /*normal execution*/
        Assert.assertTrue(databaseConnection.addTask("write a review","write a review of IoT security problems and solutions","Apple","me"));
        /*error condition 2: duplicate tasks*/
        Assert.assertFalse(databaseConnection.addTask("write a review","a","a","me"));
    }

    @Test
    void deleteTask() {
        /*normal execution*/
        Assert.assertTrue(databaseConnection.deleteTask("write a review","Apple","me"));
        /*error condition 1: not exist task*/
        Assert.assertFalse(databaseConnection.deleteTask("write a review","Apple","me"));
    }

    @Test
    void deleteAllTasks() {
        databaseConnection.addTask("write a review","write a review of IoT security problems and solutions","Apple","me");
        /*normal execution*/
        Assert.assertTrue(databaseConnection.deleteAllTasks("me","Apple"));
        /*error condition 1: delete all tasks when there is no tasks under the specific to-do list*/
        Assert.assertFalse(databaseConnection.deleteAllTasks("me","banana"));
    }

    @Test
    void displayAllTaskNames() {
        databaseConnection.addTask("write a review","write a review of IoT security problems and solutions","Apple","me");
        /*normal execution*/
        Assert.assertTrue(databaseConnection.displayAllTaskNames("me","Apple"));
        databaseConnection.deleteAllTasks("me","Apple");
        /*error condition 1: display all tasks' names when there is no task  under a specific to-do list*/
        Assert.assertFalse(databaseConnection.displayAllTaskNames("me","Apple"));
        /*error condition 2: display all tasks' names when there is no such to-do list*/
        Assert.assertFalse(databaseConnection.displayAllTaskNames("me","banana"));
    }

    @Test
    void updateTaskStatus() {
        databaseConnection.addTask("write a review","write a review of IoT security problems and solutions","Apple","me");
        /*normal execution*/
        Assert.assertTrue(databaseConnection.updateTaskStatus("me","In-Progress","write a review"));
        /*error condition 1: the task status for updating is not correct status type (correct status type includes In-Progress, Not-Started, Completed)*/
        Assert.assertFalse(databaseConnection.updateTaskStatus("me","abc","write a review"));
    }


    @Test
    void updateTaskContent() {
        /*normal execution*/
        Assert.assertTrue(databaseConnection.updateTaskContent("me","Apple","write a review of database security issues","write a review"));
        /*error condition 1: update tasks' content when there is no such task*/
        Assert.assertFalse(databaseConnection.updateTaskContent("me", "Apple","a","a"));
    }


    @Test
    void generateToDoListsTable() {
        try {
            DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres","722713");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Connected to database" );
    }
}