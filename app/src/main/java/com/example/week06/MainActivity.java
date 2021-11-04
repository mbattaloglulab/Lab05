package com.example.week06;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    class CellListener implements View.OnClickListener {

        int row;
        int col;
        public CellListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void onClick(View view) {
            if (isCellOccupied(this.row,this.col)){
                Toast.makeText(MainActivity.this, "Cell is already occupied",Toast.LENGTH_SHORT).show();
                return;
            }
            if (player1Turn){
                board[this.row][this.col] = 1;
                ((Button)view).setText(PLAYER_1);
                moveCount++;
            }
            else{
                board[this.row][this.col] = 2;
                ((Button)view).setText(PLAYER_2);
                moveCount++;
            }
            int gameState = gameEnded(row,col);
            switch (gameState){
                case -1:
                    player1Turn = !player1Turn;
                    break;
                case 0:
                    Toast.makeText(MainActivity.this,"Draw",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(MainActivity.this,"Player 1 wins",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(MainActivity.this,"Player 2 wins",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }

    static final String PLAYER_1 = "X";
    static final String PLAYER_2 = "O";

    int moveCount = 0;

    boolean player1Turn = true;

    // 0 Empty space
    // 1 Player One
    // 2 Player Two
    byte[][] board = new byte[3][3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TableLayout tableLayout = findViewById(R.id.board);

        for (int i = 0; i < 3; i++) {
            TableRow row = (TableRow)tableLayout.getChildAt(i);
            for (int j = 0; j < 3; j++) {
                Button btn = (Button)row.getChildAt(j);
                btn.setOnClickListener(new CellListener(i,j));
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean("player1Turn",player1Turn);
        outState.putInt("moveCount",moveCount);
        byte[] boardOneDimension = new byte[9];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boardOneDimension[3*i+j] = board[i][j];
            }
        }
        outState.putByteArray("board",boardOneDimension);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        player1Turn = savedInstanceState.getBoolean("player1Turn");
        moveCount = savedInstanceState.getInt("moveCount");
        byte[] boardOneDimension = savedInstanceState.getByteArray("board");
        for (int i = 0; i < 9; i++) {
            board[i/3][i%3] = boardOneDimension[i];
        }

        TableLayout tableLayout = findViewById(R.id.board);

        for (int i = 0; i < 3; i++) {
            TableRow row = (TableRow)tableLayout.getChildAt(i);
            for (int j = 0; j < 3; j++) {
                Button btn = (Button)row.getChildAt(j);
                if(board[i][j] == 1) btn.setText(PLAYER_1);
                else if(board[i][j] == 2) btn.setText(PLAYER_2);
            }
        }
    }

    boolean isCellOccupied(int row, int col){
        return board[row][col] != 0;
    }

    int gameEnded(int row, int col){
        int player = board[row][col];
        boolean win = true;

        //Col check
        for (int i = 0; i < 3; i++) {
            if (board[i][col] != player){
                win = false;
                break;
            }
        }
        if (win) return player;

        //Row check
        win = true;
        for (int i = 0; i < 3; i++) {
            if (board[row][i] != player){
                win = false;
                break;
            }
        }
        if (win) return player;
        else if (moveCount == 9) return 0;
        else return -1;
    }
}