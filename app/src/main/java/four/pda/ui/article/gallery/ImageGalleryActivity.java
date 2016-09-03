package four.pda.ui.article.gallery;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

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

	@ViewById ViewPager pager;

	@AfterViews
	void afterViews() {
		pager.setAdapter(new ImagesPagerAdapter(this, images));
	}

}
