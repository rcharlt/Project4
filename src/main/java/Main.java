import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Iterator;
/* Rylee Charlton, 04/04/2019, Project 4
 */

public class Main {

    private static TaskCollection taskCollection = new TaskCollection();
    private static boolean running = true;
    private static Scanner scan = new Scanner(System.in);

    private static void displayMenu(){ //Display menu and ask for a menu option every loop

        System.out.println("What would you like to do?");
        System.out.println("(1)- Add task. ");
        System.out.println("(2)- Remove task. ");
        System.out.println("(3)- Edit a task.");
        System.out.println("(4)- View full to-do list.");
        System.out.println("(5) List Tasks according to priority");
        System.out.println("(0)- Exit ");


        String input = scan.nextLine();

        switch(input){
            case "1": taskCollection.addTask(); //Run add task method in taskList
                break;
            case "2": taskCollection.remTask(); //Run remove task method in taskList
                break;
            case "3": taskCollection.editTask(); //Run edit task method in taskList
                break;
            case "4":
                Collections.sort(taskCollection.taskCollection);
                for(Task task: taskCollection) {
                    System.out.println(task.getTaskNum() +") " + task.getTitle() + " ~ " + task.getPriority() +"\n" + task.getDescription());
                }//Run list task method in taskList
                break;
            case "5": taskCollection.listTasksPriority(); //Run listTaskPriority method in taskList
                break;
            case "0": running=false; //Exit program
                break;
            default:
                System.out.println("Invalid input. Please try again, enter an integer value (1-5 or 0 to exit)"); //Invalid input
        }
    }
    public static void main(String[] args) {

        while(running){ //While program is running, keep asking for options
            displayMenu();
        }
    }

    static void serializeToDo(){

    }

}

    class Task implements Comparable<Task>{

    private int taskNum;
    private String title;
    private String description;
    private int priority;

    public Task(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }
    //Getters and setters for all properties.
    public int getTaskNum() {
        return taskNum;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public int getPriority() {
        return priority;
    }

    public void setTaskNum(int taskNum) {
        this.taskNum = taskNum;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(Task o) {
        if (priority != (o.priority)) {
            if (priority>o.priority){
                return -1;
            }else{
                return 1;
            }
        }
        else {
            return title.compareTo(o.title);
        }
    }
}

    class TaskCollection implements Iterable<Task>{

    private Scanner scan = new Scanner(System.in);
    public ArrayList<Task> taskCollection = new ArrayList<>();

    public void addTask() {
        int priority = 0;

        System.out.println("What is the name of the task?");
        String title = scan.nextLine();

        System.out.println("Give a description of the task. ");
        String description = scan.nextLine();

        boolean validInput;
        do {
            try {
                System.out.println("What is the task priority? (0-5) ");
                priority = scan.nextInt();
                scan.nextLine();
                if (-1 < priority && priority < 6) {
                    validInput = true;
                } else {
                    validInput = false;
                    System.out.println("Invalid input. Please try again, enter an integer value (0-5)");
                }
            } catch (Exception e) {
                scan.nextLine();
                System.out.println("Invalid input. Please try again, enter an integer value (0-5)");
                validInput = false;
            }
        } while (!validInput);

        Task task = new Task(title, description, priority);
        taskCollection.add(task);

        Gson gson = new Gson();
        try {
            FileWriter writer = new FileWriter("data.json");
            gson.toJson(taskCollection, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void remTask(){

        int taskID = 0;
        boolean validInput = false;
        do {
            try {
                listTasks();
                System.out.println("What is the task you would like to remove?");
                taskID = Integer.parseInt(scan.nextLine());
                for (Task task : taskCollection){
                    if (task.getTaskNum() == taskID){
                        taskCollection.remove(task);
                        validInput = true;
                        break;

                    }
                }
            } catch (Exception e) {
                System.out.println("Invalid input.");
                validInput = false;
            }
        }while(!validInput);
    }

    public void editTask(){
        int taskID = 0;
        boolean validInput = false;
        do {
            try {
                listTasks();
                System.out.println("What is the task you would like to edit?");
                taskID = Integer.parseInt(scan.nextLine());
                for (Task task : taskCollection){
                    if (task.getTaskNum() == taskID){

                        int taskPriority = 0;

                        System.out.println("What would you like to change the title to?");
                        String taskTitle = scan.nextLine();

                        System.out.println("What would you like to change the description to?");
                        String taskDesc = scan.nextLine();

                        boolean validInput2;
                        do {
                            try {
                                System.out.println("What would you like to change the priority to?(0-5)");
                                taskPriority = scan.nextInt();
                                scan.nextLine();
                                if (-1<taskPriority && taskPriority<6) {
                                    validInput2 = true;
                                }else{
                                    validInput2 = false;
                                    System.out.println("Invalid input. Please try again, enter an integer value (0-5)");
                                }
                            } catch (Exception e) {
                                scan.nextLine();
                                System.out.println("Invalid input. Please try again, enter an integer value (0-5)");
                                validInput2 = false;
                            }
                        }while(!validInput2);
                        task.setTitle(taskTitle); //Update task title
                        task.setDescription(taskDesc); //Update task description
                        task.setPriority(taskPriority); //Update task priority
                        validInput = true;
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Invalid input.");
                validInput = false;
            }
        }while(!validInput);
    }

    public void listTasks(){
        int ID = 1;
        for (int i=5;i>=0;i--){
            for (Task task : taskCollection){
                if(task.getPriority()==i){
                    task.setTaskNum(ID);
                    System.out.println(task.getTaskNum() +") " + task.getTitle() + " ~ " + task.getPriority() +"\n" + task.getDescription());
                    ID++;

                    }
                }
            }
        }


    public void listTasksPriority(){
        int taskPriority = 0;
        boolean validInput;
        do {
            try {
                System.out.println("What priority tasks would you like to see?(0-5)");
                taskPriority = scan.nextInt();
                scan.nextLine();
                if (-1<taskPriority && taskPriority<6) {
                    validInput = true;
                }else{
                    validInput = false;
                    System.out.println("Invalid input.");
                }
            } catch (Exception e) {
                scan.nextLine();
                System.out.println("Invalid input.");
                validInput = false;
            }
        }while(!validInput);
        for (Task task : taskCollection){
            if(task.getPriority()==taskPriority){
                task.setTaskNum(taskPriority);
                System.out.println(task.getTaskNum() +") " + task.getTitle() + " ~ " + task.getPriority() +"\n" + task.getDescription());
            }
        }
    }

    @Override
    public Iterator<Task> iterator() {
        return taskCollection.iterator();
    }
}