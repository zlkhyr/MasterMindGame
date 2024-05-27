package com.example.projectpbm.Views;

import static android.graphics.drawable.GradientDrawable.Orientation.BOTTOM_TOP;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.projectpbm.R;


@SuppressLint("AppCompatCustomView")
public abstract class FieldButton extends Button {

	public FieldButton(Context context) {
		super(context);
	}

	public FieldButton(Context context, int colorLight, int colorDark) {
		super(context);

		setGradientColor(colorLight, colorDark);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

		setElevation(3f);
		setTextColor(getResources().getColor(R.color.darkBackgroundText));

		final int TILE_SIZE = 120;
		ViewGroup.LayoutParams params = getLayoutParams();
		params.width = TILE_SIZE;
		params.height = TILE_SIZE;
		setLayoutParams(params);
	}

	public void setGradientColor(int colorLight, int colorDark) {
		int[] gradientColors = {
				colorLight,
				colorDark
		};

		GradientDrawable gradient = new GradientDrawable(BOTTOM_TOP, gradientColors);
		gradient.setCornerRadius(999f);
		setBackground(gradient);
	}
}
