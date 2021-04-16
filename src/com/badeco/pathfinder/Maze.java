package com.badeco.pathfinder;

public class Maze {

	private static String mazeName;
	private static int length;
	private static boolean [] [] maze;
	
	private final static int DEFAULT_LENGTH = 10;
	private final static String DEFAULT_NAME = "NewMaze";
	public static final boolean [] [] DEFAULT_MAZE ={{true,true,true,false,true,false,true,true,true,true},
													{true,false,true,false,true,false,true,false,false,true},
													{true,false,true,true,true,true,true,false,true,true},
													{true,true,false,true,false,false,true,true,false,false},
													{false,true,false,false,true,false,false,true,true,true},     
													{false,true,false,true,true,true,false,true,false,true},
													{true,true,false,true,false,true,true,true,false,true},
													{true,false,true,true,true,true,false,true,true,true},
													{true,false,false,false,false,true,false,true,false,false},
													{true,true,true,true,true,true,false,true,true,true}};

	
	public Maze (boolean isDefault) {
		Maze.mazeName = DEFAULT_NAME;
		Maze.length = DEFAULT_LENGTH;
		
		if (isDefault) {
			Maze.maze = DEFAULT_MAZE;
		} else {
		Maze.maze = new boolean [DEFAULT_LENGTH][DEFAULT_LENGTH];
		Maze.maze[0][0] = true;
		Maze.maze[DEFAULT_LENGTH-1][DEFAULT_LENGTH-1] = true;
		}
	}
	
	public static void setFullMaze (String newMazeName, int newLength, boolean[][] newMaze) {
		Maze.mazeName = newMazeName;
		Maze.length = newLength;
		Maze.maze = newMaze;
	}
	
	public static void setFullMaze (String newMazeName,boolean[][] newMaze) {
		Maze.mazeName = newMazeName;
		Maze.length = newMaze.length;
		Maze.maze = newMaze;
	}
	
	public static String getMazeName() {
		return mazeName;
	}

	public static void setMazeName(String mazeName) {
		Maze.mazeName = mazeName;
	}

	public static int getLength() {
		return length;
	}
	
	public static void setLength(int length) {
		Maze.length = length;
	}

	public static boolean [][] getMaze() {
		return maze;
	}
	
	public static void setMaze(boolean [][] maze) {
		Maze.maze = maze;
	}
	
	public static boolean [][] getMazeCopy() {
		boolean [][] mazeCopy = new boolean[length][length];
		for (int x = 0; x < mazeCopy.length; x++) {
			for (int y = 0; y < mazeCopy.length; y++) {
				mazeCopy[x][y] = maze[x][y];
			}
		}
		return mazeCopy;
	}

}
