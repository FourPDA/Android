package four.pda;

import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import four.pda.ui.article.list.ListActivity;
import four.pda.ui.article.list.ListActivity_;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Klishin.Pavel on 01.02.2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class BaseFragmentTest {

	private UiDevice device;
	private static final String TAG = "four.pda";

	@Rule
	public ActivityTestRule<ListActivity> mActivityRule = new ActivityTestRule(ListActivity_.class);


	@Before
	public void startMainActivityFromHomeScreen() throws RemoteException {
		// Initialize UiDevice instance
		device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
		device.wakeUp();

	}

	@Test
	public void mainDrawerActivityTest() throws UiObjectNotFoundException, InterruptedException {

		//Открываем Navigaton Drawer методами UiAutomator:
		UiObject openDrawerButton = device.findObject(new UiSelector().className("android.widget.ImageButton").packageName("four.pda.debug").instance(0));
		openDrawerButton.click();
		//Убеждаемся, что все пункты в наличии через Espresso:
		onView(withId(four.pda.R.id.all_category_view)).check(matches(isDisplayed())).check(matches(isClickable()));
		onView(withId(four.pda.R.id.news_category_view)).check(matches(isDisplayed())).check(matches(isClickable()));
		onView(withId(four.pda.R.id.articles_category_view)).check(matches(isDisplayed())).check(matches(isClickable()));
		onView(withId(four.pda.R.id.reviews_category_view)).check(matches(isDisplayed())).check(matches(isClickable()));
		onView(withId(four.pda.R.id.software_category_view)).check(matches(isDisplayed())).check(matches(isClickable()));
		onView(withId(four.pda.R.id.games_category_view)).check(matches(isDisplayed())).check(matches(isClickable()));
		onView(withId(four.pda.R.id.about_view)).check(matches(isDisplayed())).check(matches(isClickable()));
		Log.v(TAG, "Installed, all is OK");
	}
}
