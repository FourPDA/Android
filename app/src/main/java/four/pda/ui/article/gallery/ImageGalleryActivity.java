package four.pda.ui.article.gallery;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import four.pda.R;

/**
 * Created by asavinova on 01/09/16.
 */
@EActivity(R.layout.image_gallery_activity)
public class ImageGalleryActivity extends AppCompatActivity {

	@Extra String currentUrl;
	@Extra ArrayList<String> images;

	@ViewById ImageView closeView;
	@ViewById TextView currentIndexView;
	@ViewById TextView imagesCountView;
	@ViewById ViewPager pager;

	@AfterViews
	void afterViews() {
		closeView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		int index = getCurrentImageIndex();
		currentIndexView.setText(String.valueOf(index + 1));
		imagesCountView.setText(String.valueOf(images.size()));

		pager.setAdapter(new ImagesPagerAdapter(this, images));
		pager.setCurrentItem(index, true);
		pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				currentIndexView.setText(String.valueOf(position + 1));
			}
		});

	}

	private int getCurrentImageIndex() {
		int index = 0;
		for (String image : images) {
			if (image.equals(currentUrl)) {
				break;
			}
			index++;
		}
		return index;
	}

}
