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

import four.pda.ui.article.NewsActivity;
import four.pda.ui.article.NewsActivity_;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Klishin.Pavel on 08.02.2016.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class AllArticlesTest {

	private UiDevice device;
	final String packageName = BuildConfig.APPLICATION_ID;
	final String WORKING_DIR = Environment.getExternalStorageDirectory().getAbsolutePath();

	@Rule
	public ActivityTestRule<NewsActivity> activityTestRule = new ActivityTestRule(NewsActivity_.class);

	@Before
	public void startMainActivityFromHomeScreen() throws RemoteException {
		// Initialize UiDevice instance
		device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
		device.wakeUp();
	}

	@Test
	public void allArticlesActivityTest() throws UiObjectNotFoundException, InterruptedException {
		//Открываем Navigaton Drawer методами UiAutomator:
		UiObject openDrawerButton = device.findObject(new UiSelector()
				.className("android.widget.ImageButton")
				.packageName(packageName)
				.instance(0));
		openDrawerButton.click();
		device.waitForWindowUpdate(packageName, 100);
		//Открываем категорию "Новости"
		onView(withId(R.id.all_category_view))
				.perform(click());
		device.takeScreenshot(new File(WORKING_DIR + "/screenTwo.png"));
		//Открываем первую статью из списка
		UiObject openfirstButton = device.findObject(new UiSelector()
				.className("android.widget.ImageView").packageName(packageName)
				.resourceId(packageName + ":id/image_view")
				.instance(0));
		openfirstButton.click();
		//Ждем пока окошко загрузиться
		device.waitForWindowUpdate(packageName, 100);
		//Убеждаемся что кнопка "комментарии" есть на экране
		onView(withId(R.id.comments_button))
				.check(matches(isDisplayed()))
				.check(matches(isClickable()));
		//Свайпаем туда-сюда
		onView(withId(R.id.drawer_layout))
				.perform(swipeUp())
				.perform(swipeUp())
				.perform(swipeUp())
				.perform(swipeUp());
		pressBack();
		onView(withId(R.id.drawer_layout))
				.perform(swipeUp())
				.perform(swipeUp())
				.perform(swipeUp())
				.perform(swipeUp());
		device.waitForIdle();
		//Нажимаем на Floating Button для возврата в начало списка
		onView(withId(R.id.up_button)).perform(click());
		onView(withId(R.id.drawer_layout))
				.perform(swipeDown())
				.perform(swipeDown());
		device.waitForWindowUpdate(packageName, 100);
		device.waitForIdle(150);
		//Открываем вторую статью
		UiObject openSecondButton = device.findObject(new UiSelector()
				.className("android.widget.ImageView").packageName(packageName)
				.resourceId(packageName + ":id/image_view")
				.instance(1));
		openSecondButton.click();
		//Ждем пока окошко загрузиться
		device.waitForWindowUpdate(packageName, 100);
		//Убеждаемся что кнопка "комментарии" есть на экране
		onView(withId(R.id.comments_button))
				.check(matches(isDisplayed()))
				.check(matches(isClickable()));
		//Свайпаем туда-сюда
		onView(withId(R.id.drawer_layout))
				.perform(swipeUp())
				.perform(swipeUp())
				.perform(swipeUp());
		//Возвращаемся назад
		pressBack();

	}

}
