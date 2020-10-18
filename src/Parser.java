import java.util.Scanner;

public class Parser {
    private CommandWords command; // all the available command words
    private Scanner scanner; // command input

    /**
     * initializeing variables
     */
    public Parser(){
        command = new CommandWords();
        scanner = new Scanner(System.in);
    }

    /**
     * detects the first two words in the string (first being the command word
     * and second being the second word) and passes them as parameters in command
     * constructor
     * @return command object
     */
    public Command returnCommand(){
        String line = scanner.nextLine();
        String commandWord = null , secondWord = null;

        System.out.print(">");

        //get the first two words in the line
        Scanner token = new Scanner(line);
        if(token.hasNext()){
            commandWord = token.next(); //first word is the command word
            if(token.hasNext()){
                secondWord = token.next(); //second word
            }
        }
        //now we will check if the command word is in the list of commands
        if(command.isValid(commandWord)){
            return new Command(commandWord, secondWord);
        }
        else{
            return new Command(null, secondWord);
        }
    }

    /**
     * prints out all command words
     */
    public void outputCommands(){
        command.showCommads();
    }
}
