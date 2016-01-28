package four.pda.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rey.material.widget.ProgressView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import four.pda.R;

/**
 * Created by asavinova on 06/05/15.
 */
@EViewGroup(R.layout.support_view)
public class SupportView extends FrameLayout {

	@ViewById ProgressView progressView;
	@ViewById LinearLayout errorPanel;
	@ViewById TextView errorMessage;
	@ViewById TextView retryView;

	public SupportView(Context context) {
		super(context);
	}

	public SupportView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SupportView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@UiThread
	public void showProgress() {
		errorPanel.setVisibility(GONE);
		progressView.setVisibility(VISIBLE);
		setVisibility(VISIBLE);
	}

	@UiThread
	public void hide() {
		setVisibility(GONE);
	}

	@UiThread
	public void showError(String message, OnClickListener onClickListener) {
		progressView.setVisibility(GONE);
		errorMessage.setText(message);
		retryView.setOnClickListener(onClickListener);
		errorPanel.setVisibility(VISIBLE);
		setVisibility(VISIBLE);
	}
}
