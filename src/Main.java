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
  private static final int MAX_AGE = 50;
  private static final int MIN_AGE = 10;

  private static final int MAX_HEIGHT = 205;
  private static final int MIN_HEIGHT = 150;

  private static final int MAX_PROPERTIES = 10;

  private static final int MAX_OPINIONS = 20;

  private static final String PROPERTYFILE = "Resources/definitions.properties";
  private static final String LOCATIONFILE = "Resources/groups.locations";

  private static PrintWriter printWriter;

  private static int age;
  private static int height;

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
    for(int i = 1; i < count + 1; i++) // starts counting at 1
    {
      writeToFile("{");
      System.out.println("Persona #" + i);
      writeToFile("id: '" + i + "'");
      System.out.println("Name: " + generateName());
      System.out.println("Sex: " + generateSex());
      age = generateAge();
      height = generateHeight();
      generateWorkplace();
      generateAttributes();
      generateProperties();
      System.out.println("--------------------");
      writeToFile("}");
      writeToFile("&"); // marks end of person for main program
    }
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
    throws FileNotFoundException
  {
    // strength, charisma, intelligence, dexterity, constitution
    writeToFile("attributes");
    writeToFile("{");
    Random random = new Random();

    for(int i = 0; i < 5; i++) // generate 5 numbers with max 20 and min 1, expected value 11
    {
      int x = (int) (random.nextGaussian() * 5) + 11;
      if(x > 20)
      {
        x = 20;
      }
      else if(x < 1)
      {
        x = 1;
      }
      writeToFile("'" + x + "'");
    }
    writeToFile("}");
  }

  private static void generateProperties()
    throws IOException
  {
    writeToFile("properties");
    writeToFile("{");
    ArrayList<String> properties = (ArrayList<String>) readFile(PROPERTYFILE);

    Random random = new Random();
    int border;

    if(MAX_PROPERTIES < properties.size())
    {
      border = random.nextInt(MAX_PROPERTIES) + 1;
    }
    else
    {
      border = random.nextInt(properties.size()) + 1;
    }

    for(int i = 0; i < border; i++)
    {
      int propertyID = random.nextInt(properties.size());
      writeToFile("'" + properties.get(propertyID) + "'");
      properties.remove(propertyID);
    }
    writeToFile("}");

    generateOpinions(properties);  // generate only opinions for properties the person doesn't have
  }

  private static void generateOpinions(ArrayList<String> properties)
    throws FileNotFoundException
  {
    writeToFile("opinions");
    writeToFile("{");

    Random random = new Random();
    int amount = random.nextInt(MAX_OPINIONS);

    if(amount > properties.size())
    {
      amount = properties.size();
    }
    else if(amount < 1)
    {
      amount = 1;
    }

    for(int i = 0; i < amount; i++)
    {
      writeToFile("{");
      int propertyID = random.nextInt(properties.size());
      writeToFile("property: '" + properties.get(propertyID) + "'");

      if(random.nextBoolean())  // generate impact of a maximum 99 with maybe a - in front
      {
        writeToFile("impact: '" + random.nextInt(100) + "'");
      }
      else
      {
        writeToFile("impact: '-" + random.nextInt(100) + "'");
      }

      writeToFile("personalLevel: '" + random.nextInt(101) + "'"); // generate personalLevel with Max of 100

      writeToFile("}");
      properties.remove(propertyID);
    }
    writeToFile("}");
  }

  private static List<String> readFile(String filename)
    throws IOException
  {
    Path path = Paths.get(filename);
    return Files.readAllLines(path);
  }

  private static void generateWorkplace()
    throws IOException
  {
    String workplaceGroupID = "";
    ArrayList<String> locationGroups = (ArrayList<String>) readFile(LOCATIONFILE);
    ArrayList<String> possibleWorkplaces = new ArrayList<>();

    for(String locationGroup : locationGroups)
    {
      String[] splitGroup = locationGroup.split(";");

      if(splitGroup[1].contains(",")) // check for multiple conditions
      {
        String[] splitConditions = splitGroup[1].split(",");
        int checkCounter = 0;
        for(String splitCondition : splitConditions)
        {
          if(decryptWorkplaceCondition(splitCondition))
          {
            checkCounter++;
          }
        }
        if(checkCounter == splitConditions.length) // only add if all conditions are met
        {
          possibleWorkplaces.add(splitGroup[0]);
        }
      }
      else if(decryptWorkplaceCondition(splitGroup[1]))
      {
        possibleWorkplaces.add(splitGroup[0]);
      }
    }

    if(possibleWorkplaces.size() > 0) // if there are possible Workplaces choose one at random
    {
      Random random = new Random();
      workplaceGroupID = possibleWorkplaces.get(random.nextInt(possibleWorkplaces.size()));
    }
    writeToFile("workplace: '#" + workplaceGroupID + "'");
  }

  private static boolean decryptWorkplaceCondition(String condition)
  {
    String[] conditionPoints = condition.split("-");

    switch(conditionPoints[0]) // add conditions here and check against already generated stuff
    {
      case "MAXAGE":
        return age <= Integer.parseInt(conditionPoints[1]);
      case "MINAGE":
        return age >= Integer.parseInt(conditionPoints[1]);
      case "MINHEIGHT":
        return height >= Integer.parseInt(conditionPoints[1]);
      case "MAXHEIGHT":
        return height <= Integer.parseInt(conditionPoints[1]);
    }
    return false;
  }
}
