package stroom.timeline.loader;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import stroom.timeline.loader.health.DirectoryWatcherHealthCheck;

public class App extends Application<Config> {

    private Injector injector = null;

    public static void main(String[] args) throws Exception {
        new App().run(args);
    }

    @Override
    public String getName() {
        return "timeline-loader";
    }

    @Override
    public void initialize(Bootstrap<Config> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(Config config, Environment environment) {
        injector = Guice.createInjector(new Module(config));
        DirectoryWatcher directoryWatcher = injector.getInstance(DirectoryWatcher.class);

        final DirectoryWatcherHealthCheck healthCheck = new DirectoryWatcherHealthCheck(directoryWatcher);
        environment.healthChecks().register("DirectoryWatcher", healthCheck);
    }


}