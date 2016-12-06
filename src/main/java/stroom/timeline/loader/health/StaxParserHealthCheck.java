package stroom.timeline.loader.health;

import com.codahale.metrics.health.HealthCheck;
import stroom.timeline.loader.StaxParser;

public class StaxParserHealthCheck extends HealthCheck {
    private StaxParser staxParser;

    public StaxParserHealthCheck(StaxParser staxParser) {
        this.staxParser = staxParser;
    }

    @Override
    protected Result check() throws Exception {
        return Result.healthy("Processed " + staxParser.getInputCount());
    }
}
