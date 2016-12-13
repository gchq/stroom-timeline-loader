package stroom.timeline.loader;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class Config extends Configuration {

    @Valid
    @NotNull
    @JsonProperty
    private String inputDirectory;

    @Valid
    @NotNull
    @JsonProperty
    private String timeline;

    @Valid
    @NotNull
    @JsonProperty
    private String timeCreatedXPath;

    @Valid
    @NotNull
    @JsonProperty
    private String defaultElementNamespace;

    public String getInputDirectory() {
        return inputDirectory;
    }

    public void setInputDirectory(String name) {
        this.inputDirectory = name;
    }

    public String getTimeline() {
        return timeline;
    }

    public String getTimeCreatedXPath() {
        return timeCreatedXPath;
    }

    public String getDefaultElementNamespace() {
        return defaultElementNamespace;
    }
}

