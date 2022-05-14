package edu.uwb.css533.homework;

import edu.uwb.css533.homework.resources.UserResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class toDoListApplication extends Application<toDoListConfiguration> {

    public static void main(final String[] args) throws Exception {
        new toDoListApplication().run(args);
    }

    @Override
    public String getName() {
        return "toDoList";
    }

    @Override
    public void initialize(final Bootstrap<toDoListConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final toDoListConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
        UserResource userResource = new UserResource();
        environment.jersey().register(userResource);
    }

}
