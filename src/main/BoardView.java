package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class BoardView extends JFrame implements ActionListener, KeyListener, 
MouseListener, MouseMotionListener, ChangeListener{
	private JPanel view = new JPanel();	
	
	private Game g;
	
	private int screenWidth, screenHeight;
	private int buttonWidth, buttonHeight;
	private int textAreaWidth;
	
	private JButton next;
	private JButton stop;
	private JButton go;
	private JButton back;
	private JButton clear;
	private JButton dragTo;
	
	private JTextArea columnsTextArea;
	private JTextArea columnsTextAreaBox;
	private JTextArea rowsTextArea;
	private JTextArea rowsTextAreaBox;
	private JTextArea dragToArea;
	private JTextArea changeTimer;
	
	private JSlider updates;
	
	private Timer goTimer;
	
	private boolean going = false;
	private boolean spawn = true;
	
	public BoardView(){		
		super("Skylar's game of life");
		screenWidth = 1050;
		screenHeight = 650;
	}
	public BoardView(Game game){
		super("Skylar's game of life");
		
		/*
		 * Create the size of the window as well as button sizes
		 */
		screenWidth = 1050;
		screenHeight = 650;
		buttonWidth = 60;
		buttonHeight = 20;
		textAreaWidth = 105;
		
		/*
		 * Update the board to be the one passed in
		 */
		g = game;		
		
		/*
		 * Create our array of cells
		 */
		
		g.setBounds(0, 1, screenWidth - 120, screenHeight - 50);
		g.setCellWidth((screenWidth - 120) / g.getColumns());
		g.setCellHeight((screenHeight - 50) / g.getRows());
		g.refresh();
		g.addMouseListener(this);
		g.addMouseMotionListener(this);
		
		/*
		 * When the user presses go, this timer will trigger every 
		 * set amount(1000 = 1 second)
		 */
		goTimer = new Timer(250, this);	
		
		
		/*
		 * Instantiate our "next" button	
		 */
		next = new JButton("next");
		next.setBounds(screenWidth - (int)(buttonWidth * 1.5), 
				buttonHeight, buttonWidth, buttonHeight);
		next.addActionListener(this);
		next.setFont(new Font("times new roman", Font.BOLD, 12));
		
		/*
		 * Instantiate our "back" button
		 */
		back = new JButton("back");
		back.setBounds(screenWidth - (int)(buttonWidth * 1.5), 
				buttonHeight * 2, buttonWidth, buttonHeight);
		back.addActionListener(this);
		back.setFont(new Font("times new roman", Font.BOLD, 12));
		
		/*
		 * Instantiate our "go" button
		 */
		go = new JButton("go");
		go.setBounds(screenWidth - (int)(buttonWidth * 1.5), 
				buttonHeight * 4, buttonWidth, buttonHeight);
		go.addActionListener(this);
		go.setFont(new Font("times new roman", Font.BOLD, 12));
		
		/*
		 * Instantiate our "stop" button
		 */
		stop = new JButton("stop");
		stop.setBounds(screenWidth - (int)(buttonWidth * 1.5), 
				buttonHeight * 5, buttonWidth, buttonHeight);
		stop.addActionListener(this);
		stop.setFont(new Font("times new roman", Font.BOLD, 12));
		
		/*
		 * Instantiate our "clear" button
		 */
		clear = new JButton("clear");
		clear.setBounds(screenWidth - (int)(buttonWidth * 1.5),
				buttonHeight * 12, buttonWidth, buttonHeight);
		clear.addActionListener(this);
		clear.setFont(new Font("times new roman", Font.BOLD, 12));
		
		/*
		 * Instantiate our button that controls if dragging spawns or kills
		 */
		dragTo = new JButton("spawn");
		dragTo.setBounds(screenWidth - (int)(buttonWidth * 1.6),
				buttonHeight * 16, buttonWidth + 10, buttonHeight);
		dragTo.addActionListener(this);
		dragTo.setFont(new Font("times new roman", Font.BOLD, 12));
		
		/*
		 * Instantiate our text box for the rows changing
		 * instruction
		 */
		rowsTextArea = new JTextArea("Enter new rows");
		rowsTextArea.setEditable(false);
		rowsTextArea.setBounds(screenWidth - (int)(buttonWidth * 1.9), 
				(int)(buttonHeight * 7), textAreaWidth, buttonHeight);
		rowsTextArea.setWrapStyleWord(false);
		rowsTextArea.setLineWrap(false);
		rowsTextArea.setBackground(getBackground());
		rowsTextArea.setName("row");
		
		/*
		 * Instantiate the text box for changing height
		 */
		rowsTextAreaBox = new JTextArea();
		rowsTextAreaBox.setBounds(screenWidth - (int)(buttonWidth * 1.5), 
				buttonHeight * 8, buttonWidth, buttonHeight);
		rowsTextAreaBox.addKeyListener(this);
		rowsTextAreaBox.setFont(new Font("times new roman", Font.BOLD, 12));
		
		/*
		 * Instantiate our text box for the columns changing
		 * instruction
		 */
		columnsTextArea = new JTextArea("Enter new columns");
		columnsTextArea.setEditable(false);
		columnsTextArea.setBounds(screenWidth - (int)(buttonWidth * 1.9), 
				(int)(buttonHeight * 9), textAreaWidth, buttonHeight);
		columnsTextArea.setWrapStyleWord(false);
		columnsTextArea.setLineWrap(false);
		columnsTextArea.setBackground(getBackground());
		columnsTextArea.setName("col");
		/*
		 * Instantiate the text box for changing width
		 */
		columnsTextAreaBox = new JTextArea();
		columnsTextAreaBox.setBounds(screenWidth - (int)(buttonWidth * 1.5), 
				buttonHeight * 10, buttonWidth, buttonHeight);
		columnsTextAreaBox.addKeyListener(this);
		columnsTextAreaBox.setFont(new Font("times new roman", Font.BOLD, 12));
		
		/*
		 * Instantiate the instructions for spawning or killing when dragging
		 */
		dragToArea = new JTextArea("Drag to do what the button says");
		dragToArea.setBounds(screenWidth - (int)(buttonWidth * 1.8), 
				(int)(buttonHeight * 14.25), textAreaWidth - 5, buttonHeight * 2);
		dragToArea.setEditable(false);
		dragToArea.setWrapStyleWord(true);
		dragToArea.setLineWrap(true);
		dragToArea.setBackground(getBackground());
		
		
		/*
		 * Instantiate the instructions for spawning or killing when dragging
		 */
		changeTimer = new JTextArea("Slide to the number of updates per second");
		changeTimer.setBounds(screenWidth - (int)(buttonWidth * 1.8), 
				(int)(buttonHeight * 18), textAreaWidth - 5, (int)(buttonHeight * 3.25));
		changeTimer.setEditable(false);
		changeTimer.setWrapStyleWord(true);
		changeTimer.setLineWrap(true);
		changeTimer.setBackground(getBackground());
		/*
		 * Create a JSlider that will change the update time of the timer.
		 */
		updates = new JSlider(JSlider.HORIZONTAL, 0, 20, 4);
		updates.addChangeListener(this);
		updates.setMajorTickSpacing(5);
		updates.setMinorTickSpacing(1);
		updates.setPaintTicks(true);
		updates.setPaintLabels(true);
		updates.setBounds(screenWidth - (int)(buttonWidth * 2), 
				(int)(buttonHeight * 21.5), textAreaWidth - 5, (int)(buttonHeight * 2.25));
		
		/*
		 * Clear the layout so we can position the buttons
		 * Change the border to be pretty
		 */
		view.setLayout(null);
		view.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		/*
		 * Add the buttons to our buttons JPanel
		 */
		view.add(next);
		view.add(back);
		view.add(go);
		view.add(stop);
		view.add(clear);
		view.add(dragTo);
		view.add(changeTimer);
		view.add(columnsTextArea);
		view.add(columnsTextAreaBox);
		view.add(rowsTextArea);
		view.add(rowsTextAreaBox);
		view.add(dragToArea);
		view.add(updates);
		/*
		 * Add a mouse listener to the cell JPanel
		 */
		view.addMouseListener(this);
		
		/*
		 * Add the panels to this
		 */

		this.add(g);
		this.add(view);	
		/*
		 * Place the JFrame in the right spot with the right size
		 */
		this.setPreferredSize(new Dimension(screenWidth,screenHeight));
		this.setLocation(300, 200);
		this.setResizable(true);
		/* 
		 * Put it on the screen
		 */
		this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
	}
	private void updateButtons(){
		if(going){
			go.setEnabled(false);
			stop.setEnabled(true);			
		}
		else{
			go.setEnabled(true);
			stop.setEnabled(false);
		}
		if(g.gameOver()){
			next.setEnabled(false);
		}
		else{
			next.setEnabled(true);
		}
		if(g.stackSize() > 0){
			back.setEnabled(true);
		}
		else{
			back.setEnabled(false);
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source == go){			
			going = true;	
			goTimer.start();
			updateButtons();			
		}
		else if(source == stop){
			going = false;
			goTimer.stop();
			updateButtons();
		}
		else if(source == next){			
			g.updateBoard();
			g.paint();
			updateButtons();		
		}
		else if(source == back){
			g.back();
			g.paint();
			updateButtons();			
		}
		else if(source == clear){
			g.refresh();
			g.paint();
			updateButtons();
		}
		else if(source == dragTo){
			spawn = !spawn;
			if(spawn){
				dragTo.setText("spawn");
			}
			else{
				dragTo.setText("kill");
			}
		}
		else if(source == goTimer){
			if(g.gameOver()){
				going = false;
				goTimer.stop();
				updateButtons();
			}
			else{
				g.updateBoard();
				g.paint();
				updateButtons();
			}			
		}
		
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		going = false;
		goTimer.stop();
		if(e.getY() > 0 && e.getY() < g.getCellHeight() * g.getRows() &&
				e.getX() > 0 && e.getX() < g.getCellWidth() * g.getColumns()){
			g.switchState(e.getY() / g.getCellHeight(), e.getX() / g.getCellWidth());
			g.paint();
			updateButtons();
		}
		
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		going = false;
		goTimer.stop();
		if(e.getY() > 0 && e.getY() < g.getCellHeight() * g.getRows() &&
				e.getX() > 0 && e.getX() < g.getCellWidth() * g.getColumns()){
			if(spawn){
				g.spawn(e.getY() / g.getCellHeight(), e.getX() / g.getCellWidth());
			}
			else{
				g.kill(e.getY() / g.getCellHeight(), e.getX() / g.getCellWidth());
			}			
			g.paint();
			updateButtons();
		}
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_ENTER){
			/*
			 * This is for rows
			 */
			if(e.getComponent().getLocation().y == 160){				
				String s = rowsTextAreaBox.getText();
				s = s.replaceAll("\n", "");
				if(!s.isEmpty()){
					going = false;
					goTimer.stop();
					int newRows = Integer.parseInt(s);
					g.setRows(newRows);
					g.setCellWidth((screenWidth - 120) / g.getColumns());
					g.setCellHeight((screenHeight - 50) / g.getRows());
					g.reposition();
					g.paint();
					rowsTextAreaBox.setText("");
				}
			}
			/*
			 * This is for columns
			 */
			else if(e.getComponent().getLocation().y == 200){
				String s = columnsTextAreaBox.getText();
				s = s.replaceAll("\n", "");
				if(!s.isEmpty()){
					going = false;
					goTimer.stop();
					int newCols = Integer.parseInt(s);
					g.setColumns(newCols);
					g.setCellWidth((screenWidth - 120) / g.getColumns());
					g.setCellHeight((screenHeight - 50) / g.getRows());
					g.reposition();
					g.paint();
					columnsTextAreaBox.setText("");
				}
			}
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == updates){			
			if(updates.getValue() == 0){
				going = false;
				goTimer.stop();
			}
			else{
				going = true;
				goTimer.start();
				goTimer.setDelay(1000 / updates.getValue());				
			}
			updateButtons();
		}		
	}
	
}
