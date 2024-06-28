import java.util.Random;

public class MockData {
    private static final String[] FIRST_NAMES = {"John", "Alice", "Bob", "Emma", "Michael", "Sophia", "David", "Olivia", "James", "Emily",
    "Daniel", "Isabella", "Matthew", "Charlotte", "Joseph", "Abigail", "Samuel", "Avery", "Benjamin", "Grace","Robespierre", "Gojo", "Saitama", "Kakashi", "Itachi", 
    "Naruto", "Sasuke", "Luffy", "Zoro", "Sanji", "Goku", "Vegeta", "Gon", "Killua", "Kurapika", "Leorio", "Hisoka", "Ging", "Netero", "Meruem", "Pitou", "Kite", 
    "Chrollo", "Illumi", "Hisoka", "Feitan", "Phinks", "Uvogin", "Machi", "Shizuku", "Kalluto", "Alluka", "Nanika", "Mito", "Kikyo", "Kite", "Gon", "Killua", "Kurapika", 
    "Leorio", "Hisoka", "Ging", "Netero", "Meruem", "Pitou", "Kite", "Chrollo", "Illumi", "Hisoka", "Feitan", "Phinks", "Uvogin", "Machi", "Shizuku", "Kalluto", "Alluka", 
    "Nanika", "Mito", "Kikyo", "Kite"};
    
    private static final String[] LAST_NAMES = {"Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor",
    "Anderson", "Thomas", "Jackson", "White", "Harris", "Martin", "Thompson", "Garcia", "Martinez", "Robinson"};

    private static final String[] EMAIL_PROVIDERS = { "gmail.com", "yahoo.com", "hotmail.com", "outlook.com",
            "icloud.com" };
    private static final Random RANDOM = new Random();

    public static String getEmail() {
        String firstName = FIRST_NAMES[RANDOM.nextInt(FIRST_NAMES.length)];
        String lastName = LAST_NAMES[RANDOM.nextInt(LAST_NAMES.length)];
        String emailProvider = EMAIL_PROVIDERS[RANDOM.nextInt(EMAIL_PROVIDERS.length)];
        return firstName.toLowerCase() + "." + lastName.toLowerCase() + "@" + emailProvider;
    }

    public static String getPassword(int length) {
        StringBuilder password = new StringBuilder();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < length; i++) {
            password.append(characters.charAt(RANDOM.nextInt(characters.length())));
        }
        return password.toString();
    }

    public static String getName() {
        String firstName = FIRST_NAMES[RANDOM.nextInt(FIRST_NAMES.length)];
        String lastName = LAST_NAMES[RANDOM.nextInt(LAST_NAMES.length)];
        return firstName + " " + lastName;
    }

    public static int getID() {
        return 10000 + RANDOM.nextInt(9000); // Generate a 4-digit ID
    }

    public static int getAge(int min, int max) {
        return min + RANDOM.nextInt(max - min); // Generate an age between 18 and 100
    }

    public static double getSalary(double min, double max) {
        return min + (max - min) * RANDOM.nextDouble(); // Generate a salary between 1000 and 10000
    }

    public static boolean getBool() {
        return RANDOM.nextBoolean();
    }

    public static String getPhone() {
        StringBuilder phone = new StringBuilder();
        phone.append("(");
        phone.append(RANDOM.nextInt(900) + 100);
        phone.append(") ");
        phone.append(RANDOM.nextInt(9000) + 1000);
        phone.append("-");
        phone.append(RANDOM.nextInt(9000) + 1000);
        return phone.toString();
    }

    public static String getAddress() {
        String[] streets = { "Main St", "Park Ave", "Elm St", "Maple Ave", "Oak St", "Washington St", "Lake St",
                "Pine St", "Broadway", "Cedar St" };
        String[] cities = { "New York", "Los Angeles", "Chicago", "Houston", "Phoenix", "Philadelphia", "San Antonio",
                "San Diego", "Dallas", "San Jose" };
        String street = streets[RANDOM.nextInt(streets.length)];
        String city = cities[RANDOM.nextInt(cities.length)];
        return RANDOM.nextInt(1000) + " " + street + ", " + city;
    }

    public static char getGender() {
        boolean gender = RANDOM.nextBoolean();
        if(gender){
            return 'M';
        }
        return 'F';
    }

}
