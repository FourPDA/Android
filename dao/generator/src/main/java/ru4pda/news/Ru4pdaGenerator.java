package ru4pda.news;

import java.io.File;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class Ru4pdaGenerator {

	public static void main(String[] args) throws Exception {

		Schema schema = new Schema(1, "ru4pda.news.dao");
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
	}

}
