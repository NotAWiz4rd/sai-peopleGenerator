import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

  public static void main(String[] args)
    throws IOException
  {
    System.out.println("How many people should be generated?");
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    int input = Integer.parseInt(bufferedReader.readLine());
    generatePeople(input);
    System.out.println(input + " people generated");
  }

  private static void generatePeople(int count)
  {
    for(int i = 0; i < count; i++)
    {
      System.out.println("Persona " + i);
      System.out.println("Name: " + generateName());
      System.out.println("Age: " + generateAge());
      System.out.println("Height: " + generateHeight());
      System.out.println("Sex: " + generateSex());
      System.out.println("--------------------");
    }

  }

  private static String generateName()
  {
    char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    StringBuilder sb = new StringBuilder();
    Random random = new Random();
    for(int i = 0; i < random.nextInt(17) + 3; i++)
    {
      char c = chars[random.nextInt(chars.length)];
      sb.append(c);
    }
    return sb.toString();
  }

  private static int generateAge()
  {
    Random random = new Random();
    return MAX_AGE - random.nextInt(MAX_AGE - MIN_AGE);
  }

  private static int generateHeight()
  {
    Random random = new Random();
    return MAX_HEIGHT - random.nextInt(MAX_HEIGHT - MIN_HEIGHT);
  }

  private static String generateSex()
  {
    Random random = new Random();
    if(random.nextBoolean())
    {
      return "male";
    }
    else
    {
      return "female";
    }
  }
}
