import java.util.*;

public class CommandLine {

	public CommandLine(String[] args) {
		java.util.Scanner input = new Scanner(System.in);
		
		System.out.println("Enter file name: ");
		String userInput = input.nextLine();
		System.out.println(userInput);
		input.close();
		
	}
	
	public static void main(String[] args) {
		CommandLine test = new CommandLine(args);
	}
}
