package four.pda.client;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import four.pda.client.model.Profile;
import four.pda.client.parsers.ProfileParser;

/**
 * Created by asavinova on 07/06/16.
 */
public class ProfileParserTest extends AbstractTest {

	@Test
	public void profileVarann() throws IOException {
		String pageSource = getHtmlSource("/forum/index.php?showuser=4975039");
		Profile profile = new ProfileParser().parse(pageSource);

		Assert.assertEquals("Wrong profile login", "var.ann", profile.getLogin());
		Assert.assertFalse("Wrong profile photo", profile.getPhoto().isEmpty());
		Assert.assertFalse("Wrong profile info", profile.getPhoto().isEmpty());
	}

	@Test
	public void profileWadym72() throws IOException {
		String pageSource = getHtmlSource("/forum/index.php?showuser=1929816");
		Profile profile = new ProfileParser().parse(pageSource);

		Assert.assertEquals("Wrong profile login", "Wadym72", profile.getLogin());
		Assert.assertFalse("Wrong profile photo", profile.getPhoto().isEmpty());
		Assert.assertFalse("Wrong profile info", profile.getPhoto().isEmpty());
	}

	@Test
	public void profileNews() throws IOException {
		String pageSource = getHtmlSource("/forum/index.php?showuser=204809");
		Profile profile = new ProfileParser().parse(pageSource);

		Assert.assertEquals("Wrong profile login", "News", profile.getLogin());
		Assert.assertFalse("Wrong profile photo", profile.getPhoto().isEmpty());
		Assert.assertFalse("Wrong profile info", profile.getPhoto().isEmpty());
	}

}
