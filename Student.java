
/**
	* Student, repurposed from lab 4 and edited to now have an associated array of classes
	*/
import structure5.*;
public class Student {
	private String name, address;
	private long homePhone, campusPhone;
	private int boxSU;
	String [] classes;
//$This class needs documentation (-1) It needs comments and an explanation of its purpose
	public Student(String name, String address, long homePhone, long campusPhone, int boxSU) {
		this.name=name;
		this.address=address;
		this.homePhone=homePhone;
		this.campusPhone=campusPhone;
		this.boxSU=boxSU;
	}
	/**
		* New constructor, made for needs of lab 10 exam scheduling
		*/
	public Student(String name, String [] classes){
		this.name = name;
		Assert.pre(classes.length ==4, "Student is taking a number of classes other than 4.");
		this.classes = classes;
	}

	// Be sure to include a toString method here.
	// public String toString() {
	// 	return (name + ",\n" + address + ",\n" + homePhone + ", " + campusPhone + ", " + boxSU+ ". \n");
	// }
	public String toString() {
		String result =  name + ": ";
		for(String s: classes){
			result+= s + " ";
		}
		result+= "\n";
		return result;
	}

	public String getName(){
		return this.name;
	}
	public int getBox(){
		return this.boxSU;
	}
	public long getHomePhone(){
		return this.homePhone;
	}
	public String getAddress(){
		return this.address;
	}
	public String [] getClasses(){
		return classes;
	}
}
