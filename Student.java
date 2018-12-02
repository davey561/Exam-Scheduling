
/**
	* Student, repurposed from lab 4 and edited to now have an associated array of classes
	*/
import structure5.*;
public class Student {
	private String name, address;
	private long homePhone, campusPhone;
	private int boxSU;
	String [] classes;
	/**
		* Constructor (for lab 4, not relevant to lab 10)
		* @param name name of the student
		* @param address address of the student
		* @param homePhone homephone of the given student
		* @param campusPhone campusphone number
		* @param boxSU su box of the student
		*/
	public Student(String name, String address, long homePhone, long campusPhone, int boxSU) {
		this.name=name;
		this.address=address;
		this.homePhone=homePhone;
		this.campusPhone=campusPhone;
		this.boxSU=boxSU;
	}
	/**
		* New constructor, made for needs of lab 10 exam scheduling
		* @param name the name of the student
		* @param classes *the four classes the student is taking
		* pre: classes must be of length 4
		* post: constructs student for lab 10 purposes
		*/
	public Student(String name, String [] classes){
		this.name = name;
		Assert.pre(classes.length ==4, "Student is taking a number of classes other than 4.");
		this.classes = classes;
	}

	//Commented out the toString() method below so that I could code a more relevant one for Lab 10.
	// public String toString() {
	// 	return (name + ",\n" + address + ",\n" + homePhone + ", " + campusPhone + ", " + boxSU+ ". \n");
	// }
	/**
		* Gives string representation of the current student
		* pre: needs to have been constructed using lab 10 constructor, not the one from lab 4
		*/
	public String toString() {
		String result =  name + ": ";
		for(String s: classes){
			result+= s + " ";
		}
		result+= "\n";
		return result;
	}
	/**
		* returns the name of the student
		*/
	public String getName(){
		return this.name;
	}
	/**
		* returns the su box number of the student
		*/
	public int getBox(){
		return this.boxSU;
	}
	/**
		* returns the home phone of the student
		*/
	public long getHomePhone(){
		return this.homePhone;
	}
	/**
		* returns the address of the student
		*/
	public String getAddress(){
		return this.address;
	}
	/**
		* returns the classes array of the student
		*/
	public String [] getClasses(){
		return classes;
	}
}
