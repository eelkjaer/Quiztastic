package quiztastic.domain;

import quiztastic.core.Board;
import quiztastic.core.Category;
import quiztastic.core.Question;
import quiztastic.core.Spiller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
    private final Board board;
    private final List<Answer> answerList;
    private int score = 0;
    private final List<Spiller> players;

    public Game(Board board, List<Answer> answerList) {
        this.board = board;
        this.answerList = answerList;
        this.players = new ArrayList<>();
    }

    public List<Category> getCategories() {
        List<Category> list = new ArrayList<>();
        for (Board.Group group : this.board.getGroups()) {
            Category category = group.getCategory();
            list.add(category);
        }
        return list;
    }

    public String answerQuestion(int categoryNumber, int questionNumber, String answer, Spiller player) {
        Question q = getQuestion(categoryNumber, questionNumber);
        var correct = q.getAnswer().equalsIgnoreCase(answer);
        answerList.add(new Answer(categoryNumber, questionNumber, answer, player, correct));
        if (correct) {
            int points = (questionNumber + 1) * 100; //Converts from array index to actual points.
            setScore(points);
            return null;
        } else {
            return q.getAnswer();
        }
    }

    public synchronized void addPlayer(Spiller player){
     players.add(player);
    }

    public synchronized Map<Spiller, Integer> getScores(){
        Map<Spiller, Integer> scores = new HashMap<>();


        for(Spiller p: players){
            int score = 0;
            for(Answer a: answerList){
                if(a.player.equals(p) && a.correct){
                    score += (a.questionNumber + 1) * 100;
                }
            }
            scores.put(p,score);
        }

        return scores;
    }

    public int getScore() {
        return score;
    }

    private void setScore(int score){
        this.score += score;
    }

    public String getQuestionText(int categoryNumber, int questionNumber) {
        return getQuestion(categoryNumber, questionNumber).getQuestion();
    }

    private Question getQuestion(int categoryNumber, int questionNumber) {
        return this.board.getGroups().get(categoryNumber).getQuestions().get(questionNumber);
    }

    public boolean isAnswered(int categoryNumber, int questionNumber) {
        for (Answer a : answerList) {
            if (a.hasIndex(categoryNumber, questionNumber)) {
                return true;
            }
        }
        return false;
    }

    private class Answer {
        private final int categoryNumber;
        private final int questionNumber;
        private final String answer;
        private final Spiller player;
        private final boolean correct;

        private Answer(int categoryNumber, int questionNumber, String answer, Spiller player, boolean correct) {
            this.categoryNumber = categoryNumber;
            this.questionNumber = questionNumber;
            this.answer = answer;
            this.player = player;
            this.correct = correct;
        }

        public boolean hasIndex(int categoryNumber, int questionNumber)  {
            return this.categoryNumber == categoryNumber && this.questionNumber == questionNumber;
        }
    }
}
