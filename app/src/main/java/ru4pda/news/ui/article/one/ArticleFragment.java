package ru4pda.news.ui.article.one;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru4pda.news.Dao;
import ru4pda.news.R;
import ru4pda.news.client.Ru4pdaClient;
import ru4pda.news.ui.ViewUtils;

/**
 * Created by asavinova on 11/04/15.
 */
@EFragment(R.layout.article_one)
public class ArticleFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

	private static final Logger L = LoggerFactory.getLogger(ArticleFragment.class);

	private static final int LOADER_ID = 0;

	@FragmentArg long id;
	@FragmentArg String title;

	@ViewById ScrollView scrollView;
	@ViewById ImageView imageView;
	@ViewById WebView webView;
	@ViewById View headerLayout;

	@ViewById View infoLayout;
	@ViewById TextView titleView;
	@ViewById TextView dateView;

	@Bean Dao dao;
	@Bean Ru4pdaClient client;

	@AfterViews
	void afterViews() {

		webView.getSettings().setJavaScriptEnabled(true);

		loadData();

		scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
			@Override
			public void onScrollChanged() {
				int scrollY = scrollView.getScrollY();
				ViewGroup.LayoutParams params = headerLayout.getLayoutParams();
				params.height = Math.max(infoLayout.getHeight(), imageView.getHeight() - scrollY);
				headerLayout.setLayoutParams(params);
			}
		});
	}

	private void loadData() {
		getLoaderManager().restartLoader(LOADER_ID, null, new Callbacks());
	}

	@Override
	public void onRefresh() {
		loadData();
	}

	@UiThread
	void updateData(ArticleTaskLoader.WrapperInfo info) {
		ViewUtils.loadImage(imageView, info.article.getImage());
		titleView.setText(info.article.getTitle());
		dateView.setText(ViewUtils.VERBOSE_DATE_FORMAT.format(info.article.getDate()));
		webView.setWebChromeClient(new WebChromeClient());
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				openActionViewIntent(url);
				return true;
			}
		});
		webView.loadData(getFormattedText(info.content), "text/html; charset=utf-8", null);
	}

	private void openActionViewIntent(String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		try {
			startActivity(intent);
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(getActivity(), R.string.no_content_applications_installed, Toast.LENGTH_SHORT).show();
		}
	}

	private String getFormattedText(String content) {
		return "<!DOCTYPE html>\n"
				+ "<html lang=\"ru-RU\">\n"
				+ "<head>"
				+ "<link rel=\"stylesheet\" href=\"http://s.4pda.to/css/site.min.css?_=1429170453\"/>"
				+ "</head>\n"
				+ "\t<body itemscope itemtype=\"http://schema.org/WebPage\">"
				+ "<div class=\"container\" itemscope=\"\" itemtype=\"http://schema.org/Article\">"
				+ "<div class=\"content\"><div class=\"content-box\" itemprop=\"description\">"
				+ content
				+ "</div></div></div>"
				+ "</body></html>";
	}

	class Callbacks implements LoaderManager.LoaderCallbacks<ArticleTaskLoader.WrapperInfo> {

		@Override
		public Loader onCreateLoader(int loaderId, final Bundle args) {
			return new ArticleTaskLoader(getActivity(), dao, client, id);
		}

		@Override
		public void onLoadFinished(Loader loader, ArticleTaskLoader.WrapperInfo wrapperInfo) {
			updateData(wrapperInfo);
		}

		@Override
		public void onLoaderReset(Loader<ArticleTaskLoader.WrapperInfo> loader) {
		}

	}

}
