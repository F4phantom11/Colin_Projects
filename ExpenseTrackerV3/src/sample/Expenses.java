package sample;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Expenses {

    private Connection connection;
    public Expenses(){

    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public String getAllExpenses() throws SQLException {
        PreparedStatement statement =connection.prepareStatement("select * from purchases");
        ResultSet resultSet = statement.executeQuery();
        String result = "";
        while (resultSet.next()){
            result+= "Purchase: "+resultSet.getString("id")+"\nStore: "+resultSet.getString("store")+"\n Price: "+resultSet.getString("price")+"\n Date(dd/mm/yyyy): "+resultSet.getString("day")
                    +"/"+resultSet.getString("month")+"/"+resultSet.getString("year")+"\n";
        }
        return result;
    }
    public String getAllStores(String name) throws SQLException{
        PreparedStatement statement =connection.prepareStatement("select * from purchases where store like ?");
        name=name.toLowerCase();
        statement.setString(1,"%"+name+"%");
        ResultSet resultSet = statement.executeQuery();
        String result= "";
        while(resultSet.next()){

            result+= "Purchase: "+resultSet.getString("id")+"\nStore: "+resultSet.getString("store")+"\n Price: "+resultSet.getString("price")+"\n Date(dd/mm/yyyy): "+resultSet.getString("day")
                    +"/"+resultSet.getString("month")+"/"+resultSet.getString("year")+"\n";

        }
        return result;
    }
    public void addExpense(String store, double price, String date) throws SQLException {
        PreparedStatement statement =connection.prepareStatement("insert into  purchases (store, price,day,month,year) values(?,?,?,?,?)");
        String[] dates = date.split("/");
        statement.setString(1,store);
        statement.setDouble(2,price);
        statement.setString(3,dates[0]);
        statement.setString(4,dates[1]);
        statement.setString(5,dates[2]);
        statement.executeUpdate();

    }
    public double getTotalCost() throws SQLException {
        PreparedStatement statement =connection.prepareStatement("select price from purchases");
        ResultSet resultSet = statement.executeQuery();
        double result = 0.0;
        while (resultSet.next()){
            result+= resultSet.getDouble("price");
        }

        return result;
    }
    public double getPriceFromSpecificStores(String store) throws SQLException {
        PreparedStatement statement =connection.prepareStatement("select price from purchases where store like ?");
        store=store.toLowerCase();
        statement.setString(1,"%"+store+"%");
        ResultSet resultSet = statement.executeQuery();
        double result = 0.0;
        while (resultSet.next()){
            result+= resultSet.getDouble("price");
        }

        return result;

    }
}
