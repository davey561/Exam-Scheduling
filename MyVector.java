/**
  * MyVector class, repurposed and edited from Lab 4 on Sorting.
  * MyVector is a vector that can be sorted via an Comparator, allowing a method of comparison among the vector's elements that is different than the method of comparison built into that element's class (a comparable type of sorting)
  */
import structure5.*;
import java.util.Comparator;
public class MyVector<E> extends Vector<E> {
  /**
    * Constructor for MyVector
    */
  public MyVector() {
    super();
  }
  /**
    * Sorts the vector's contents using the given comparator c
    * It makes use of a Selection sort algorithm, building a sorted portion of the vector on the right, iteratively swapping the relative-maximum element in unsorted (left) section and swapping with the rightmost element of the unsorted section.
    * @param c the given Comparator, which provides the method for comparing any two elements of this
    * pre: c.sort() must return an number
    * post: this vector will be sorted
    */
  public void sort(Comparator<E> c) {
    int numUnsorted = size(); //keeping track of the number unsorted
    int index; //index of last sorted member
    int maxIndex; //
    //while there is still a section of unsorted elements
    while (numUnsorted > 0) {
      maxIndex = 0;
      //Find the index of the maximum element in the not yet sorted section
      //for each element in the vector after the given index
      for (index = 1; index < numUnsorted; index++) {
        //if the current one is greater than the previously stored index
        if (c.compare(this.get(maxIndex), this.get(index)) <0) {
            maxIndex = index; //record the new max index
        }
      }
      this.swap(maxIndex,numUnsorted-1); //swap the maximum index with the rightmost unsorted element
      numUnsorted--; //one more element has now been sorted
    }
  }
  /**
    * Swaps one element with another, exchanging their indices.
    * @param indexA the index of the first element
    * @param indexB the index of the second element
    * pre: both indices must be in range of the vector, i.e. 0 thru size-1
    * post: the vector will now have the two elements in swapped positions.
    */
  public void swap(int indexA, int indexB){
    E temp = this.get(indexA);
    this.setElementAt(this.get(indexB), indexA);
    this.setElementAt(temp, indexB);
  }
  /**
    * Prints a string representation of this array
    */
  public void print(){
    System.out.println(this.toString());
  }
  /**
    * Prints a part of this vector, used when MyVector is super long
    */
  public void printPart(int numberCharacters, String label){
    System.out.println(label);
    System.out.println(this.toString().substring(0,numberCharacters));
  }
}
