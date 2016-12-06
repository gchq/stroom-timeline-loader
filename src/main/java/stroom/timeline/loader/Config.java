package stroom.timeline.loader;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class Config extends Configuration {

    @NotEmpty
    private String inputDirectory;

    @JsonProperty
    public String getInputDirectory() {
        return inputDirectory;
    }

    @JsonProperty
    public void setInputDirectory(String name) {
        this.inputDirectory = name;
    }
}

