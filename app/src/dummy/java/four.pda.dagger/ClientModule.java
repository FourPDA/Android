package four.pda.dagger;

import android.content.Context;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.CookiePersistor;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import dagger.Module;
import dagger.Provides;
import four.pda.client.FourPdaClient;
import okhttp3.OkHttpClient;

/**
 * Created by asavinova on 23/02/16.
 */
@Module
public class ClientModule extends BaseModule {

	public ClientModule(Context context) {
		super(context);
	}

	@Provides
	public CookiePersistor cookiePersistor() {
		return new SharedPrefsCookiePersistor(context);
	}

	@Provides
	public FourPdaClient client() {
		ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), cookiePersistor());
		OkHttpClient httpClient = new OkHttpClient.Builder()
				.cookieJar(cookieJar)
				.build();
		return new DummyFourPdaClient(httpClient);
	}

}
