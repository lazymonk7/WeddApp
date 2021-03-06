package Main;

import Controllers.*;
import javafx.application.Application;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import jsonUtils.FileWriter;
import model.*;
import sceneUtils.SceneManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class App extends Application {

    private Stage stage;
    private static App instance;
    public static void main(String[] args) { launch(args); }

    private static User userLoggedIn=null;
    private static TableView<Task> table= new TableView<>();

    public static User getUserLoggedIn() {
        return userLoggedIn;
    }

    public static void setUserLoggedIn(User userLoggedIn) {     //when the logout is clicked, this must be set to null or sth like that

        App.userLoggedIn = userLoggedIn;

        if(userLoggedIn==null)
        {
            InvitesListController invitesListController = SceneManager.getInstance().getController(SceneManager.SceneType.INVITES);
            invitesListController.makeVisible();
            return;
        }

        if(userLoggedIn.getRole().equals("organizer"))
        {
            String username = userLoggedIn.getUsername();
            if(FileWriter.wedMap.get(username)==null)
            {
                OrgNoWedController noWedController = SceneManager.getInstance().getController(SceneManager.SceneType.ORG_NO_WED);
                noWedController.setNameLabels(userLoggedIn);     //!!!!!!!!!!!!!!!!!!!
            }
            else
            {
                OrgWedController wedController = SceneManager.getInstance().getController(SceneManager.SceneType.ORG_WED);
                wedController.setNameLabels(userLoggedIn);     //!!!!!!!!!!!!!!!!!!!
                ManageTaskListController task = SceneManager.getInstance().getController(SceneManager.SceneType.TASK_LIST);
                task.setUser(userLoggedIn);
                ChangeDetailsController detailsController = SceneManager.getInstance().getController(SceneManager.SceneType.WED_DETAILS);
                detailsController.loadDetails(userLoggedIn);
            }
        }
        else System.out.println("wtf");

    }




    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        instance = this;
        this.stage.setTitle("WED APP");
        //this.stage.setHeight(600);
        //this.stage.setWidth(600);

        //load users from file in the arrays to have them globally available;
        FileWriter.loadDataFromFile();

//        FileWriter.addWedd(new Wedding(new Date(1,1,2012), "here", 20000, 200, "RustiPaula"));
//        FileWriter.persistWed();

        //from now use FileWriter.list instead

        //iterate over hash map
//        Iterator it = FileWriter.wedMap.entrySet().iterator();
//        System.out.println("iteration");
//        while (it.hasNext())
//        {
//            Map.Entry pair = (Map.Entry)it.next();
//            System.out.println(pair.getKey() + "=" + pair.getValue());
//            it.remove();
//        }
        //System.out.println(FileWriter.weddings);

        // Initialize all scenes
        SceneManager.getInstance();

        //Choose first appearing scene
        stage.setScene(SceneManager.getInstance().getScene(SceneManager.SceneType.LOGIN));

        stage.setResizable(false);
        stage.setTitle("WeddApp");

        stage.show();
    }

    public static App getI() {
        return instance;
    }

    public void changeSceneOnMainStage(SceneManager.SceneType sceneType)
    {
        stage.setScene(SceneManager.getInstance().getScene(sceneType));
    }

    public static TableView<Task> getTable() {
        return table;
    }

    public static void setTable(TableView<Task> table1) {
        table = table1;
    }
}