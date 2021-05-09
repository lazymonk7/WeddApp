package Controllers;

import Main.App;
import Validation.RegisterValidation;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import exceptions.CredentialsAreNullException;
import exceptions.IncorrectCredentials;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import jsonUtils.FileWriter;
import sceneUtils.SceneManager;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable
{
    @FXML
    private JFXButton backButton;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private JFXComboBox role;
    @FXML
    private Label message;
    @FXML
    private JFXButton loginButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        backButton.setOnAction(e -> backButtonClicked());
        role.getItems().addAll("Guest", "Organizer");
        loginButton.setOnAction(e -> loginButtonClicked());

    }

    public void backButtonClicked()
    {
        username.clear();
        password.clear();
        message.setText("");
        App.getI().changeSceneOnMainStage(SceneManager.SceneType.REGISTER);
    }

    public void loginButtonClicked()
    {
        try {
            handleLogin();
            message.setText("Login Successful!");
        }catch (CredentialsAreNullException | IncorrectCredentials e)
        {
            message.setText(e.getMessage());
        }
    }

    public void handleLogin() throws CredentialsAreNullException, IncorrectCredentials
    {
        if (role.getValue()==null || password.getText()==null || username.getText()==null)
            throw new CredentialsAreNullException();
        else
        {
            String s = (String)role.getValue();
            if(s.equals("Guest"))
            {
                //try validating in the guests list
                //System.out.println("here");
                for(var guest: FileWriter.guests)
                {
                    //System.out.println(guest.getUsername());
                    if(guest.getUsername().equals(username.getText())) //if a user with that username is present in the file
                    {
                        System.out.println("guest username found");
                        if(guest.getPassword().equals(RegisterValidation.encodePassword(guest.getUsername(), password.getText())))
                        {
                            //System.out.println("Login success");
                        }
                        else
                        {
                            //System.out.println("but no correct pass");
                            throw new IncorrectCredentials();
                        }
                        return;
                    }
                }
                //System.out.println("username not in file");
                throw new IncorrectCredentials();
            }
            if(s.equals("Organizer"))
            {
                //try validating in the guests list
                //System.out.println("here");
                for(var org: FileWriter.organizers)
                {
                    //System.out.println(org.getUsername());
                    if(org.getUsername().equals(username.getText())) //if a user with that username is present in the file
                    {
                        System.out.println("guest username found");
                        if(org.getPassword().equals(RegisterValidation.encodePassword(org.getUsername(), password.getText())))
                        {
                            //System.out.println("Login success");
                        }
                        else
                        {
                            //System.out.println("but no correct pass");
                            throw new IncorrectCredentials();
                        }
                        return;
                    }
                }
                //System.out.println("username not in file");
                throw new IncorrectCredentials();
            }
            //throw new IncorrectCredentials();
        }
    }
}