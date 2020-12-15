package sample;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Recipe {
    private int totalNumberOfRecipes;
    private Connection connection;



    public Recipe(){
        totalNumberOfRecipes =0;

    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public int getTotalNumberOfRecipes() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select count(nameR) from recipe");

        ResultSet resultSet = statement.executeQuery();
        String result= "";
        while(resultSet.next()){
            totalNumberOfRecipes = resultSet.getInt(1);
        }

        return totalNumberOfRecipes;
    }

    public void setTotalNumberOfRecipes(int totalNumberofRecipes) {
        this.totalNumberOfRecipes = totalNumberofRecipes;
    }



    public void addRecipe(String name) throws SQLException {

        PreparedStatement statement = connection.prepareStatement("insert into recipe (nameR)  values (?)");
        statement.setString(1,name);
        statement.executeUpdate();


    }

    public  String getAllRecipes() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select * from recipe");

        ResultSet resultSet = statement.executeQuery();
        String result= "";
        while(resultSet.next()){
            result +=resultSet.getString("nameR")+"\n";
        }
        return result;
    }
    public String searchRecipe(String name) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select nameR from recipe where nameR like ?");
        statement.setString(1,"%"+name+"%");
        ResultSet resultSet = statement.executeQuery();
        String result= "";
        while(resultSet.next()){

                result += resultSet.getString("nameR") + "\n";

        }
        return result;

    }


}
