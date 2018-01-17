
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
	
	public String toString()
	{
		return (this.name + ", " + this.grade + ", " + this.skill + ", " + this.friendRequest + ", " + this.school);
	}

//	public String getName() {
//		return _name;
//	}
//	
//	public void setName(String _name) {
//		this._name = _name;
//	}
//	
//	public int getGrade() {
//		return _grade;
//	}
//
//	public void setGrade(int grade) {
//		this._grade = grade;
//	}
//
//	public int getSkill() {
//		return _skill;
//	}
//
//	public void setSkill(int _skill) {
//		this._skill = _skill;
//	}
//
//	public String getFriendRequest() {
//		return _friendRequest;
//	}
//
//	public void setFriendRequest(String _friendRequest) {
//		this._friendRequest = _friendRequest;
//	}
//
//	public String getSchool() {
//		return _school;
//	}
//
//	public void setSchool(String _school) {
//		this._school = _school;
//	}


}
