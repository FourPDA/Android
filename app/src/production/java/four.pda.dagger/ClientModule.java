package four.pda.dagger;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import four.pda.client.FourPdaClient;
import okhttp3.OkHttpClient;

/**
 * Created by asavinova on 23/02/16.
 */
@Module
public class ClientModule {

	private Context context;

	public ClientModule(Context context) {
		this.context = context;
	}

	@Provides
	public FourPdaClient client() {
		OkHttpClient httpClient = new OkHttpClient.Builder()
				.cookieJar(new ForcePersistCookieJar(context))
				.build();
		return new FourPdaClient(httpClient);
	}

}
