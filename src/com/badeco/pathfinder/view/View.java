package com.badeco.pathfinder.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.badeco.pathfinder.*;

public class View {
	
	public final static String TITLE = "PathFinder 1.0";
	public final static int DEFAULT_FIELD_LENGTH = 10;
	public final static int DEFAULT_BUTTON_SIZE = 30;
	
	private JFrame frame;
	private JPanel mainPanel, gamePanel,titlePanel;
	private JLabel titleLabel, mapLabel;
	
	private int fieldLength, buttonSize;
	private static JButton [][] buttonField;
	
	private JMenuBar menuBar;
	private JMenu pathFinder, mazeMenu, help;
	private JMenuItem runPath, resetPath, preferences, quit, resetMaze, loadMaze, saveMaze, howToPlay, about;
	
	public View() {
		
	fieldLength = DEFAULT_FIELD_LENGTH;
	buttonSize = DEFAULT_BUTTON_SIZE;
	
	frame = new JFrame(TITLE);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setLocation(500,60);
	frame.setResizable(false);
	
	intializeMenuBar();
	
	mainPanel = new JPanel();
	mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    frame.getContentPane().add(mainPanel);
  	
    titlePanel = new JPanel();
    titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
	titleLabel = new JLabel("", SwingConstants.CENTER);
    titleLabel.setOpaque(true);
    titleLabel.setBackground(Color.BLACK);
    titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    titleLabel.setText("<html><head><style>h1{text-align:center;}h1{font-size:50px;}h1{color:orange;}</style><h1><b>PathFinder 1.0</b></h1></head></html>");
    
    mapLabel = new JLabel("", SwingConstants.LEFT);
    mapLabel.setFont (new Font(Font.SANS_SERIF,0,14));
    mapLabel.setOpaque(true);
    mapLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    mapLabel.setText(" ");
    titlePanel.add(titleLabel);
    titlePanel.add(Box.createVerticalStrut(5));
    titlePanel.add(mapLabel);
    titlePanel.add(Box.createVerticalStrut(5));
    
   
    gamePanel = new JPanel(new GridLayout(fieldLength,fieldLength));
    buttonField = getField(fieldLength);
  	
  	mainPanel.add(titlePanel);
  	mainPanel.add(gamePanel);
  	frame.pack();
  	frame.setVisible(true);
  	
	}
	
	

	private JButton[][] getField(int size) {
		JButton [][] field = new JButton [size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				JButton tile = new JButton();
				tile.setOpaque(true);
				
				tile.setFont(new Font(Font.SANS_SERIF, 0, buttonSize));
				
				tile.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (tile.getText() != ">") {
							String mazeName = Maze.getMazeName();
							if (!mazeName.endsWith("*")) {
								Maze.setMazeName(mazeName + "*");
								mapLabel.setText( mazeName + "*.maze");
							}
							if (tile.getBackground() == Color.BLACK) {
								tile.setBackground(Color.WHITE);
							} else {
								tile.setBackground(Color.BLACK);
							}
						} else {
							runPath();
							
						} 
					}
				});
				
				if (i == 0 && j == 0) {
					tile.setText(">");
				} else if (i == Maze.getLength() - 1 && j == Maze.getLength() - 1) {
					tile.setText(">");
				}
					
					
				if (i < size && j < size) {
					if (Maze.getMaze()[i][j]) {
						tile.setBackground(Color.WHITE);
					} else {
						tile.setBackground(Color.BLACK);
					}
				} else {
					tile.setBackground(Color.BLUE);
				}
				
				field[i][j] = tile;
				gamePanel.add(tile);
				
			}
		}
		return field;
	}
	
	
	
private void runPath() {
	erasePath();
	Maze.setMaze(getMazePicture());
	PathFinder.updateMaze();
	PathFinder.runMaze();
}



private void intializeMenuBar() {
		
		menuBar = new JMenuBar();
		
		pathFinder = new JMenu("PathFinder");
		runPath = new JMenuItem("Run Path");
		runPath.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				runPath();
			}

			
		});
		resetPath = new JMenuItem("Reset Path");
		resetPath.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				erasePath();
			}

			
		});
		preferences = new JMenuItem("Preferences");
		preferences.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showPreferences();
			}

		});
		quit = new JMenuItem("Quit");
		quit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		pathFinder.add(runPath);
		pathFinder.add(resetPath);
		pathFinder.add(preferences);
		pathFinder.add(quit);
		
		mazeMenu = new JMenu("Maze");
		resetMaze = new JMenuItem("Reset Maze");
		resetMaze.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(null, "Are you sure you want to erase this map?", 
						TITLE , JOptionPane.YES_NO_OPTION) == 0) {
				resetButtonField();
				mapLabel.setText("newMaze.maze");
				
				}
			}
		});
		loadMaze = new JMenuItem("Load Maze");
		loadMaze.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {				
				if (mapLabel.getText().contains("*")) {
					if (JOptionPane.showConfirmDialog(null, "This maze hasn't been saved yet. "
						+ "\nWould you like to save it before loading a new one?"
							, TITLE , JOptionPane.YES_NO_OPTION) == 0) {
						saveMaze.doClick();
					} 
				} 
				loadNewFile();
			}
		});
		
		saveMaze = new JMenuItem("Save Maze");
		saveMaze.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String strMazeName = JOptionPane.showInputDialog(null, "Please choose a name for this map:", 
						TITLE , JOptionPane.OK_CANCEL_OPTION);
				if (strMazeName != null) {	
					Maze.setMazeName(strMazeName);
					erasePath();
					try {
						createNewFile(Maze.getMazeName(), Maze.getLength(), getMazePicture());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					mapLabel.setText(Maze.getMazeName() + ".maze");
				}
			}

		});
		mazeMenu.add(resetMaze);
		mazeMenu.add(loadMaze);
		mazeMenu.add(saveMaze);
		
		help = new JMenu("Help");
		howToPlay = new JMenuItem("How To Play");
		howToPlay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showHowToPlay();
			}

			private void showHowToPlay() {
				// TODO Auto-generated method stub
				
			}
		});
		about = new JMenuItem("About");
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showAbout();
			}

			
		});
		help.add(howToPlay);
		help.add(about);
		
		menuBar.add(pathFinder);
		menuBar.add(mazeMenu);
		menuBar.add(help);
		frame.setJMenuBar(menuBar);
	}

	

	private boolean[][] turnLineIntoMazeRow(boolean[][] newMaze, String line, int rowNum) {
		for (int y = 0; y < newMaze[0].length; y++) {
			if (line.charAt(y) == '1') {
				newMaze[rowNum][y] = true;
			}
		}
		return newMaze;
	}


	protected void updateButtonField() {
		for (int x = 0; x < buttonField.length; x++) {
			for (int y = 0; y < buttonField[0].length; y++) {
				if (PathFinder.maze[x][y])  {
					buttonField[x][y].setBackground(Color.WHITE);
				} else {
					buttonField[x][y].setBackground(Color.BLACK);
				}
			}
		}
	}

	private void resetButtonField() {
		for (int x = 0; x < buttonField.length; x++) {
			for (int y = 0; y < buttonField[0].length; y++) {
					buttonField[x][y].setBackground(Color.BLACK);
			}
		}
		buttonField[0][0].setBackground(Color.WHITE);
		buttonField[buttonField.length-1][buttonField[0].length-1].setBackground(Color.WHITE);
	}

	public static void erasePath() {
		for (int x = 0; x < buttonField.length; x++) {
			for (int y = 0; y < buttonField[0].length; y++) {
				if (buttonField[x][y].getBackground() != Color.WHITE && 
						buttonField[x][y].getBackground() != Color.BLACK) {
					buttonField[x][y].setBackground(Color.WHITE);
				}
			}
		}
	}
	
	private void showPreferences() {
		JFrame prefFrame = new JFrame();
		prefFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		prefFrame.setTitle(TITLE);
		prefFrame.setLocation(480,300);
		prefFrame.setResizable(false);
		
		JPanel prefPanel = new JPanel();
		prefPanel.setLayout(new BoxLayout(prefPanel, BoxLayout.Y_AXIS));
		prefPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		prefPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JLabel prefLabel = new JLabel();
		prefLabel.setText("PREFERENCES");
		prefLabel.setBackground(Color.BLACK);
		prefLabel.setOpaque(true);
		prefLabel.setText("<html><head><style>h1{text-align:center;}"
				+ "h1{font-size:20px;}h1{color:orange;}</style><h1><b>  PREFERENCES  </b></h1></head></html>");
		      
		JCheckBox checkPlayerTwo = new JCheckBox("Activate Second Engine", PathFinder.isPlayerTwoPlaying);
		checkPlayerTwo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (PathFinder.isPlayerTwoPlaying) {
					PathFinder.isPlayerTwoPlaying = false;
				} else {
					PathFinder.isPlayerTwoPlaying = true;
				}
			}
          }); 
		JCheckBox checkStepByStep = new JCheckBox("Show Step-By-Step Progress", PathFinder.isStepByStep);
		checkStepByStep.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (PathFinder.isStepByStep) {
					PathFinder.isStepByStep = false;
				} else {
					PathFinder.isStepByStep = true;
				}
			}
          });
		JCheckBox checkPrintScores = new JCheckBox("Print Scores", PathFinder.isPrintingScores);
		checkPrintScores.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (PathFinder.isPrintingScores) {
					PathFinder.isPrintingScores = false;
				} else {
					PathFinder.isPrintingScores = true;
				}
			}
          });
		
		prefPanel.add(prefLabel);
		prefPanel.add(Box.createVerticalStrut(10));
		prefPanel.add(checkPlayerTwo);
		prefPanel.add(Box.createVerticalStrut(10));
		prefPanel.add(checkStepByStep);
		prefPanel.add(Box.createVerticalStrut(10));
		prefPanel.add(checkPrintScores);
		    
	    prefFrame.getContentPane().add(prefPanel);
		prefFrame.pack();
		prefFrame.setVisible(true);
	}

	private void showAbout() {
		JFrame aboutFrame = new JFrame();
		aboutFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		aboutFrame.setTitle(TITLE);
		aboutFrame.setLocation(480,300);
		aboutFrame.setResizable(false);
		
		JPanel aboutPanel = new JPanel();
		aboutPanel.setLayout(new BoxLayout(aboutPanel, BoxLayout.Y_AXIS));
		aboutPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	   
	    JLabel aboutLabel = new JLabel();
	    aboutLabel.setText("<html><style>h1{text-align:center;}h1{font-size:20px;}h1{color:orange;}</style>"
	    		+ "<head><h1><b>PathFinder 1.0<br><br>Made by Caio Badner<br><br> in Caesarea, Israel 2020 <br>"
	    		+ "</b></h1></head></html>");
	    aboutLabel.setFont (new Font(Font.SANS_SERIF,0,25));
	    aboutLabel.setOpaque(true);
	    aboutLabel.setForeground(Color.ORANGE);
	    aboutLabel.setBackground(Color.BLACK);
	    
	    JButton okButton = new JButton();
	    okButton.setText("Ok");
	    okButton.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
				aboutFrame.dispose();
			}
		});
	    
	    aboutPanel.add(aboutLabel);
	    aboutPanel.add(Box.createVerticalStrut(10));
	    aboutPanel.add(okButton);
	    aboutPanel.add(Box.createVerticalStrut(10));
	    
	    aboutFrame.getContentPane().add(aboutPanel);
	    aboutFrame.pack();
	    aboutFrame.setVisible(true);
	}
	
	
	public boolean [][] getMazePicture() {
		boolean [][] mazePicture = new boolean [buttonField.length][buttonField.length];
		
		for (int x = 0; x < buttonField.length; x++) {
			for (int y = 0; y < buttonField[0].length; y++) {
				if (buttonField[x][y].getBackground() == Color.WHITE) {
					mazePicture[x][y] = true;
				} 
			}
		}
		return mazePicture;
	}

	public static void leaveMark(int x, int y) {
		
		if (x == PathFinder.maze.length - 1 && y == x) {
			buttonField[x][y].setBackground(Color.GREEN);
		}
		
		if (buttonField[x][y].getBackground() == Color.WHITE /*|| buttonField[x][y].getBackground() == Color.BLACK*/) {
			buttonField[x][y].setBackground(Color.YELLOW);
		} else if (buttonField[x][y].getBackground() == Color.YELLOW) {
			buttonField[x][y].setBackground(Color.ORANGE);
		} else if (buttonField[x][y].getBackground() == Color.ORANGE) {
			buttonField[x][y].setBackground(Color.RED);	
		} else if (buttonField[x][y].getBackground() == Color.RED) {
			buttonField[x][y].setBackground(Color.MAGENTA);
		} else if (buttonField[x][y].getBackground() == Color.MAGENTA) {
			buttonField[x][y].setBackground(Color.CYAN);
		} else if (buttonField[x][y].getBackground() == Color.CYAN) {
			buttonField[x][y].setBackground(Color.BLUE);
		}
	}
	
	private void loadNewFile() {
		JFileChooser jfc = new JFileChooser("C://Users//caio//Desktop//PathFinder//Mazes//");
		jfc.setDialogTitle("Select a maze");
		jfc.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("MAZE files", "maze");
		jfc.setFileFilter(filter);
		int returnValue = jfc.showOpenDialog(null);
		
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			
			File selectedFile = jfc.getSelectedFile();
			BufferedReader reader = null;
			
			try {
				reader = new BufferedReader(new FileReader(selectedFile));
			} catch (FileNotFoundException e2) {
				e2.printStackTrace();
				return;
			}
			
			String line = null;
			
			try {
				line = reader.readLine();
			} catch (IOException e2) {
				e2.printStackTrace();
				
			}
			
			String newMazeName = line;
			
			try {
				line = reader.readLine();
			} catch (IOException e2) {
				e2.printStackTrace();
				
			}
			
			int newLength;
			try {
				newLength = Integer.parseInt(line);
			} catch (NumberFormatException e3) {
				newLength = -1;
			}
			if (buttonField.length != newLength) {
				//this version still doesnt allow for different maze sizes
				JOptionPane.showMessageDialog(null, "The size of the maze is incorrect for this board", TITLE, 0);
				//future implementation will regenerate the button field with the new maze's length and then 
				//load the whole frame correctly again
				//buttonField = getField(newLength);
			  	
			} else {
				
				boolean[][] newMaze = new boolean[newLength][newLength];
				
				for (int row = 0; row < newMaze.length; row++) {
					try {
						line = reader.readLine();
					} catch (IOException e2) {
						e2.printStackTrace();
						break;
					}
					newMaze = turnLineIntoMazeRow(newMaze, line, row);
				}
				
				try {
					reader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				Maze.setFullMaze(newMazeName, newMaze);
				PathFinder.updateMaze();
				updateButtonField();
				mapLabel.setText(Maze.getMazeName() + ".maze");
				System.out.println("Maze " + Maze.getMazeName() + ".maze was loaded successfully!");
			}
		}
	
	}
	
	private static void createNewFile(String mazeName, int length, boolean[][] mazePicture) throws IOException  {
	          
		File file = new File("C://Users//caio//Desktop//PathFinder//Mazes//" + mazeName + ".maze");
	  
          //Create the file
          if (file.createNewFile()){
            System.out.println("Maze " + mazeName + ".maze was created successfully!");
            
          }else{
            System.out.println("Error");
          }
           
          FileWriter writer = new FileWriter(file);
          writer.write(mazeName + "\n");
          writer.write("" + length + "\n");
          for (int x = 0; x < mazePicture.length; x++) {
			for (int y = 0; y < mazePicture[0].length; y++) {
				if (mazePicture[x][y]) {
					writer.write("1");
				} else {
					writer.write("0");
				} 
			}
			writer.write("\n");
          }	
          writer.close();
	}

}
