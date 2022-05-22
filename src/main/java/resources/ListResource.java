package edu.uwb.css533.homework.resources;

import edu.uwb.css533.homework.DatabaseConnection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

public class ListResource {
    DatabaseConnection databaseConnection;
    @GET
    @Path("/addList")
    public Response addList(
            @QueryParam("userName") String userName,
            @QueryParam("listName") String listName,
            @QueryParam("listType") String listType) {
        String msg = "";
        boolean success = databaseConnection.addList(userName,listName,listType);
        if(success){
            System.out.println( listType +" list " + listName + " has been added for user: " + userName);
            msg = listType +" list " + listName + " has been added for user: " + userName;
            return Response.ok(msg).build();
        }
        else{
            System.out.println(" Adding " +listType +" list " +listName + "for user: "+ userName + "has failed");
            msg = " Adding " +listType +" list " +listName + "for user: "+ userName + "has failed";
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(msg)
                    .build();
        }
    }
    @GET
    @Path("/deleteList")
    public Response deleteList(
            @QueryParam("userName") String userName,
            @QueryParam("listName") String listName) {
        String msg = "";
        boolean success = databaseConnection.deleteList(userName,userName);
        if(success){
            msg = " list " + listName + " has been deleted for user: " + userName;
            return Response.ok(msg).build();
        }
        else{
            msg=" Deleting list " +listName + "for user: "+ userName + "has failed";
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(msg)
                    .build();
        }
    }
    @GET
    @Path("deleteAllLists")
    public Response deleteAllLists(
            @QueryParam("userName") String userName) {
        String msg = "";
        boolean success = databaseConnection.deleteAllLists(userName);
        if(success){
            msg= " All lists has been deleted for user: " + userName;
            return Response.ok(msg).build();
        }
        else{
            msg=" Deleting all lists for user: "+ userName + "has failed";
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(msg)
                    .build();
        }
    }
    @GET
    @Path("/displayAllListsNames")
    public Response displayAllListsNames(
            @QueryParam("userName") String userName) {
        String msg = "";
        boolean success = databaseConnection.deleteAllLists(userName);
        if(success){
            msg=" All lists' names have been displayed for user: " + userName;
            return Response.ok(msg).build();
        }
        else{
            msg=" Displaying all lists' names for user: "+ userName + "has failed";
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(msg)
                    .build();
        }
    }
}
