package sample;

import javafx.application.Application;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends Application {
    private static final Text textTittle = new Text("Snake Game");
    private static final Text textStart = new Text("Start");
    private static final Text textHowToPlay = new Text("How To Play");
    private static final Text textQuit = new Text("Quit");
    private static final Text textBack = new Text("Back");
    private static final Text text1 = new Text("Use keyboard to control");
    private static final Text text2 = new Text("Eat food to get higher mark");
    private static final Text text3 = new Text("Cannot collide with your body");
    private static final Text text4 = new Text("Cannot collide the wall");
    private static final Text textGameOver = new Text("Game Over");
    private static final Text textPlayAgain = new Text("Play Again");
    private static final Text textExcellent = new Text("");

    //Variable for snake
    static int speed = 6;
    static int score = 0;
    static int width = 24;
    static int height = 20;
    static int foodX = 0;
    static int foodY = 0;
    static int cornerSize = 15;
    static boolean upDirection = false;
    static boolean downDirection = false;
    static boolean leftDirection = true;
    static boolean rightDirection = false;
    static boolean gameOver = false;
    static boolean paused = false;
    static List<Corner> snake = new ArrayList<>();
    static Random random = new Random();
    static AnimationTimer a;
    static GraphicsContext gc;
    static Canvas c;
    static Stage stageGame = new Stage();
    static Stage stageGameOver = new Stage();

    @Override
    public void start(Stage primaryStage) {
        //Create stackPane to hold the text(tittle)
        StackPane stackPane = new StackPane();
        textTittle.setFont(Font.font("cooper black",50));
        textTittle.setFill(Color.WHITESMOKE);
        stackPane.getChildren().add(textTittle);
        stackPane.setPadding(new Insets(2, 10, 0, 10));
        stackPane.setAlignment(Pos.CENTER);

        //Setting text (button)
        textStart.setX(140);
        textStart.setY(65);
        textStart.setFill(Color.WHITESMOKE);
        textStart.setFont(Font.font("cooper black",20));

        textHowToPlay.setX(102);
        textHowToPlay.setY(100);
        textHowToPlay.setFill(Color.WHITESMOKE);
        textHowToPlay.setFont(Font.font("cooper black",20));

        textQuit.setX(142);
        textQuit.setY(130);
        textQuit.setFill(Color.WHITESMOKE);
        textQuit.setFont(Font.font("cooper black",20));

        //Create vBox to hold text (button)
        Pane paneButton = new Pane();
        paneButton.getChildren().addAll(textStart, textHowToPlay, textQuit);

        //Create borderPane to hole title and all the text (button)
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(0, 10, 100, 10));
        borderPane.setCenter(paneButton);
        borderPane.setTop(stackPane);
        borderPane.setBackground(new Background(new BackgroundFill(Color.PINK, CornerRadii.EMPTY, Insets.EMPTY)));

        //Create first scene
        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Snake Game");
        primaryStage.show();
        primaryStage.setResizable(false);

        //Create scene (how to play)
        ImageView imageView1 = new ImageView(new Image("file:control.png"));
        imageView1.setFitHeight(100);
        imageView1.setFitWidth(100);
        text1.setFont(Font.font("cooper black", 20));

        ImageView imageView2 = new ImageView(new Image("file:eatFood.png"));
        imageView2.setFitHeight(100);
        imageView2.setFitWidth(100);
        text2.setFont(Font.font("cooper black",20));

        ImageView imageView3 = new ImageView(new Image("file:suicide.png"));
        imageView3.setFitHeight(100);
        imageView3.setFitWidth(100);
        text3.setFont(Font.font("cooper black",20));

        ImageView imageView4 = new ImageView(new Image("file:wall.png"));
        imageView4.setFitHeight(100);
        imageView4.setFitWidth(100);
        text4.setFont(Font.font("cooper black",20));

        //Setting text (button)
        textBack.setFont(Font.font("cooper black",20));
        textBack.setFill(Color.RED);

        //Create gridPane to hole the image and text
        GridPane gridPane = new GridPane();
        gridPane.add(imageView1,0,0);
        gridPane.add(text1,0,1);
        gridPane.add(imageView2,0,2);
        gridPane.add(text2,0,3);
        gridPane.add(imageView3,1,0);
        gridPane.add(text3,1,1);
        gridPane.add(imageView4,1,2);
        gridPane.add(text4,1,3);
        gridPane.setVgap(5);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);

        //Create borderPane to hole the gridPane and textBack
        BorderPane borderPane2 = new BorderPane();
        borderPane2.setCenter(gridPane);
        borderPane2.setBottom(textBack);
        borderPane2.setPadding(new Insets(5,5,5,5));
        borderPane2.setBackground(new Background(new BackgroundFill(Color.PINK, CornerRadii.EMPTY, Insets.EMPTY)));
        Scene sceneHowToPlay = new Scene(borderPane2);
        Stage stageHowToPlay = new Stage();
        stageHowToPlay.setScene(sceneHowToPlay);
        stageHowToPlay.setTitle("How To Play");
        stageHowToPlay.setResizable(false);

        //Add text (button) function
        textStart.setOnMouseClicked(e -> {
            primaryStage.close();
            stageGame.show();
            startGame();
        });

        textHowToPlay.setOnMouseClicked(e -> {
            primaryStage.close();
            stageHowToPlay.show();
        });

        textQuit.setOnMouseClicked(e -> primaryStage.close());

        textBack.setOnMouseClicked(e -> {
            stageHowToPlay.close();
            primaryStage.show();
        });

        textPlayAgain.setOnMouseClicked(e -> {
            stageGameOver.close();
            primaryStage.show();
        });
    }

    //Start the game
    public void startGame() {
        newFood();

        VBox root = new VBox();
        c = new Canvas(width * cornerSize, height * cornerSize);
        gc = c.getGraphicsContext2D();
        root.getChildren().add(c);

        a = new AnimationTimer() {
            long lastTick = 0;

            public void handle(long now) {
                if (lastTick == 0) {
                    lastTick = now;
                    tick(gc);
                    return;
                }

                if (now - lastTick > 1000000000 / speed) {
                    lastTick = now;
                    tick(gc);
                }
            }
        };
        a.start();

        Scene sceneGame = new Scene(root, width * cornerSize, height * cornerSize);

        //Control event
        sceneGame.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            if ((key.getCode() == KeyCode.UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
            if ((key.getCode() == KeyCode.DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
            if ((key.getCode() == KeyCode.LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if ((key.getCode() == KeyCode.RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if (!paused) {
                if (key.getCode() == KeyCode.P) {
                    a.stop();
                    paused=true;
                }
            }
            else {
                a.start();
                paused = false;
            }

        });

        //Add initial snake parts
        snake.add(new Corner(width / 2, height / 2));
        snake.add(new Corner(width / 2, height / 2));
        snake.add(new Corner(width / 2, height / 2));
        stageGame.setScene(sceneGame);
        stageGame.setTitle("Playing");
        stageGame.setResizable(false);
    }

    public static class Corner {
        int x;
        int y;

        public Corner(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    //Tick
    public void tick(GraphicsContext gc) {
        if (gameOver) {
            gameOver();
            return;
        }

        //Set background color
        gc.setFill(Color.PINK);
        gc.fillRect(0, 0, width * cornerSize, height * cornerSize);

        //Display Score
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("cooper black", 20));
        gc.fillText("Score: " + score, 5, 20);

        //Set food color
        Color cc = Color.RED;
        gc.setFill(cc);
        gc.fillOval(foodX * cornerSize, foodY * cornerSize, cornerSize, cornerSize);

        //Set snake color
        for (Corner c : snake) {
            gc.setFill(Color.PALEGREEN);
            gc.fillRect(c.x * cornerSize, c.y * cornerSize, cornerSize - 2, cornerSize - 2);
        }

        for (int i = snake.size() - 1; i >= 1; i--) {
            snake.get(i).x = snake.get(i - 1).x;
            snake.get(i).y = snake.get(i - 1).y;
        }

        //Set direction
        if (upDirection) {
            snake.get(0).y--;
            if (snake.get(0).y < 0) {
                gameOver = true;
            }
        }
        if (downDirection) {
            snake.get(0).y++;
            if (snake.get(0).y >= height) {
                gameOver = true;
            }
        }
        if (leftDirection) {
            snake.get(0).x--;
            if (snake.get(0).x < 0) {
                gameOver = true;
            }
        }
        if (rightDirection) {
            snake.get(0).x++;
            if (snake.get(0).x >= width) {
                gameOver = true;
            }
        }

        //Eat food and add length of snake
        if (foodX == snake.get(0).x && foodY == snake.get(0).y) {
            snake.add(new Corner(-1, -1));
            newFood();
        }

        //Collide body
        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y) {
                gameOver = true;
                break;
            }
        }
    }

    //Random food location and score++
    public static void newFood() {
        start:
        while (true) {
            foodX = random.nextInt(width);
            foodY = random.nextInt(height);

            for (Corner c : snake) {
                if (c.x == foodX && c.y == foodY) {
                    continue start;
                }
            }
            score++;
            break;
        }
    }

    //If gameOver
    public static void gameOver() {
        stageGame.close();

        //Create vBox to hole textGameOver, score and textPlayAgain
        VBox vBox = new VBox();
        textGameOver.setFont(Font.font("cooper black",50));
        textGameOver.setFill(Color.RED);
        textPlayAgain.setFont(Font.font("cooper black",20));
        textPlayAgain.setFill(Color.WHITESMOKE);
        Text textScore = new Text("Score: " + score);
        textScore.setFont(Font.font("cooper black",20));
        textScore.setFill(Color.WHITESMOKE);

        //Create a text when score >= 20 or score >= 10
        textExcellent.setFont(Font.font("cooper black",20));
        textExcellent.setFill(Color.WHITESMOKE);
        if (score >= 20) {
            textExcellent.setText("Excellent!!!");
        }else if (score >= 10) {
            textExcellent.setText("Good try~");
        }

        vBox.getChildren().addAll(textGameOver, textScore, textExcellent, textPlayAgain);
        vBox.setAlignment(Pos.CENTER);

        //Use borderPane hold the vBox
        BorderPane borderPaneGameOver = new BorderPane();
        borderPaneGameOver.setCenter(vBox);
        borderPaneGameOver.setBackground(new Background(new BackgroundFill(Color.PINK, CornerRadii.EMPTY, Insets.EMPTY)));

        //Create scene for game over
        Scene sceneGameOver = new Scene(borderPaneGameOver);
        stageGameOver.setScene(sceneGameOver);
        stageGameOver.setTitle("Game Over");
        stageGameOver.setHeight(300);
        stageGameOver.setWidth(350);
        stageGameOver.setResizable(false);
        stageGameOver.show();

        //Reset the game
        gameOver=false;
        a.stop();
        snake.clear();
        score = 0;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
