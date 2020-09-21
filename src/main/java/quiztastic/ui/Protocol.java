package quiztastic.ui;

import quiztastic.app.Quiztastic;
import quiztastic.core.Category;
import quiztastic.core.Player;
import quiztastic.domain.Game;
import quiztastic.entries.RunServer;

import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

public class Protocol {
    private final Quiztastic quiz;
    private final Scanner in;
    private final PrintWriter out;
    private final Game game;


    public Protocol(Scanner in, PrintWriter out) {
        this.in = in;
        this.out = out;
        this.quiz = Quiztastic.getInstance();
        this.game = quiz.getCurrentGame();
    }

    private String fetchCommand () {
        out.print("> ");
        out.flush();
        String word = in.next(); // answer a100 -> answer

        while(word.equals("")){
            out.println("Invalid command!");
            in.reset();
            word = in.next();
        }
        return word;
    }

    private String fetchAnswer () { //TODO: Fix scanner bug
        out.print("? ");
        out.flush();

        String word = in.nextLine();

        return word;
    }

    public List<Player> players(){
        return game.getPlayerList();
    }

    private void help(){
        out.println("Your options are: \n" +
                "  - [h]elp: ask for help\n" +
                "  - [d]raw: draw the board\n" +
                "  - [a]nswer A200: get the question for category A, question 200.\n" +
                "  - [q]uit: exits the game.");
    }

    public void run () {
        out.print("Indtast navn");
        Player player = new Player(fetchAnswer(), game.getPlayers());
        game.addPlayer(player);

        boolean running = true;
        help();
        String cmd = fetchCommand();
        while (running) {
            switch (cmd) {
                case "h":
                case "help":
                   help();
                   in.nextLine();
                   break;
                case "draw":
                case "d":
                    displayBoard();
                    in.nextLine();
                    break;
                case "answer":
                case "a":
                    String question = in.next();
                    String a = question.substring(0, 1).toLowerCase(); // "A100" -> "a"
                    in.nextLine();
                    String cats = "abcdef";
                    int questionScore = Integer.parseInt(question.substring(1)); // "A100" -> 100
                    questionScore = (questionScore/100)-1;
                    if(questionScore >= 0 && questionScore <= 6) { //TODO: Check category as well.
                        answerQuestion(cats.indexOf(a), questionScore, player);
                    } else {
                        out.println("Question does not exist. Try picking another one!");
                    }
                    break;
                case "score":
                    var scores = game.getScores();
                    out.println(scores);
                    in.nextLine();
                    break;
                case "stop":
                    RunServer.keepRunning = false;
                    break;
                case "quit":
                case "q":
                    running = false;
                    continue;
                default:
                   out.println("Unknown command! " + cmd);
            }
            out.flush();
            cmd = fetchCommand();
        }

    }

    private String printNicely(String str, int width){
        return String.format("%-" + width  + "s", String.format("%" + (str.length() + (width - str.length()) / 2) + "s", str));
    }



    private synchronized void answerQuestion(int categoryNumber, int questionScore, Player player) {
        if(!game.isAnswered(categoryNumber, questionScore)){
            out.println(game.getQuestionText(categoryNumber,questionScore));
            String answer = game.answerQuestion(categoryNumber,questionScore,fetchAnswer(), player);
            if(answer != null){
                out.println("Incorrect, the correct answer is \"" + answer + "\"");
            } else {
                out.println("Correct! New score: " + game.getScore());
            }
        } else {
            out.println("You've already tried once!");
        }
    }

    private synchronized void displayBoard() {

        List<Integer> scores = List.of(100,200,300,400,500);
        List<String> categories = List.of("A","B","C","D","E","F");
        int i = 0;

        out.print("|" );
        for(Category c: game.getCategories()){
            out.print("\t");
            String str = categories.get(i) + ": " + c.getName();
            out.print(printNicely(str,30));
            i++;
            out.print("\t|");
        }
        out.println();


        for (int questionNumber = 0; questionNumber < 5; questionNumber++) {
            out.print("|");
            for (int category = 0; category < 6; category++) {
                out.print("\t");
                if (game.isAnswered(category, questionNumber)) {
                    out.print(printNicely("---",30));
                } else {
                    out.print(printNicely(scores.get(questionNumber).toString(),30));
                }
                out.print("\t|");
            }
            out.println();
        }
    }

}
