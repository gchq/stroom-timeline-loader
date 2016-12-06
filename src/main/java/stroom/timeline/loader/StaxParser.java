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

@Singleton
public class StaxParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(StaxParser.class);
    private int inputCount = 0;
    private Transformer transformer;
    private XMLInputFactory xmlInputFactory;
    private Loader loader;

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

                xsr.nextTag(); // Advance to statements element
                while (xsr.nextTag() == XMLStreamConstants.START_ELEMENT) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    transformer.transform(new StAXSource(xsr), new StreamResult(baos));
                    incrementInputCount();
                    loader.load(baos.toByteArray());
                }
            } catch (XMLStreamException | TransformerException | IOException e) {
                LOGGER.error("Unable to split out element!", e);
            }
        };
    }


    private void incrementInputCount(){
        inputCount++;
    }
    public int getInputCount() {
        return inputCount;
    }
}
