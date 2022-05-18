package edu.uwb.css533.homework.resources;

import edu.uwb.css533.homework.DatabaseConnection;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

///http://server:port/path?param=something
//path
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    DatabaseConnection databaseConnection;
    @GET
    @Path("{userName}/{userPassword}/addList/{listName}/{listType}")
    public Response addList(
            @PathParam("userName") String userName,
            @PathParam("userPassword") String userPassword,
            @PathParam("listName") String listName,
            @PathParam("listType") String listType) {
            if(databaseConnection.logIn(userName,userPassword)){
                boolean success = databaseConnection.addList(userName,listName,listType);
                if(success){
                    System.out.println( listType +" list " + listName + " has been added for user: " + userName);
                }
                else{
                    System.out.println(" Adding " +listType +" list " +listName + "for user: "+ userName + "has failed");
                }
            }
            else{
                System.out.println("Incorrect user or password");
            }
            return Response.ok().build();
    }
    @GET
    @Path("{userName}/{userPassword}/deleteList/{listName}")
    public Response deleteList(
            @PathParam("userName") String userName,
            @PathParam("userPassword") String userPassword,
            @PathParam("listName") String listName) {
        if(databaseConnection.logIn(userName,userPassword)){
            boolean success = databaseConnection.deleteList(userName,userName);
            if(success){
                System.out.println( " list " + listName + " has been deleted for user: " + userName);
            }
            else{
                System.out.println(" Deleting list " +listName + "for user: "+ userName + "has failed");
            }
        }
        else{
            System.out.println("Incorrect user or password");
        }
        return Response.ok().build();
    }
    @GET
    @Path("{userName}/{userPassword}/deleteAllLists")
    public Response deleteAllLists(
            @PathParam("userName") String userName,
            @PathParam("userPassword") String userPassword) {
        if(databaseConnection.logIn(userName,userPassword)){
            boolean success = databaseConnection.deleteAllLists(userName);
            if(success){
                System.out.println( " All lists has been deleted for user: " + userName);
            }
            else{
                System.out.println(" Deleting all lists for user: "+ userName + "has failed");
            }
        }
        else{
            System.out.println("Incorrect user or password");
        }
        return Response.ok().build();
    }
    @GET
    @Path("{userName}/{userPassword}/displayAllListsNames")
    public Response displayAllListsNames(
            @PathParam("userName") String userName,
            @PathParam("userPassword") String userPassword) {
        if(databaseConnection.logIn(userName,userPassword)){
            boolean success = databaseConnection.deleteAllLists(userName);
            if(success){
                System.out.println( " All lists' names have been displayed for user: " + userName);
            }
            else{
                System.out.println(" Displaying all lists' names for user: "+ userName + "has failed");
            }
        }
        else{
            System.out.println("Incorrect user or password");
        }
        return Response.ok().build();
    }

    @GET
    @Path("{userName}/{userPassword}/list/{listName}/addTask/{taskName}/{taskDescription}")
    public Response addTask(
            @PathParam("userName") String userName,
            @PathParam("userPassword") String userPassword,
            @PathParam("listName") String listName,
            @PathParam("taskName") String taskName,
            @PathParam("taskDescription") String taskDescription) {
        if(databaseConnection.logIn(userName,userPassword)){
            boolean success = databaseConnection.addTask(taskName,taskDescription,listName,userName);
            if(success){
                System.out.println( " task " + taskName +"of list" + listName + " has been added for user: " + userName);
            }
            else{
                System.out.println(" Adding " +taskName +" of  list " +listName + "for user: "+ userName + "has failed");
            }
        }
        else{
            System.out.println("Incorrect user or password");
        }
        return Response.ok().build();
    }

    @GET
    @Path("{userName}/{userPassword}/list/{listName}/deleteTask/{taskName}")
    public Response deleteTask(
            @PathParam("userName") String userName,
            @PathParam("userPassword") String userPassword,
            @PathParam("listName") String listName,
            @PathParam("taskName") String taskName) {
        if(databaseConnection.logIn(userName,userPassword)){
            boolean success = databaseConnection.deleteTask(taskName,listName,userName);
            if(success){
                System.out.println( " task " + taskName +"of list" + listName + " has been deleted for user: " + userName);
            }
            else{
                System.out.println(" Deleting " +taskName +" of  list " +listName + "for user: "+ userName + "has failed");
            }
        }
        else{
            System.out.println("Incorrect user or password");
        }
        return Response.ok().build();
    }

    @GET
    @Path("{userName}/{userPassword}/list/{listName}/deleteAllTasks")
    public Response deleteAllTasks(
            @PathParam("userName") String userName,
            @PathParam("userPassword") String userPassword,
            @PathParam("listName") String listName) {
        if(databaseConnection.logIn(userName,userPassword)){
            boolean success = databaseConnection.deleteAllTasks(userName,listName);
            if(success){
                System.out.println( " All task of list" + listName + " has been deleted for user: " + userName);
            }
            else{
                System.out.println(" Deleting all tasks of  list " +listName + "for user: "+ userName + "has failed");
            }
        }
        else{
            System.out.println("Incorrect user or password");
        }
        return Response.ok().build();
    }
    @GET
    @Path("{userName}/{userPassword}/list/{listName}/displayAllTasksNames")
    public Response displayAllTasksNames(
            @PathParam("userName") String userName,
            @PathParam("userPassword") String userPassword,
            @PathParam("listName") String listName) {
        if(databaseConnection.logIn(userName,userPassword)){
            boolean success = databaseConnection.displayAllTaskNames(userName,listName);
            if(success){
                System.out.println( " All task of list" + listName + " has been displayed for user: " + userName);
            }
            else{
                System.out.println(" Displaying all tasks of  list " +listName + "for user: "+ userName + "has failed");
            }
        }
        else{
            System.out.println("Incorrect user or password");
        }
        return Response.ok().build();
    }
    @GET
    @Path("{userName}/{userPassword}/list/{listName}/updateTaskContent/{taskName}/{taskContent}")
    public Response updateTaskContent(
            @PathParam("userName") String userName,
            @PathParam("userPassword") String userPassword,
            @PathParam("listName") String listName,
            @PathParam("taskName") String taskName,
            @PathParam("taskContent") String taskContent) {
        if(databaseConnection.logIn(userName,userPassword)){
            boolean success = databaseConnection.updateTaskContent(userName,listName,taskContent,taskName);
            if(success){
                System.out.println( "Update task content to "+taskContent+" of task " + taskName +"of list" + listName + " has been added for user: " + userName);
            }
            else{
                System.out.println("Updating task content to "+taskContent+" of task " +taskName +" of  list " +listName + "for user: "+ userName + "has failed");
            }
        }
        else{
            System.out.println("Incorrect user or password");
        }
        return Response.ok().build();
    }
    @GET
    @Path("{userName}/{userPassword}/list/{listName}/updateTaskContent/{taskName}/{taskStatus}")
    public Response updateTaskStatus(
            @PathParam("userName") String userName,
            @PathParam("userPassword") String userPassword,
            @PathParam("listName") String listName,
            @PathParam("taskName") String taskName,
            @PathParam("taskStatus") String taskStatus) {
        if(databaseConnection.logIn(userName,userPassword)){
            boolean success = databaseConnection.updateTaskStatus(userName,listName,taskStatus,taskName);
            if(success){
                System.out.println( "Update task status to "+taskStatus+" of task " + taskName +"of list" + listName + " has been added for user: " + userName);
            }
            else{
                System.out.println("Updating task status to "+taskStatus+" of task " +taskName +" of  list " +listName + "for user: "+ userName + "has failed");
            }
        }
        else{
            System.out.println("Incorrect user or password");
        }
        return Response.ok().build();
    }
}
