package ru4pda.news.ui;

import android.support.v4.app.Fragment;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

import java.util.ArrayList;
import java.util.List;

import ru4pda.news.R;

/**
 * Created by asavinova on 12/04/15.
 */
@EFragment(R.layout.drawer)
public class DrawerFragment extends Fragment {

	public interface ChangeCategoryListener {
		void onChange(CategoryType type);
	}

	private List<ChangeCategoryListener> listeners = new ArrayList<>();

	@Click(R.id.all_category_view)
	void allCategoryClicked() {
		clickCategory(CategoryType.ALL);
	}

	@Click(R.id.news_category_view)
	void newsCategoryClicked() {
		clickCategory(CategoryType.NEWS);
	}

	@Click(R.id.reviews_category_view)
	void reviewsCategoryClicked() {
		clickCategory(CategoryType.REVIEWS);
	}

	@Click(R.id.articles_category_view)
	void articlesCategoryClicked() {
		clickCategory(CategoryType.ARTICLES);
	}

	@Click(R.id.software_category_view)
	void softwareCategoryClicked() {
		clickCategory(CategoryType.SOFTWARE);
	}

	@Click(R.id.games_category_view)
	void gamesCategoryClicked() {
		clickCategory(CategoryType.GAMES);
	}

	private void clickCategory(CategoryType type) {
		for (ChangeCategoryListener listener : listeners) {
			listener.onChange(type);
		}
	}

	public void addListener(ChangeCategoryListener listener) {
		listeners.add(listener);
	}

	public void removeListener(ChangeCategoryListener listener) {
		listeners.remove(listener);
	}

}
