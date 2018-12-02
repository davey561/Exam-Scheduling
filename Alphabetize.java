import java.util.Comparator;
import structure5.*;
import java.util.Iterator;
 public class Alphabetize implements Comparator<BinarySearchTree<String>>{
  public int compare (BinarySearchTree<String> a, BinarySearchTree<String> b){
    //System.out.println("Comparing " + a.iterator().next() + " and " + b.iterator().next() + ": " + (a.iterator().next()).compareTo(b.iterator().next()));
    return (a.iterator().next()).compareTo(b.iterator().next());
  }
 }
