/**
  * Repurposed from Lab 4 in order to sort a vector of students alphabetically
  */
import java.util.Comparator;
 public class SortByName implements Comparator<Student>{
  public int compare (Student a, Student b){
    return a.getName().compareTo(b.getName());
  }
 }
