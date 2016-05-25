package four.pda.dagger;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import four.pda.ui.Keyboard;

/**
 * Created by pavel on 21/05/16.
 */
@Module
public abstract class BaseModule {

	protected Context context;

	public BaseModule(Context context) {
		this.context = context;
	}

	@Provides
	public Keyboard keyboard() {
		return new Keyboard(context);
	}

}
