package four.pda.ui;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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
		Glide.with(view.getContext())
				.load(url)
				.fitCenter()
				.listener(new RequestListener<String, GlideDrawable>() {
					@Override
					public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
						L.error(String.format("Can't load image with url [%s]", url), e);
						Crashlytics.logException(new RuntimeException("Can't load image by Glide", e));
						return false;
					}

					@Override
					public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
						return false;
					}
				})
				.into(view);
	}

	public static void loadWithZoom(ImageView view, String url, final PhotoViewAttacher attacher) {
		Picasso.with(view.getContext())
				.load(url)
				.fit()
				.centerInside()
				.into(view, new Callback() {
					@Override
					public void onSuccess() {
						attacher.update();
					}

					@Override
					public void onError() {
						L.error("Can't load image with url {}", url);
						Crashlytics.logException(new RuntimeException("Can't load image by Picasso"));
					}
				});
	}

}
