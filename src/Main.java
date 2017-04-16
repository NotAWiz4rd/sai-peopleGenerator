import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by NotAWiz4rd on 16.04.2017.
 */
public class Main
{
  public static void main(String[] args)
    throws IOException
  {
    System.out.println("How many people should be generated?");
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    int input = Integer.parseInt(bufferedReader.readLine());
    generatePeople(input);
  }

  private static void generatePeople(int count)
  {
    System.out.println(count + " people generated");
  }
}
