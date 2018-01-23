import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class Team
{
	
	//private String coach; //not currently relevant
	public Vector<Player> players;
	int[] numGrade; //the number of players in each grade
	int[] skillGrade; //the TOTAL skill of each grade
	Hashtable<String, Integer> schools;
	
	public Team()
	{
		players = new Vector<Player>();
		
		numGrade = new int[12];
		skillGrade = new int[12];
		schools = new Hashtable<String, Integer>();
	}
	
	/**
	 * Adds a player to the team, and updates team statistics;
	 * @param p the player to add
	 */
	public void addPlayer(Player p)
	{
		players.add(p);
		numGrade[p.grade]++;
		skillGrade[p.grade] += p.skill;
		
		if (!schools.containsKey(p.school))
		{
			schools.put(p.school, 1);
		}
		else
		{
			int n = schools.get(p.school);
			schools.put(p.school, n+1);
		}
	}
	
	/**
	 * Adds all players on team t to this team and updates team statistics
	 * @param t The team of players to add to this one
	 */
	public void addPlayers(Team t)
	{
		for (Player p : t.players) 
		{
			this.addPlayer(p);
		}
	}
	
	/**
	 * Returns the number of players on the team
	 * @return the size of the team
	 */
	public int size()
	{
		return players.size();
	}
	
	/**
	 * Gives the number of players from a specified school that are on this team
	 * @param school the school to search for
	 * @return the number of players from school
	 */
	public int schoolNum(String school)
	{
		if (schools.containsKey(school))
		{
			return schools.get(school);
		}
		else
		{
			return 0;
		}
	}
	
	/**
	 * Returns the grade with the most players on the team, or the lowest grade among a tie, or 0 if team is empty
	 * @return The largest grade on the team
	 */
	public int largestGrade()
	{
		int grade = 0;
		
		for (int i = 0; i < numGrade.length; i++) 
		{
			if (numGrade[i] > numGrade[grade])
			{
				grade = i;
			}
		}
		
		return grade;
	}
	
	/**
	 * Returns the school with the most players on the team, or the first school found among a tie
	 * @return
	 */
	public String largestSchool()
	{
		String school = "";
		
		for (Enumeration<String> e = schools.keys(); e.hasMoreElements();) //iterates through each school on a team
		{
			String next = e.nextElement();
			
			if (schools.get(next) > (Integer)schools.get(school)) //the cast is incase .get returns null
			{
				school = next;
			}
		
		}
		
		return school;
	}
	
	
	/**
	 * @return The total skill of a team. The sum of all individual player's skill
	 */
	public int totalSKill()
	{
		int totalSkill = 0;
		
		for (int i = 0; i < skillGrade.length; i++)
		{
			totalSkill += skillGrade[i];
		}
		
		return totalSkill;
	}
	
	
	@Override
	public String toString()
	{
		return ("largest grade: " + largestGrade() + ", largest school: " + largestSchool() + ", # players: " + size());
	}

}
