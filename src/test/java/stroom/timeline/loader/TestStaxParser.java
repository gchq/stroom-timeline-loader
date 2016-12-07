package stroom.timeline.loader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TestStaxParser {

    @Mock
    Loader mockLoader;

    @InjectMocks
    StaxParser staxParser;

    @Test
    public void testSimpleFileSplitting() throws IOException {
        ArgumentCaptor<byte[]> argument = ArgumentCaptor.forClass(byte[].class);

        FileProcessor fileProcessor = staxParser.getFileProcessor();
        String testDataFileName = "src/test/resources/TestStaxParser/testSimpleFileSplitting_data.xml";
        fileProcessor.processFile(testDataFileName);

        verify(mockLoader, times(3)).load(argument.capture());
        List<byte[]> results = argument.getAllValues();
        // We need to know that each byte[] is different and as we expect
        assertThat(new String(results.get(0)).contains("<TimeCreated>2010-01-01T02:03:36.000Z</TimeCreated>"), is(true));
        assertThat(new String(results.get(1)).contains("<TimeCreated>2010-01-01T02:03:37.000Z</TimeCreated>"), is(true));
        assertThat(new String(results.get(2)).contains("<TimeCreated>2010-01-01T02:03:38.000Z</TimeCreated>"), is(true));

    }
}
