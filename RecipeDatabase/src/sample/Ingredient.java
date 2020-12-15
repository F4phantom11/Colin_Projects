package sample;


import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Ingredient {
    String amount;
    String name;
   // String recipe;
    Connection connection;

    public Ingredient(){

    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void addIngredients(ArrayList<TextField> ingredientName, String recipe, ArrayList<TextField> ingredientAmount, int numberOfIngredients) throws SQLException {
        String result ="insert into ingredients (nameI, recipe, amount)  values ";
        for(int i =0;i<numberOfIngredients;i++){
            result += "('"+ingredientName.get(i).getText()+"','"+recipe+"','"+ingredientAmount.get(i).getText()+"'),";
        }
        result = result.substring(0,result.length()-1);
        PreparedStatement statement = connection.prepareStatement( result);

        statement.executeUpdate();
    }

    public  String getAllIngredients() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select nameI,recipe,amount from ingredients");

        ResultSet resultSet = statement.executeQuery();
        String result= "";
        String prevRecipe="";
        while(resultSet.next()){
            if(!prevRecipe.equals(resultSet.getString("recipe"))) {
                prevRecipe=resultSet.getString("recipe");
                result += resultSet.getString("recipe") + ":\n";
            }
                if(resultSet.getString("nameI").length()<5) {
                    result += resultSet.getString("nameI") + "\t\t" + resultSet.getString("amount") + "\n";
                }
                else{
                    result += resultSet.getString("nameI") + "\t" + resultSet.getString("amount") + "\n";
                }

        }
        return result;
    }


}
