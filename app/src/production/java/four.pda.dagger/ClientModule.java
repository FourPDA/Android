package four.pda.dagger;

import android.content.Context;

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
		OkHttpClient httpClient = new OkHttpClient.Builder()
				.cookieJar(new ForcePersistCookieJar(cookiePersistor()))
				.build();
		return new FourPdaClient(httpClient);
	}

}
