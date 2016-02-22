package four.pda;

import android.content.Context;
import android.support.annotation.NonNull;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import org.androidannotations.annotations.EBean;

import okhttp3.OkHttpClient;

/**
 * Created by asavinova on 15/02/16.
 */
@EBean(scope = EBean.Scope.Singleton)
public class FourPdaClient extends four.pda.client.FourPdaClient {

	public FourPdaClient(Context context) {
		super(getOkHttpClient(context));
	}

	@NonNull
	private static OkHttpClient getOkHttpClient(Context context) {
		ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
		return new OkHttpClient.Builder()
				.cookieJar(cookieJar)
				.build();
	}

}
