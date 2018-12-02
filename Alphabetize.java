import java.util.Comparator;
import structure5.*;
import java.util.Iterator;
  /**
    * Compares BinarySearchTree<String>'s by their first element. This Comparator is primarily used by the MyVector slots in the ScheduleExams class, so that the exam slots can be ordered alphabetically.
    */
 public class Alphabetize implements Comparator<BinarySearchTree<String>>{
  /**
    * compares two slots
    * @param a the first given slot
    * @param b the second given slot
    * pre: both a and b must have first elements
    * post: compares the first elements of a and break;
    * @return an integer i. if negative, a comes before b.
    */
  public int compare (BinarySearchTree<String> a, BinarySearchTree<String> b){
    //Note: the only way to access the first element of a BinarySearchTree is to call the iterator. Inefficient, but I don't know how else to get the lowest valued element.
    return (a.iterator().next()).compareTo(b.iterator().next());
  }
 }
