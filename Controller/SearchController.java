package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Model.PageLoader;
import Model.SearchModel;
import Model.User;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import server.Repository;

public class SearchController implements Initializable {

    @FXML
    private JFXTextField filterField;
    @FXML
    private TableView<User> tableview;
    @FXML
    private TableColumn<User, String> name;
    @FXML
    private TableColumn<User, String> username;
    @FXML
    private TableColumn<User, String> address;


    private final ObservableList<User> dataList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Properties.lastPage = "search_page";
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        username.setCellValueFactory(new PropertyValueFactory<>("ID"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));

        try {
            dataList.addAll(SearchModel.getAllUsers());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        FilteredList<User> filteredData = new FilteredList<>(dataList, b -> true);

        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(user -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (user.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (user.getID().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (user.getAddress().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else
                    return false;
            });
        });

        SortedList<User> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(tableview.comparatorProperty());

        tableview.setItems(sortedData);


    }
    @FXML
    public void clickItem(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2)
        {
            Properties.profile = tableview.getSelectionModel().getSelectedItem();
            new PageLoader().load("profile_page");
        }
    }
    public void back(MouseEvent mouseEvent) throws IOException {
        new PageLoader().load("timeline_page");
    }
}
