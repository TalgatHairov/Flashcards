package flashcards;

public class Card {
    private String notion;
    private String definition;
    private int mistakes;

    public Card(String notion, String definition, int mistakes) {
        this.notion = notion;
        this.definition = definition;
        this.mistakes = mistakes;
    }

    public Card() {
    }

    public String getNotion() {
        return notion;
    }

    public void setNotion(String notion) {
        this.notion = notion;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public int getMistakes() {
        return mistakes;
    }

    public void setMistakes(int mistakes) {
        this.mistakes = mistakes;
    }
}
