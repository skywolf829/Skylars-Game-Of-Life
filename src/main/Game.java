package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.JPanel;

public class Game extends JPanel{
	
	private int rows, columns;
	private int cellWidth, cellHeight;
	private int[][] cellX, cellY;
	private int[][] boardState;
	private int[][] updatedBoard;
	private boolean gameOver;
	private Stack<int[][]> s;
	
	public Game(){
		this.setLayout(null);
		rows = 100;
		columns = 100;
		
		s = new Stack<int[][]>();
		boardState = new int[rows][columns];
		updatedBoard = new int[rows][columns];
	}
	public Game(int x){
		this.setLayout(null);
		rows = x;
		columns = x;
		
		s = new Stack<int[][]>();
		boardState = new int[rows][columns];
		updatedBoard = new int[rows][columns];
		cellX = new int[rows][columns];
		cellY = new int[rows][columns];
	}
	public Game(int c, int r){
		this.setLayout(null);
		rows = r;
		columns = c;
		
		s = new Stack<int[][]>();
		boardState = new int[rows][columns];
		updatedBoard = new int[rows][columns];
		cellX = new int[rows][columns];
		cellY = new int[rows][columns];
		refresh();
	}
	public void refresh(){
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < columns; j++){
				boardState[i][j] = 0;
				cellX[i][j] = j * cellWidth;
				cellY[i][j] = i * cellHeight;
			}			
		}
	}
	public int getCellWidth(){
		return cellWidth;
	}
	public int getCellHeight(){
		return cellHeight;
	}
	public void setCellWidth(int x){
		cellWidth = x;
	}
	public void setCellHeight(int y){
		cellHeight = y;
	}
	public int getRows(){
		return rows;
	}
	public int getColumns(){
		return columns;
	}
	public void SetStart(ArrayList<String> lines){

		setRows(lines.size());
		setColumns(lines.size());
		refresh();
		for(int i = 0; i < lines.size(); i++){
			String line = lines.get(i);
			for(int j = 0; j < lines.size(); j++){
				char c = line.charAt(j);
				boardState[i][j] = Character.getNumericValue(c);
			}	
		}	
	}
	public void setRows(int r){
		rows = r;

		s = new Stack<int[][]>();
		int[][] newBoard = new int[rows][columns];
		cellX = new int[rows][columns];
		cellY = new int[rows][columns];
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < columns; j++){
				if(i < boardState.length && j < boardState[i].length){
					newBoard[i][j] = boardState[i][j];
				}
				else{
					newBoard[i][j] = 0;
				}
			}
		}
		
		
		boardState = newBoard;
		updatedBoard = new int[rows][columns];
	}
	public void setColumns(int c){
		columns = c;
		
		s = new Stack<int[][]>();
		int[][] newBoard = new int[rows][columns];
		cellX = new int[rows][columns];
		cellY = new int[rows][columns];
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < columns; j++){
				if(i < boardState.length && j < boardState[i].length){
					newBoard[i][j] = boardState[i][j];
				}
				else{
					newBoard[i][j] = 0;
				}
			}
		}
		boardState = newBoard;
		updatedBoard = new int[rows][columns];
	}
	public void reposition(){
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < columns; j++){
				cellX[i][j] = j * cellWidth;
				cellY[i][j] = i * cellHeight;
			}			
		}
	}
	public void switchState(int r, int c){
		s.push(boardState);
		gameOver = false;
		if(boardState[r][c] == 0){
			boardState[r][c] = 1;
		}
		else{
			boardState[r][c] = 0;
		}	
	}
	public Dimension getPreferredSize() {
		   return new Dimension(cellWidth * columns, cellHeight * rows);
	}
	public void printBoard(){
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < columns; j++){
				if(boardState[i][j] == 1){
					System.out.print("1");
				}
				else{
					System.out.print("0");
				}
			}
			System.out.println();
		}
	}
	public void paint(){
		repaint();
	}
	public String returnBoardString(){
		String b = "   ";
		for(int i = 0; i < columns; i++){
			if(i % 10 == 0){
				b += i / 10;
			}
			else{
				b += " ";
			}
		}
		b += "\n   ";
		for(int i = 0; i < columns; i++){
			b += i % 10;
		}
		b+= "\n";				
		for(int i = 0; i < rows; i++){
			if(i % 10 == 0){
				b += i / 10;
			}
			else{
				b += " ";
			}
		
			b += i % 10 + " ";
			for(int j = 0; j < columns; j++){
				if(boardState[i][j] == 1){
					b += "X";
				}
				else{
					b += "-";
				}
			}
			b += "\n";
		}
		return b;
	}
	public int[][] returnBoard(){
		return boardState;
	}
	public void changeBoard(int i, int j, int on){
		boardState[i][j] = on;
	}
	public void switchCell(int i, int j){
		if(boardState[i][j] == 0){
			boardState[i][j] = 1;
		}
		else{
			boardState[i][j] = 0;
		}
		gameOver = false;
	}
	public void spawn(int i, int j){
		boardState[i][j] = 1;
		gameOver = false;
	}
	public void kill(int i, int j){
		boardState[i][j] = 0;
		gameOver = false;
	}
	public boolean gameOver(){
		return gameOver;
	}
	private int numberOfNeighbors2(int i, int j){
		int num = 0;
		
		/*
		 * Top left corner needs to only check the boxes to the right,
		 * bottom right, and bottom
		 */
		if(i == 0 && j == 0){
			num = boardState[i + 1][j] + boardState[i + 1][j + 1]
					+ boardState[i][j + 1];
		}
		/*
		 * Top right corner needs to only check the boxes to the left, 
		 * bottom left, and bottom
		 */
		else if(i == rows - 1 && j == 0){
			num = boardState[i - 1][j] + boardState[i - 1][j + 1]
					+ boardState[i][j + 1];
		}
		/*
		 * Bottom right corner needs to only check the boxes on the top,
		 * top left, and left
		 */
		else if(i == rows - 1 && j == columns - 1){
			num = boardState[i - 1][j] + boardState[i - 1][j - 1]
					+ boardState[i][j - 1];
		}
		/*
		 * Bottom left corner only needs to check the boxes on top,
		 * top right, and right
		 */
		else if(i == 0 && j == columns - 1){
			num = boardState[i + 1][j] + boardState[i + 1][j - 1]
					+ boardState[i][j - 1];
		}
		/*
		 * Top needs to check three on bottom, left, and right side
		 */
		else if(j == 0){
			num = boardState[i - 1][j] + boardState[i + 1][j] +
					boardState[i - 1][j + 1] + boardState[i][j + 1] +
					boardState[i + 1][j + 1];
		}
		/*
		 * Left wall needs to check the top, bottom, and three on right
		 */
		else if(i == 0){
			num = boardState[i][j - 1] + boardState[i][j + 1] +
					boardState[i + 1][j - 1] + boardState[i + 1][j] +
					boardState[i + 1][j + 1];
		}
		/*
		 * Right wall needs to check the top, bottom, and three on left
		 */
		else if(i == rows - 1){
			num = boardState[i][j - 1] + boardState[i][j + 1] +
					boardState[i - 1][j - 1] + boardState[i - 1][j] +
					boardState[i - 1][j + 1];
		}
		/*
		 * Bottom needs to check the left, right, and three on top
		 */
		else if(j == columns - 1){
			num = boardState[i - 1][j] + boardState[i + 1][j] +
					boardState[i - 1][j - 1] + boardState[i][j - 1] +
					boardState[i + 1][j - 1];
		}
		/*
		 * Other spots need to check all surroundings
		 */
		else{
			num = boardState[i - 1][j - 1] + boardState[i][j - 1] +
					boardState[i + 1][j - 1] + boardState[i - 1][j] + 
					boardState[i + 1][j] + boardState[i - 1][j + 1] + 
					boardState[i][j + 1] + boardState[i + 1][j + 1];
		}
		return num;
	}
	private int numberOfNeighbors(int i, int j){
		int num = 0;
		
		/*
		 * Top left corner needs to only check the boxes to the right,
		 * bottom right, and bottom
		 */
		if(i == 0 && j == 0){
			num = boardState[i + 1][j] 
					+ boardState[i][j + 1];
		}
		/*
		 * Top right corner needs to only check the boxes to the left, 
		 * bottom left, and bottom
		 */
		else if(i == rows - 1 && j == 0){
			num = boardState[i - 1][j] 
					+ boardState[i][j + 1];
		}
		/*
		 * Bottom right corner needs to only check the boxes on the top,
		 * top left, and left
		 */
		else if(i == rows - 1 && j == columns - 1){
			num = boardState[i - 1][j] 
					+ boardState[i][j - 1];
		}
		/*
		 * Bottom left corner only needs to check the boxes on top,
		 * top right, and right
		 */
		else if(i == 0 && j == columns - 1){
			num = boardState[i + 1][j] 
					+ boardState[i][j - 1];
		}
		/*
		 * Top needs to check three on bottom, left, and right side
		 */
		else if(j == 0){
			num = boardState[i - 1][j] + boardState[i + 1][j] +
					 boardState[i][j + 1];
		}
		/*
		 * Left wall needs to check the top, bottom, and three on right
		 */
		else if(i == 0){
			num = boardState[i][j - 1] + boardState[i][j + 1] +
					boardState[i + 1][j];
		}
		/*
		 * Right wall needs to check the top, bottom, and three on left
		 */
		else if(i == rows - 1){
			num = boardState[i][j - 1] + boardState[i][j + 1] +
					boardState[i - 1][j];
		}
		/*
		 * Bottom needs to check the left, right, and three on top
		 */
		else if(j == columns - 1){
			num = boardState[i - 1][j] + boardState[i + 1][j] +
					boardState[i][j - 1];
		}
		/*
		 * Other spots need to check all surroundings
		 */
		else{
			num = boardState[i][j - 1] +
					boardState[i - 1][j] + 
					boardState[i + 1][j] + 
					boardState[i][j + 1];
		}
		return num;
	}
	private void compareBoards(){
		if(!gameOver){
			boolean isSame = true;
			for(int i = 0; i < rows && isSame; i++){
				for(int j = 0; j < columns && isSame; j++){
					if(boardState[i][j] != updatedBoard[i][j]){
						isSame = false;
					}
				}
			}
			gameOver = isSame;
		}
	}
	private void checkEmpty(){
		if(!gameOver){
			boolean hasItems = false;
			for(int i = 0; i < rows && !hasItems; i++){
				for(int j = 0; j < columns && !hasItems; j++){
					if(boardState[i][j] == 1){
						hasItems = true;
					}
				}
			}
			gameOver = !hasItems;
		}		
	}
	public void updateBoard(){
		for(int i = 0; i < rows; i++){
			for (int j = 0; j < columns; j++){
				int neighbors = numberOfNeighbors(i, j);
				if(boardState[i][j] == 0){
					if(neighbors >= 2){
						updatedBoard[i][j] = 1;
					}
				}
				else if(boardState[i][j] == 1){
					updatedBoard[i][j] = 1;
				}
			}
		}
		s.push(boardState);
		compareBoards();
		boardState = updatedBoard;
		checkEmpty();
		updatedBoard = new int[rows][columns];
	
	}
	public void updateBoard2(){
		for(int i = 0; i < rows; i++){
			for (int j = 0; j < columns; j++){
				int neighbors = numberOfNeighbors2(i, j);
				
				/*
				 * If the cell is dead
				 */
				if(boardState[i][j] == 0){
					/*
					 * The only way to come alive is if the neighbors = something
					 */
					if(neighbors == 3){
						updatedBoard[i][j] = 1;
					}
				}
				/*
				 * If the cell is alive
				 */
				else{
					/*
					 * It will die from too many or too little neighbors
					 */
					if(neighbors < 2 || neighbors > 3){
						updatedBoard[i][j] = 0;
					}
					/*
					 * else, it will live on
					 */
					else{
						updatedBoard[i][j] = 1;
					}
				}
			}
		}

		s.push(boardState);
		compareBoards();
		boardState = updatedBoard;
		checkEmpty();
		updatedBoard = new int[rows][columns];
	}
	public void back(){
		updatedBoard = s.pop();
		boardState = updatedBoard;
		gameOver = false;		
		updatedBoard = new int[rows][columns];
	}
	public int stackSize(){
		return s.size();
	}
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		for(int i = 0; i < rows; i++){
			for (int j = 0; j < columns; j++){
				if(boardState[i][j] == 1){
					g.setColor(Color.MAGENTA);
					g.fillRect(cellX[i][j], cellY[i][j], cellWidth, cellHeight);
				}
				else{
					g.setColor(Color.GRAY);
					g.fillRect(cellX[i][j], cellY[i][j], cellWidth, cellHeight);
					g.setColor(Color.BLACK);
					g.drawRect(cellX[i][j], cellY[i][j], cellWidth, cellHeight);
				}				
			}
		}		
	}
}
