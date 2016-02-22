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

import four.pda.R;
import four.pda.ui.article.list.ListActivity;
import four.pda.ui.article.list.ListActivity_;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Klishin.Pavel on 08.02.2016.
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class GamesActivityTest {

	private UiDevice mDevice;
	private static final String TAG = "four.pda";

	@Rule
	public ActivityTestRule<ListActivity> mActivityRule = new ActivityTestRule(ListActivity_.class);


	@Before
	public void startMainActivityFromHomeScreen() throws RemoteException {
		// Initialize UiDevice instance
		mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
		mDevice.wakeUp();
	}

	@Test
	public void GamesActivityTest () throws UiObjectNotFoundException, InterruptedException {

		UiObject openDrawerButton = mDevice.findObject(new UiSelector().className("android.widget.ImageButton").packageName("four.pda.debug").instance(0));
		openDrawerButton.click();

		onView(withId(R.id.games_category_view)).perform(click());

		UiObject openfirstButton = mDevice.findObject(new UiSelector().className("android.widget.ImageView").packageName("four.pda.debug")
				.resourceId("four.pda.debug:id/image_view")
				.instance(0));
		openfirstButton.click();

		mDevice.waitForWindowUpdate("four.pda", 100);

		onView(withId(four.pda.R.id.drawer_layout)).perform(swipeUp()).perform(swipeUp()).perform(swipeUp()).perform(swipeUp());
		pressBack();
		onView(withId(four.pda.R.id.drawer_layout)).perform(swipeUp()).perform(swipeUp()).perform(swipeUp()).perform(swipeUp());
		mDevice.waitForIdle();

		onView(withId(four.pda.R.id.up_button)).perform(click());
		onView(withId(four.pda.R.id.drawer_layout)).perform(swipeDown()).perform(swipeDown());
		mDevice.waitForWindowUpdate("four.pda", 100);
		mDevice.waitForIdle(150);

		UiObject openSecondButton = mDevice.findObject(new UiSelector().className("android.widget.ImageView").packageName("four.pda.debug")
				.resourceId("four.pda.debug:id/image_view")
				.instance(1));
		openSecondButton.click();

		mDevice.waitForWindowUpdate("four.pda", 100);

		onView(withId(four.pda.R.id.drawer_layout)).perform(swipeUp()).perform(swipeUp()).perform(swipeUp());

		pressBack();

	}

}
