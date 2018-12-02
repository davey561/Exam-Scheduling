# Lab 10: Exam Scheduling

### Description of Contents of Repository
- *ScheduleExams*: The primary class in the repository. Designs an exam schedule for students that no student has more than one exam during the same time slot and no new time slots could be merged without new conflicts arising. ScheduleExams has a few extra features as well:
  - Sorts the time slots alphabetically by name of the first course in each.
  - Prints out an exam schedule for each of the students.
  - Divides students into study groups, as if they were permanent for all of reading period. I think this feature is a form of 'clustering'. Not all students get a study group.
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
