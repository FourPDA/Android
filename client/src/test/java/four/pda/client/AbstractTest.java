package four.pda.client;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by swap_i on 25/10/15.
 */
public class AbstractTest {
	protected String getTestFile(String htmlFile) throws IOException {
		try (InputStream stream = getClass().getResourceAsStream(htmlFile)) {
			Assert.assertNotNull("Test resource stream is null", stream);
			return IOUtils.toString(stream);
		}
	}
}
