package four.pda.templates.exporter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;

import four.pda.client.parsers.ProfileParser;
import four.pda.template.ProfileTemplate;

/**
 * Created by asavinova on 26/07/16.
 */
public class ProfileExporter {

	private static final Logger L = LoggerFactory.getLogger(ProfileExporter.class);

	private static final String SERVER = "http://4pda.ru/";

	public static void main(String[] args) {
		convertPage("forum/index.php?showuser=4975039", "profile_varann");
		convertPage("forum/index.php?showuser=1929816", "profile_Wadym72");
		convertPage("forum/index.php?showuser=204809", "profile_News");
		convertPage("forum/index.php?showuser=1477609", "profile_Syncaine");
	}

	private static void convertPage(String url, String filename) {
		try {
			String originalPage = IOUtils.toString(URI.create(SERVER + url), "CP1251");
			String croppedPage = new ProfileParser().parse(originalPage).getInfo();
			String wrappedPage = new ProfileTemplate().make(croppedPage);
			FileUtils.write(new File(filename + ".htm"), wrappedPage);
		} catch (Exception e) {
			L.error("Can't convert page " + filename, e);
		}
	}

}

