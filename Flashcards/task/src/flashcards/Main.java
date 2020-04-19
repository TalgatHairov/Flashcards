package flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

public class Main {


    static String EXPORT_FILE_NAME = null;
    static String IMPORT_FILE_NAME = null;
    static StringBuilder message = new StringBuilder();
    static ArrayList<StringBuilder> outputLines = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            Map<String, String> flashcards = new LinkedHashMap<>();


            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-import")) {
                    IMPORT_FILE_NAME = args[i + 1];
                }
                if (args[i].equals("-export")){
                    EXPORT_FILE_NAME = args[i + 1];
                }

            }

            if (IMPORT_FILE_NAME != null) {
                import_(sc, flashcards);
            }

            while (true) {
                printAndSaveMessage("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
                String answer = sc.next();//.replace("", "_");
                saveInput(answer);
                switch (answer) {
                    case "add":
                        add(flashcards);
                        break;
                    case "remove":
                        remove(sc, flashcards);
                        break;
                    case "import":
                        import_(sc, flashcards);
                        break;
                    case "export":
                        export(sc, flashcards);
                        break;
                    case "ask":
                        ask(sc, flashcards);
                        break;
                    case "exit":
                        printAndSaveMessage("Bye bye!");
                        if (EXPORT_FILE_NAME != null) {
                            export(sc, flashcards);
                        }
                        return;
                    case "log":
                        log();
                        break;
                    case "hardest_card":
                        System.out.println("in hardest card");
                        showStatistics();
                        break;
                    case "reset_stats":
                        resetStatistics(flashcards);
                        break;
                    default:
                        //System.out.println("in default");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void showStatistics() {
        String result = "There are no cards with errors";

        printAndSaveMessage(result);
    }

    private static void resetStatistics(Map<String, String> flashcards) {
    }

    private static void log() {
        printAndSaveMessage("File name:");
        String fileName = sc.next();
        saveInput(fileName);

        try (PrintWriter outputFile = new PrintWriter(fileName)) {
            for (StringBuilder line : outputLines) {
                outputFile.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printAndSaveMessage(String newMessage) {
        message.replace(0, message.length(), newMessage);
        System.out.println(message);
        outputLines.add(message);
    }

    private static void saveInput(String newMessage) {
        outputLines.add(new StringBuilder(newMessage));
    }

    private static void saveInput(int newMessage) {
        outputLines.add(new StringBuilder(newMessage));
    }

    private static void add(Map<String, String> flashcards) {
        String notion;
        String definition;

        printAndSaveMessage("The card:");
        sc.nextLine();
        notion = sc.nextLine();
        saveInput(notion);

        if (null != flashcards.get(notion)) {
            printAndSaveMessage("The card " + notion + " already exists.");
            return;
        }

        printAndSaveMessage("The definition of the card:");
        definition = sc.nextLine();
        saveInput(definition);

        for (String def : flashcards.values()) {
            if (def.equals(definition)) {
                printAndSaveMessage("The definition " + definition + " already exists.");
                return;
            }
        }
        printAndSaveMessage("The pair (" + notion + ":" + definition + ") has been added.");
        flashcards.put(notion, definition);
    }

    private static void remove(Scanner sc, Map<String, String> flashcards) {
        printAndSaveMessage("The card:");
        sc.nextLine();
        String word = sc.nextLine();
        saveInput(word);
        String message = "Can't remove \"" + word + "\": there is no such card.";

        for (String notion : flashcards.keySet())
            if (notion.equals(word)) {
                flashcards.remove(notion);
                message = "The card has been removed.";
                break;
            }
        printAndSaveMessage(message);
    }

    private static void import_(Scanner sc, Map<String, String> flashcards) {
        String fileName;

        if (IMPORT_FILE_NAME == null) {
            printAndSaveMessage("File name:");
            sc.nextLine();
            fileName = sc.nextLine();
            saveInput(fileName);
        } else {
            fileName = IMPORT_FILE_NAME;
        }

        File file = new File(fileName);
        String message;
        int count = 0;

        try (Scanner fileSc = new Scanner(file)) {
            while (fileSc.hasNext()) {
                count++;
                flashcards.put(fileSc.nextLine(), fileSc.nextLine());
            }
            message = count + " cards have been loaded.";
        } catch (FileNotFoundException e) {
            message = "File not found.";
        } catch (Exception e) {
            message = count + " cards have been loaded.";
        }
        printAndSaveMessage(message);
    }

    private static void export(Scanner sc, Map<String, String> flashcards) {
        String fileName;
        String message;

        PrintWriter fileWriter = null;

        try {
            if (EXPORT_FILE_NAME == null) {
                printAndSaveMessage("File name:");
                fileName = sc.next();
                saveInput(fileName);
            } else {
                fileName = EXPORT_FILE_NAME;
            }


            File file = new File(fileName);
            int count = 0;
            fileWriter = new PrintWriter(file);

            for (Map.Entry<String, String> entry : flashcards.entrySet()) {
                fileWriter.println(entry.getKey());
                fileWriter.println(entry.getValue());
                count++;
            }
            message = count + " cards have been saved.";
        } catch (FileNotFoundException e) {
            message = "File not found.";
        } finally{
            if (fileWriter != null) {
                fileWriter.close();
            }
        }
        printAndSaveMessage(message);
    }

    private static void ask(Scanner sc, Map<String, String> flashcards) {
        printAndSaveMessage("How many times to ask?");
        int askNumber = sc.nextInt();
        sc.nextLine();
        saveInput(askNumber);

        int askCount = 0;
        Iterator<Map.Entry<String, String>> entries = flashcards.entrySet().iterator();
        Map.Entry<String, String> entry = entries.next();

        String definition;
        while (askCount < askNumber) {
            printAndSaveMessage("Print the definition of \"" + entry.getKey() + "\"" );
            definition = sc.nextLine();
            if (definition.equals(entry.getValue()))
                printAndSaveMessage("Correct answer.");
            else {
                Map.Entry<String, String> entryByValue = findByValue(flashcards, definition);
                String message = entryByValue != null ? ", you've just written the definition of \"" + entryByValue.getKey() + "\"." : "";
                printAndSaveMessage("Wrong answer. The correct one is \"" + entry.getValue() + "\"" + message);
            }
            askCount++;
        }
    }

    private static Map.Entry<String, String> findByValue(Map<String, String> flashcards, String value) {
        for (Map.Entry<String, String> entry : flashcards.entrySet())
            if (entry.getValue().equals(value))
                return entry;
        return null;
    }
}
