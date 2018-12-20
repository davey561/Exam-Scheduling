import java.util.Scanner;
import java.util.Iterator;
import structure5.*;
/**
  * ScheduleExams is the primary class for scheduling the input students exams so that 1) there are no conflicts and 2) no two exam-slots could be combined without conflicts.
  * I am the sole author of the work in this repository.
  * Extra features. The very first is a practically meaningless, but a somewhat interesting exercise. The first two are the first two that are recommended. The third is my own idea.
  * 0) Prints out the number of students taking each course. This would be easy if based on the file input or the subsequently built students vector, but I thought it'd be interesting to derive each course size from the graph.
  * 1) Exam slots are printed in alphabetical order based on the first class name in each slot. E.g., Slot 1: ARTH 101 ..., Slot 2: BIOL 202..., ... Slot N:
  * 2) Each student's exam schedules (set of four slot numbers) is printed.
  * 3) Study groups. I made a MakeStudyGroups class to deal with this part. This is the feature I spent the most time on.
  */
public class ScheduleExams{
  /**
    * Parses the student/class file, and generates exam schedule
    * @param args the lines of text in the input file, should be input in command line via: java ScheduleExams < [filename]
    */


/* $
  This main method is cumbersome, would've been nice to separate out into
  several helper methods
  (-1)

*/

  public static void main(String[] args) {
    //Declarations
    Scanner scan = new Scanner(System.in);
    String templine; //temporary line to be read in from file, consists of either student or course name
    String temp_studentname= null; //temporarily store's student_name as parses the file
    String [] temp_classes = new String [4]; //temporarily stores the given student's classes as it parses the file

    /* Graph stores information about which classes are being taken by at least one same student. Class names are vertices; an edge between two classes indicates that there is a student enrolled in both. (The integer label of that edge is the number of students enrolled in both.)*/
    Graph<String,Integer> conflictGraph = new GraphListUndirected <String,Integer>();
    MyVector<Student> students = new MyVector<Student>(); //stores data in the file, so that even when graph is destroyed, data is preserved

    //BUILD THE CONFLICT GRAPH
    //Parse input into a unit for the student and their classes
    int linenum = 0; //the number of the line being processed
    int modfive; //linenum mod five, keeps track of relative position within each student's data set
    Edge<String, Integer> tempedge; //temporary edge

    Assert.pre(scan.hasNextLine(), "No student data given.");
    // while there is a next line to process
    while (scan.hasNextLine()) {
      modfive = linenum%5; //keeps track of relative position within this student's lines of info
      templine = scan.nextLine(); //store this line
      //if this is the first of the student's five lines, it's their name
      if(modfive==0){
        temp_studentname = templine;
      }
      //otherwise, it's a class name
      else{
        temp_classes[modfive-1] = templine;
        //if the last class for this student has been read in, load each student's classes into the graph, and generate new edges if they don't already exist
        if(modfive==4){ //4 indicates that the loop is on the last class for this student
          //also store this student's record in a vector of students
          students.add(new Student(temp_studentname, temp_classes.clone()));
          //System.out.println("49: " + students);

          //add all classes not already in class
          for(int i = 0; i<temp_classes.length; i++){
            conflictGraph.add(temp_classes[i]); //should not produce duplicate, as vertices are a set, not a list
          }
          //add edge between each of these class vertices
          for(int i = 0; i<4; i++){
            for(int j = 0; j<i; j++){
              tempedge = conflictGraph.getEdge(temp_classes[i], temp_classes[j]);
              if(tempedge==null){
                conflictGraph.addEdge(temp_classes[i], temp_classes[j], 1);
              }
              else{
                tempedge.setLabel(tempedge.label()+1); //increment label (weight) by 1
              }
            }
          }
        }
      }
      linenum++;
    }

    //Some new declarations
      //Iterators:
    Iterator<String> it = conflictGraph.iterator();
    Iterator<String> neighbors;
      //Time slots:
    MyVector <BinarySearchTree<String>> slots = new MyVector<BinarySearchTree<String>>();
    BinarySearchTree<String> temp_slot;
    boolean first_iteration = true; //whether this is the first iteration
    String temp_class;
    String current_class;
    String neighbor = "";
    String course;
    double count;

    //PRINT OUT HOW MANY STUDENTS ARE TAKING EACH COURSE
    System.out.println("\nCOURSE SIZES:");
    //for each course
    while(it.hasNext()){
      count = 0;
      course = it.next();
      neighbors = conflictGraph.neighbors(course);
      while(neighbors.hasNext()){
        neighbor = neighbors.next();
        count += conflictGraph.getEdge(course, neighbor).label(); //increment by the number of students represented by each edge
      }
      //count will overcount the number of students in the given course by a factor of three, given that, for each student, three edges are drawn from it (to the student's other three courses)
      System.out.println(course + " has " + (count/3) + " students.");
    }

    //GENERATE SLOTS by iterating through the graph
    //while there are still nodes left in the graph
    while(conflictGraph.size()>0){
      slots.add(new BinarySearchTree<String>()); //initialize this slot
      temp_slot = slots.lastElement(); //get reference to this slot
      //Iterate through all other vertices in the graph. if another is not a neighbor of the given one (or any others that have been added to the temp_slot list in the meantime), add it also to temp_slot
      it = conflictGraph.iterator(); //repurpose iterator
      boolean isNeighbor; //keeps track of whether class being looked at is a neighbor
      //for each vertex in the graph
      while(it.hasNext()){
        current_class = it.next();
        //if this is the first iteration, pick the first class to add to slot
        if(first_iteration){
          temp_slot.add(current_class);
          //conflictGraph.remove(current_class); //remove it from the graph
          first_iteration=false;
          continue;
        }

        //Check whether current vertex  is neighbor of any of vertices already in slot
          //To do so, iterate through neighbors of current vertex, checking whether each is contained in slot
        isNeighbor = false; //keeps track of whether current vertex is a neighbor of any in the slot already
        neighbors = conflictGraph.neighbors(current_class); //initialize iterator for neighbors of current vertex
        //while there is a another neighbor of current vertex
        while(neighbors.hasNext()){
          neighbor = neighbors.next();
          //System.out.println("current neighbor: " + neighbor);
          //if that neighbor is in slot already, meaning that current vertex  is a neighbor of one of vertices in the slot already:
          if(temp_slot.contains(neighbor)){
            //System.out.println("contains "  + neighbor);
            isNeighbor = true; //record that current vertex is a neighbor (and therefore should not be added to the same slot)
            temp_class = neighbor; //record this class, so at the end of iterations of 'it', the next slot has a vertex with which to be initialized
          }
        }
        //if this node is not connected to any others, then add it to the slot
        if(!isNeighbor){
          temp_slot.add(current_class); //add it to the current slot
          //conflictGraph.remove(current_class); //remove it from the graph
        }
      }
      //conflictGraph.remove(first_class);
      it = temp_slot.iterator();
      while(it.hasNext()){
        conflictGraph.remove(it.next());
      }
    }

    //PRINT OUT THE LIST OF STUDENTS AND SLOTS
    Alphabetize alphabetical = new Alphabetize();
    slots.sort(alphabetical);
    //slots.print();
    //System.out.println(slots.toString()); --this looks messy, make a nice string representation of the slots below
    //for each slot
    System.out.println("\nSTUDENT INFO:");
    SortByName byName = new SortByName();
    students.sort(byName);
    System.out.println(students);

    System.out.print("\nEXAM SCHEDULES (By Slot):");
    for(int i = 0; i<slots.size(); i++){
      System.out.print("\nSlot " + (i+1) + ": ");
      it = slots.get(i).iterator();
      while(it.hasNext()){
        System.out.print(it.next() + "  ");
      }
    }
    System.out.println();

    //PRINT OUT EACH STUDENT'S EXAM SCHEDULE
    System.out.print("\nSTUDENT SCHEDULES (Slot #'s'):");
    Student davey;
    //for each student
    for(int stud = 0; stud<students.size(); stud++){
      davey = students.get(stud);
      System.out.print("\n" + davey.getName() + ": ");
      //for each slot
      for(int slot = 0; slot<slots.size(); slot++) {
        //for each class
        for(int i = 0; i<davey.getClasses().length; i++) {
          //if this slot contains that class
          if(slots.get(slot).contains(davey.getClasses()[i])) {
            System.out.print((slot+1) + " "); //prints out slot number
          }
        }
      }
    } System.out.println();

    //MAKE STUDY GROUPS. Divides the students into groups with as much overlap as possible with each other.
    MakeStudyGroups maker = new MakeStudyGroups(students);
    maker.makeStudyGroups(); //all code is in MakeStudyGroups class
  }
}
