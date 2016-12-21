package link_state_router;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Link_State
{
  Map<Integer, Router> routers;
  
  /*---------------------------------------------------------*/
  /* Function Name: Link_State                               */
  /*                                                         */
  /* Description: Main prototype for Link State              */
  /*                                                         */
  /*---------------------------------------------------------*/
  public Link_State()
  {
    routers = new HashMap<Integer, Router>();
  }
  
  /*---------------------------------------------------------*/
  /* Function Name: Process_File                             */
  /*                                                         */
  /* Description: This function takes in a file and          */
  /*              extracts all necessary data needed to      */
  /*              put together the network of routers.       */
  /*                                                         */
  /*---------------------------------------------------------*/
  public void Process_File(File input_file) throws FileNotFoundException
  {
    Scanner scanner = new Scanner(input_file);
    Router current  = null;
    String[] split;
    
    // Continue reading file as long as there is another line.
    while (scanner.hasNextLine()) 
    {
      // Split the string up based on spaces. 
      split = scanner.nextLine().split(" ");
      
      // If the first part is not empty then we are out router information
      if(!split[0].equals(""))
      {
        // Extract the ID of the router and create a temp router
        Integer id  = Integer.parseInt(split[0]);
        Router temp = new Router(split[0], split[1],"1");
        
        // If user added cost then add it otherwise set cost to 1
        if(split.length == 3)
        {
          temp.network_cost = Integer.parseInt(split[2]);
        }

        // Add routers to our hash map list and set it to current
        routers.put(id, temp);
        current = temp;
      }
      // Here we add direct link information
      else
      {
        // Parse integer information
        Integer id = Integer.parseInt(split[1]);
        Integer cost = 1; 
        
        // If user added cost then set it otherwise remain at 1
        if(split.length == 3)
        {
          cost = Integer.parseInt(split[2]);
        }

        // Add direct link to current router and update routing table
        current.direct_links.put(id, new Pair(cost, 0));
        current.Update_Routing_Table();
      }
    }  // End While loop
    
    // Close Scanner
    scanner.close();
  }  //End function Process_File
  
  /*---------------------------------------------------------*/
  /* Function Name: Print_Routing_Table                      */
  /*                                                         */
  /* Description: This function prints the routing table     */
  /*              for a specific router.                     */
  /*                                                         */
  /*---------------------------------------------------------*/
  public void Print_Routing_Table(String input)
  {
    // Change String to id
    Integer id = Integer.parseInt(input);
    if (routers.containsKey(id))
    {
      // Get current router out of our list
      Router current = routers.get(id);
      
      // Ensure the router is updated by running dijkstra algorithm 
      current.Update_Routing_Table();
      
      // Loop through all edges and print to the user 
      for(Edge s : current.edges)
      {
        // Get the router to get the network name
        // and then print to information to the user. 
        Router print = routers.get(s.id);
        if(s.cost < 100000000)
        {
          System.out.println(print.network + ", " + s.cost + ", " + s.direct_link);
        }
        else
        {
          System.out.println(print.network + ", Infinity, " + s.direct_link);
        }
      }  // End for loop
    }
  }  // End function Print_Routing_Table
  
  /*---------------------------------------------------------*/
  /* Function Name: Change_Router_State                      */
  /*                                                         */
  /* Description: This function changes the state of a       */
  /*              router if it is on or off.                 */
  /*                                                         */
  /*---------------------------------------------------------*/
  public void Change_Router_State(String input, boolean new_state)
  {
    // Get id by parse integer
    Integer id = Integer.parseInt(input);
    
    // Make sure the router is apart of our list
    if(routers.containsKey(id))
    {
      // Update its information
      Router temp = routers.get(id);
      temp.network_on = new_state;
    }
  }  // End function Change_Router_State
  
  /*---------------------------------------------------------*/
  /* Function Name: Continue_Process                         */
  /*                                                         */
  /* Description: This function goes through a cycle by      */
  /*              flooding the network with link state       */
  /*              packets.                                   */
  /*                                                         */
  /*---------------------------------------------------------*/
  public void Continue_Process()
  {
    Router current;
    
    // Loop through all routers and get them to originate their 
    // link state packet
    for(Map.Entry<Integer, Router> outer : routers.entrySet())
    {
      outer.getValue().Originate_Packet();;
    }
    
    // Loop through again to have all link state packets 
    // flood the network
    for(Map.Entry<Integer, Router> outer : routers.entrySet())
    {
      // Get current router
      current = outer.getValue();
      
      // Make sure the router created a link state packet to send
      if(current.current_link_state_packet != null)
      {
        // Loop through all direct links to send its link state packet
        for(Map.Entry<Integer, Pair> inner : current.direct_links.entrySet())
        {
          // Make sure the router exist and then send the information
          if(routers.containsKey(inner.getKey()))
          {
            routers.get(inner.getKey()).Receive_Packet(current.current_link_state_packet, routers);
          }
        }
      }
    }  // End for loop
  }  // End function Continue_Process
}  // End class Link_State
