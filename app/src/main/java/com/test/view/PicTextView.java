package com.test.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.test.mytest.R;

/**
 * Created by Administrator on 2018/7/17.
 */

public class PicTextView extends AppCompatTextView {
	String s = "dsajgkldsjaewtewq;tjefdsdasfsjg;vdsgjds;gldjdsajgkldsjaewtewq" +
			";tjefdsdasfsjg;vdsgjds;gldjdsajgkldsjaewtewq;tjefdsdasfsjg;vdsgjds;gldjdsajgklds" +
			"jaewtewq;tjefdsdasfsjg;vdsgjds;gldjdsajgkldsjaewtewq;tjefdsdasfsjg;vdsgjds;gldjdsajg" +
			"kldsjaewtewq;tjefdsdasfsjg;vdsgjds;gldjdsajgkldsjaewtewq;tjefdsdasfsjg;vdsgjds;gldjdsajgkl" +
			"dsjaewtewq;tjefdsdasfsjg;vdsgjds;gldjdsajgkldsjaewtewq;tjefdsdasfsjg;vdsgjds;gldjdsajgkldsjaewtewq;" +
			"tjefdsdasfsjg;vdsgjds;gldjdsajgkldsjaewtewq;tjefdsdasfsjg;vdsgjds;" +
			"gldjdsajgkldsjaewtewq;tjefdsdasfsjg;vdsgjds;gldj";
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

	public PicTextView(Context context) {
		super(context);
	}

	public PicTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PicTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mPaint.setTextSize(20);
		canvas.drawText(s, 0, 0, mPaint);
		canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_settings_style), 200, 200, mPaint);

	}
}
