package four.pda.client;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by swap_i on 25/10/15.
 */
public class AbstractParser {

	protected IdAndDate getIdAndDateFromUrl(String url) {

		String[] urlParts = url.split("/");

		IdAndDate idAndDate = new IdAndDate();

		idAndDate.id = Long.parseLong(urlParts[4]);

		Calendar calendar = Calendar.getInstance();
		int year = Integer.parseInt(urlParts[1]);
		int month = Integer.parseInt(urlParts[2]);
		int date = Integer.parseInt(urlParts[3]);
		calendar.set(year, month, date, 0, 0, 0);
		idAndDate.date = calendar.getTime();

		return idAndDate;
	}

	class IdAndDate {
		long id;
		Date date;
	}

}
