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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import four.pda.ui.article.NewsActivity;
import four.pda.ui.article.NewsActivity_;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Klishin Pavel on 06.04.2016.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class TiltInterfaceTest {

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
	public void tiltDevInterfaceTest() throws RemoteException, UiObjectNotFoundException {
		device.waitForIdle();
		device.setOrientationRight();

		UiObject openDrawerButton = device.findObject(new UiSelector()
				.className("android.widget.ImageButton")
				.packageName(APP_ID)
				.instance(0));
		openDrawerButton.click();

		UiObject scrollView = device.findObject(new UiSelector()
				.className("android.widget.ScrollView"));
		scrollView.swipeUp(3);

		device.waitForIdle();
		scrollView.swipeDown(3);
		device.waitForIdle();

		device.setOrientationLeft();
		openDrawerButton.click();

		device.waitForIdle();
		scrollView.swipeDown(3);
		device.waitForIdle();

		device.setOrientationNatural();
		openDrawerButton.click();
		device.waitForIdle();
		scrollView.swipeDown(3);
		scrollView.swipeUp(3);

		device.waitForIdle();
		device.setOrientationLeft();
		openDrawerButton.click();
		device.setOrientationRight();
		device.setOrientationNatural();
		device.setOrientationLeft();
		device.setOrientationRight();
	}
}
