import java.util.ArrayList;
public class MainPart1 {
    /*
    * Question 1:
    * - In this question you will use the Data.users array that includes
    * a list of users. Formatted as : firstname,lastname,age,email,gender,city,state
    * - Create a User class that should parse all the parameters for each user.
    * - Insert each of the users in a list.
    * - Print out the TOP 10 oldest users.
    * */

    public static void main(String[] args) {

        //example on how to access the Data.users array.
        ArrayList<User> userList = new ArrayList();
        for (String str : Data.users) {
            userList.add(new User(str));
        }

        System.out.println(userList.get(1));



    }
}



