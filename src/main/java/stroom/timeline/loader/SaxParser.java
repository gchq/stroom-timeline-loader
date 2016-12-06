package stroom.timeline.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SaxParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(SaxParser.class);

    public FileProcessor getFileProcessor(){
        return (String fileData) -> {
            LOGGER.info("Processing file data");
           //TODO process file data
        };
    }
}
