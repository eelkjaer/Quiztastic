package quiztastic.ui;

import quiztastic.app.Quiztastic;
import quiztastic.core.Category;
import quiztastic.domain.Game;
import quiztastic.entries.RunServer;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringWriter;
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
        return word;
    }

    private String fetchAnswer () {
        out.print("? ");
        out.flush();
        String word = in.next(); // answer a100 -> answer
        return word;
    }

    public void run () {
        String cmd = fetchCommand();
        while (!cmd.equals("quit")) {
            switch (cmd) {
                case "h":
                case "help":
                   out.println("Your options are: \n" +
                           "  - [h]elp: ask for help\n" +
                           "  - [d]raw: draw the board\n" +
                           "  - [a]nswer A200: get the question for category A, question 200.\n" +
                           "  - [q]uit: exits the game.");
                   break;
                case "draw":
                case "d":
                    displayBoard();
                    break;
                case "answer":
                case "a":
                    String question = in.next();
                    String a = question.substring(0, 1).toLowerCase(); // "A100" -> "a"
                    int questionScore = Integer.parseInt(question.substring(1)); // "A100" -> 100
                    questionScore = (questionScore/100)-1; //TODO: Quick fix for array of questions (max 5)
                    answerQuestion("abcdef".indexOf(a), questionScore);
                    break;
                case "stop":
                    RunServer.keepRunning = false;
                    break;
                default:
                   out.println("Unknown command! " + cmd);
            }
            in.nextLine();
            out.flush();
            cmd = fetchCommand();
        }

    }

    private String printNicely(String str, int width){
        return String.format("%-" + width  + "s", String.format("%" + (str.length() + (width - str.length()) / 2) + "s", str));
    }

    private void answerQuestion(int categoryNumber, int questionScore) {
        out.println(game.getQuestionText(categoryNumber,questionScore));
        String answer = game.answerQuestion(categoryNumber,questionScore,fetchAnswer());
        if(answer != null){
            out.println("Incorrect, the correct answer is \"" + answer + "\"");
        } else {
            out.println("Correct!");
        }

    }

    private void displayBoard() {

        List<Integer> scores = List.of(100,200,300,400,500);
        List<String> categories = List.of("A","B","C","D","E","F");
        int i = 0;

        out.print("|" );
        for(Category c: game.getCategories()){
            out.print("\t");
            String str = categories.get(i) + ": " + c.getName();
            out.print(printNicely(str,10));
            i++;
            out.print("\t|");
        }
        out.println();


        for (int questionNumber = 0; questionNumber < 5; questionNumber++) {
            out.print("|");
            for (int category = 0; category < 6; category++) {
                out.print("\t");
                if (game.isAnswered(category, questionNumber)) {
                    out.print("---");
                } else {
                    //out.print(scores.get(questionNumber));
                    out.print(printNicely(scores.get(questionNumber).toString(),30));
                }
                out.print("\t|");
            }
            out.println();
        }
    }

}
