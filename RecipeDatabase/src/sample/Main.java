package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main extends Application {
    private Recipe recipe;
    private Ingredient ingredient;

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        BorderPane mainPane = new BorderPane();
        HBox hBox = new HBox();

        ingredient= new Ingredient();
        recipe = new Recipe();
        try {
            connectToDB();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }


        Button buttonSelect = new Button("All Recipe");
        Button buttonAdd = new Button("Add Recipe");
        Button buttonFind = new Button("Find Recipe");
        Button buttonGoToIngredient = new Button("Ingredient");
        Button buttonAllIngredients = new Button("All Ingredients");
        Button buttonBack = new Button("Back");

        TextField textField = new TextField();
        
        hBox.getChildren().addAll(buttonAdd,buttonFind,buttonSelect,buttonAllIngredients,buttonGoToIngredient,textField);
        hBox.setPadding(new Insets(15,12,15,12));
        hBox.setSpacing(10);
        
        mainPane.setTop(hBox);
        Scene scene = new Scene(mainPane, 500, 500);
        buttonAllIngredients.setOnAction(event -> {
            try {

                GridPane selectPane = new GridPane();
                Text text = new Text(10,50, ingredient.getAllIngredients() );
                selectPane.add(buttonBack,1,2);
                selectPane.add(text,1,1);
                ScrollPane selectScrollPane = new ScrollPane(selectPane);
                scene.setRoot(selectScrollPane);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        buttonBack.setOnAction(event -> scene.setRoot(mainPane));

        buttonGoToIngredient.setOnAction(event -> {
           int total  = Integer.parseInt( textField.getText());
           GridPane ingredientPane = new GridPane();
            ArrayList<TextField> textFieldArrayListName = new ArrayList<TextField>();
            ArrayList<TextField> textFieldArrayListAmount = new ArrayList<TextField>();

            TextField recipeForIngredient= new TextField();
            Label recipeLabel = new Label("Recipe");
           for (int i=0;i<total;i++){
               TextField textFieldIngredientName = new TextField();
               Label labelName = new Label((i+1)+". Name:");
               TextField textFieldIngredientAmount = new TextField();
               Label labelAmount = new Label("Amount:");

               textFieldArrayListName.add(textFieldIngredientName);
               textFieldArrayListAmount.add(textFieldIngredientAmount);

               ingredientPane.add(labelName,1,i+2);
               ingredientPane.add(textFieldIngredientName,2,i+2);

               ingredientPane.add(labelAmount,3,i+2);
               ingredientPane.add(textFieldIngredientAmount,4,i+2);
           }
           Button addIngredient = new Button("Add Ingredients");
           addIngredient.setOnAction(event1 -> {
               try {

                   ingredient.addIngredients(textFieldArrayListName,recipeForIngredient.getText(),textFieldArrayListAmount,total);
               } catch (SQLException e) {
                   e.printStackTrace();
               }
           });
            ingredientPane.add(recipeLabel,1,1);
            ingredientPane.add(recipeForIngredient,2,1);
            ingredientPane.add(buttonBack,2,total+2);
            ingredientPane.add(addIngredient,5,total+2);
            ScrollPane scrollPaneIngredient = new ScrollPane(ingredientPane);
            scene.setRoot(scrollPaneIngredient);
        });

        buttonFind.setOnAction(event -> {
            try {
                String output = textField.getText();
                if(output.equals("")){
                    Alert a = new Alert(Alert.AlertType.WARNING);
                    a.setContentText("Cannot have a blank search.");
                    a.showAndWait();
                }
                else {

                    GridPane findPane = new GridPane();
                    Text text = new Text(10, 50, recipe.searchRecipe(textField.getText()));

                    findPane.add(buttonBack, 1, 2);
                    findPane.add(text, 1, 1);
                    ScrollPane findScrollPane = new ScrollPane(findPane);
                    scene.setRoot(findScrollPane);

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        buttonSelect.setOnAction(event -> {
            try {
                GridPane selectPane = new GridPane();
                Text text = new Text(10,50,recipe.getAllRecipes() + "Total: "+recipe.getTotalNumberOfRecipes());
                selectPane.add(buttonBack,1,2);
                selectPane.add(text,1,1);
                ScrollPane selectScrollPane = new ScrollPane(selectPane);
                scene.setRoot(selectScrollPane);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        buttonAdd.setOnAction(event -> {

            try {
                String output = textField.getText();
                if(output.equals("")){
                    Alert a = new Alert(Alert.AlertType.WARNING);
                    a.setContentText("Cannot have a blank recipe.");
                    a.showAndWait();
                }
                else {
                    recipe.addRecipe(output);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        /*mainPane.add(buttonAdd,1,2);
        mainPane.add(buttonSelect,2,2);
        mainPane.add(buttonFind,3,2);
        mainPane.add(textField,1,1);
*/
        primaryStage.setTitle("Recipe Program");
        primaryStage.setScene(scene);
        primaryStage.show();


    }


    public static void main(String[] args) {
        launch(args);
    }
    private void connectToDB() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/recipes", "root", "rootpw");
        recipe.setConnection(connection);
        ingredient.setConnection(connection);
    }
}
