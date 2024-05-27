package com.example.projectpbm.Views;

import android.content.Context;

import com.example.projectpbm.R;
import com.example.projectpbm.core.ClueType;

public class ClueButton extends FieldButton {

	public ClueButton(Context context) {
		super(context);
	}

	public ClueButton(Context context, ClueType type, Object guessed) {
		super(context);

		super.setGradientColor(
				getResources().getColor(R.color.colorPrimary),
				getResources().getColor(R.color.colorPrimaryDark)
		);

		int stringId = 0;

		switch (type) {
			case COLOR:
				stringId = R.string.colors_clue_format;
				break;
			case PLACE:
				stringId = R.string.places_clue_format;
				break;
		}

		setText(getResources().getString(stringId, guessed));
	}
}
