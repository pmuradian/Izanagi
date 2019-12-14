package main.generators;

import main.models.User;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UserGenerator {

    public static void main(String[] args) {
        Integer count = 1000;
//        generateUsers(count);
        localGenerateUsers(count);
    }

    private static ArrayList users = new ArrayList<User>();

    private static void localGenerateUsers(Integer count) {
        while (count-- > 0) {
            users.add(nextUser());
        }
    }

    private static List<User> generateUsers(Integer count) {
        List<User> users = new ArrayList<>();
        while (count-- > 0) {
            User user = nextUser();
            String json = jsonFromUser(user);
            try {
                Requester.sendRequest(json);
                users.add(user);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        return users;
    }

    private static int userCounter = 0;
    private static User nextUser() {
        String id = "id";
        String login = "rick_" + userCounter;
        String password = "password" + userCounter;
        String email = "rickSanchez" + userCounter + "@citadel.mul";
        User user = new User(id, login, password, email);
        userCounter++;
        return user;
    }

    private static String jsonFromUser(User user) {
        String json = "{" +
                "\"login\": \"" + user.getLogin() +
                "\", \"password\":\"" + user.getPassword() +
                "\", \"email\":\"" + user.getEmail() +
                 "\"}";
        return json;
    }
}

class Requester {
    static void sendRequest(String json) throws Exception {
        URL url = new URL("http://localhost:8080/users/");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");

        con.setDoOutput(true);
        // send request
        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.writeBytes(json);
            wr.flush();
        }

        int responseCode = con.getResponseCode();
        if (responseCode == 200) {
            System.out.println("created user");
        }

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()))) {

            String line;
            StringBuilder response = new StringBuilder();

            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            //print result
            System.out.println(response.toString());

        }
    }
}

