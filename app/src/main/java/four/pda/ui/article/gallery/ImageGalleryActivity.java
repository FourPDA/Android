package four.pda.ui.article.gallery;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

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

	@ViewById Toolbar toolbar;
	@ViewById ViewPager pager;

	@AfterViews
	void afterViews() {
		toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		int index = getCurrentImageIndex();
		toolbar.setTitle(getString(R.string.gallery_title, index + 1, images.size()));

		pager.setAdapter(new ImagesPagerAdapter(this, images));
		pager.setCurrentItem(index, true);
		pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				toolbar.setTitle(getString(R.string.gallery_title, position + 1, images.size()));
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
