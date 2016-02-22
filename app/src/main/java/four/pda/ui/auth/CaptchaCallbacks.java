package four.pda.ui.auth;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.os.Bundle;
import android.view.View;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import four.pda.R;
import four.pda.client.model.Captcha;
import four.pda.ui.LoadResult;
import four.pda.ui.ViewUtils;

/**
 * Created by asavinova on 21/02/16.
 */
class CaptchaCallbacks implements LoaderManager.LoaderCallbacks<LoadResult<Captcha>> {

	private static final Logger L = LoggerFactory.getLogger(CaptchaCallbacks.class);

	private AuthActivity activity;

	public CaptchaCallbacks(AuthActivity activity) {
		this.activity = activity;
	}

	@Override
	public Loader<LoadResult<Captcha>> onCreateLoader(int id, Bundle args) {
		return new AsyncTaskLoader<LoadResult<Captcha>>(activity) {
			@Override
			public LoadResult<Captcha> loadInBackground() {
				try {
					return new LoadResult<>(activity.client.getInstance().getCaptcha());
				} catch (IOException e) {
					L.error("Captcha request error", e);
					return new LoadResult<>(e);
				}
			}
		};
	}

	@Override
	public void onLoadFinished(Loader<LoadResult<Captcha>> loader, LoadResult<Captcha> result) {

		if (result.isError()) {
			activity.supportView.showError(activity.getString(R.string.auth_network_error), new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					activity.loadCaptcha();
				}
			});
			return;
		}

		activity.captcha = result.getData();
		ViewUtils.loadImage(activity.captchaImageView, activity.captcha.getUrl());
		activity.supportView.hide();
	}

	@Override
	public void onLoaderReset(Loader<LoadResult<Captcha>> loader) {
	}

}
