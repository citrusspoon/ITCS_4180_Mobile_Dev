public class User implements Comparable {
    public String firstName, lastName;
    public int age;
    public String email, gender, city, state;

    public User(){
        firstName = "none";
        lastName = "none";
        age = 0;
        email = "none";
        gender = "none";
        city = "none";
        state = "none";
    }

    public User(String f, String l, int a, String e, String g, String c, String s ){
        firstName = f;
        lastName = l;
        age = a;
        email = e;
        gender = g;
        city = c;
        state = s;
    }

    public User(String s){
        String[] result = s.split(",");
        firstName = result[0];
        lastName = result[1];
        age = Integer.parseInt(result[2]);
        email = result[3];
        gender = result[4];
        city = result[5];
        state = result[6];
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

    @Override
    public int compareTo(User u) {
        return this.age - u.age;
    }
}
