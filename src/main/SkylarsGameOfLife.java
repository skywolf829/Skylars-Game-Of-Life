package main;

public class SkylarsGameOfLife {
	public static void main(String[]args){
		Game g = new Game(50, 50);
		BoardView bv = new BoardView(g);
	}
}
