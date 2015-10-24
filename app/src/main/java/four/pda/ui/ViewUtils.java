package four.pda.ui;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

/**
 * Created by asavinova on 10/04/15.
 */
public class ViewUtils {

	public static final SimpleDateFormat VERBOSE_DATE_FORMAT = new SimpleDateFormat("dd MMMM yyyy");

	public static void loadImage(ImageView view, String url) {
		Picasso.with(view.getContext())
				.load(url)
				.into(view);
	}

}
