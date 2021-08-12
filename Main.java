package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("SudokuSolver");
        TextField[] sudoku = new TextField[81];
        Sudoku currentSudoku = new Sudoku(new int[81]);

        Label instructions = new Label("fill in the known values\npress 'show hint' to reveal the next step\npress 'show all' to reveal the solution");

        //reveals next field with only one possible value
        Button hint = new Button("show hint");
        hint.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int[] data = new int[81];
                int hintI;
                for (int i=0; i<sudoku.length;i++) {
                    if (sudoku[i].getText().length() == 0) {
                        data[i] = 0;
                        continue;
                    }
                    data[i] = Integer.parseInt(sudoku[i].getText());
                }
                hintI = Sudoku.sudokuHint(data, currentSudoku);
                System.out.println(hintI);

                if (hintI>0) {
                    sudoku[hintI].setStyle("-fx-background-color: #9ACD32");
                }
            }
        });

        //reveals the entire solution
        Button solve = new Button("show all");
        solve.setOnAction(actionEvent -> {
            int[] data = new int[81];
            for (int i=0; i<sudoku.length;i++) {
                if (sudoku[i].getText().length() == 0) {
                    data[i] = 0;
                    continue;
                }
                data[i] = Integer.parseInt(sudoku[i].getText());
            }
            data = Sudoku.sudokuSolve(data, currentSudoku);
            for (int i=0; i<sudoku.length;i++) {
                sudoku[i].setText(Integer.toString(data[i]));
            }
        });

        //setting up layout
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(10,10,10,10));

        HBox buttons = new HBox();
        buttons.setSpacing(10);
        buttons.getChildren().addAll(hint, solve);

        GridPane sudokuGrid = new GridPane();
        sudokuGrid.setVgap(0.5);
        sudokuGrid.setHgap(0.5);

        //initializing sudoku grid
        for (int i=0; i<sudoku.length; i++) {
            sudoku[i] = new TextField();
            sudokuGrid.add(sudoku[i], (i%9)*10, (i/9)*10);
            TextField sudokuField = sudoku[i];
            //only allow single digit integer values in the sudoku
            sudokuField.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    if (!t1.matches("\\d*")) {
                        sudokuField.setText(t1.replaceAll("[^\\d]",""));
                    }
                    if (t1.length()>1) {
                        sudokuField.setText(s);
                    }
                }
            });
        }

        layout.setTop(instructions);
        layout.setCenter(sudokuGrid);
        layout.setBottom(buttons);

        Scene scene = new Scene(layout, 500, 650);
        scene.getStylesheets().add(getClass().getResource("GUI.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
