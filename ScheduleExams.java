/**
  * ScheduleExams is the primary class for scheduling the input students exams so that 1) there are no conflicts and 2) no two exam-slots could be combined without conflicts.
  * I am the sole author of the work in this repository.
  * Extra features. The first two are the first two that are recommended. The third is my own idea.
  * 1) Exam slots are printed in alphabetical order based on the first class name in each slot. E.g., Slot 1: ARTH 101 ..., Slot 2: BIOL 202..., ... Slot N:
  * 2) Each student's exam schedules (set of four slot numbers) is printed.
  * 3) Study groups
  */

import java.util.Scanner;
import java.util.Iterator;
import structure5.*;
public class ScheduleExams{
  /**
    * Parses the student/class file, and generates exam schedule
    * @param args the lines of text in the input file, should be input in command line via: java ScheduleExams < [filename]
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

    //Parse input into a unit for the student and their classes
    int linenum = 0; //the number of the line being processed
    int modfive; //linenum mod five, keeps track of relative position within each student's data set
    Edge<String, Integer> tempedge; //temporary edge
    // //while there is a next line to process
    Assert.pre(scan.hasNextLine(), "No student data given.");
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
      //  System.out.println("added " +temp_classes[modfive-1]+ "to class array at index " + (modfive-1));
        //if the last class for this student has been read in, load each student's classes into the graph, and generate new edges if they don't already exist
        if(modfive==4){ //4 indicates that the loop is on the last class for this student
          //System.out.println("modfive is four");
          //also store this student's record in a vector of students
          students.add(new Student(temp_studentname, temp_classes.clone()));
          //System.out.println("49: " + students);

          //add all classes not already in class
          for(int i = 0; i<temp_classes.length; i++){
            //System.out.println("adding "+ temp_classes[0] + "to the graph");
            conflictGraph.add(temp_classes[i]); //should not produce duplicate, as vertices are a set, not a list
          }
          //add edge between each of these class vertices
          for(int i = 0; i<4; i++){
            for(int j = 0; j<i; j++){
              //System.out.println("looking at class " + temp_classes[i]);
              tempedge = conflictGraph.getEdge(temp_classes[i], temp_classes[j]);
              if(tempedge==null){
              //  System.out.println("edge between these classes (" + temp_classes[i]+ ", " + temp_classes[j] + ") doesn't yet exist");
                conflictGraph.addEdge(temp_classes[i], temp_classes[j], 1);
              }
              else{
              //  System.out.println("edge already exists between these two classes (" + temp_classes[i]+ ", " + temp_classes[j] + ")");
                tempedge.setLabel(tempedge.label()+1); //increment label (weight) by 1
              }
            }
          }
        }
      }
      linenum++;
    }

      Iterator<String> it;

      // System.out.println(conflictGraph.toString());
      // System.out.println("Edge count: " + conflictGraph.edgeCount());
      // it = conflictGraph.neighbors("CSCI 136");
      // while(it.hasNext()){
      //   System.out.println(it.next().toString());
      // }

    //Generate slots and exam times by iterating through the graph
    MyVector <BinarySearchTree<String>> slots = new MyVector<BinarySearchTree<String>>();
    BinarySearchTree<String> temp_slot;
    Iterator<String> neighbors;
    boolean first_iteration = true; //whether this is the first iteration
    String temp_class;
    String current_class;
    String neighbor;

    //print out number of students taking each course
    //print out how many students are taking each class from the graph
    System.out.println("number of students taking each course");
    it = conflictGraph.iterator();
    String course;
    neighbor = "";
    double count;
    while(it.hasNext()){
      count = 0;
      course = it.next();
      neighbors = conflictGraph.neighbors(course);
      while(neighbors.hasNext()){
        neighbor = neighbors.next();
        count += conflictGraph.getEdge(course, neighbor).label();
      }
      System.out.println(course + " has " + (count/3) + " students.");
    }

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
    Alphabetize alphabetical = new Alphabetize();
    slots.sort(alphabetical);
    //slots.print();
    //System.out.println(slots.toString()); --this looks like crap, make a nice string representation of the slots below
    //for each slot
    System.out.println("\nSTUDENT INFO");
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

    //for a given student, print out their exam schedule
    System.out.print("\nSTUDENT SCHEDULES (Slot #'s'):");
    Student davey;
    for(int stud = 0; stud<students.size(); stud++){
      davey = students.get(stud);
      System.out.print("\n" + davey.getName() + ": ");
      //for each slot
      for(int slot = 0; slot<slots.size(); slot++) {
        //for each class
        for(int i = 0; i<davey.getClasses().length; i++) {
          //if this slot contains that class
        //  System.out.print("\nDoes slot " + (slot+1) + " contain " + davey.getClasses()[i] + "?");
          if(slots.get(slot).contains(davey.getClasses()[i])) {
            System.out.print((slot+1) + " "); //prints out slot number
          }
        }
      }
    } System.out.println();

    //My extra feature: putting students in Study Groups with given size (4).
    //Not ordinary study groups though, but study groups where students' courses overlap as much as possible with their group-members. It's as if each group had to stay in a single room together during all of reading period--in this situation, which groupings would be best? (I'm selecting this problem cuz I think it will be cool mathematically and challenging, not cuz it's realistic.)
      //the metric for a good division into study groups is: minimizing average number of classes total taken by members of a group. i.e. maximizing overlap.
      //I'm going to find an approximation of optimal solution, a solution. The algorithm might take a while. I also expect that the quality of groups it constructs will degrade as goes on.
      //Doing so will take two steps, and both will require initially the construction of a new type of graph to represent the students and edges (both as nodes; this is step 0).
        //0) Construct a bipartite graph where, initially there exists an edge between a student and a class when that student is taking that class.
        // 1) Count the number of classes in common between all pairs of students. Do this by revising the graph in the following way. Iterate through each of the classes. For a given class, all of its neighbors are students. Draw an edge between each of these students if it doesn't exist already, and increment the edge's label by one to represent the number of classes every two students share in common. Delete the course node afterward, then move on to the next course. Will be left with graph of students connected to each other when they overlap in classes.
        // 2) Start with a random student. Find the three other students that she shares the most classes with. Group them, remove them all from the graph, then look to another random student. Continue this process until there are less than four students left in the graph. Don't care about them, can put them in a wack group.

      //Step 0: Construct Initial Graph
        //not dense enough to use graphmatrix, I think.
      Graph<String,Integer> overlap = new GraphListUndirected<String,Integer>();
      Student temp;
      temp_classes = null;
      for(int i = 0; i<students.size(); i++){
        temp = students.get(i);
        overlap.add(temp.getName());
        overlap.visit(temp.getName()); //mark as visited if its a student

        //Add all classes.
        temp_classes = temp.getClasses();
        //for each class of given student
        for(int j = 0; j<temp_classes.length; j++){
          overlap.add(temp_classes[j]);
          overlap.addEdge(temp.getName(), temp_classes[j], 0); //the value of edge doesn't matter here
          //System.out.println("Just added edge between " + temp.getName() + " and " + temp_classes[j]);
        }
      }
      //System.out.println(overlap.toString());

      //Step 1: Draw edge between each pair of students with overlap, set label of edge to number of classes they have in common. Delete class nodes from overlap.
      //iterate through each course in graph
      it = overlap.iterator();
      course = null;
      String student1;
      String student2;
      Iterator<String> students1;
      Iterator<String> students2;
      Edge<String,Integer> temp_edge;
      while(it.hasNext()){
        course = it.next();
        //if this is not a student, but a course
        if(!overlap.isVisited(course)){
          //Draw edge between all pairs of students taking this course.
            //there's gonna be double repetition, but don't know how else.
          students1 = overlap.neighbors(course);
          students2 = overlap.neighbors(course);
          while(students1.hasNext()){
            student1 = students1.next();
            while(students2.hasNext()){
              student2 = students2.next();
              if(!student1.equals(student2)){
                //Add edge between these two students if it doesn't already exist.
                if(overlap.getEdge(student1,student2)==null) overlap.addEdge(student1, student2, 0);
                temp_edge = overlap.getEdge(student1, student2); //store this edge
                temp_edge.setLabel(temp_edge.label()+1); //increment number of classes in common between these two students
              }
            }
          }
          overlap.remove(course);
        }
      }
      //System.out.println(overlap);

      //Step 2 (final step): Divide students into groups. Use recursive helper method in this class.
      overlap.reset();
      int groupsize = 2;
      MyVector<MyVector<String>> groups = new MyVector<MyVector<String>>(); //each inner MyVector has the four students in a group
      Vector<Integer> forMeanOverlap = new Vector<Integer>();
      //for each student:
      it = overlap.iterator();
      student1 = student2 = neighbor = null;
      Association<String, Integer> max_neighbor;
      neighbors = null;
      int remaining = groupsize;
      MyVector<String> group;
      int num_courses;
      while(it.hasNext()){
        remaining = groupsize;
        group = new MyVector<String>();
        student1 = it.next();
        group = new MyVector<String>();
        if(overlap.isVisited(student1)) continue;
        group.add(student1);
        overlap.visit(student1);
        remaining--;
        //find other students with highest overlap with student1
        while(remaining>0){
          max_neighbor = null;
          neighbors = overlap.neighbors(student1); //iterate over neighbors
          while(neighbors.hasNext()){
            neighbor = neighbors.next();
            if (overlap.isVisited(neighbor)) continue;
            //if no max_neighbor yet or if this neighbor has greater overlap than seen yet, record new association
            if(max_neighbor==null || max_neighbor.getValue()<overlap.getEdge(student1, neighbor).label()){
              max_neighbor = new Association<String, Integer>(neighbor, overlap.getEdge(student1, neighbor).label());
            }
          }
          if(max_neighbor==null) break; //if there are no more neighbors left
          group.add(max_neighbor.getKey());
          overlap.visit(max_neighbor.getKey());
          remaining--;
        }
        overlap.remove(student1);
        //if group has one student, discard, else, add.
        if(group.size()>1) groups.add(group);
      }
      System.out.print("\nSTUDY GROUPS (of size " + groupsize + "):");
      for(int i = 0; i<groups.size();i++){
        System.out.print("\nGroup #" + i + ": ");
        for(int j = 0; j<groups.get(i).size(); j++){
          System.out.print(groups.get(i).get(j) + " ");
        }
      }
      System.out.println();

  }
}
