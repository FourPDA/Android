package four.pda.ui;

import android.view.MotionEvent;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.DefaultOnDoubleTapListener;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by asavinova on 10/04/15.
 */
public class Images {

	public static void load(ImageView view, String url) {
		load(view, url, null);
	}

	public static void load(final ImageView view, String url, Callback callback) {
		Picasso.with(view.getContext())
				.load(url)
				.fit()
				.centerInside()
				.into(view, callback);
	}

	public static void loadWithZoom(final ImageView view, String url) {
		Callback callback = new Callback() {
			@Override
			public void onSuccess() {
				PhotoViewAttacher attacher = new PhotoViewAttacher(view);
				attacher.setScaleLevels(1, 3, 8);
				attacher.setOnDoubleTapListener(new DoubleTapListener(attacher));
				attacher.update();
			}

			@Override
			public void onError() {
			}
		};
		load(view, url, callback);
	}

	private static class DoubleTapListener extends DefaultOnDoubleTapListener {
		
		private PhotoViewAttacher attacher;

		public DoubleTapListener(PhotoViewAttacher attacher) {
			super(attacher);
			this.attacher = attacher;
		}

		@Override
		public boolean onDoubleTap(MotionEvent ev) {
			
			if (attacher == null)
				return false;

			try {
				float scale = attacher.getScale();
				float x = ev.getX();
				float y = ev.getY();
	
				if (scale < attacher.getMediumScale()) {
					attacher.setScale(attacher.getMediumScale(), x, y, true);
				} else {
					attacher.setScale(attacher.getMinimumScale(), x, y, true);
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				// Can sometimes happen when getX() and getY() is called
			}

			return true;
		}

	}

}
