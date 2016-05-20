package four.pda.dagger;

import android.content.Context;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import four.pda.client.FourPdaClient;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
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

		ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context)) {
			@Override
			public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
				List<Cookie> updatedCookies = new ArrayList<>();

				for (Cookie cookie : cookies) {

					if (cookie.persistent()) {
						updatedCookies.add(cookie);
					} else {

						Calendar calendar = new GregorianCalendar();
						calendar.setTime(new Date());
						calendar.add(Calendar.YEAR, 1);

						Cookie.Builder cookieBuilder = new Cookie.Builder()
								.path(cookie.path())
								.domain(cookie.domain())
								.expiresAt(calendar.getTimeInMillis())
								.name(cookie.name())
								.value(cookie.value());

						if (cookie.secure()) {
							cookieBuilder.secure();
						}

						if (cookie.httpOnly()) {
							cookieBuilder.httpOnly();
						}

						if (cookie.hostOnly()) {
							cookieBuilder.hostOnlyDomain(cookie.domain());
						}

						updatedCookies.add(cookieBuilder.build());
					}

				}

				super.saveFromResponse(url, updatedCookies);
			}

		};

		OkHttpClient httpClient = new OkHttpClient.Builder()
				.cookieJar(cookieJar)
				.build();

		return new FourPdaClient(httpClient);
	}

}
