package sample;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import sample.database.DbUtil;

public class addWord {

    public addWord(){

    }

    public void add(int id, String english, String indonesia, Date dateCreated, Date dateModified){

        String sql = "INSERT INTO `dictionaryEngIdn`(`id`, `english`, `indonesia`, `date_created`, `date_modified`) VALUES (?,?,?,?,?)";

        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ){
            statement.setInt(1, id);
            statement.setString(2, english);
            statement.setString(3, indonesia);
            statement.setDate(4, dateCreated);
            statement.setDate(5, dateModified);

            statement.execute();
        } catch (Exception e){

        }
    }
}
