package four.pda.ui;

import android.support.v4.app.Fragment;
import android.view.View;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import four.pda.R;
import four.pda.analytics.Analytics;
import four.pda.App;

/**
 * @author Anna Savinova
 * @author Pavel Savinov
 */
@EFragment(R.layout.drawer)
public class DrawerFragment extends Fragment {

	@ViewById View allCategoryView;
	@ViewById View newsCategoryView;
	@ViewById View reviewsCategoryView;
	@ViewById View articlesCategoryView;
	@ViewById View softwareCategoryView;
	@ViewById View gamesCategoryView;

	private List<ChangeCategoryListener> listeners = new ArrayList<>();
    private Map<View, CategoryType> map = new HashMap<>();
    private Analytics analytics;

    public void addListener(ChangeCategoryListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ChangeCategoryListener listener) {
        listeners.remove(listener);
    }

    @AfterViews
    void afterViews() {

        analytics = ((App) getContext().getApplicationContext()).getAnalytics();

        map.put(allCategoryView, CategoryType.ALL);
        map.put(newsCategoryView, CategoryType.NEWS);
        map.put(gamesCategoryView, CategoryType.GAMES);
        map.put(reviewsCategoryView, CategoryType.REVIEWS);
        map.put(articlesCategoryView, CategoryType.ARTICLES);
        map.put(softwareCategoryView, CategoryType.SOFTWARE);

        for (View view : map.keySet()) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    analytics.drawer().categoryClicked(map.get(view));
                    setViewSelected(view);
                }
            });
        }

    }

    @Click(R.id.about_view)
    void aboutClicked() {
        analytics.drawer().aboutClicked();
        AboutActivity_.intent(getActivity()).start();
    }

    public void setCategorySelected(CategoryType categoryType) {
        for (Map.Entry<View, CategoryType> entry : map.entrySet()) {
            if (entry.getValue() == categoryType) {
                setViewSelected(entry.getKey());
                return;
            }
        }
        throw new IllegalStateException("No view for category " + categoryType.name());
    }

    private void setViewSelected(View view) {
        for (View iterView : map.keySet()) {
            iterView.setSelected(false);
        }
        view.setSelected(true);
        for (ChangeCategoryListener listener : listeners) {
            listener.onChange(map.get(view));
        }
    }

    public interface ChangeCategoryListener {
        void onChange(CategoryType type);
    }

}
