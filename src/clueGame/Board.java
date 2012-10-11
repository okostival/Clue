package clueGame;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Board {

	private ArrayList<BoardCell> cells;
	private Map<Character, String> rooms;
	private int numRows;
	private int numColumns;
	
	//Use this section when testing my config file
	private static final String LEGEND_FILE = "~/../Legend.csv";
	private static final String CONFIG_FILE = "~/../Layout.csv";
	
	//Use this section when testing the teacher's config files
	//private static final String LEGEND_FILE = "~/../TeacherLegend.csv";
	//private static final String CONFIG_FILE = "~/../TeacherLayout.csv";
	
	public Board() {
		// TODO Auto-generated constructor stub
		cells = new ArrayList<BoardCell>();
		rooms = new HashMap<Character, String>();
		numRows = -1;
		numColumns = -1;
		
		loadConfigFiles();
	}
	
	public BoardCell getCellAt(int index)
	{
		return cells.get(index);
	}
	
	public Map<Character, String> getRooms()
	{
		return rooms;
	}
	
	public int getNumRows()
	{
		return numRows;
	}
	
	public int getNumColumns()
	{
		return numColumns;
	}

	public void loadConfigFiles()
	{
		try {
			loadLegend();
			
			loadRoomConfig();
			
		} catch (BadConfigFormatException ex) {
			System.out.println(ex.toString());
		}
		
		return;
	}
	
	public int calcIndex (int row, int col)
	{
		return (row*numColumns) + col;
	}
	
	public RoomCell getRoomCellAt (int row, int col)
	{
		return (RoomCell)cells.get(calcIndex(row, col));
	}
	
	public void loadLegend() throws BadConfigFormatException {		
		try {
			FileReader reader = new FileReader(LEGEND_FILE);
			Scanner scan = new Scanner(reader);
			
			while (scan.hasNext())
			{
				String inputLine = scan.nextLine();
				String [] legendLine = inputLine.split(",");
				char initial;
				String roomName;
				
				if (legendLine.length == 2)
				{
					initial = legendLine[0].trim().charAt(0);
					roomName = legendLine[1].trim();
					
					rooms.put(initial, roomName);
				}
				else
				{
					throw new BadConfigFormatException(LEGEND_FILE, "File contains more than 2 items per line");
				}
			}
		}
		catch (FileNotFoundException ex)
		{
			System.out.println(LEGEND_FILE + " could not be found!");
		}
		
		return;
	}
	
	public void loadRoomConfig() throws BadConfigFormatException {
		try {
			FileReader reader = new FileReader(CONFIG_FILE);
			Scanner scan = new Scanner(reader);
			int currentRow = -1;
			int currentCol = -1;
			
			while (scan.hasNext())
			{
				currentCol = -1;
				currentRow++;
				
				String inputLine = scan.nextLine();
				String [] roomLine = inputLine.split(",");
				
				if (numColumns == -1)
				{
					numColumns = roomLine.length;
				}
				else if (numColumns != roomLine.length)
				{
					throw new BadConfigFormatException(CONFIG_FILE, "Not all of the rows have the same number of columns");
				}
				
				for (String s : roomLine)
				{
					currentCol++;
					
					if (rooms.containsKey(s.trim().charAt(0)))
					{
						if (s.length() > 1)
						{
							if (s.charAt(1) == 'U')
							{
								RoomCell newRoom = new RoomCell(RoomCell.DoorDirection.UP, s.charAt(0), currentRow, currentCol);
								cells.add(newRoom);
							}
							else if (s.charAt(1) == 'R')
							{
								RoomCell newRoom = new RoomCell(RoomCell.DoorDirection.RIGHT, s.charAt(0), currentRow, currentCol);
								cells.add(newRoom);
							}
							else if (s.charAt(1) == 'D')
							{
								RoomCell newRoom = new RoomCell(RoomCell.DoorDirection.DOWN, s.charAt(0), currentRow, currentCol);
								cells.add(newRoom);
							}
							else if (s.charAt(1) == 'L')
							{
								RoomCell newRoom = new RoomCell(RoomCell.DoorDirection.LEFT, s.charAt(0), currentRow, currentCol);
								cells.add(newRoom);
							}
							else 
							{
								//This will later be used to determine where the name goes on the room board
								RoomCell newRoom = new RoomCell(RoomCell.DoorDirection.NONE, s.charAt(0), currentRow, currentCol);
								cells.add(newRoom);
							}
						}
						else
						{
							if (s.charAt(0) == 'W')
							{
								WalkwayCell newWalkway = new WalkwayCell();
								cells.add(newWalkway);
							}
							else 
							{
								RoomCell newRoom = new RoomCell(RoomCell.DoorDirection.NONE, s.charAt(0), currentRow, currentCol);
								cells.add(newRoom);
							}
						}
					}
					else
					{
						throw new BadConfigFormatException(CONFIG_FILE, "One or more room initials do not correspond to a valid room");
					}
				}
			}
			
			numRows = currentRow+1;
		}
		catch (FileNotFoundException ex)
		{
			System.out.println(CONFIG_FILE + " could not be found!");
		}
		
		return;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
