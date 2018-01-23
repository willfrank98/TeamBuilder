import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Vector;

public class SOURCE {
	
	final static int NUM_GRADES = 12; //maybe change the 12

	public static void main(String[] args) {
		
		//TODO: get filepath from stdin
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
		int[] gradeCounts = new int[NUM_GRADES]; 
		int[] gradeTotals = new int[NUM_GRADES];
		//Used to calculate how many players of each grad need to be on a team
		int totalPlayers = 0;
		double avgGrade = 0;
		
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
		
		//creates a team for every friend request pair
		for (Enumeration<Player> e = players.elements(); e.hasMoreElements();) 
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
		}
		
		Vector<Player> playersVec = new Vector<Player>(players.values()); //creates a vector of all remaining solo players
		
		//makes sure each stub team has 3-5 from the already-represented school
		for (Team team : teams) //iterates through all teams
		{
			//iterates through each school on a team
			for (Enumeration<String> e = team.schools.keys(); e.hasMoreElements();) 
			{
				String school = e.nextElement();
				
				while (team.schoolNum(school) < 3)
				{ //there are less than 3 players from a school on this team, finds more
					Player temp = findPlayer(school, team.largestGrade(), playersVec);
					
					if (temp != null)
					{
						team.addPlayer(temp);
						playersVec.remove(temp);
					}
					else
					{
						break;
					}
				}
			}
		}
		
		int numTeams = (totalPlayers/14) + 1; //max of 14 per team
		int maxPlayers = 14;
		
		TeamSkillComparator comp = new TeamSkillComparator();
		//sorts the teams by total skill level, then adds the worst team to the best team
		
		while (teams.size() > numTeams)
		{
			teams.sort(comp);
			
			//if the best and worst team are too big to be added moves on to the 2nd best and 2nd worst, etc.
			for (int i = 0; i < teams.size(); i++) 
			{
				if (teams.get(i).size() + teams.get(teams.size() - 1 - i).size() < 14)
				{
					teams.get(i).addPlayers(teams.get(teams.size() - 1 - i));
					teams.remove(teams.get(teams.size() - 1 - i));
					break;
				}
			}
			
			
			
		}
		

	}


	/**
	 * Returns a player from players with the appropriate school and grade
	 * @param school the desired school
	 * @param grade the desired grade
	 * @param players the list of players to choose from
	 * @return the appropriate Player
	 */
	static Player findPlayer(String school, int grade, Vector<Player> players)
	{
		for (Player player : players)
		{
			if (player.grade == grade && player.school.equals(school))
			{
				return player;
			}
		}
		
		return null;
	}
	
	/**
	 * 
	 * A Comparator for the Team class, based on team's total skill
	 *
	 */
	private static class TeamSkillComparator implements Comparator<Team>
	{

		@Override
		public int compare(Team t1, Team t2) {
			return t1.totalSKill() - t2.totalSKill();
		}
		
	}

}
