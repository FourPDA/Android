package four.pda.ui.article.gallery;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import four.pda.ui.ViewUtils;

/**
 * Created by asavinova on 01/09/16.
 */
public class ImagesPagerAdapter extends PagerAdapter {

	private Context context;
	private List<String> images = new ArrayList<>();

	public ImagesPagerAdapter(Context context, List<String> images) {
		this.context = context;
		this.images = images;
	}

	@Override
	public int getCount() {
		return images.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public Object instantiateItem(ViewGroup collection, int position) {
		ImageView imageView = new ImageView(context);
		imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		ViewUtils.loadImage(imageView, images.get(position));
		collection.addView(imageView);
		return imageView;
	}

	@Override
	public void destroyItem(ViewGroup collection, int position, Object view) {
		collection.removeView((View) view);
	}
}
