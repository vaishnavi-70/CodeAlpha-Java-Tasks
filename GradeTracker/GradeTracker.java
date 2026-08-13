import java.util.ArrayList;
import java.util.Scanner;

class Student {
    private String name;
    private double grade;

    public Student(String name, double grade) {
        this.name = name;
        this.grade = grade;
    }

    public String getName() { return name; }
    public double getGrade() { return grade; }
}

public class GradeTracker {
    public static void main(String[] args) {
        ArrayList<Student> studentList = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== Student Grade Tracker ===");
        
        while (true) {
            System.out.print("\nEnter student name (or type 'done' to finish): ");
            String name = scanner.nextLine().trim();
            
            if (name.equalsIgnoreCase("done")) {
                break;
            }
            
            double grade = -1;
            while (grade < 0 || grade > 100) {
                System.out.print("Enter grade for " + name + " (0-100): ");
                if (scanner.hasNextDouble()) {
                    grade = scanner.nextDouble();
                    if (grade < 0 || grade > 100) {
                        System.out.println("Invalid input. Grade must be between 0 and 100.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a valid number.");
                    scanner.next(); 
                }
            }
            scanner.nextLine(); 
            
            studentList.add(new Student(name, grade));
        }
        
        if (studentList.isEmpty()) {
            System.out.println("\nNo student data entered. Exiting program.");
        } else {
            displaySummaryReport(studentList);
        }
        
        scanner.close();
    }

    private static void displaySummaryReport(ArrayList<Student> students) {
        double total = 0;
        double highest = students.get(0).getGrade();
        double lowest = students.get(0).getGrade();
        
        System.out.println("\n=================================");
        System.out.println("         SUMMARY REPORT          ");
        System.out.println("=================================");
        System.out.printf("%-20s | %-10s\n", "Student Name", "Grade");
        System.out.println("---------------------------------");
        
        for (Student s : students) {
            System.out.printf("%-20s | %-10.2f\n", s.getName(), s.getGrade());
            
            total += s.getGrade();
            if (s.getGrade() > highest) {
                highest = s.getGrade();
            }
            if (s.getGrade() < lowest) {
                lowest = s.getGrade();
            }
        }
        
        double average = total / students.size();
        
        System.out.println("---------------------------------");
        System.out.printf("Total Students : %d\n", students.size());
        System.out.printf("Average Score  : %.2f\n", average);
        System.out.printf("Highest Score  : %.2f\n", highest);
        System.out.printf("Lowest Score   : %.2f\n", lowest);
        System.out.println("=================================");
    }
}