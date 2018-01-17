import java.io.File;
import java.io.FileNotFoundException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Vector;

public class SOURCE {

	public static void main(String[] args) {
		String filepath = "F:\\Unsorted Downloads\\23G Registrants.csv";
		
		Scanner sc;
		
		try {
			sc = new Scanner(new File(filepath));
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + filepath);
			return;
		}
		
		//Skips the two header lines
		sc.nextLine();
		sc.nextLine();
		
		Hashtable<String, Player> players = new Hashtable<String, Player>(); //A list of all players
		//Used to calculate average skill of each grade
		final int NUM_GRADES = 12; //maybe change the 12
		int[] gradeCounts = new int[NUM_GRADES]; 
		int[] gradeTotals = new int[NUM_GRADES];
		//Used to calculate how many players of each grad need to be on a team
		int totalPlayers = 0;
		int avgGrade = 0;
		
		//initialize the arrays. Is this neccessary?
		for (int i = 0; i < NUM_GRADES; i++)
		{
			gradeCounts[i] = 0;
			gradeTotals[i] = 0;
		}
		
		while(sc.hasNextLine())
		{
			//Grabs the next player and splits their information
			String[] line = sc.nextLine().split(",");
			
			String name = (line[1] + " " + line[0]).toLowerCase();
			int grade = Integer.parseInt(line[2]);
			int skill = Integer.parseInt(line[3]);
			
			String friend = "";
			try
			{
				friend = line[4].toLowerCase();
			}
			catch (Exception e) {}
			
			
			String school = "";
			try 
			{
				school = line[5].toLowerCase();
			} 
			catch (Exception e) {}
			
			Player temp = new Player(name, grade, skill, friend, school);
			
			players.put(name, temp);
			
			gradeCounts[grade]++;
			gradeTotals[grade] += skill;
			totalPlayers++;
			avgGrade += grade;
		}
		sc.close();
		
		avgGrade = avgGrade/totalPlayers; //the "average grade" of all players
		
		//calculates the average skill level of each grade
		//this is done to balance each team by skill per grade
		int[] gradeAvg = new int[NUM_GRADES];
		for (int i = 0; i < NUM_GRADES; i++)
		{
			if (gradeCounts[i] > 0)
			{
				gradeAvg[i] = gradeTotals[i]/gradeCounts[i];
			}
		}
		
		
		Vector<Team> teams = new Vector<Team>();
		
		for (Enumeration<Player> e = players.elements(); e.hasMoreElements();) //creates a team for every friend request pair and solo players
		{
			Player player = e.nextElement();
			
			Player playerFriend = players.get(player.friendRequest); //finds the Player that player has requested to be on their team, or null
			if (playerFriend != null && playerFriend.friendRequest.equals(player.name)) //returns true only if both players have requested each other
			{
				//adds the pair to a team
				Team t = new Team();
				t.addPlayer(player);
				t.addPlayer(playerFriend);
				teams.add(t);
				
				//removes both players from the master list
				players.remove(player.name);
				players.remove(playerFriend.name);
			}
			else 
			{
				//adds the solo player to a team
				Team t = new Team();
				t.addPlayer(player);
				teams.add(t);
				
				//removes both players from the master list
				players.remove(player.name);
			}
			
		}
		
		

	}
	
	
//	/**
//	 * Finds a player in the given vector with a matching name, or null if no such player is found
//	 * @param players a vector of Player objects
//	 * @param playerName the name of the desired player
//	 * @return the Player with playerName, or null if not found
//	 */
//	static Player findPlayer(Hashtable<String, Player> players, String playerName)
//	{
//		for (Player player : players) {
//			if (player.name == playerName)
//			{
//				return player;
//			}
//		}
//		return null;
//	}

}
