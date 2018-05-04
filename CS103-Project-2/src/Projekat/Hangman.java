/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Projekat;

import javafx.stage.Stage;
import java.util.HashMap;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/**
 *
 * @author darko
 */
public class Hangman extends Application{

    private static final int SIRINA = 900;
    private static final int VISINA = 500;
    private static final Font FONT = new Font("Courier", 36);

    private static final int POEN_ZA_SLOVO = 100;
    private static final float BONUS = 0.2f;
    
    private SimpleStringProperty rec = new SimpleStringProperty();
    private SimpleIntegerProperty slovaZaPogadjanje = new SimpleIntegerProperty(); //koliko je ostalo slova za pogadjanje
    private SimpleIntegerProperty rezultat = new SimpleIntegerProperty(); //trenutni rez
    private float scoreModifier = 1.0f;
    private SimpleBooleanProperty playable = new SimpleBooleanProperty();
    private ObservableList<Node> slova; //lista slova za rec
    private HashMap<Character, Text> alphabet = new HashMap<Character, Text>();
    private HangmanImg hangman = new HangmanImg();
    private UcitavanjeReci ucitavanjeReci = new UcitavanjeReci();
    
    
        public Parent createContent() {
        HBox rowLetters = new HBox();
        rowLetters.setAlignment(Pos.CENTER);
        slova = rowLetters.getChildren();

        playable.bind(hangman.lives.greaterThan(0).and(slovaZaPogadjanje.greaterThan(0)));
        playable.addListener((obs, old, newValue) -> {
            if (!newValue.booleanValue())
                stopGame();
        });

        Button btnAgain = new Button("NEW GAME");
        btnAgain.disableProperty().bind(playable);
        btnAgain.setOnAction(event -> startGame());

        HBox row1 = new HBox();
        row1.setAlignment(Pos.CENTER);
        Text trow1 = new Text("Hangman CS103");
        trow1.setFont(FONT);
        row1.getChildren().add(trow1);

        HBox rowAlphabet = new HBox(5);
        rowAlphabet.setAlignment(Pos.CENTER);
        for (char c = 'A'; c <= 'Z'; c++) {
            Text t = new Text(String.valueOf(c));
            t.setFont(FONT);
            alphabet.put(c, t);
            rowAlphabet.getChildren().add(t);
        }

        Text crta = new Text("-");
        crta.setFont(FONT);
        alphabet.put('-', crta);
        rowAlphabet.getChildren().add(crta);

        Text textScore = new Text();
        textScore.textProperty().bind(rezultat.asString().concat(" Poena"));

        HBox rowHangman = new HBox(10, btnAgain, textScore, hangman);
        rowHangman.setAlignment(Pos.CENTER);

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(
                row1,
                rowLetters,
                rowAlphabet,
                rowHangman);
        return vBox;
    }

    private void stopGame() {
        for (Node n : slova) {
            Letter letter = (Letter) n;
            letter.show();
        }
    }
    
     private void startGame() {
        for (Text t : alphabet.values()) {
            t.setStrikethrough(false);
            t.setFill(Color.BLACK);
        }

        hangman.reset();
        rec.set(ucitavanjeReci.randomRec().toUpperCase());
        slovaZaPogadjanje.set(rec.length().get());

        slova.clear();
        for (char c : rec.get().toCharArray()) {
            slova.add(new Letter(c));
        }
    }
     
     
        private static class HangmanImg extends Parent {
        private static final int SPINE_START_X = 100;
        private static final int SPINE_START_Y = 20;
        private static final int SPINE_END_X = SPINE_START_X;
        private static final int SPINE_END_Y = SPINE_START_Y + 50;

        private SimpleIntegerProperty lives = new SimpleIntegerProperty();

        public HangmanImg() {
            Circle head = new Circle(20);
            head.setTranslateX(SPINE_START_X);

            Line spine = new Line();
            spine.setStartX(SPINE_START_X);
            spine.setStartY(SPINE_START_Y);
            spine.setEndX(SPINE_END_X);
            spine.setEndY(SPINE_END_Y);

            Line leftArm = new Line();
            leftArm.setStartX(SPINE_START_X);
            leftArm.setStartY(SPINE_START_Y);
            leftArm.setEndX(SPINE_START_X + 40);
            leftArm.setEndY(SPINE_START_Y + 10);

            Line rightArm = new Line();
            rightArm.setStartX(SPINE_START_X);
            rightArm.setStartY(SPINE_START_Y);
            rightArm.setEndX(SPINE_START_X - 40);
            rightArm.setEndY(SPINE_START_Y + 10);

            Line leftLeg = new Line();
            leftLeg.setStartX(SPINE_END_X);
            leftLeg.setStartY(SPINE_END_Y);
            leftLeg.setEndX(SPINE_END_X + 25);
            leftLeg.setEndY(SPINE_END_Y + 50);

            Line rightLeg = new Line();
            rightLeg.setStartX(SPINE_END_X);
            rightLeg.setStartY(SPINE_END_Y);
            rightLeg.setEndX(SPINE_END_X - 25);
            rightLeg.setEndY(SPINE_END_Y + 50);

            getChildren().addAll(head, spine, leftArm, rightArm, leftLeg, rightLeg);
            lives.set(getChildren().size());
        }

        public void reset() {
            getChildren().forEach(node -> node.setVisible(false));
            lives.set(getChildren().size());
        }

        public void takeAwayLife() {
            for (Node n : getChildren()) {
                if (!n.isVisible()) {
                    n.setVisible(true);
                    lives.set(lives.get() - 1);
                    break;
                }
            }
        }
    }
        
        
    private static class Letter extends StackPane {
        private Rectangle bg = new Rectangle(40, 60);
        private Text text;

        public Letter(char letter) {
            bg.setFill(Color.WHITE);
            bg.setStroke(Color.BLUE);

            text = new Text(String.valueOf(letter).toUpperCase());
            text.setFont(FONT);
            text.setVisible(false);

            setAlignment(Pos.CENTER);
            getChildren().addAll(bg, text);
        }

        public void show() {
            RotateTransition rt = new RotateTransition(Duration.seconds(1), bg);
            rt.setAxis(Rotate.Y_AXIS);
            rt.setToAngle(180);
            rt.setOnFinished(event -> text.setVisible(true));
            rt.play();
        }

        public boolean isEqualTo(char other) {
            return text.getText().equals(String.valueOf(other).toUpperCase());
        }
    }
    
    
    public void start(Stage primaryStage) {
        Scene scene = new Scene(createContent());
        scene.setOnKeyPressed((KeyEvent event) -> {
            if (event.getText().isEmpty())
                return;

            char pressed = event.getText().toUpperCase().charAt(0);
            if ((pressed < 'A' || pressed > 'Z') && pressed != '-')
                return;

            if (playable.get()) {
                Text t = alphabet.get(pressed);
                if (t.isStrikethrough())
                    return;

                t.setFill(Color.BLUE);
                t.setStrikethrough(true);

                boolean found = false;

                for (Node n : slova) {
                    Letter letter = (Letter) n;
                    if (letter.isEqualTo(pressed)) {
                        found = true;
                        rezultat.set(rezultat.get() + (int)(scoreModifier * POEN_ZA_SLOVO));
                        slovaZaPogadjanje.set(slovaZaPogadjanje.get() - 1);
                        letter.show();
                    }
                }

                if (!found) {
                    hangman.takeAwayLife();
                    scoreModifier = 1.0f;
                }
                else {
                    scoreModifier += BONUS;
                }
            }
        });

        primaryStage.setResizable(false);
        primaryStage.setWidth(SIRINA);
        primaryStage.setHeight(VISINA);
        primaryStage.setTitle("Hangman");
        primaryStage.setScene(scene);
        primaryStage.show();
        startGame();
    }
    
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
