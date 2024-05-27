package com.example.projectpbm;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.example.projectpbm.core.*;
import com.example.projectpbm.core.BallColor;
import com.example.projectpbm.core.ClueType;
import com.example.projectpbm.core.Field;
import com.example.projectpbm.Views.*;
import com.example.projectpbm.core.GameState;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private Field field;
    private int rows;
    private int columns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnNewGame = findViewById(R.id.buttonNewGame);
        btnNewGame.setOnClickListener(view -> {
            context = getApplicationContext();
            newGame(8,4, false);
        });
    }
    @SuppressLint("ClickableViewAccessibility")
    private void populateField() {
        TableLayout table = findViewById(R.id.gameTable);
        table.removeAllViews();

        for (int row = 0; row < rows; row++) {
            TableRow ballRow = new TableRow(context);
            table.addView(ballRow);
            if (field.getCurrentRow() == row) {
                ballRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkRow();
                    }
                });
            }

            HashMap clueMap = field.getClueHashMap(field.getClue(), row);

            // Create place clue button
            ClueButton placesClueButton = new ClueButton(context, ClueType.PLACE, clueMap.get("places"));
            ballRow.addView(placesClueButton);

            // Create tile buttons
            for (int column = 0; column < columns; column++) {

                Ball ball = field.getBall(row, column);

                // Free tile colors
                int ballColorLight = getResources().getColor(R.color.freeTileColor);
                int ballColorDark = getResources().getColor(R.color.freeTileColor);

                if (row == 0) {
                    // Hidden tile colors
                    if (field.getState() == GameState.PLAYING) {
                        ballColorLight = getResources().getColor(R.color.colorPrimary);
                        ballColorDark = getResources().getColor(R.color.colorPrimaryDark);
                    } else {
                        ballColorLight = Color.parseColor(ball.getColor().getColorLight());
                        ballColorDark = Color.parseColor(ball.getColor().getColorDark());
                    }
                } else if (ball != null) {
                    // Filled tile colors
                    ballColorLight = Color.parseColor(ball.getColor().getColorLight());
                    ballColorDark = Color.parseColor(ball.getColor().getColorDark());
                }

                BallButton ballButton = new BallButton(context, ballColorLight, ballColorDark);
                ballRow.addView(ballButton);

                if (row == 0) {
                    ballButton.setTextColor(getResources().getColor(R.color.darkBackgroundText));
                    ballButton.setText("?");
                }

                // On button click function
                final int ROW_POSITION = row;
                final int COLUMN_POSITION = column;
                if (field.getCurrentRow() == row) {
                    ballButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (field.getState() == GameState.PLAYING) {
                                Ball ball = field.getBall(ROW_POSITION, COLUMN_POSITION);
                                BallColor nextColor = BallColor.getNext(ball);
                                field.setBall(COLUMN_POSITION + 1, nextColor);
                            }
                            populateField();
                        }
                    });
                }
            }

            // Create color clue button
            ClueButton colorsClueButton = new ClueButton(context, ClueType.COLOR, clueMap.get("colors"));
            ballRow.addView(colorsClueButton);

            if (row <= field.getCurrentRow()) {
                placesClueButton.setVisibility(View.INVISIBLE);
                colorsClueButton.setVisibility(View.INVISIBLE);
            }
        }
    }
    public void checkRow() {
        switch (field.getState()) {
            case PLAYING:
                if (field.isRowFilled()) {
                    field.updateGameState();
                    field.generateClue();
                    HashMap clueMap = field.getClueHashMap(field.getClue(), field.getCurrentRow());
                    Toast.makeText(context, clueMap.get("places") + " posisis dan warna, " +
                            clueMap.get("colors") + " warna", Toast.LENGTH_SHORT).show();
                    field.nextRow();
                } else {
                    Toast.makeText(context, R.string.incomplete_row, Toast.LENGTH_SHORT).show();
                }
                break;
            case SOLVED:
                Toast.makeText(context, R.string.win_msg, Toast.LENGTH_SHORT).show();
                break;
            case FAILED:
                Toast.makeText(context, R.string.lost_msg, Toast.LENGTH_SHORT).show();
                break;
        }
        populateField();
    }
    public void newGame(int rows, int columns, boolean duplicates) {
        this.rows = rows;
        this.columns = columns;
        field = new Field(this.rows++, this.columns, duplicates);
        populateField();
    }
}