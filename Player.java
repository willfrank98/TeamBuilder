
public class Player {
	
	public String name;
	public int grade;
	public int skill;
	public String friendRequest;
	public String school;
	
	
	public Player()
	{
		
	}
	
	public Player(String name, int grade, int skill, String friendRequest, String school)
	{
		this.name = name;
		this.grade = grade;
		this.skill = skill;
		this.friendRequest = friendRequest;
		this.school = school;
	}
	
	@Override
	public String toString()
	{
		return (this.name + "," + this.grade + "," + this.skill + "," + this.friendRequest + "," + this.school);
	}

}
