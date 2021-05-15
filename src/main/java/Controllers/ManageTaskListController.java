package Controllers;

import Main.App;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import jsonUtils.FileWriter;
import model.Date;
import model.Task;
import model.User;
import sceneUtils.SceneManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


    public class ManageTaskListController implements Initializable
    {
        private String username;
        @FXML
        public TableView<Task> table;
        @FXML
        private TableColumn<Task,String> task;
        @FXML
        private TableColumn<Task,String> status;
        @FXML
        private TableColumn<Task,String> description;
        @FXML
        private TableColumn<Task, Date> deadline;
        @FXML
        private TextField taskname;
        @FXML
        private TextField descriptionFiled;

        @FXML
        private Button addButton;
        @FXML
        private Button back;
        @FXML
        private Button deleteButton;

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            back.setOnAction(e -> backButtonClicked());
            addButton.setOnAction(e -> addButtonClicked());
            task.setCellValueFactory(new PropertyValueFactory<Task, String>("Name"));
            status.setCellValueFactory(new PropertyValueFactory<Task, String>("Status"));
            deadline.setCellValueFactory(new PropertyValueFactory<Task, Date>("Deadline"));
            description.setCellValueFactory(new PropertyValueFactory<Task, String>("Description"));


        }
        public void setUser(User u){
            if(u==null)
                return;
            username=u.getUsername();
            FillTable();
        }
        public void FillTable()
        {
            ObservableList<Task> it = FXCollections.observableArrayList();
            List<Task> l= FileWriter.wedMap.get(username).getTaskList();
            //if(l!=null) {
            for (Task t : l)
                it.add(t);
            //}
            System.out.println("username"+username);
            table.setItems(it);
            App.setTable(table);
        }


        private void addButtonClicked() {
           Task t= new Task(taskname.getText(),"1.1.2",descriptionFiled.getText());
           FileWriter.addTask(username,t);
           FileWriter.persistWed();
           table.getItems().add(t);
        }

        private void backButtonClicked() {App.getI().changeSceneOnMainStage(SceneManager.SceneType.ORG_WED); }

    }