package four.pda.ui;

import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by asavinova on 10/04/15.
 */
public class Images {

	private static final Logger L = LoggerFactory.getLogger(Images.class);

	public static void load(ImageView view, String url) {
		load(view, url, null);
	}

	public static void loadWithZoom(final ImageView view, String url, final PhotoViewAttacher attacher) {
		Callback callback = new Callback() {
			@Override
			public void onSuccess() {
				attacher.update();
			}

			@Override
			public void onError() {
			}
		};
		load(view, url, callback);
	}

	private static void load(final ImageView view, String url, Callback callback) {
		Picasso.with(view.getContext())
				.load(url)
				.fit()
				.centerInside()
				.into(view, new Callback() {
					@Override
					public void onSuccess() {
						if (callback != null) {
							callback.onSuccess();
						}
					}

					@Override
					public void onError() {
						L.error("Can't load image with url {}", url);
						Crashlytics.logException(new RuntimeException("Can't load image"));

						if (callback != null) {
							callback.onError();
						}
					}
				});
	}

}
