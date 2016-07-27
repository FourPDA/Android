package four.pda.template;

import com.github.mustachejava.Mustache;

import org.apache.commons.io.output.StringBuilderWriter;

import java.util.HashMap;

/**
 * Created by asavinova on 26/07/16.
 */
public class ProfileTemplate extends Template {

	public String make(String source) {

		HashMap<String, String> scopes = new HashMap<>();
		scopes.put("content", source);

		StringBuilderWriter writer = new StringBuilderWriter();
		Mustache template = getTemplate("ProfileTemplate.htm", "Profile");
		template.execute(writer, scopes);

		return writer.toString();
	}

}
