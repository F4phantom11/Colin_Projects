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

        Button buttonAdd = new Button("Add Expense");
        Button buttonFindOptions = new Button("Find Store Options");
        Button buttonBack = new Button("Back");
        Button buttonPriceOptions = new Button("Price Options");
        Button buttonLimitOptions = new Button("Limit Options");
        GridPane search= new GridPane();
        Scene scene = new Scene(mainPane, 500, 500);
        expensesController = new Expenses();

        try {
            connectToDB();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setContentText(expensesController.getTotalsAndLimitPerson());
        a.showAndWait();
        hBox.getChildren().addAll(buttonAdd,buttonFindOptions,buttonPriceOptions,buttonLimitOptions);
        hBox.setPadding(new Insets(15,12,15,12));
        hBox.setSpacing(10);

        mainPane.setLeft(search);
        mainPane.setTop(hBox);

        setBackButton(buttonBack,scene,mainPane);
        setFindExpensesOptions(buttonFindOptions,buttonBack,scene);
        setAddExpenses(buttonAdd,buttonBack,scene);
        setPriceButton(buttonPriceOptions,buttonBack,scene);
        setLimitOptions(buttonLimitOptions,buttonBack,scene);

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
    private void setBackButton(Button buttonBack, Scene scene,  BorderPane mainPane) {
        buttonBack.setOnAction(event -> scene.setRoot(mainPane));
    }

    private void setAddExpenses(Button buttonAdd, Button buttonBack, Scene scene){
        buttonAdd.setOnAction(event -> {
            GridPane addPane = new GridPane();
            Button buttonSubmit = new Button("Submit");
            addPane.add(buttonBack,1,6);
            TextField store = new TextField();
            TextField price = new TextField();
            TextField date = new TextField();
            TextField name = new TextField();
            Label storeLabel = new Label("Store: ");
            Label priceLabel = new Label("Price: ");
            Label dateLabel = new Label("Date(dd/mm/yyyy): ");
            Label nameLabel = new Label("Name: ");
            addPane.add(storeLabel,1,1);
            addPane.add(priceLabel,1,2);
            addPane.add(nameLabel,1,3);
            addPane.add(dateLabel,1,4);
            addPane.add(store,2,1);
            addPane.add(price,2,2);
            addPane.add(name,2,3);
            addPane.add(date,2,4);
            addPane.add(buttonSubmit,1,5);

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
                            expensesController.addExpense(store.getText(), priceStringToDouble, date.getText(), name.getText());
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
    }

    private void setFindExpensesOptions(Button buttonFind, Button buttonBack, Scene scene){
        buttonFind.setOnAction(event -> {
            BorderPane findPaneFormat = new BorderPane();
            GridPane findPane = new GridPane();
            HBox findButtonPane = new HBox();
            Button buttonAllExpenses = new Button("All Expenses");
            Button buttonFindByStore = new Button("Find by Store");
            Button buttonFindByName = new Button("Find by Name");
            Button buttonFindByDate =  new Button("Find by Date");
            Button buttonBackLocal = new Button("Back");
            TextField store = new TextField();
            TextField date = new TextField();
            TextField name = new TextField();
            Label storeLabel = new Label("Store: ");
            Label dateLabel = new Label("Date(dd/mm/yyyy): ");
            Label nameLabel = new Label("Name: ");
            Label dayLabel = new Label("Day: ");
            Label monthLabel = new Label("Month: ");
            Label yearLabel = new Label("Year: ");
            CheckBox dayCheckbox = new CheckBox();
            CheckBox monthCheckbox = new CheckBox();
            CheckBox yearCheckbox = new CheckBox();


            findPane.add(storeLabel,1,1);
            findPane.add(nameLabel,1,3);
            findPane.add(dateLabel,1,4);
            findPane.add(store,2,1);
            findPane.add(name,2,3);
            findPane.add(date,2,4);
            findPane.add(dayLabel,1,5);
            findPane.add(monthLabel,1,6);
            findPane.add(yearLabel,1,7);
            findPane.add(dayCheckbox,2,5);
            findPane.add(monthCheckbox,2,6);
            findPane.add(yearCheckbox,2,7);

            findPane.add(buttonBack, 1, 9);


            findButtonPane.getChildren().addAll(buttonAllExpenses,buttonFindByStore,buttonFindByName,buttonFindByDate);
            findButtonPane.setPadding(new Insets(15,12,15,12));
            findButtonPane.setSpacing(10);
            findPaneFormat.setTop(findButtonPane);
            findPaneFormat.setLeft(findPane);
            scene.setRoot(findPaneFormat);

            buttonBackLocal.setOnAction(event1 -> { scene.setRoot(findPaneFormat); });

          buttonAllExpenses.setOnAction(event1 -> {
              try {
                  GridPane selectPane = new GridPane();
                  Text text = new Text(10,50,expensesController.getAllExpenses() );
                  selectPane.add(text,1,2);
                  selectPane.add(buttonBackLocal,1,3);

                  ScrollPane selectScrollPane = new ScrollPane(selectPane);
                  scene.setRoot(selectScrollPane);
              } catch (SQLException e) {
                  e.printStackTrace();
              }
        });
          buttonFindByDate.setOnAction(event1 -> {
              String output = date.getText();
              if (output.equals("")) {
                  Alert a = new Alert(Alert.AlertType.WARNING);
                  a.setContentText("Cannot have store field blank.");
                  a.showAndWait();
              }
                  else
               {
                  GridPane selectPane = new GridPane();
                  Text text = null;
                  try {
                      boolean dayResult = dayCheckbox.isSelected();
                      boolean monthResult = monthCheckbox.isSelected();
                      boolean yearResult = yearCheckbox.isSelected();

                      if(dayResult&&!monthResult&&!yearResult) { // day
                          text = new Text(10, 50, expensesController.getAllExpensesByDate(date.getText(), DateCombo.DAY));
                      }
                      else if(!dayResult&&monthResult&&!yearResult) { // month
                          text = new Text(10, 50, expensesController.getAllExpensesByDate(date.getText(), DateCombo.MONTH));
                      }
                      else if(!dayResult&&!monthResult&&yearResult) { // year
                          text = new Text(10, 50, expensesController.getAllExpensesByDate(date.getText(), DateCombo.YEAR));
                      }
                      else if(dayResult&&monthResult&&!yearResult) { // day month
                          text = new Text(10, 50, expensesController.getAllExpensesByDate(date.getText(), DateCombo.DAY_MONTH));
                      }
                      else if(dayResult&&!monthResult&&yearResult) { // day year
                          text = new Text(10, 50, expensesController.getAllExpensesByDate(date.getText(), DateCombo.DAY_YEAR));
                      }
                      else if(!dayResult&&monthResult&&yearResult) { // month year
                          text = new Text(10, 50, expensesController.getAllExpensesByDate(date.getText(), DateCombo.MONTH_YEAR));
                      }
                      else if(dayResult&&monthResult&&yearResult) { // day month year
                          text = new Text(10, 50, expensesController.getAllExpensesByDate(date.getText(), DateCombo.DAY_MONTH_YEAR));
                      }
                      else if(!dayCheckbox.isSelected() &&
                      !monthCheckbox.isSelected()&&
                      !yearCheckbox.isSelected()){
                          Alert a = new Alert(Alert.AlertType.WARNING);
                          a.setContentText("Cannot have store field blank.");
                          a.showAndWait();
                          text = new Text(10,50,"No Checkbox specified");
                      }
                  } catch (SQLException throwables) {
                      throwables.printStackTrace();
                  }
                  selectPane.add(text, 1, 2);
                  selectPane.add(buttonBackLocal, 1, 3);

                  ScrollPane selectScrollPane = new ScrollPane(selectPane);
                  scene.setRoot(selectScrollPane);
              }

          });
          buttonFindByStore.setOnAction(event1 -> {
              try {
                  String output = store.getText();
                  if (output.equals("")) {
                      Alert a = new Alert(Alert.AlertType.WARNING);
                      a.setContentText("Cannot have store field blank.");
                      a.showAndWait();
                  } else {
                      GridPane selectPane = new GridPane();
                      Text text = new Text(10, 50, expensesController.getAllExpensesByStores(output));
                      selectPane.add(text, 1, 2);
                      selectPane.add(buttonBackLocal, 1, 3);

                      ScrollPane selectScrollPane = new ScrollPane(selectPane);
                      scene.setRoot(selectScrollPane);
                  }
              }
                  catch(SQLException e){
                          e.printStackTrace();
                }

          });

          buttonFindByName.setOnAction(event1 -> {
              String output = name.getText();
              if (output.equals("")) {
                  Alert a = new Alert(Alert.AlertType.WARNING);
                  a.setContentText("Cannot have store field blank.");
                  a.showAndWait();
              } else {
                  GridPane selectPane = new GridPane();
                  Text text = null;
                  try {
                      text = new Text(10, 50, expensesController.getAllExpensesByFamilyName(output));
                  } catch (SQLException throwables) {
                      throwables.printStackTrace();
                  }
                  selectPane.add(text, 1, 2);
                  selectPane.add(buttonBackLocal, 1, 3);

                  ScrollPane selectScrollPane = new ScrollPane(selectPane);
                  scene.setRoot(selectScrollPane);
              }


          });
        });


    }

    private void setLimitOptions(Button buttonLimitOptions, Button buttonBack, Scene scene){
        buttonLimitOptions.setOnAction(event -> {
            BorderPane totalPaneFormat = new BorderPane();
            GridPane totalPane = new GridPane();
            HBox totalButtonPane = new HBox();
            Button buttonSetSpendingLimit = new Button("Set Spending Limit");
            Button buttonGetTotalAndLimit =  new   Button("Everyone's Limit");
            Button buttonGetTotalAndLimitForPerson= new Button("Specific Person Limit");

            TextField totalLimit = new TextField();
            Label totalLimitLabel= new Label("Limit: ");
            TextField totalLimitName = new TextField();
            Label totalLimitNameLabel= new Label("Name: ");
            buttonGetTotalAndLimit.setOnAction(event1 -> {
                Alert a = new Alert(Alert.AlertType.WARNING);
                try {
                    a.setContentText(expensesController.getTotalsAndLimitPerson());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                a.showAndWait();
            });
            buttonGetTotalAndLimitForPerson.setOnAction(event1 -> {
                Alert a = new Alert(Alert.AlertType.WARNING);
                try {
                    a.setContentText(expensesController.getTotalAndLimitSpecificPerson(totalLimitName.getText()));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                a.showAndWait();
            });
            buttonSetSpendingLimit.setOnAction(event1 -> {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                try {

                    expensesController.setSpendingLimit(Double.parseDouble(totalLimit.getText()),totalLimitName.getText());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                a.showAndWait();
            });

            totalButtonPane.getChildren().addAll(buttonSetSpendingLimit,buttonGetTotalAndLimit,buttonGetTotalAndLimitForPerson);
            totalButtonPane.setPadding(new Insets(15,12,15,12));
            totalButtonPane.setSpacing(10);
            totalPane.add(totalLimitLabel,1,1);
            totalPane.add(totalLimit,2,1);
            totalPane.add(totalLimitNameLabel,1,2);
            totalPane.add(totalLimitName,2,2);
            totalPane.add(buttonBack, 1, 5);
            totalPaneFormat.setLeft(totalPane);
            totalPaneFormat.setTop(totalButtonPane);
            scene.setRoot(totalPaneFormat);

        });

    }

    private void setPriceButton(Button buttonPriceOptions, Button buttonBack, Scene scene){
        buttonPriceOptions.setOnAction(event -> {
            BorderPane totalPaneFormat = new BorderPane();
            GridPane totalPane = new GridPane();
            HBox totalButtonPane = new HBox();
            GridPane searchBarsPane = new GridPane();
            Button buttonTotalPriceOfAllItems = new Button("Price All Items");
            Button buttonTotalPriceFromSpecificStores = new Button("Price from Stores");
            TextField storeOption = new TextField();
            Label storeOptionLabel= new Label("Store: ");
            TextField priceOption = new TextField();
            Button buttonPriceRange = new Button("Price Range");
            Label priceOptionLabel= new Label("Price: ");
            Label greaterThanLabel = new Label("Greater Than: ");
            Label lessThanLabel = new Label("Less Than: ");
            Label equalToLabel = new Label("Equal To: ");
            CheckBox greaterThanCheckbox = new CheckBox();
            CheckBox lessThanCheckbox = new CheckBox();
            CheckBox equalToCheckbox = new CheckBox();

            Button buttonBackLocal = new Button("Back");
            buttonBackLocal.setOnAction(event1 -> {
                        scene.setRoot(totalPaneFormat);

                    }

            );
            buttonTotalPriceOfAllItems.setOnAction(event1 -> {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                try {
                    BigDecimal totalCost = BigDecimal.valueOf(expensesController.getTotalCost()).setScale(2, RoundingMode.HALF_UP);
                    a.setContentText("Total Price: "+totalCost+"\n" +"Total number of Stores: "+ expensesController.getTotalNumberOfStores("all"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                a.showAndWait();
            });
            buttonTotalPriceFromSpecificStores.setOnAction(event1 -> {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                try {
                    BigDecimal totalCost = BigDecimal.valueOf(expensesController.getSumOfPriceFromSpecificStores(storeOption.getText())).setScale(2, RoundingMode.HALF_UP);
                    a.setContentText("Total Price: "+totalCost+"\n" +"Total number of Stores: "+ expensesController.getTotalNumberOfStores(storeOption.getText()));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                a.showAndWait();
            });
            buttonPriceRange.setOnAction(event1 ->
            {
                if(priceOption.getText().equals("")){
                    Alert a = new Alert(Alert.AlertType.WARNING);
                    a.setContentText("Cannot have a blank fields.");
                    a.showAndWait();
                }
                else {
                    double priceStringToDouble = Double.parseDouble(priceOption.getText());
                    try {
                        if(lessThanCheckbox.isSelected()) {
                                GridPane selectPane = new GridPane();
                                Text text = new Text(10,50,"Successful total number of prices: "+expensesController.getTotalNumberofStores(priceStringToDouble,"less")+
                                        "\n"+expensesController.getStoresFromPrice(priceStringToDouble,"less")+"\nTotal Price: "+
                                        BigDecimal.valueOf(expensesController.getTotalPriceFromStores(priceStringToDouble,"less")).setScale(2, RoundingMode.HALF_UP) );
                                selectPane.add(text,1,2);
                                selectPane.add(buttonBackLocal,1,3);

                                ScrollPane selectScrollPane = new ScrollPane(selectPane);
                                scene.setRoot(selectScrollPane);
                        }
                        else if (greaterThanCheckbox.isSelected()){
                                GridPane selectPane = new GridPane();
                                Text text = new Text(10,50,"Successful total number of prices: "+expensesController.getTotalNumberofStores(priceStringToDouble,"greater")+
                                        "\n"+expensesController.getStoresFromPrice(priceStringToDouble,"greater") +"\nTotal Price: "+
                                        BigDecimal.valueOf(expensesController.getTotalPriceFromStores(priceStringToDouble,"greater")).setScale(2, RoundingMode.HALF_UP) );
                                selectPane.add(text,1,2);
                                selectPane.add(buttonBackLocal,1,3);

                                ScrollPane selectScrollPane = new ScrollPane(selectPane);
                                scene.setRoot(selectScrollPane);

                        }
                        else if(equalToCheckbox.isSelected()) {

                                GridPane selectPane = new GridPane();
                                Text text = new Text(10,50,"Successful total number of prices: "+expensesController.getTotalNumberofStores(priceStringToDouble,"equal")+
                                        "\n"+expensesController.getStoresFromPrice(priceStringToDouble,"equal") +"\nTotal Price: "+
                                        BigDecimal.valueOf(expensesController.getTotalPriceFromStores(priceStringToDouble,"equal")).setScale(2, RoundingMode.HALF_UP) );
                                selectPane.add(text,1,2);
                                selectPane.add(buttonBackLocal,1,3);

                                ScrollPane selectScrollPane = new ScrollPane(selectPane);
                                scene.setRoot(selectScrollPane);

                        }
                    }
                    catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            });
            totalButtonPane.getChildren().addAll(buttonTotalPriceOfAllItems,buttonTotalPriceFromSpecificStores,buttonPriceRange);
            totalButtonPane.setPadding(new Insets(15,12,15,12));
            totalButtonPane.setSpacing(10);
            totalPane.add(storeOptionLabel,1,1);
            totalPane.add(storeOption,2,1);
            totalPane.add(priceOptionLabel,1,2);
            totalPane.add(priceOption,2,2);
            totalPane.add(greaterThanLabel,1,3);
            totalPane.add(greaterThanCheckbox,2,3);
            totalPane.add(lessThanLabel,1,4);
            totalPane.add(lessThanCheckbox,2,4);
            totalPane.add(equalToLabel,1,5);
            totalPane.add(equalToCheckbox,2,5);

            totalPane.add(buttonBack, 1, 6);
            totalPaneFormat.setLeft(totalPane);
            totalPaneFormat.setTop(totalButtonPane);
            scene.setRoot(totalPaneFormat);

        });

    }


}