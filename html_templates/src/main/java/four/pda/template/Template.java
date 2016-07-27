package four.pda.template;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;

/**
 * Created by asavinova on 26/07/16.
 */
public class Template {

	protected WeakReference<MustacheFactory> factoryRef;
	protected WeakReference<Mustache> templateRef;

	protected MustacheFactory getFactory() {
		if (factoryRef == null || factoryRef.get() == null) {
			factoryRef = new WeakReference<MustacheFactory>(new DefaultMustacheFactory());
		}
		return factoryRef.get();
	}

	protected Mustache getTemplate(String resource, String name) {
		if (templateRef == null || templateRef.get() == null) {
			InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(resource));
			try {
				Mustache mustache = getFactory().compile(reader, name);
				templateRef = new WeakReference<>(mustache);
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return templateRef.get();
	}

}
