package stroom.timeline.loader;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class StaxParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(StaxParser.class);
    private int inputCount = 0;
    private Transformer transformer;
    private XMLInputFactory xmlInputFactory;
    private Loader loader;
    final Pattern pattern = Pattern.compile("<TimeCreated>(.+?)</TimeCreated>");

    @Inject
    public StaxParser(Loader loader){
        this.loader = loader;
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            transformer = tf.newTransformer();
            xmlInputFactory = XMLInputFactory.newInstance();
        } catch (TransformerConfigurationException e) {
            LOGGER.error("StaxParser could not be created.", e);
        }
    }

    public FileProcessor getFileProcessor() {
        return (String filePath) -> {
            try (FileReader fileReader = new FileReader(filePath)) {
                XMLStreamReader xsr = xmlInputFactory.createXMLStreamReader(fileReader);

                // The next tag will be an Event
                xsr.nextTag();
                while (xsr.nextTag() == XMLStreamConstants.START_ELEMENT) {
                    // We stream the whole Event out to bytes
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    transformer.transform(new StAXSource(xsr), new StreamResult(baos));
                    byte[] bytes = baos.toByteArray();

                    // We need the TimeCreated so we'll regex it out
                    final Matcher matcher = pattern.matcher(baos.toString());
                    if(matcher.find()) {
                        Instant timeCreated = Instant.parse(matcher.group(1));
                        loader.load(timeCreated, bytes);
                    }
                    else {
                        LOGGER.error("The following file could not be processed because the date could not be extracted: {}", filePath);
                    }

                    incrementInputCount();
                }
            } catch (XMLStreamException | TransformerException | IOException e) {
                LOGGER.error("Unable to split out element!", e);
            }
        };
    }

    //TODO Add a 'failed' count

    private void incrementInputCount(){
        inputCount++;
    }
    public int getInputCount() {
        return inputCount;
    }
}
