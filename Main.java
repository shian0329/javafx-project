package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends Application {
    private static final Text textTittle = new Text("Snake Game");
    private static final Text textGameOver = new Text("Game Over");
    private static final Button buttonPlayAgain = new Button("Play Again");
    private static final Text textStart = new Text("Start");
    private static final Text textHowToPlay = new Text("How To Play");
    private static final Text textQuit = new Text("Quit");
    private static final Text textBack = new Text("Back");
    private static final Text text1 = new Text("Use keyboard to control");
    private static final Text text2 = new Text("Eat food to get higher mark");
    private static final Text text3 = new Text("Cannot collide with your body");
    private static final Text text4 = new Text("Cannot collide the wall");

    //variable for snake
    static int speed = 6;
    static int score = 0;
    static int width = 24;
    static int height = 20;
    static int foodX = 0;
    static int foodY = 0;
    static int cornerSize = 15;
    static List<Corner> snake = new ArrayList<>();
    static Dir direction = Dir.left;
    static boolean gameOver = false;
    static Random rand = new Random();
    static Stage stageGameOver = new Stage();
    static Stage stageGame = new Stage();
    static AnimationTimer a;
    static GraphicsContext gc;
    static Canvas c;

    @Override
    public void start(Stage primaryStage) {
        //Create button
        Rectangle rectangleStart = new Rectangle();
        rectangleStart.setFill(Color.RED);
        rectangleStart.setWidth(100);
        rectangleStart.setHeight(30);
        rectangleStart.setX(110);
        rectangleStart.setY(40);
        textStart.setX(140);
        textStart.setY(60);
        textStart.setFont(Font.font("cooper black",15));

        Rectangle rectangleHowToPlay = new Rectangle();
        rectangleHowToPlay.setFill(Color.CYAN);
        rectangleHowToPlay.setWidth(100);
        rectangleHowToPlay.setHeight(30);
        rectangleHowToPlay.setX(110);
        rectangleHowToPlay.setY(75);
        textHowToPlay.setX(112);
        textHowToPlay.setY(95);
        textHowToPlay.setFont(Font.font("cooper black",15));

        Rectangle rectangleQuit = new Rectangle();
        rectangleQuit.setFill(Color.RED);
        rectangleQuit.setWidth(100);
        rectangleQuit.setHeight(30);
        rectangleQuit.setX(110);
        rectangleQuit.setY(110);
        textQuit.setX(142);
        textQuit.setY(130);
        textQuit.setFont(Font.font("cooper black",15));

        //Create vBox to hold buttons
        Pane paneButton = new Pane();
        paneButton.getChildren().addAll(rectangleStart, textStart, rectangleHowToPlay, textHowToPlay, rectangleQuit, textQuit);

        //Create stackPane to hold the text(tittle)
        StackPane stackPane = new StackPane();
        textTittle.setFont(Font.font("cooper black",50));
        textTittle.setFill(Color.BLUE);
        stackPane.getChildren().add(textTittle);
        stackPane.setPadding(new Insets(2, 10, 0, 10));
        stackPane.setAlignment(Pos.CENTER);

        //Create borderPane
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(0, 10, 100, 10));
        borderPane.setCenter(paneButton);
        borderPane.setTop(stackPane);
        borderPane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        //Create first scene
        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Snake Game");

        primaryStage.show();
        primaryStage.setResizable(false);

        //Create scene (how to play)
        ImageView imageView1 = new ImageView(new Image("image/control.png"));
        imageView1.setFitHeight(100);
        imageView1.setFitWidth(100);
        text1.setFont(Font.font("cooper black", 20));

        ImageView imageView2 = new ImageView(new Image("image/eatFood.png"));
        imageView2.setFitHeight(100);
        imageView2.setFitWidth(100);
        text2.setFont(Font.font("cooper black",20));

        ImageView imageView3 = new ImageView(new Image("image/suicide.png"));
        imageView3.setFitHeight(100);
        imageView3.setFitWidth(100);
        text3.setFont(Font.font("cooper black",20));

        ImageView imageView4 = new ImageView(new Image("image/wall.png"));
        imageView4.setFitHeight(100);
        imageView4.setFitWidth(100);
        text4.setFont(Font.font("cooper black",20));

        //Create button
        textBack.setFont(Font.font("cooper black",20));
        textBack.setFill(Color.RED);

        GridPane gridPane = new GridPane();
        gridPane.add(text1,0,1);
        gridPane.add(text2,0,3);
        gridPane.add(imageView1,0,0);
        gridPane.add(imageView2,0,2);
        gridPane.add(imageView3,1,0);
        gridPane.add(imageView4,1,2);
        gridPane.add(text3,1,1);
        gridPane.add(text4,1,3);
        gridPane.setVgap(5);
        gridPane.setHgap(8);
        gridPane.setAlignment(Pos.CENTER);

        BorderPane borderPane2 = new BorderPane();
        borderPane2.setCenter(gridPane);
        borderPane2.setBottom(textBack);
        borderPane2.setPadding(new Insets(5,5,5,5));
        Scene sceneHowToPlay = new Scene(borderPane2);
        Stage stage2 = new Stage();
        stage2.setScene(sceneHowToPlay);
        stage2.setTitle("How To Play");
        stage2.setResizable(false);

        //Add button function
        rectangleStart.setOnMouseClicked(e -> {
            primaryStage.close();
            stageGame.show();
            startGame();
        });
        textStart.setOnMouseClicked(e -> {
            primaryStage.close();
            stageGame.show();
            startGame();
        });

        rectangleHowToPlay.setOnMouseClicked(e -> {
            primaryStage.close();
            stage2.show();
        });
        textHowToPlay.setOnMouseClicked(e -> {
            primaryStage.close();
            stage2.show();
        });

        rectangleQuit.setOnMouseClicked(e -> primaryStage.close());
        textQuit.setOnMouseClicked(e -> primaryStage.close());

        textBack.setOnMouseClicked(e -> {
            stage2.close();
            primaryStage.show();
        });

        buttonPlayAgain.setOnAction(e -> {
            stageGameOver.close();
            primaryStage.show();
        });
    }

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

        // control
        sceneGame.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            if (key.getCode() == KeyCode.UP) {
                direction = Dir.up;
            }
            if (key.getCode() == KeyCode.LEFT) {
                direction = Dir.left;
            }
            if (key.getCode() == KeyCode.DOWN) {
                direction = Dir.down;
            }
            if (key.getCode() == KeyCode.RIGHT) {
                direction = Dir.right;
            }
        });

        // add start snake parts
        snake.add(new Corner(width / 2, height / 2));
        snake.add(new Corner(width / 2, height / 2));
        snake.add(new Corner(width / 2, height / 2));
        stageGame.setScene(sceneGame);
        stageGame.setTitle("Playing");
    }

    public enum Dir {
        left, right, up, down
    }

    public static class Corner {
        int x;
        int y;

        public Corner(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    // tick
    public void tick(GraphicsContext gc) {
        if (gameOver) {
            gameOver();
            return;
        }

        for (int i = snake.size() - 1; i >= 1; i--) {
            snake.get(i).x = snake.get(i - 1).x;
            snake.get(i).y = snake.get(i - 1).y;
        }

        switch (direction) {
            case up -> {
                snake.get(0).y--;
                if (snake.get(0).y < 0) {
                    gameOver = true;
                }
            }
            case down -> {
                snake.get(0).y++;
                if (snake.get(0).y > height) {
                    gameOver = true;
                }
            }
            case left -> {
                snake.get(0).x--;
                if (snake.get(0).x < 0) {
                    gameOver = true;
                }
            }
            case right -> {
                snake.get(0).x++;
                if (snake.get(0).x > width) {
                    gameOver = true;
                }
            }
        }

        // eat food
        if (foodX == snake.get(0).x && foodY == snake.get(0).y) {
            snake.add(new Corner(-1, -1));
            newFood();
        }

        // self destroy
        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y) {
                gameOver = true;
                break;
            }
        }

        // background
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width * cornerSize, height * cornerSize);

        // score
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("", 20));
        gc.fillText("Score: " + score, 10, 30);

        // random foodColor
        Color cc = Color.RED;
        gc.setFill(cc);
        gc.fillOval(foodX * cornerSize, foodY * cornerSize, cornerSize, cornerSize);

        // snake
        for (Corner c : snake) {
            gc.setFill(Color.GREEN);
            gc.fillRect(c.x * cornerSize, c.y * cornerSize, cornerSize - 2, cornerSize - 2);
        }
    }

    // food
    public static void newFood() {
        start:
        while (true) {
            foodX = rand.nextInt(width);
            foodY = rand.nextInt(height);

            for (Corner c : snake) {
                if (c.x == foodX && c.y == foodY) {
                    continue start;
                }
            }
            score++;
            break;
        }
    }

    public static void gameOver() {
        stageGame.close();

        //Create vBox to hole textGameOver and buttonPlayAgain
        VBox vBox = new VBox();
        textGameOver.setFont(Font.font(50));
        textGameOver.setFill(Color.RED);
        vBox.getChildren().addAll(textGameOver, buttonPlayAgain);
        vBox.setAlignment(Pos.CENTER);
        BorderPane borderPaneGameOver = new BorderPane();
        borderPaneGameOver.setCenter(vBox);

        //Create scene for game over
        Scene sceneGameOver = new Scene(borderPaneGameOver);
        stageGameOver.setScene(sceneGameOver);
        stageGameOver.setTitle("Game Over");
        stageGameOver.setHeight(300);
        stageGameOver.setWidth(350);
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