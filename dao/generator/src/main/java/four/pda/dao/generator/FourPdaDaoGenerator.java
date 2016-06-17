package four.pda.dao.generator;

import java.io.File;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class FourPdaDaoGenerator {

	public static void main(String[] args) throws Exception {

		Schema schema = new Schema(4, "four.pda.dao");
		addArticleEntity(schema);

		String outDir = "dao/src/main/java";
		new File(outDir).mkdirs();

		new DaoGenerator().generateAll(schema, outDir);
	}

	private static void addArticleEntity(Schema schema) {
		Entity article = schema.addEntity("Article");
		article.addIdProperty();
		article.addDateProperty("date");
		article.addStringProperty("title");
		article.addStringProperty("description");
		article.addStringProperty("category");
		article.addStringProperty("image");
		article.addDateProperty("publishedDate");
		article.addIntProperty("commentsCount");
	}

}
