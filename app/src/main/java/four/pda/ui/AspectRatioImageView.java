package four.pda.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import four.pda.R;


/**
 * @author Pavel Savinov
 * @version 25/12/13 15:50
 */
public class AspectRatioImageView extends ImageView {

	private static final Logger L = LoggerFactory.getLogger(AspectRatioImageView.class.getSimpleName());

	private float aspectRatio = 1;

	public AspectRatioImageView(Context context) {
		super(context);
	}

	public AspectRatioImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	public AspectRatioImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(widthMeasureSpec, (int) (getMeasuredWidth() * aspectRatio));
	}

	private void init(AttributeSet attrs) {
		TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.AspectRatio);
		aspectRatio = typedArray.getFloat(R.styleable.AspectRatio_aspectRatio, 1);
		typedArray.recycle();
	}

	public void setAspectRatio(float aspectRatio) {
		this.aspectRatio = aspectRatio;
		requestLayout();
	}
}
