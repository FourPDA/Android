package four.pda;

import android.content.Context;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import okhttp3.OkHttpClient;

/**
 * Created by asavinova on 15/02/16.
 */
@EBean(scope = EBean.Scope.Singleton)
public class FourPdaClient {

	@RootContext Context context;

	four.pda.client.FourPdaClient client;

	public four.pda.client.FourPdaClient getInstance() {
		if (client == null) {
			init();
		}
		return client;
	}

	private void init() {
		ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
		OkHttpClient okHttpClient = new OkHttpClient.Builder()
				.cookieJar(cookieJar)
				.build();
		client = new four.pda.client.FourPdaClient(okHttpClient);
	}
}
