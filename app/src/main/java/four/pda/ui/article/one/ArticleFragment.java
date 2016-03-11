package four.pda.ui.article.one;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.Date;

import javax.inject.Inject;

import four.pda.App;
import four.pda.Dao;
import four.pda.EventBus;
import four.pda.R;
import four.pda.client.FourPdaClient;
import four.pda.ui.AspectRatioImageView;
import four.pda.ui.BaseFragment;
import four.pda.ui.LoadResult;
import four.pda.ui.SupportView;
import four.pda.ui.ViewUtils;
import four.pda.ui.article.ShowCommentsEvent;

/**
 * Created by asavinova on 11/04/15.
 */
@EFragment(R.layout.article_one)
public class ArticleFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

	private static final int LOADER_ID = 0;

	@FragmentArg long id;
	@FragmentArg Date date;
	@FragmentArg String title;
	@FragmentArg String image;

	@ViewById Toolbar toolbar;
	@ViewById CollapsingToolbarLayout collapsingToolbar;
	@ViewById AspectRatioImageView backdropImageView;
	@ViewById WebView webView;

	@ViewById SupportView supportView;

	@Bean Dao dao;
	@Bean EventBus eventBus;

	@Inject FourPdaClient client;

	@AfterViews
	void afterViews() {
		((App) getActivity().getApplication()).component().inject(this);

		webView.getSettings().setJavaScriptEnabled(true);

		toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().onBackPressed();
			}
		});

		collapsingToolbar.setTitle(title);
		ViewUtils.loadImage(backdropImageView, image);

		loadData();
	}

	@Click
	void commentsButton() {
		eventBus.post(new ShowCommentsEvent(id));
	}

	private void loadData() {
		supportView.showProgress();
		getLoaderManager().restartLoader(LOADER_ID, null, new Callbacks());
	}

	@Override
	public void onRefresh() {
		loadData();
	}

	@UiThread
	void updateData(String content) {
		webView.setWebChromeClient(new WebChromeClient());
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				openActionViewIntent(url);
				return true;
			}
		});
		webView.loadData(getFormattedText(content), "text/html; charset=utf-8", null);
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

	class Callbacks implements LoaderManager.LoaderCallbacks<LoadResult<String>> {

		@Override
		public Loader onCreateLoader(int loaderId, final Bundle args) {
			return new ArticleTaskLoader(getActivity(), client, id, date);
		}

		@Override
		public void onLoadFinished(Loader<LoadResult<String>> loader, LoadResult<String> result) {
			if (result.getException() == null) {
				updateData(result.getData());
				supportView.hide();
				return;
			}

			supportView.showError(getString(R.string.article_network_error), new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					loadData();
				}
			});
		}

		@Override
		public void onLoaderReset(Loader<LoadResult<String>> loader) {
		}

	}

}
