package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.database.DbUtil;

import java.net.URL;
import java.sql.*;
import java.time.ZoneId;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller implements Initializable{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXTextField searchField;

    @FXML
    private JFXTextField english;

    @FXML
    private JFXTextField indonesia;

    @FXML
    private JFXTextField id;


    @FXML
    private DatePicker dateCreated;

    @FXML
    private DatePicker dateModified;

    @FXML
    private JFXButton add;

    @FXML
    private JFXButton RefreshBox;

    @FXML
    private JFXButton deleteButton;

    @FXML
    private TableView<Dictionary> myTable;

    @FXML
    private TableColumn<Dictionary, Integer> idTableColumn;

    @FXML
    private TableColumn<Dictionary, String> englishTableColumn;

    @FXML
    private TableColumn<Dictionary, String> indonesiaTableColumn;

    @FXML
    private TableColumn<Dictionary, Date> dateCreatedTableColumn;

    @FXML
    private TableColumn<Dictionary, Date> dateModifiedTableColumn;

    private ObservableList<Dictionary>words;
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private Alert alert;
    private FilteredList<Dictionary> filteredList;

    @FXML
    void addWord(ActionEvent event) throws SQLException{
        String sql = "INSERT INTO `dictionaryEngIdn`(`id`, `english`, `indonesia`, `date_created`, `date_modified`) VALUES (?,?,?,?,?)";


        int idNum = Integer.parseInt(id.getText());
        String englishText = english.getText();
        String indonesiaText = indonesia.getText();
        java.util.Date dateAdd=java.util.Date.from(dateCreated.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        java.sql.Date dateCreate=new java.sql.Date(dateAdd.getTime());
        java.util.Date dateEdit=java.util.Date.from(dateModified.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        java.sql.Date dateModify=new java.sql.Date(dateEdit.getTime());

        try (Connection connection = DbUtil.getConnection()){
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, idNum);
            preparedStatement.setString(2, englishText);
            preparedStatement.setString(3, indonesiaText);
            preparedStatement.setDate(4, dateCreate);
            preparedStatement.setDate(5, dateModify);

            int result = preparedStatement.executeUpdate();
            if (result == 1)
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Data Insert Successfully");
                alert.showAndWait();


            id.clear();
            english.clear();
            indonesia.clear();
            dateCreated.setValue(null);
            dateModified.setValue(null);

        } catch (SQLException e) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE,null,e);
        } finally {
            preparedStatement.close();
        }
    }

    @FXML
    void deleteWord() {
        int idNum;

        try (Connection connection = DbUtil.getConnection()){
            Dictionary dictionary =(Dictionary) myTable.getSelectionModel().getSelectedItem();
            String query = "DELETE FROM `dictionaryEngIdn` WHERE `dictionaryEngIdn`.`id` = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, dictionary.getId());
            idNum = dictionary.getId();

            int result = preparedStatement.executeUpdate();
            if (result == 1)
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Data Deleted Successfully");
                alert.showAndWait();

            preparedStatement.close();
            resultSet.close();
        } catch (Exception e){
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE,null,e);
        }
    }

    @FXML
    void refresh(ActionEvent event) {
        refreshTable();
    }


    public void refreshTable(){
        words.clear();
        try {
            String query = "SELECT * from dictionaryEngIdn";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                words.add(new Dictionary(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getDate(4),
                        resultSet.getDate(5)

                ));
                myTable.setItems(words);
            }
            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e){
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE,null,e);
        }
    }

    @FXML
    public void initialize(URL url, ResourceBundle resources) {

        try {
            connection = DbUtil.getConnection();
            words = FXCollections.observableArrayList();
            resultSet = connection.createStatement().executeQuery("SELECT * from dictionaryEngIdn");

            while (resultSet.next()){
                words.add(new Dictionary(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getDate(4), resultSet.getDate(5)));
            }

            idTableColumn.setCellValueFactory(new PropertyValueFactory<Dictionary, Integer>("id"));
            englishTableColumn.setCellValueFactory(new PropertyValueFactory<Dictionary, String>("english"));
            indonesiaTableColumn.setCellValueFactory(new PropertyValueFactory<Dictionary, String>("indonesia"));
            dateCreatedTableColumn.setCellValueFactory(new PropertyValueFactory<Dictionary, Date>("dateCreated"));
            dateModifiedTableColumn.setCellValueFactory(new PropertyValueFactory<Dictionary, Date>("dateModified"));

            myTable.setItems(words);

            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        filteredList = new FilteredList<>(words, e -> true);
        searchField.setOnKeyReleased(e -> {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredList.setPredicate((Predicate<? super Dictionary>) dictionary->{
                    if(newValue == null || newValue.isEmpty()){
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();

                    if(dictionary.getEnglish().contains(newValue)){
                        return true;
                    } else if (dictionary.getEnglish().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    } else if (dictionary.getIndonesia().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }
                    return false;
                });
            });
            SortedList<Dictionary> sortedList = new SortedList<>(filteredList);
            sortedList.comparatorProperty().bind(myTable.comparatorProperty());
            myTable.setItems(sortedList);
        });

    }

}
