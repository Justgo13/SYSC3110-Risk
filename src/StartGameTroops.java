public enum StartGameTroops {
    TWO_PLAYER_GAME(50),THREE_PLAYER_GAME(35),FOUR_PLAYER_GAME(30),FIVE_PLAYER_GAME(25),SIX_PLAYER_GAME(20);

    int values;
    StartGameTroops(int i) {
        values = i;
    }
    public int returnValues(){
        return values;
    }
}