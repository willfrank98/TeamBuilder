import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Vector;

public class SOURCE {
	
	final static int NUM_GRADES = 13; //grades 0 to 12

	public static void main(String[] args) {
		
		//String filepath = "F:\\Programming\\Java\\TeamBuilder\\src\\SampleInput.csv";
		
		String filepath = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Location of input file: ");
		try	{
			filepath = br.readLine();
		} catch(IOException nfe) {
			System.err.println("Invalid Format!");
			return;
		}
		
		System.out.println("Location of output file: ");
		String output = "";
		try	{
			output = br.readLine();
		} catch(IOException nfe) {
			System.err.println("Invalid Format!");
			return;
		}
		
		Scanner sc;
		
		try {
			sc = new Scanner(new File(filepath));
		} catch (FileNotFoundException e) {
			System.out.println("Input file not found: " + filepath);
			return;
		}
		
		//Skips the header line
		sc.nextLine();
		
		Hashtable<String, Player> players = new Hashtable<String, Player>(); //A list of all players
		
		//Used to calculate average skill of each grade
		int[] gradePlayerCounts = new int[NUM_GRADES]; 
		//int[] gradeSkillTotals = new int[NUM_GRADES];
		int totalPlayers = 0;
		
		//the min and max grades present
		int minGrade = 999;
		int maxGrade = -1;
		
		int[][] playersByGradeAndSkillIdeal = new int[NUM_GRADES][6];
		
		while(sc.hasNextLine())
		{
			//Grabs the next player and splits their information
			String[] line = sc.nextLine().split(",");
			
			String name = (line[1] + " " + line[0]).toLowerCase();
			int grade = Integer.parseInt(line[2]);
			int skill = Integer.parseInt(line[3]);
			
			if (grade > maxGrade)
			{
				maxGrade = grade;
			}
			
			if (grade < minGrade)
			{
				minGrade = grade;
			}
			
			//TODO: should something be done with these exceptions?
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
			
			gradePlayerCounts[grade]++;
			//gradeSkillTotals[grade] += skill;
			totalPlayers++;
			playersByGradeAndSkillIdeal[grade][skill]++;
		}
		sc.close();
		
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
		
		int maxPlayers = 14; //max of 14 players per team
		int numTeams = (totalPlayers/maxPlayers) + 1; 

		System.out.print("How many teams would you like?: ");
		try
		{
			numTeams = Integer.parseInt(br.readLine());
		}
		catch(NumberFormatException | IOException nfe)
		{
			System.err.println("Invalid Format! Defaulting to 14 players per team");
		}
		
		//attempts to combine the teams so that playersByGradeAndSkill's target is maintained
		//potentially tries every team combination
		for (int i = 0; i < teams.size() - 2; i++)
		{
			if (teams.size() <= numTeams)
			{
				break;
			}
			
			Team t1 = teams.get(i);
			
			for (int j = i + 1; j < teams.size() - 1; j++)
			{
				Team t2 = teams.get(j);
				
				if (t1.size() + t2.size() > maxPlayers)
				{
					break;
				}
				
				boolean canAdd = true;
				
				for (int grade = minGrade; grade <= maxGrade; grade++)
				{
					for (int skill = 1; skill <= 5; skill++)
					{
						double playerTarget = (double)playersByGradeAndSkillIdeal[grade][skill] / (double)numTeams;
						
						if (t1.playersByGradeAndSkill[grade][skill] + t2.playersByGradeAndSkill[grade][skill] > playerTarget)
						{
							canAdd = false;
							break;
						}
					}
					if (!canAdd)
					{
						break;
					}
				}
				
				if (canAdd)
				{
					t1.addPlayers(t2);
					teams.remove(t2);
					break;
				}
			}
			
		}
		
		//now attempts to combine teams merely to maintain grade targets
		for (int i = 0; i < teams.size() - 2; i++)
		{
			if (teams.size() <= numTeams)
			{
				break;
			}
			
			Team t1 = teams.get(i);
			
			for (int j = i + 1; j < teams.size() - 1; j++)
			{
				Team t2 = teams.get(j);
				
				if (t1.size() + t2.size() > maxPlayers)
				{
					break;
				}
				
				boolean canAdd = true;
				
				for (int grade = minGrade; grade <= maxGrade; grade++)
				{
					double playerTarget = (double)gradePlayerCounts[grade]/(double)numTeams;
					if (t1.numGrade(grade) + t2.numGrade(grade) > playerTarget)
					{
						canAdd = false;
						break;
					}
				}
				
				if (canAdd)
				{
					t1.addPlayers(t2);
					teams.remove(t2);
					break;
				}
			}
			
		}
		
		TeamSizeComparator comp = new TeamSizeComparator();
		//sorts the teams by size, then adds the smallest teams together
		while (teams.size() > numTeams)
		{
			teams.sort(comp);
			
			teams.get(0).addPlayers(teams.get(1));
			teams.remove(teams.get(1));
			
		}
		
		//the total number of players by skill and grade
		int[][] playersByGradeAndSkill = new int[NUM_GRADES][6];
		
		for (Player player : playersVec)
		{
			playersByGradeAndSkill[player.grade][player.skill]++;			
		}
		
		
		//sorts players by skill from lowest to highest
		//this makes searching for the next player faster
		playersVec.sort(new PlayerSkillComparator());
		
		//sorts teams largest to smallest
		//this causes bigger teams to have fewer of the best players
		teams.sort(new TeamSizeComparator());
		
		//Makes sure each team has the same number of players by skill and grade, if possible
		for (Team team : teams)
		{
			for (int grade = minGrade; grade <= maxGrade; grade++)
			{
				for (int skill = 1; skill <= 5; skill++)
				{
					if (playersByGradeAndSkillIdeal[grade][skill] < numTeams)
					{
						break;
					}
					
					int playerTarget = playersByGradeAndSkillIdeal[grade][skill] / numTeams;
					
					while (team.playersByGradeAndSkill[grade][skill] < playerTarget)
					{
						Player p = findPlayer(grade, skill, playersVec);
						
						if (p != null)
						{
							team.addPlayer(p);
							playersVec.remove(p);
						}
						else
						{
							break;
						}
					}
				}
			}
		}
		
		playersVec.sort(new PlayerReverseSkillComparator());
		
		//adds most of the remaining players to the teams
		int grade = minGrade;
		int iter = 0;
		while (playersVec.size() > numTeams)
		{
			//alternates between ascending and descending
			if (iter%2 == 0)
			{
				teams.sort(new TeamReverseSizeComparator());
			}
			else
			{
				teams.sort(new TeamSizeComparator());
			}
			
			//adds one player to each team
			for (Team team : teams)
			{
				double gradeTarget = (double)gradePlayerCounts[grade]/(double)numTeams;
				if (team.numGrade(grade) < gradeTarget && team.size() < maxPlayers - 1) //notice this is maxPlayers - 1
				{
					Player p = findPlayer(grade, playersVec);

					if (p != null)
					{
						team.addPlayer(p);
						playersVec.remove(p);
					}
					else if (grade < maxGrade)
					{
						grade++;
					}
					else
					{
						break;
					}
					
				}
				
			}
			iter++;
		}
		
		//add remaining players to the smallest/best teams
		int i = 0;
		while (playersVec.size() > 0)
		{
			teams.sort(new TeamAverageSkillComparator());
			teams.sort(new TeamSizeComparator());
			teams.get(i).addPlayer(playersVec.get(0));
			playersVec.remove(0);
		}
				
		
		//Prints out the finalized teams
//		i = 1;
//		for (Team team : teams)
//		{
//			System.out.println("Team " + i + ": ");
//			System.out.println(team.toStringAlt());
//			i++;
//		}
		
		PrintWriter out = null;
		try {
			out = new PrintWriter(new FileWriter(output));
		} 
		catch (IOException e)
		{
			System.out.println("Invalid output file: " + output);
			return;
		} 
		
		i = 1;
		for (Team team : teams)
		{
			out.println("Team " + i);
			out.println(team.toStringAlt());
			i++;
		}
		
		out.close();
		
		System.out.println("Complete");
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
	 * Returns a player from players with the appropriate school and grade
	 * @param grade the desired grade
	 * @param skill the desired skill
	 * @param players the list of players to choose from
	 * @return the appropriate Player
	 */
	static Player findPlayer(int grade, int skill, Vector<Player> players)
	{
		for (Player player : players)
		{
			if (player.grade == grade && player.skill == skill)
			{
				return player;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns a player from players with the appropriate school and grade
	 * @param grade the desired grade
	 * @param skill the desired skill
	 * @param players the list of players to choose from
	 * @return the appropriate Player
	 */
	static Player findPlayer(int grade, Vector<Player> players)
	{
		for (Player player : players)
		{
			if (player.grade == grade)
			{
				return player;
			}
		}
		
		return null;
	}
	
//	static Player getRandomPlayer(Vector<Player> players)
//	{
//		Random rnd = new Random(System.nanoTime());
//		
//		return players.get(rnd.nextInt(players.size()));
//	}
	
	/**
	 * 
	 * A Comparator for the Team class, based on team's total skill. Sorts from highest to lowest
	 *
	 */
	private static class TeamAverageSkillComparator implements Comparator<Team>
	{
		@Override
		public int compare(Team t1, Team t2)
		{
			double difference = t2.averageSkill() - t1.averageSkill();

			if (difference > 0)
			{
				return 1;
			}
			else if (difference < 0)
			{
				return -1;
			}
			else
			{
				return 0;
			}
		}
		
	}
	
	/**
	 * 
	 * A Comparator for the Team class, based on team's size
	 *
	 */
	private static class TeamSizeComparator implements Comparator<Team>
	{
		@Override
		public int compare(Team t1, Team t2)
		{
			return t1.size() - t2.size();
		}
		
	}
	
	/**
	 * 
	 * A Comparator for the Team class, based on team's size. Sorts from largest to smallest
	 *
	 */
	private static class TeamReverseSizeComparator implements Comparator<Team>
	{
		@Override
		public int compare(Team t1, Team t2)
		{
			return t2.size() - t1.size();
		}
		
	}
	
	/**
	 * 
	 * A comparator for the Player class, based on skill
	 *
	 */
	private static class PlayerSkillComparator implements Comparator<Player>
	{
		@Override
		public int compare(Player p1, Player p2)
		{
			return p1.skill - p2.skill;
		}
		
	}
	
	/**
	 * 
	 * A comparator for the Player class, based on skill. Sorts from highest to lowest
	 *
	 */
	private static class PlayerReverseSkillComparator implements Comparator<Player>
	{
		@Override
		public int compare(Player p1, Player p2)
		{
			return p2.skill - p1.skill;
		}
		
	}

}
