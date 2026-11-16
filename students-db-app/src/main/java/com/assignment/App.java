package com.assignment;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.Statement;

public class App {
    Connection connection;

    public static void main(String[] args) {
        App app = new App();
        app.connectToDatabase();

        boolean loop = true;
        Scanner scanner = new Scanner(System.in);
        while (loop) {
        System.out.println("Get all students: 1");
        System.out.println("Add a student: 2");
        System.out.println("Update a student's email: 3");
        System.out.println("Delete a student: 4");
        System.out.print("Enter an option (5 to cancel): ");
        String option = scanner.nextLine();
        if (option.equals("1")) {
            System.out.println(app.getAllStudents());
        } else if (option.equals("2")) {
            System.out.println("Enter first name: ");
            String first_name = scanner.nextLine();
            System.out.println("Enter last name: ");
            String last_name = scanner.nextLine();
            System.out.println("Enter email: ");
            String email = scanner.nextLine();
            System.out.println("Enter enrollment date (YYYY-MM-DD): ");
            String enrollment_date_str = scanner.nextLine();
            Date enrollment_date = Date.valueOf(enrollment_date_str);
            app.addStudent(first_name, last_name, email, enrollment_date);
        } else if (option.equals("3")) {
            System.out.println("Enter student ID to update email: ");
            int student_id = Integer.parseInt(scanner.nextLine());
            System.out.println("Enter new email: ");
            String new_email = scanner.nextLine();
            app.updateStudentEmail(student_id, new_email);
        } else if (option.equals("4")) {
            System.out.println("Enter student ID to delete: ");
            int student_id = Integer.parseInt(scanner.nextLine());
            app.deleteStudent(student_id);
        } else if (option.equals("5")) {
            System.out.println("Exiting...");
            loop = false;
        } else {
            System.out.println("Invalid option. Please try again.");
        }
    }

    }
    void connectToDatabase() {
        String url = "jdbc:postgresql://localhost:5432/Assignment3";
        String user = "postgres";
        String password = "kp";

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            if (connection != null){
                System.out.println("Connected to the database");
            }
            else {
                System.out.println("Failed to connect to db");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    ArrayList<String> getAllStudents() {
        ArrayList<String> students = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM students";
            var resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int student_id = resultSet.getInt("student_id");
                String first_name = resultSet.getString("first_name");
                String last_name = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                Date enrollment_date = resultSet.getDate("enrollment_date");
                students.add(student_id + ": " + first_name + " " + last_name + ", " + email + ", enrolled on " + enrollment_date);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
    boolean addStudent(String first_name, String last_name, String email, Date enrollment_date) {
        try {
            Statement statement = connection.createStatement();
            String sql = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES ('" + first_name + "', '" + last_name + "', '" + email + "', '" + enrollment_date + "')";
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    boolean updateStudentEmail(int student_id, String new_email) {
        try {
            Statement statement = connection.createStatement();
            String sql = "UPDATE students SET email = '" + new_email + "' WHERE student_id = " + student_id;
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    boolean deleteStudent(int student_id) {
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE FROM students WHERE student_id = " + student_id;
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
