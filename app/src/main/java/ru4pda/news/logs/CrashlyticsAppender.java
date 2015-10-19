package ru4pda.news.logs;

import com.crashlytics.android.Crashlytics;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

/**
 * Created by asavinova on 19/10/15.
 */
public class CrashlyticsAppender extends AppenderBase<ILoggingEvent> {

	@Override
	protected void append(ILoggingEvent iLoggingEvent) {

		int priority;

		Level level = iLoggingEvent.getLevel();

		if (level.isGreaterOrEqual(Level.ERROR)) {
			priority = 6;
		} else if (level.isGreaterOrEqual(Level.WARN)) {
			priority = 5;
		} else if (level.isGreaterOrEqual(Level.INFO)) {
			priority = 4;
		} else if (level.isGreaterOrEqual(Level.DEBUG)) {
			priority = 3;
		} else if (level.isGreaterOrEqual(Level.TRACE)) {
			priority = 2;
		} else {
			priority = 1;
		}

		String message = iLoggingEvent.getFormattedMessage();
		Crashlytics.log(priority, iLoggingEvent.getLoggerName(), message);
	}

}
