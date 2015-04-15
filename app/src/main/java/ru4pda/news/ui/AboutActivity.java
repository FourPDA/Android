package ru4pda.news.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.MailTo;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import ru4pda.news.R;

/**
 * Created by asavinova on 15/04/15.
 */
@EActivity(R.layout.activity_about)
public class AboutActivity extends ActionBarActivity {

	@ViewById Toolbar toolbar;
	@ViewById WebView webView;

	@AfterViews
	void afterViews() {
		toolbar.setTitle(R.string.about);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		WebViewClient webClient = new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.startsWith("mailto:")) {
					MailTo mailTo = MailTo.parse(url);
					mailTo("mailto:" + mailTo.getTo(), mailTo.getSubject());
					view.reload();
					return true;
				} else {
					openLink(url);
				}
				return true;
			}
		};
		webView.setWebViewClient(webClient);

		webView.loadData(getHtml(), "text/html; charset=utf-8", null);
	}

	private void mailTo(String mailto, String subject) {
		Intent intent = new Intent(Intent.ACTION_SENDTO);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.setData(Uri.parse(mailto));

		Intent chooser = Intent.createChooser(intent, getString(R.string.chooser_title));
		chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			startActivity(chooser);
		} catch (ActivityNotFoundException ex) {
			Toast.makeText(this, R.string.no_email_applications_installed, Toast.LENGTH_SHORT).show();
		}
	}

	private void openLink(String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));

		Intent chooser = Intent.createChooser(intent, getString(R.string.chooser_title));
		chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			startActivity(chooser);
		} catch (ActivityNotFoundException ex) {
			Toast.makeText(this, R.string.no_browsers_installed, Toast.LENGTH_SHORT).show();
		}
	}

	private String getHtml() {
		return "<div style=\"margin-left: auto; margin-right: auto;\">"
				+"<p>Клиент для сайта <a href=\"http://4pda.ru\">http://4pda.ru</a></p>"
				+ "<p>Разработчики:</p>"
				+ "<ul>"
				+ "<li>Павел Савинов &lt;<a href=\"mailto:swapii@gmail.com\">swapii@gmail.com</a>&gt;</li>"
				+ "<li>Анна Савинова &lt;<a href=\"mailto:varannn@gmail.com\">varannn@gmail.com</a>&gt;</li>"
				+ "</ul></div>";
	}

}
