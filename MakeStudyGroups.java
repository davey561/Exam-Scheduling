import structure5.*;
import java.util.Iterator;
/**
  *My extra feature: putting students in Study Groups with given size (4).
  //Not ordinary study groups though, but study groups where students' courses overlap as much as possible with their group-members. It's as if each group had to stay in a single room together during all of reading period--in this situation, which groupings would be best? (I'm selecting this problem cuz I think it will be cool mathematically and challenging, not cuz it's realistic.)
    //the metric for a good division into study groups is: minimizing average number of classes total taken by members of a group. i.e. maximizing overlap.
    //I'm going to find an approximation of optimal solution, a solution. The algorithm might take a while. I also expect that the quality of groups it constructs will degrade as goes on.
    //Doing so will take two steps, and both will require initially the construction of a new type of graph to represent the students and edges (both as nodes; this is step 0).
      //0) Construct a bipartite graph where, initially there exists an edge between a student and a class when that student is taking that class.
      // 1) Count the number of classes in common between all pairs of students. Do this by revising the graph in the following way. Iterate through each of the classes. For a given class, all of its neighbors are students. Draw an edge between each of these students if it doesn't exist already, and increment the edge's label by one to represent the number of classes every two students share in common. Delete the course node afterward, then move on to the next course. Will be left with graph of students connected to each other when they overlap in classes.
      // 2) Start with a random student. Find the three other students that she shares the most classes with. Group them, remove them all from the graph, then look to another random student. Continue this process until there are less than four students left in the graph. Don't care about them, can put them in a wack group.
  */
public class MakeStudyGroups{
  MyVector<Student> students; //record of all students and their courses
  GraphListUndirected<String, Integer> overlap; //the graph to be constructed in order to do study group calculations
  MyVector<MyVector<String>> groups; //will store study groups

  //Temporary variables; only used in calculation process
  Iterator<String> it; //iterator (generally used to iterate through each vertex in the graph)
  Iterator<String> students1; //another vertex iterator, for all neighbors of a given student
  Iterator<String> students2; //for all neighbors of another given student
  Iterator <String> neighbors; //another iterator
  String course; //stores a course name temporarily
  String student1;
  String student2;
  String neighbor;
  Edge<String,Integer> temp_edge; //stores an edge between two students temporarily
  String [] temp_classes; //stores the list of classes for a given student
  int groupsize; //the number of students maximum in a group
  public MakeStudyGroups(MyVector<Student> students){
    this.students = students;
    this.groups = new MyVector<MyVector<String>>();
    this.overlap = new GraphListUndirected<String, Integer>();
    this.groupsize = (int) Math.pow(students.size(), .3); //maximum group size, should be set to reasonable value for each data set
      //this formula gives size 9 for large.txt, 2 for medium.txt
  }
  /**
    * Runs every function in this class in order in order to make and print out the study groups found
    * post: makes and prints out the study groups found
    */
  public void makeStudyGroups(){
    if(groupsPossible()){
      studentCourseGraph();
      overlapGraph();
      makeGroupsFromOverlapGraph();
      printGroups();
    }
  }
  /**
    * Determines whether the number of students is too small to split them into groups. Basically just rules out dividing students into groups from "small.txt.""
    * post: returns whether it is study groups should be made
    */
  public boolean groupsPossible(){
    //if there are less than eight students, not possible
    if(students.size()<8){
      return false;
    }
    return true; //otherwise, let's divvy em up!
  }
  /**
    * This is the initial step (Step 0 as represented in class description above) to forming groups with high overlap. It constructs a bipartite graph, where students and courses are vertices, and all students are connected to each of their four courses.
    * pre: groupPossible() is true
    * post: initializes this object's overlap graph with the students' information
    * @return this.overlap, the initialized graph
    */
  public GraphListUndirected<String, Integer> studentCourseGraph(){
    Student temp;
    temp_classes = null;
    //for each student
    for(int i = 0; i<students.size(); i++){
      temp = students.get(i);
      overlap.add(temp.getName()); //add this student to the graph
      overlap.visit(temp.getName()); //mark as visited to distinguish student from course

      //Add all classes to graph, and edge between student and them
      temp_classes = temp.getClasses();
      //for each class of given student
      for(int j = 0; j<temp_classes.length; j++){
        overlap.add(temp_classes[j]);
        overlap.addEdge(temp.getName(), temp_classes[j], 0); //the value of edge doesn't matter here
      }
    }
    return overlap;
  }
  /**
    * Makes the overlap graph from the student-course graph. This new graph will consist of only students, and edges between them if they share classes in common. The label of the edge (an int) will denote how many classes they share.
    * pre: studentCourseGraph() function has already been run
    * post: updates this.overlap to be a graph with information about all the overlap
    * @return this.overlap, as updated.
    */
  public GraphListUndirected<String, Integer> overlapGraph(){
    //Step 1: Draw edge between each pair of students with overlap, set label of edge to number of classes they have in common. Delete class nodes from overlap.
    //iterate through each course in graph
    it = overlap.iterator();
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
    return overlap;
  }
  /**
    * From the overlap graph, finds a set of pretty good study groups, where students overlap a bunch
    * pre: groupsPossible()==true, and studentCourseGraph() and overlapGraph() should have already been run, in that order
    * post: will make the groups, and update this object's groups instance variable accordingly
    * @return the groups made
    */
  public MyVector<MyVector<String>> makeGroupsFromOverlapGraph(){
    //Step 2 (final step): Divide students into groups. Use recursive helper method in this class.
    overlap.reset();
    it = overlap.iterator();
    student1 = student2 = neighbor = null;
    Association<String, Integer> max_neighbor; //represents the neighbor with the maximum overlap. the key is the neighbor's name, the string is the overlap
    int remaining = groupsize;
    MyVector<String> group;
    //for each student:
    while(it.hasNext()){
      group = new MyVector<String>();
      remaining = groupsize; //the number of students still needing to be added to this study group is the total number of students to go in that group (given that there are none in it so far)
      student1 = it.next();
      //if this student has already been added to a group (indicated by whether visited), shift focus to next student
      if(overlap.isVisited(student1)) continue;
      group.add(student1); //otherwise, add this student to the group as the first student
      overlap.visit(student1); //given that they've been added, mark them as visited
      remaining--; //now there is one less student remaining to be added
      //Find other students with highest overlap with student1
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
        if(max_neighbor==null) break; //if there are no more neighbors left, stop looking
        group.add(max_neighbor.getKey());
        overlap.visit(max_neighbor.getKey());
        remaining--;
      }
      overlap.remove(student1);
      //if group has one student, discard (those students can study alone I guess :( )), else, add.
      if(group.size()>1) groups.add(group);
    }
    return groups;
  }
  /**
    * Prints the study groups after they've been made
    * pre: all functions above have already been run, i.e. groups have been made already
    * post: prints out the groups nicely
    */
  public void printGroups(){
    //Finally, print out each study group
    System.out.print("\nSTUDY GROUPS (of size " + groupsize + ") (extra):");
    //for every study group
    for(int i = 0; i<groups.size();i++){
      System.out.print("\nGroup #" + i + ": ");
      for(int j = 0; j<groups.get(i).size(); j++){
        System.out.print(groups.get(i).get(j) + " ");
      }
    }
    System.out.println();
  }
}
