public class Command {
    private String command; //command wordretu
    private String secondWord;

    public Command(String command, String secondWord){
        this.command = command;
        this.secondWord = secondWord;
    }

    /**
     * gets you the command word inputted by the user
     * @return the command word
     */
    public String getCommand(){
        if(command != null){
            return command;
        }
        else{
            return "error";
        }
    }

    /**
     * gets you the second word inputted by the user
     * @return the second word inputted by the user
     */
    public String getSecondWord(){
        if(secondWord != null){
            return secondWord;
        }else{
            return "error";
        }
    }

    /**
     * checks if the user has used two words or one
     * @return true if second word was inputted, false otherwise
     */
    public boolean hasSecondWord(){
        if(secondWord != null){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * checks to see if the command word inputted is empty
     * or not
     * @return true if the command word is null, false otherwise
     */
    public boolean commandEmpty(){
        if(command == null){
            return true;
        }else{
            return false;
        }
    }
}
