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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Calendar;

public class Main extends Application {
  private  Expenses expensesController;

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        BorderPane mainPane = new BorderPane();
        HBox hBox = new HBox();
        expensesController = new Expenses();

        try {
            connectToDB();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        Button buttonSelect = new Button("All Expenses");
        Button buttonAdd = new Button("Add Expense");
        Button buttonFind = new Button("Find Store");
        Button buttonBack = new Button("Back");
        Button buttonPriceOptions = new Button("Price Options");

        TextField textField = new TextField();
        Label searchBar = new Label("Search: ");
        hBox.getChildren().addAll(buttonAdd,buttonFind,buttonSelect,buttonPriceOptions);
        hBox.setPadding(new Insets(15,12,15,12));
        hBox.setSpacing(10);
        GridPane search= new GridPane();
        search.add(searchBar,1,1);
        search.add(textField,2,1);
        mainPane.setLeft(search);
        mainPane.setTop(hBox);

        Scene scene = new Scene(mainPane, 500, 500);

        buttonBack.setOnAction(event -> scene.setRoot(mainPane));

        buttonSelect.setOnAction(event -> {
            try {
                GridPane selectPane = new GridPane();
                Text text = new Text(10,50,expensesController.getAllExpenses() );
                selectPane.add(text,1,2);
                selectPane.add(buttonBack,1,3);

                ScrollPane selectScrollPane = new ScrollPane(selectPane);
                scene.setRoot(selectScrollPane);
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
                    Text text = new Text(10, 50, expensesController.getAllStores(output));

                    findPane.add(buttonBack, 1, 2);
                    findPane.add(text, 1, 1);
                    ScrollPane findScrollPane = new ScrollPane(findPane);
                    scene.setRoot(findScrollPane);

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        buttonAdd.setOnAction(event -> {
                    GridPane addPane = new GridPane();
                    Button buttonSubmit = new Button("Submit");
                    addPane.add(buttonBack,1,5);
                    TextField store = new TextField();
                    TextField price = new TextField();
                    TextField date = new TextField();
                    Label storeLabel = new Label("Store: ");
                    Label priceLabel = new Label("Price: ");
                    Label dateLabel = new Label("Date(dd/mm/yyyy): ");
                    addPane.add(storeLabel,1,1);
                    addPane.add(priceLabel,1,2);
                    addPane.add(dateLabel,1,3);
                    addPane.add(store,2,1);
                    addPane.add(price,2,2);
                    addPane.add(date,2,3);
                    addPane.add(buttonSubmit,1,4);

                    buttonSubmit.setOnAction(event1 ->
                    {
                        if(store.getText().equals("")||price.getText().equals("")||date.getText().equals("")){
                            Alert a = new Alert(Alert.AlertType.WARNING);
                            a.setContentText("Cannot have a blank fields.");
                            a.showAndWait();
                        }
                        else {
                            double priceStringToDouble = Double.parseDouble(price.getText());
                            try {
                                if(properDate(date.getText())) {
                                    expensesController.addExpense(store.getText(), priceStringToDouble, date.getText());
                                    Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                                    a.setContentText("Successfully added!");
                                    a.showAndWait();
                                }
                                else{
                                    Alert a = new Alert(Alert.AlertType.WARNING);
                                    a.setContentText("Incorrect Date or Format");
                                    a.showAndWait();
                                }
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                        }
                    });

                    ScrollPane addScrollPane = new ScrollPane(addPane);
                    scene.setRoot(addScrollPane);
        });

        buttonPriceOptions.setOnAction(event -> {
            BorderPane totalPaneFormat = new BorderPane();
            GridPane totalPane = new GridPane();
            HBox totalButtonPane = new HBox();
            GridPane searchBarsPane = new GridPane();
            Button buttonTotalPriceOfAllItems = new Button("Price All Items");
            Button buttonTotalPriceFromSpecificStores = new Button("Price from Stores");
            TextField storeOption = new TextField();
            Label storeOptionLabel= new Label("Store: ");
            buttonTotalPriceOfAllItems.setOnAction(event1 -> {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                try {
                    BigDecimal totalCost = BigDecimal.valueOf(expensesController.getTotalCost()).setScale(2, RoundingMode.HALF_UP);
                    a.setContentText("Total Price: "+totalCost);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                a.showAndWait();
            });
            buttonTotalPriceFromSpecificStores.setOnAction(event1 -> {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                try {
                    BigDecimal totalCost = BigDecimal.valueOf(expensesController.getPriceFromSpecificStores(storeOption.getText())).setScale(2, RoundingMode.HALF_UP);
                    a.setContentText("Total Price: "+totalCost);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                a.showAndWait();
            });
            totalButtonPane.getChildren().addAll(buttonTotalPriceOfAllItems,buttonTotalPriceFromSpecificStores);
            totalButtonPane.setPadding(new Insets(15,12,15,12));
            totalButtonPane.setSpacing(10);
            totalPane.add(storeOptionLabel,1,1);
            totalPane.add(storeOption,2,1);
            totalPane.add(buttonBack, 1, 2);
            totalPaneFormat.setLeft(totalPane);
            totalPaneFormat.setTop(totalButtonPane);
            scene.setRoot(totalPaneFormat);

        });

        primaryStage.setTitle("Expense Program");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
    private void connectToDB() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/expense", "root", "root");
        expensesController.setConnection(connection);

    }
    private boolean properDate(String date){
        String[] dates = date.split("/");
        if(dates.length!=3){
            return false;
        }
       int year = Calendar.getInstance().get(Calendar.YEAR);

        return Integer.parseInt(dates[0]) <= 31 && Integer.parseInt(dates[1]) <= 12 && Integer.parseInt(dates[2]) <= year;
    }
}