public class CommandWords {
    // an array that holds constant valid command words
    private static final String[] validCommands = {
            "attack", "map", "endTurn", "help"
    };

    /*
    Constructor intitialising the commmand words
     */
    public CommandWords(){
        //do nothing for now...
    }

    /**
     * Verifies if the user has used a word that is inside the validCommands
     * arrayList
     * @param word is the input String by the user
     * @return true if valid command, false otherwise
     */
    public Boolean isValid(String word){
        for(int i = 0; i< validCommands.length; i++){
            if(validCommands[i].equals(word)){
                //if the word is in the arrayList then return true
                return true;
            }
        }
        //if the word is not found in the arrayList then return false
        return false;
    }

    /**
     * Outputs all command words in the validCommands
     * arrayList
     */
    public void showCommads(){
        for(String command: validCommands){
            System.out.print(command + ", ");
        }
    }
}
