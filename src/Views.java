/*
 *@author Xiling Wang
 */
public interface Views {
    void showMessage(Event event);

    int getNumberFromDiolog(Event event);

    void updateCountryButton(Event event);

    void addPlayer(Event event);

    void showEndMessage(Event event);

    void updatePlayerTurnTextHandler(Event event);

    void updatePlaceNum(Event event);
}
