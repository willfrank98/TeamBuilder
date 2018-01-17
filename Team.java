import java.util.Vector;

public class Team {
	//private String coach; //not currently relevant
	public Vector<Player> players;
	int[] numGrade;
	int[] skillGrade;
	
	public Team()
	{
		players = new Vector<Player>();
	}
	
	public boolean addPlayer(Player p)
	{
		return players.add(p);
	}
	
	public int size()
	{
		return players.size();
	}

}
