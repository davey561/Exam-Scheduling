# Lab 10 High-level Feedback
 * Completed 3 extensions:
   * printing out a schedule for each individual student in alphabetical order.
   * printed out the number of students enrolled in each course.
   * Students are assigned to study groups based on common classes they share.
 * One point is taken off because of an overly long main method in the main Scheduler class that should have been broken up into helper methods.
 * Other than that an amazing job implementing and applying learned concepts!

__Lab 10 Score: 19 + 2 = 21/20__



# Lab 10: Exam Scheduling

### Description of My Code in the Repository
- *ScheduleExams*: The primary class in the repository. Designs an exam schedule for students that no student has more than one exam during the same time slot and no new time slots could be merged without new conflicts arising. ScheduleExams has a few extra features as well:
  - Sorts the time slots alphabetically by name of the first course in each.
  - Prints out an exam schedule for each of the students.
  - Divides students into study groups, as if they were permanent for all of reading period. I think this feature is a form of 'clustering'. Not all students get a study group. Uses class listed below to do so.
- *MakeStudyGroups*: stores the student-course information in graphs to determine groups of four students with a lot of overlap. Doesn't make a group for every student (as there comes a point after many groups have been made where there are some students that no longer share classes with each other)
Divides students into study groups, as if they were permanent for all of reading period. I think this feature is a form of 'clustering'. Not all students get a study group.
- Classes repurposed from Lab 4:
  - *MyVector*: a Vector that can be sorted by an independent Comparator Class.
  - *Student*: a representation of a student, including a name and a String array of four courses.
- Comparators:
  - *Alphabetize*: compares time slots (BinarySearchTree<String>) by comparing their first elements (Strings).
  - *SortByName*: compares student objects by comparing their names.

## Useful Links
 * [Course Homepage](http://cs.williams.edu/~cs136/index.html) (with TA schedule)
 * [Lab Webpage](http://cs.williams.edu/~cs136/labs/exam-scheduling.html)


## Repository Contents
This repository contains the starter files for the lab scheduling lab.
The starter files contain fictional student schedules with the format of
```
student-name
class1
class2
class3
class4
student-name
class1
class2
class3
class4
...
```
