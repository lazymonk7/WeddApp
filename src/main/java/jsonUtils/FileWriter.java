package jsonUtils;
import Validation.RegisterValidation;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import exceptions.*;
import javafx.collections.ObservableList;
import model.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FileWriter
{
    private static final Path GuestPATH = FilePath.getPathToFile("config", "guests.json");
    private static final Path OrganizerPATH = FilePath.getPathToFile("config", "organizers.json");
    private static final Path WeddingPATH = FilePath.getPathToFile("config", "weddings.json");
    private static final Path WedListPATH = FilePath.getPathToFile("config", "wedList.json");
    private static final Path InvitesPATH = FilePath.getPathToFile("config", "invites.json");

    public static List<Organizer> organizers;      //the lists are edited in memory and then are written to file
    public static List<Guest> guests;
    public static List<Wedding> weddings;
    public static List<WedListElem> wedList;
    public static List<Invitation> invites;

    public  static Map<String, User> userMap=new HashMap<>();   //username -> user
    public static Map<String, Wedding> wedMap=new HashMap<>(); //username -> wedding

    public static void loadDataFromFile() throws IOException   //loads data from file in the class lists
    {

        if (!Files.exists(GuestPATH)) {
            FileUtils.copyURLToFile(FileWriter.class.getClassLoader().getResource("guests.json"), GuestPATH.toFile());
        }
        if (!Files.exists(OrganizerPATH)) {
            FileUtils.copyURLToFile(FileWriter.class.getClassLoader().getResource("organizers.json"), OrganizerPATH.toFile());
        }
        if (!Files.exists(WeddingPATH)) {
            FileUtils.copyURLToFile(FileWriter.class.getClassLoader().getResource("weddings.json"), WeddingPATH.toFile());
        }
        if (!Files.exists(WedListPATH)) {
            FileUtils.copyURLToFile(FileWriter.class.getClassLoader().getResource("wedList.json"), WedListPATH.toFile());
        }
        if (!Files.exists(InvitesPATH)) {
            FileUtils.copyURLToFile(FileWriter.class.getClassLoader().getResource("invites.json"), InvitesPATH.toFile());
        }

        ObjectMapper objectMapper = new ObjectMapper();

        guests = objectMapper.readValue(GuestPATH.toFile(), new TypeReference<List<Guest>>() {
        });
        organizers = objectMapper.readValue(OrganizerPATH.toFile(), new TypeReference<List<Organizer>>() {
        });
        weddings = objectMapper.readValue(WeddingPATH.toFile(), new TypeReference<List<Wedding>>() {
        });
        wedList = objectMapper.readValue(WedListPATH.toFile(), new TypeReference<List<WedListElem>>() {
        });
        invites = objectMapper.readValue(InvitesPATH.toFile(), new TypeReference<List<Invitation>>() {
        });

        //populate the hash map with users
        for(var temp : organizers)
        {
            userMap.put(temp.getUsername(), temp);
        }
        for(var temp : guests)
        {
            userMap.put(temp.getUsername(), temp);
        }
        //populate wed map
        for(var temp : weddings)
        {
            wedMap.put(temp.getUsername(), temp);
        }

    }

    public static void persistUsers() {    //writes users list to file
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(GuestPATH.toFile(), guests);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(OrganizerPATH.toFile(), organizers);
        } catch (IOException e) {
            throw new CouldNotWriteUsersException();
        }
    }



    public static void persistWedList() {    //writes wedLIst to file
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(WedListPATH.toFile(), wedList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void persistInvites() {    //writes wedLIst to file
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(InvitesPATH.toFile(), invites);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addUser(User u) throws UsernameAlreadyExistsException,CredentialsAreNullException , InvalidPhoneNumberException {
        RegisterValidation.checkCredentialsAreNotNull(u);
        RegisterValidation.checkUserDoesNotAlreadyExist(u.getUsername());
        RegisterValidation.checkPhoneNumber(u.getTel());
        RegisterValidation.checkValidUsername(u.getUsername());
        RegisterValidation.checkValidPassword(u.getPassword());
        u.setPassword(RegisterValidation.encodePassword(u.getUsername(),u.getPassword()));
        if(u instanceof Guest)
            guests.add((Guest) u);
        else if(u instanceof Organizer)
            organizers.add((Organizer) u);
        persistUsers();

        userMap.put(u.getUsername(), u);
    }
    public static void addTask(String username, Task t){
        for(Wedding w : weddings)
            if(w.getUsername().equals(username)) {
                System.out.print("File found user");
                w.addTask(t);
                System.out.println(w.getTaskList());
            }

    }
    public static void addWedd(Wedding wed)
    {
        weddings.add(wed);
        //populate the wedmap with <username, wedding>
        for(var temp : weddings)
        {
            wedMap.put(temp.getUsername(), temp);
        }
    }

    public static void addWedList(WedListElem x)
    {
        wedList.add(x);
    }

    public static void addInvite(Invitation x)
    {
        invites.add(x);
    }
    public static void persistWed() {    //writes weddings list to file
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            //objectMapper.enableDefaultTyping();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(WeddingPATH.toFile(), weddings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteTask(ArrayList<Task> allTasks, String username) {
        for(Wedding w: weddings)
            if(w.getUsername().equals(username))
                w.setTaskList( allTasks);
    }
}
