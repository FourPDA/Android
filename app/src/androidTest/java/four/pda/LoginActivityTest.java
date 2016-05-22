package four.pda;

import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import four.pda.ui.article.NewsActivity;
import four.pda.ui.article.NewsActivity_;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;


/**
 * Created by Seva Powerman on 27.02.2016.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class LoginActivityTest {

	private UiDevice device;
	static final String APP_ID = BuildConfig.APPLICATION_ID;

	@Rule
	public ActivityTestRule<NewsActivity> activityTestRule = new ActivityTestRule(NewsActivity_.class);

	@Before
	public void startMainActivityFromHomeScreen() throws RemoteException {
		// Initialize UiDevice instance
		device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
		device.wakeUp();
	}

	@Test
	public void loginActivityTest() throws UiObjectNotFoundException, InterruptedException {

		UiObject openDrawerButton = device.findObject(new UiSelector()
				.className("android.widget.ImageButton")
				.packageName(APP_ID)
				.instance(0));
		openDrawerButton
				.click();
		device.waitForWindowUpdate(APP_ID, 100);

		onView(withId(R.id.login_view)).perform(click());
		device.waitForIdle();

		closeSoftKeyboard();
		device.waitForIdle();

		UiObject loginField = device.findObject(new UiSelector()
				.className("android.widget.EditText")
				.resourceId(APP_ID + ":id/login_view"));
		Assert.assertTrue(loginField.exists());

		UiObject passwordField = device.findObject(new UiSelector()
				.className("android.widget.EditText")
				.resourceId(APP_ID + ":id/password_view"));
		Assert.assertTrue(passwordField.exists());

		UiObject capchaPic = device.findObject(new UiSelector()
				.className("android.widget.ImageView")
				.resourceId(APP_ID + ":id/captcha_image_view"));
		Assert.assertTrue(capchaPic.exists());

		UiObject capchaText = device.findObject(new UiSelector()
				.className("android.widget.EditText")
				.resourceId(APP_ID + ":id/captcha_text_view"));
		Assert.assertTrue(capchaText.exists());

		UiObject capchaEnterButton = device.findObject(new UiSelector()
				.className("android.widget.Button")
				.resourceId(APP_ID + ":id/enter_view"));
		Assert.assertTrue(capchaEnterButton.exists());
	}
}
