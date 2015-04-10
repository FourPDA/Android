package ru4pda.news.ui;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

/**
 * Created by asavinova on 10/04/15.
 */
public class ViewUtils {

	public static final SimpleDateFormat VERBOSE_DATE_FORMAT = new SimpleDateFormat("dd MMMM yyyy");

	private static final String IMAGE_BASE_URL = "http://s.4pda.to/wp-content/uploads/headbands/213/phband-%s.jpg";

	public static void loadImage(ImageView view, long id) {
		Picasso.with(view.getContext())
				.load(String.format(IMAGE_BASE_URL, id))
				.into(view);
	}
}
