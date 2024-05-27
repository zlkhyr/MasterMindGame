package com.example.projectpbm.core;

import android.util.Log;

import java.util.HashMap;

public class Field {

	private final int rowCount;
	private final int colCount;
	private int currentRow;
	private Ball[][] balls;
	private ClueType[][] clue;
	private GameState state = GameState.PLAYING;
	private boolean duplicates;
	private long startTime;

	public Field(int rowCount, int colCount, boolean duplicates) {
		if (colCount > BallColor.values().length) {
			throw new IllegalArgumentException("Maximum of " + BallColor.values().length + " columns is allowed!");
		}

		this.rowCount = rowCount + 1;
		this.colCount = colCount;

		this.currentRow = rowCount;
		this.duplicates = duplicates;

		balls = new Ball[this.rowCount][this.colCount];
		clue = new ClueType[this.rowCount][this.colCount];

		if (duplicates) {
			generateRandomRow();
		} else {
			generateUniqueRow();
		}

		startTime = System.currentTimeMillis();
	}

	/**
	 * Generates sequence of balls with random colors on 0. row of playing field
	 */
	private void generateRandomRow() {
		for (int col = 0; col < colCount; col++) {
			balls[0][col] = new Ball(BallColor.getRandom());
			Log.d("=== DUPLICATES ===", "Col " + col + ": " + balls[0][col].getColor());
		}
	}

	/**
	 * Generates sequence of balls with unique colors on 0. row of playing field
	 */
	private void generateUniqueRow() {
		balls[0][0] = new Ball(BallColor.getRandom());
		Log.d("=== UNIQUE ===", "Col " + 0 + ": " + balls[0][0].getColor());
		for (int col = 1; col < colCount; col++) {
			do {
				balls[0][col] = new Ball(BallColor.getRandom());
			} while (!checkForUniqueColors(col));
			Log.d("=== UNIQUE ===", "Col " + col + ": " + balls[0][col].getColor());
		}
	}

	/**
	 * Compares color on @param col position to all previous colors in the same row
	 * Returns true if color is unique
	 */
	private boolean checkForUniqueColors(int col) {
		for (int i = 0; i < col; i++) {
			if (balls[0][col].getColor() == balls[0][i].getColor()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Changes game state based on current row
	 */
	public void updateGameState() {
		state = GameState.SOLVED;
		for (int col = 0; col < getColCount(); col++) {
			if (balls[0][col].getColor() != balls[currentRow][col].getColor()) {
				state = currentRow - 1 > 0 ? GameState.PLAYING : GameState.FAILED;
				break;
			}
		}
	}

	/**
	 * Generates a new clue and stores it on tho current row inside clue[][]
	 */
	public void generateClue() {
		int cluePosition = 0;
		for (int secretCol = 0; secretCol < getColCount(); secretCol++) {
			for (int guessCol = 0; guessCol < getColCount(); guessCol++) {
				if (balls[0][secretCol].getColor() == balls[currentRow][guessCol].getColor()) {
					clue[currentRow][cluePosition++] = ClueType.COLOR;
					break;
				}
			}
		}
		cluePosition = 0;
		for (int col = 0; col < getColCount(); col++) {
			if (balls[0][col].getColor() == balls[currentRow][col].getColor()) {
				clue[currentRow][cluePosition++] = ClueType.PLACE;
			}
		}
	}

	public void nextRow() {
		currentRow--;
	}

	public boolean isRowFilled() {
		for (int col = 0; col < getColCount(); col++) {
			if (balls[currentRow][col] == null) {
				return false;
			}
		}
		return true;
	}

	public void clearCurrentRow() {
		for (int col = 0; col < getColCount(); col++) {
			balls[getCurrentRow()][col] = null;
		}
	}


	public void setBall(int colCount, BallColor color) {
		balls[currentRow][colCount - 1] = new Ball(color);
	}

	public Ball getBall(int row, int col) {
		return balls[row][col];
	}

	public int getRowCount() {
		return rowCount;
	}

	public int getColCount() {
		return colCount;
	}

	public int getCurrentRow() {
		return currentRow;
	}

	public ClueType[][] getClue() {
		return clue;
	}

	public GameState getState() {
		return state;
	}

	public void restart() {
		state = GameState.PLAYING;
		currentRow = rowCount - 1;
		balls = new Ball[rowCount][colCount];
		clue = new ClueType[rowCount][colCount];
		if (duplicates) {
			generateRandomRow();
		} else {
			generateUniqueRow();
		}
		startTime = System.currentTimeMillis();
	}

	public HashMap getClueHashMap(ClueType[][] clue, int row) {
		int colors = 0;
		int places = 0;
		for (int col = 0; col < getColCount(); col++) {
			colors = clue[row][col] == ClueType.COLOR ? colors + 1 : colors;
			places = clue[row][col] == ClueType.PLACE ? places + 1 : places;
		}

		HashMap<String, Integer> clues = new HashMap<>();
		clues.put("colors", colors);
		clues.put("places", places);

		return clues;
	}
}
