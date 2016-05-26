package four.pda.templates.exporter;

import four.pda.template.NewsArticleTemplate;

/**
 * Created by pavel on 26/05/16.
 */
public class Exporter {

	public static void main(String[] args) {
		String make = new NewsArticleTemplate().make("Hello World");
		System.out.println(make);
	}

}
