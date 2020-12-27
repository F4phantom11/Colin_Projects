package sample;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

enum DateCombo{
    DAY,MONTH,YEAR,DAY_MONTH,DAY_YEAR,MONTH_YEAR,DAY_MONTH_YEAR
}
public class Expenses {

    private Connection connection;
    public Expenses(){

    }

    public void addExpense(String store, double price, String date,String name) throws SQLException {

        PreparedStatement statement = connection.prepareStatement("select  exists(select familyName from familylimit_purchases where familyName = ?)" );
        statement.setString(1,name);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        if(resultSet.getInt(1)==0) {
            statement = connection.prepareStatement("insert into  familylimit_purchases (familyName) values(?)");
            statement.setString(1, name);
            statement.executeUpdate();
        }
        statement =connection.prepareStatement("insert into  purchases (store, price,day,month,year, familyName) values(?,?,?,?,?,?)");
        String[] dates = date.split("/");
        statement.setString(1,store);
        statement.setDouble(2,price);
        statement.setString(3,dates[0]);
        statement.setString(4,dates[1]);
        statement.setString(5,dates[2]);
        statement.setString(6, name);
        statement.executeUpdate();

    }



    public String getAllExpenses() throws SQLException {
        PreparedStatement statement =connection.prepareStatement("select * from purchases");
        ResultSet resultSet = statement.executeQuery();
        String result = "";
        while (resultSet.next()){
            result+= "Purchase: "+resultSet.getString("id")+"\nStore: "+resultSet.getString("store")+"\n Price: "+resultSet.getString("price")+"\n Name: " + resultSet.getString("familyName")+"\n Date(dd/mm/yyyy): "+resultSet.getString("day")
                    +"/"+resultSet.getString("month")+"/"+resultSet.getString("year")+"\n";
        }
        return result;
    }
    public String getAllExpensesByDate(String date, DateCombo combo) throws SQLException {//TODO Have to finish the combos for date
        String[] splitDate = getDateFromString(date,combo);
        String result = "";
        ResultSet resultSet;
        if(combo.equals(DateCombo.DAY)){
            PreparedStatement statement =connection.prepareStatement("select * from purchases where day = ?");
            statement.setString(1,splitDate[0]);
            resultSet = statement.executeQuery();
            while (resultSet.next()){
                result+= "Purchase: "+resultSet.getString("id")+"\nStore: "+resultSet.getString("store")+"\n Price: "+resultSet.getString("price")+"\n Name: " + resultSet.getString("familyName")+"\n Date(dd/mm/yyyy): "+resultSet.getString("day")
                        +"/"+resultSet.getString("month")+"/"+resultSet.getString("year")+"\n";
            }
        }
        else if(combo.equals(DateCombo.MONTH)){
            PreparedStatement statement =connection.prepareStatement("select * from purchases where month = ?");
            statement.setString(1,splitDate[0]);
            resultSet = statement.executeQuery();
            while (resultSet.next()){
                result+= "Purchase: "+resultSet.getString("id")+"\nStore: "+resultSet.getString("store")+"\n Price: "+resultSet.getString("price")+"\n Name: " + resultSet.getString("familyName")+"\n Date(dd/mm/yyyy): "+resultSet.getString("day")
                        +"/"+resultSet.getString("month")+"/"+resultSet.getString("year")+"\n";
            }
        }
        else if(combo.equals(DateCombo.YEAR)){
            PreparedStatement statement =connection.prepareStatement("select * from purchases where year = ?");
            statement.setString(1,splitDate[0]);
            resultSet = statement.executeQuery();
            while (resultSet.next()){
                result+= "Purchase: "+resultSet.getString("id")+"\nStore: "+resultSet.getString("store")+"\n Price: "+resultSet.getString("price")+"\n Name: " + resultSet.getString("familyName")+"\n Date(dd/mm/yyyy): "+resultSet.getString("day")
                        +"/"+resultSet.getString("month")+"/"+resultSet.getString("year")+"\n";
            }
        }
        else if(combo.equals(DateCombo.DAY_MONTH)){
            PreparedStatement statement =connection.prepareStatement("select * from purchases where day = ? and month = ?");
            statement.setString(1,splitDate[0]);
            statement.setString(2,splitDate[1]);
            resultSet = statement.executeQuery();
            while (resultSet.next()){
                result+= "Purchase: "+resultSet.getString("id")+"\nStore: "+resultSet.getString("store")+"\n Price: "+resultSet.getString("price")+"\n Name: " + resultSet.getString("familyName")+"\n Date(dd/mm/yyyy): "+resultSet.getString("day")
                        +"/"+resultSet.getString("month")+"/"+resultSet.getString("year")+"\n";
            }
        }
        else if(combo.equals(DateCombo.DAY_YEAR)){
            PreparedStatement statement =connection.prepareStatement("select * from purchases where day = ? and year = ?");
            statement.setString(1,splitDate[0]);
            statement.setString(2,splitDate[1]);
            resultSet = statement.executeQuery();
            while (resultSet.next()){
                result+= "Purchase: "+resultSet.getString("id")+"\nStore: "+resultSet.getString("store")+"\n Price: "+resultSet.getString("price")+"\n Name: " + resultSet.getString("familyName")+"\n Date(dd/mm/yyyy): "+resultSet.getString("day")
                        +"/"+resultSet.getString("month")+"/"+resultSet.getString("year")+"\n";
            }
        }
        else if(combo.equals(DateCombo.MONTH_YEAR)){
            PreparedStatement statement =connection.prepareStatement("select * from purchases where month = ? and year = ?");
            statement.setString(1,splitDate[0]);
            statement.setString(2,splitDate[1]);
            resultSet = statement.executeQuery();
            while (resultSet.next()){
                result+= "Purchase: "+resultSet.getString("id")+"\nStore: "+resultSet.getString("store")+"\n Price: "+resultSet.getString("price")+"\n Name: " + resultSet.getString("familyName")+"\n Date(dd/mm/yyyy): "+resultSet.getString("day")
                        +"/"+resultSet.getString("month")+"/"+resultSet.getString("year")+"\n";
            }
        }
        else if(combo.equals(DateCombo.DAY_MONTH_YEAR)){
            PreparedStatement statement =connection.prepareStatement("select * from purchases where day = ? and month = ? and year = ?");
            statement.setString(1,splitDate[0]);
            statement.setString(2,splitDate[1]);
            statement.setString(3,splitDate[2]);
            resultSet = statement.executeQuery();
            while (resultSet.next()){
                result+= "Purchase: "+resultSet.getString("id")+"\nStore: "+resultSet.getString("store")+"\n Price: "+resultSet.getString("price")+"\n Name: " + resultSet.getString("familyName")+"\n Date(dd/mm/yyyy): "+resultSet.getString("day")
                        +"/"+resultSet.getString("month")+"/"+resultSet.getString("year")+"\n";
            }
        }


        return result;
    }
    public String getAllExpensesByFamilyName(String name) throws SQLException {

        PreparedStatement statement =connection.prepareStatement("select * from purchases where familyName like ?");
        name=name.toLowerCase();
        statement.setString(1,"%"+name+"%");
        ResultSet resultSet = statement.executeQuery();
        String result= "";
        while(resultSet.next()){

            result+= "Purchase: "+resultSet.getString("id")+"\nStore: "+resultSet.getString("store")+"\n Price: "+resultSet.getString("price")+"\n Name: " + resultSet.getString("familyName")+"\n Date(dd/mm/yyyy): "+resultSet.getString("day")
                    +"/"+resultSet.getString("month")+"/"+resultSet.getString("year")+"\n";

        }
        return result;

    }
    public String getAllExpensesByStores(String name) throws SQLException{
        PreparedStatement statement =connection.prepareStatement("select * from purchases where store like ?");
        name=name.toLowerCase();
        statement.setString(1,"%"+name+"%");
        ResultSet resultSet = statement.executeQuery();
        String result= "";
        while(resultSet.next()){

            result+= "Purchase: "+resultSet.getString("id")+"\nStore: "+resultSet.getString("store")+"\n Price: "+resultSet.getString("price")+"\n Name: " + resultSet.getString("familyName")+"\n Date(dd/mm/yyyy): "+resultSet.getString("day")
                    +"/"+resultSet.getString("month")+"/"+resultSet.getString("year")+"\n";

        }
        return result;
    }

    public int getTotalNumberOfStores(String store) throws SQLException {
        if(store.equals("all")) {
            PreparedStatement statement = connection.prepareStatement("select count(price) from purchases");
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);

        }
        else {
            PreparedStatement statement = connection.prepareStatement("select count(price) from purchases where store like ?");
            store = store.toLowerCase();
            statement.setString(1, "%" + store + "%");
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        }

    }

    public int getTotalNumberofStores(double price, String operator) throws SQLException {
        if(operator.equals("greater")) {
            PreparedStatement statement = connection.prepareStatement("select count(price) from purchases where price > ?");
            statement.setDouble(1,   price);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            return resultSet.getInt(1);
        }
        else if(operator.equals("less")){
            PreparedStatement statement = connection.prepareStatement("select count(price) from purchases where price < ?");
            statement.setDouble(1, price);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        }
        else{
            PreparedStatement statement = connection.prepareStatement("select count(price) from purchases where price = ?");
            statement.setDouble(1, price);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        }

    }

    public double getSumOfPriceFromSpecificStores(String store) throws SQLException {
        PreparedStatement statement =connection.prepareStatement("select sum(price) from purchases where store like ?");
        store=store.toLowerCase();
        statement.setString(1,"%"+store+"%");
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        return  resultSet.getDouble(1);

    }

    public void setSpendingLimit(double limit, String familyName) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select  exists(select familyName from familylimit_purchases where familyName = ?)" );
        statement.setString(1,familyName);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        if(resultSet.getInt(1)==0) {
            statement = connection.prepareStatement("insert into  familylimit_purchases (familyName) values(?)");
            statement.setString(1, familyName);
            statement.executeUpdate();
            statement = connection.prepareStatement("insert into  familyLimit (limitAmount, familyName) values(?,?)");
            statement.setDouble(1,limit);
            statement.setString(2,familyName);
            statement.executeUpdate();
        }
        statement = connection.prepareStatement("update  familyLimit set limitAmount = ? where familyName = ?");
        statement.setDouble(1,limit);
        statement.setString(2,familyName);
        statement.executeUpdate();


    }

    public String getStoresFromPrice(double price, String operator) throws SQLException {
        String result= "";
        if(operator.equals("greater")) {
            PreparedStatement statement =connection.prepareStatement("select * from purchases where price > ?");
            statement.setDouble(1,price);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){

                result+= "Purchase: "+resultSet.getString("id")+"\nStore: "+resultSet.getString("store")+"\n Price: "+resultSet.getString("price")+"\n Name: " + resultSet.getString("familyName")+"\n Date(dd/mm/yyyy): "+resultSet.getString("day")
                        +"/"+resultSet.getString("month")+"/"+resultSet.getString("year")+"\n" ;

            }


        }
        else if (operator.equals("less")){
            PreparedStatement statement = connection.prepareStatement("select * from purchases where price < ?");
            statement.setDouble(1,price);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){

                result+= "Purchase: "+resultSet.getString("id")+"\nStore: "+resultSet.getString("store")+"\n Price: "+resultSet.getString("price")+"\n Name: " + resultSet.getString("familyName")+"\n Date(dd/mm/yyyy): "+resultSet.getString("day")
                        +"/"+resultSet.getString("month")+"/"+resultSet.getString("year")+"\n";

            }


        }
        else{
            PreparedStatement statement = connection.prepareStatement("select * from purchases where price = ?");
            statement.setDouble(1,price);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){

                result+= "Purchase: "+resultSet.getString("id")+"\nStore: "+resultSet.getString("store")+"\n Price: "+resultSet.getString("price")+"\n Name: " + resultSet.getString("familyName")+"\n Date(dd/mm/yyyy): "+resultSet.getString("day")
                        +"/"+resultSet.getString("month")+"/"+resultSet.getString("year")+"\n";

            }


        }
        return result;
    }

    public double getTotalCost() throws SQLException {
        PreparedStatement statement =connection.prepareStatement("select sum(price) from purchases");
        ResultSet resultSet = statement.executeQuery();
        double result = 0.0;
        resultSet.next();
        return  resultSet.getDouble(1);
    }

    public double getTotalPriceFromStores(double price, String operator) throws SQLException {
        if(operator.equals("greater")) {
            PreparedStatement statement =connection.prepareStatement("select sum(price) from purchases where price > ?");
            statement.setDouble(1,price);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return  resultSet.getDouble(1);
        }
        else if(operator.equals("less")){
            PreparedStatement statement = connection.prepareStatement("select sum(price) from purchases where price < ?");
            statement.setDouble(1,price);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return  resultSet.getDouble(1);
        }
        else{
            PreparedStatement statement = connection.prepareStatement("select sum(price) from purchases where price = ?");
            statement.setDouble(1,price);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return  resultSet.getDouble(1);
        }

    }

    public  String getTotalsAndLimitPerson() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select familylimit_purchases.familyName , sum(price), familylimit.limitAmount from familylimit_purchases left join purchases on purchases.familyName = familylimit_purchases.familyName left join familylimit on familylimit_purchases.familyName = familylimit.familyName group by familylimit_purchases.familyName order by familylimit_purchases.familyName" );

        ResultSet resultSet = statement.executeQuery();

        String result ="";
        while (resultSet.next()) {
          BigDecimal totalExpense = BigDecimal.valueOf(resultSet.getDouble(2)).setScale(2, RoundingMode.HALF_UP);
          double limit = resultSet.getDouble(3);
          if(totalExpense.doubleValue()>limit&&limit!=0.0){
              result += "Name: " + resultSet.getString(1) + " \nTotal Expense: " + totalExpense + " \nLimit: " + limit + "\n" + resultSet.getString(1)+" is over their limit!\n";
          }
          else {
              result += "Name: " + resultSet.getString(1) + " \nTotal Expense: " + totalExpense + " \nLimit: " + limit + "\n";
          }
        }
        return result;
    }
    public String getTotalAndLimitSpecificPerson(String name) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select familylimit_purchases.familyName , sum(price), familylimit.limitAmount from familylimit_purchases left join purchases on purchases.familyName = familylimit_purchases.familyName left join familylimit on familylimit_purchases.familyName = familylimit.familyName where familylimit_purchases.familyName like ? group by familylimit_purchases.familyName order by familylimit_purchases.familyName" );
        statement.setString(1,"%"+name+"%");
        ResultSet resultSet = statement.executeQuery();

        String result ="";
        while (resultSet.next()) {
            BigDecimal totalExpense = BigDecimal.valueOf(resultSet.getDouble(2)).setScale(2, RoundingMode.HALF_UP);
            double limit = resultSet.getDouble(3);
            if(totalExpense.doubleValue()>limit&&limit!=0.0){
                result += "Name: " + resultSet.getString(1) + " \nTotal Expense: " + totalExpense + " \nLimit: " + limit + "\n" + resultSet.getString(1)+" is over their limit!\n";
            }
            else {
                result += "Name: " + resultSet.getString(1) + " \nTotal Expense: " + totalExpense + " \nLimit: " + limit + "\n";
            }
        }
        return result;
    }


    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    private String[] getDateFromString(String date, DateCombo combo){
        String[] splitDate = date.split("/");

        if(combo.equals(DateCombo.DAY)){

        }
        return splitDate;
    }
   /* PreparedStatement statementAll =connection.prepareStatement("select * from purchases where price > ?");
            statement.setDouble(1,price);
    ResultSet resultSetAll = statement.executeQuery();
    String resultAll= "";
            while(resultSet.next()){

        resultAll+= "Purchase: "+resultSet.getString("id")+"\nStore: "+resultSet.getString("store")+"\n Price: "+resultSet.getString("price")+"\n Date(dd/mm/yyyy): "+resultSet.getString("day")
                +"/"+resultSet.getString("month")+"/"+resultSet.getString("year")+"\n";

    }*/
}
