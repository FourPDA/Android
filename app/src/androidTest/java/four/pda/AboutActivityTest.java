package four.pda;

import android.os.Environment;
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

import java.io.File;

import four.pda.ui.AboutActivity;
import four.pda.ui.AboutActivity_;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Klishin.Pavel on 08.02.2016.
 */
@SmallTest
@RunWith(AndroidJUnit4.class)
public class AboutActivityTest {

	private UiDevice device;
	final String packageName = BuildConfig.APPLICATION_ID;
	final String WORKING_DIR = Environment.getExternalStorageDirectory().getAbsolutePath();

	@Rule
	public ActivityTestRule<AboutActivity> activityTestRule = new ActivityTestRule(AboutActivity_.class);

	@Before
	public void startMainActivityFromHomeScreen() throws RemoteException {
		// Initialize UiDevice instance
		device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
		device.wakeUp();
	}

	@Test
	public void elementsPresented() throws UiObjectNotFoundException {

		device.waitForWindowUpdate(packageName, 100);
		if (packageName.matches("four.pda.debug")) {
			device.takeScreenshot(new File(WORKING_DIR + "/screenFour.png"));
		}

		//Проверяем наличие всех элементов
		onView(withId(R.id.description_text_view))
				.check(matches(isDisplayed()));

		onView(withId(R.id.version_text_view))
				.check(matches(isDisplayed()));

		onView(withId(R.id.build_number_text_view))
				.check(matches(isDisplayed()));

		onView(withId(R.id.build_type_text_view))
				.check(matches(isDisplayed()));

		//Свайпаем вверх, чтобы увидеть остальные элементы
		UiObject aboutActivityField = device.findObject(new UiSelector()
				.className("android.widget.LinearLayout")
				.packageName(packageName));
		aboutActivityField.swipeUp(2);
		onView(withText("swapii@gmail.com"))
				.check(matches(isDisplayed()))
				.check(matches(isClickable()));
		onView(withText("varann@gmail.com"))
				.check(matches(isDisplayed()))
				.check(matches(isClickable()));
		onView(withId(R.id.swapi_4pda))
				.check(matches(isDisplayed()))
				.check(matches(isClickable()));
		onView(withId(R.id.varann_4pda))
				.check(matches(isDisplayed()))
				.check(matches(isClickable()));
	}

}
