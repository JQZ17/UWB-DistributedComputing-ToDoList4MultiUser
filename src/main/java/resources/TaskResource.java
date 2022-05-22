package edu.uwb.css533.homework.resources;
import edu.uwb.css533.homework.DatabaseConnection;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

public class TaskResource {
    DatabaseConnection databaseConnection;
    @GET
    @Path("/addTask}")
    public Response addTask(
            @QueryParam("userName") String userName,
            @QueryParam("listName") String listName,
            @QueryParam("taskName") String taskName,
            @QueryParam("taskDescription") String taskDescription) {
        String msg = "";
        boolean success = databaseConnection.addTask(taskName,taskDescription,listName,userName);
        if(success){
            msg = " task " + taskName +"of list" + listName + " has been added for user: " + userName;
            return Response.ok(msg).build();
        }
        else{
            msg=" Adding " +taskName +" of  list " +listName + "for user: "+ userName + "has failed";
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(msg)
                    .build();
        }
    }

    @GET
    @Path("/deleteTask")
    public Response deleteTask(
            @QueryParam("userName") String userName,
            @QueryParam("listName") String listName,
            @QueryParam("taskName") String taskName) {
        String msg = "";
        boolean success = databaseConnection.deleteTask(taskName,listName,userName);
        if(success){
            msg=" task " + taskName +"of list" + listName + " has been deleted for user: " + userName;
            return Response.ok(msg).build();
        }
        else{
            msg=" Deleting " +taskName +" of  list " +listName + "for user: "+ userName + "has failed";
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(msg)
                    .build();
        }
    }

    @GET
    @Path("/deleteAllTasks")
    public Response deleteAllTasks(
            @QueryParam("userName") String userName,
            @QueryParam("listName") String listName) {
        String msg = "";
        boolean success = databaseConnection.deleteAllTasks(userName,listName);
        if(success){
            msg=" All task of list" + listName + " has been deleted for user: " + userName;
            return Response.ok(msg).build();
        }
        else{
            msg=" Deleting all tasks of  list " +listName + "for user: "+ userName + "has failed";
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(msg)
                    .build();
        }
    }
    @GET
    @Path("/displayAllTasksNames")
    public Response displayAllTasksNames(
            @QueryParam("userName") String userName,
            @QueryParam("listName") String listName) {
        String msg = "";
        boolean success = databaseConnection.displayAllTaskNames(userName,listName);
        if(success){
            System.out.println( " All task of list" + listName + " has been displayed for user: " + userName);
            return Response.ok(msg).build();
        }
        else{
            System.out.println(" Displaying all tasks of  list " +listName + "for user: "+ userName + "has failed");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(msg)
                    .build();
        }
    }
    @GET
    @Path("/updateTaskContent")
    public Response updateTaskContent(
            @QueryParam("userName") String userName,
            @QueryParam("listName") String listName,
            @QueryParam("taskName") String taskName,
            @QueryParam("taskContent") String taskContent) {
        String msg = "";
        boolean success = databaseConnection.updateTaskContent(userName,listName,taskContent,taskName);
        if(success){
            msg= "Update task content to "+taskContent+" of task " + taskName +"of list" + listName + " has been added for user: " + userName;
            return Response.ok(msg).build();
        }
        else{
            System.out.println("Updating task content to "+taskContent+" of task " +taskName +" of  list " +listName + "for user: "+ userName + "has failed");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(msg)
                    .build();
        }
    }
    @GET
    @Path("/updateTaskContent")
    public Response updateTaskStatus(
            @QueryParam("userName") String userName,
            @QueryParam("listName") String listName,
            @QueryParam("taskName") String taskName,
            @QueryParam("taskStatus") String taskStatus) {
        String msg = "";
        boolean success = databaseConnection.updateTaskStatus(userName,listName,taskStatus,taskName);
        if(success){
            msg="Update task status to "+taskStatus+" of task " + taskName +"of list" + listName + " has been added for user: " + userName;
            return Response.ok(msg).build();
        }
        else{
            msg="Updating task status to "+taskStatus+" of task " +taskName +" of  list " +listName + "for user: "+ userName + "has failed";
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(msg)
                    .build();
        }
    }
}
