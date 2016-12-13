package stroom.timeline.loader;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stroom.timeline.api.TimelineService;
import stroom.timeline.api.TimelineServiceFactory;
import stroom.timeline.hbase.HBaseConnectionImplBuilder;
import stroom.timeline.model.OrderedEvent;
import stroom.timeline.model.Timeline;

import java.time.Instant;
import java.util.Optional;

@Singleton
public class Loader {
    private static final Logger LOGGER = LoggerFactory.getLogger(Loader.class);

    private Config config;
    private TimelineService timelineService;
    private Timeline timeline;

    @Inject
    public Loader(Config config){
        this.config = config;

        timelineService = TimelineServiceFactory.getTimelineService(HBaseConnectionImplBuilder.instance().build());
        Optional<Timeline> timelineResult = timelineService.fetchTimeline(config.getTimeline());
        if(!timelineResult.isPresent()){
            throw new RuntimeException("Timeline does not exist!");
        }
        else{
            timeline = timelineResult.get();
        }
    }

    public void load(Instant instant, byte[] bytes) {
        OrderedEvent event = new OrderedEvent(instant, bytes);
        timelineService.putEvent(timeline, event);
    }
}
