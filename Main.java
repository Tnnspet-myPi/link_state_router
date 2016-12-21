package link_state_router;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main
{

  public static void main(String[] args) throws FileNotFoundException
  {
    Link_State link_state = new Link_State();
    File       input_file = new File("infile.dat");  
    String     input      = "";
    BufferedReader br     = new BufferedReader(new InputStreamReader(System.in));
    
    link_state.Process_File(input_file);

    
    while(!input.equals("Q"))
    {
      System.out.println("Conintue    (C)");
      System.out.println("Print Table (P <Integer>)");
      System.out.println("Shutdown    (S <Integer>)");
      System.out.println("Turn on     (T <Integer>)");
      System.out.println("Quit        (Q)");
      try
      {
        input = br.readLine();
        
        if(input.contains("C"))
        {
          link_state.Continue_Process();
        }
        else if(input.contains("S"))
        {
          String split[] = input.split(" ");
          
          link_state.Change_Router_State(split[1], false);
          System.out.println("Turning off " + split[1]);
        }
        else if(input.contains("T"))
        {
          String split[] = input.split(" ");
          
          link_state.Change_Router_State(split[1], true);
          System.out.println("Turning on " + split[1]);
        }
        else if(input.contains("P"))
        {
          String split[] = input.split(" ");
          link_state.Print_Routing_Table(split[1]);
        }
        else if(!input.contains("Q"))
        {
          System.out.println("Invalid input please try again");
        }
      }
      catch (IOException e)
      {
        System.out.println("Invalid input please try again");
      }
      catch(NumberFormatException e)
      {
        System.out.println("Invalid input please try again");
      }
    }
  }
}
