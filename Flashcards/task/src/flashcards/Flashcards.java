package flashcards;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Flashcards {

    private Map<String, Card> cardsByNotion;
    private Map<String, Card> cardsByDefinitoin;
    private Map<Integer, ArrayList<Card>> cardsByMistakes;

    public Flashcards() {
        cardsByNotion = new LinkedHashMap<>();
        cardsByDefinitoin = new LinkedHashMap<>();
        cardsByMistakes = new LinkedHashMap<>();
    }

    public void addCard(Card card){
        cardsByNotion.put(card.getNotion(), card);
        cardsByDefinitoin.put(card.getDefinition(), card);

        if(card.getMistakes() != 0) {
            ArrayList<Card> cards = cardsByMistakes.getOrDefault(card.getMistakes(), null);
            if (cards != null) {
                cards.add(card);
            } else {
                cards = new ArrayList<>();
                cards.add(card);
            }
            cardsByMistakes.put(card.getMistakes(), cards);
        }
    }
}
