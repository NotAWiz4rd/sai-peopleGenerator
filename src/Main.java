import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by NotAWiz4rd on 16.04.2017.
 */
public class Main
{
  private static int MAX_AGE = 50;
  private static int MIN_AGE = 10;

  private static int MAX_HEIGHT = 205;
  private static int MIN_HEIGHT = 150;

  private static String propertyFile = "Resources/definitions.properties";

  private static PrintWriter printWriter;

  public static void main(String[] args)
    throws IOException
  {
    printWriter = new PrintWriter("definitions.people");

    System.out.println("How many people should be generated?");
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    int input = Integer.parseInt(bufferedReader.readLine());

    generatePeople(input);
    System.out.println(input + " people generated");

    printWriter.close();
  }

  private static void generatePeople(int count)
    throws IOException
  {
    writeToFile("people");
    writeToFile("{");
    for(int i = 1; i < count + 1; i++) // starts counting at 1
    {
      writeToFile("{");
      System.out.println("Persona " + i);
      System.out.println("Name: " + generateName());
      System.out.println("Sex: " + generateSex());
      System.out.println("Age: " + generateAge());
      System.out.println("Height: " + generateHeight());
      generateAttributes();
      generateProperties();
      generateOpinions();
      System.out.println("--------------------");
      writeToFile("}");
      writeToFile("");
    }
    writeToFile("}");
  }

  private static void writeToFile(String writable) // writes generated String into a file which is readable by the main application
    throws FileNotFoundException
  {
    printWriter.println(writable);
  }

  private static String generateName()
    throws FileNotFoundException  // generates a random "name" which is a random configuration of chars
  {
    char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    StringBuilder sb = new StringBuilder();
    Random random = new Random();
    for(int i = 0; i < random.nextInt(17) + 3; i++)
    {
      char c = chars[random.nextInt(chars.length)];
      sb.append(c);
    }
    String name = sb.toString();
    writeToFile("name: '" + name + "'");
    return name;
  }

  private static int generateAge()
    throws FileNotFoundException // generates an age between the set Max and Min age
  {
    Random random = new Random();
    int age = MAX_AGE - random.nextInt(MAX_AGE - MIN_AGE);
    writeToFile("age: '" + age + "'");
    return age;
  }

  private static int generateHeight()
    throws FileNotFoundException // generates a height between the set Max and Min height
  {
    Random random = new Random();
    int height = MAX_HEIGHT - random.nextInt(MAX_HEIGHT - MIN_HEIGHT);
    writeToFile("height: '" + height + "'");
    return height;
  }

  private static String generateSex()
    throws FileNotFoundException // generates a sex as String
  {
    Random random = new Random();
    if(random.nextBoolean())
    {
      writeToFile("sex: 'male'");
      return "male";
    }
    else
    {
      writeToFile("sex: 'female'");
      return "female";
    }
  }

  private static void generateAttributes()
  {
    // strength, charisma, intelligence, dexterity, constitution
    // TODO think about max and min amounts of attribute points to be spend here randomly
  }

  private static void generateProperties()
    throws IOException
  {
    writeToFile("properties");
    writeToFile("{");
    ArrayList<String> properties = (ArrayList<String>) readProperties();

    Random random = new Random();
    int border = random.nextInt(properties.size() + 1);
    for(int i = 0; i < border; i++)
    {
      int propertyID = random.nextInt(properties.size());
      writeToFile("'" + properties.get(propertyID) + "'");
      properties.remove(propertyID);
    }
    writeToFile("}");
  }

  private static void generateOpinions()
    throws FileNotFoundException
  {
    writeToFile("opinions");
    writeToFile("{");
    // TODO read stored properties and choose random ones and generate a random impact and personalLevel
    writeToFile("}");
  }

  private static List<String> readProperties()
    throws IOException
  {
    Path path = Paths.get(propertyFile);
    return Files.readAllLines(path);
  }
}
