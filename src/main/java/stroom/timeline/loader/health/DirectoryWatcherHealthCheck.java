package stroom.timeline.loader.health;

import com.codahale.metrics.health.HealthCheck;
import stroom.timeline.loader.DirectoryWatcher;

public class DirectoryWatcherHealthCheck extends HealthCheck {
    private DirectoryWatcher directoryWatcher;

    public DirectoryWatcherHealthCheck(DirectoryWatcher directoryWatcher) {
        this.directoryWatcher = directoryWatcher;
    }

    @Override
    protected Result check() throws Exception {
        if(!directoryWatcher.isWatching()){
          return Result.unhealthy("I am not watching the input directory! The error is: '" + directoryWatcher.getErrorMessage() + "'");
        } else{

        }
        return Result.healthy("Watching '" + directoryWatcher.getWatchedDirectory() + "'");
    }
}
