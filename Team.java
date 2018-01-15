import java.util.Vector;

public class Team {
	private String coach;
	public Vector<Player> players;
	//public Vector<Integer> avgSkillByGrade;
	
	public Team()
	{
		
	}
	
	public boolean addPlayer(Player p)
	{
		return players.add(p);
	}

}
