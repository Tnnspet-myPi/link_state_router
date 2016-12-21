package link_state_router;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;

public class Router
{
  Integer id;
  Integer network_cost;
  Integer sequence_num;
  String  network;
  boolean network_on;
  Packet  current_link_state_packet;
  
  Map<Integer, Packet>  link_state_packets;
  Map<Integer, Pair>    direct_links;
  Map<Integer, Integer> save_cost;
  LinkedList<Edge>      edges;

  /*---------------------------------------------------------*/
  /* Function Name: Router                                   */
  /*                                                         */
  /* Description: Prototype for Router class.                */
  /*                                                         */
  /*---------------------------------------------------------*/
  public Router()
  {
    this.network_on   = true;
    this.direct_links = new HashMap<Integer, Pair>();
    this.sequence_num = 0;
    this.save_cost    = new HashMap<Integer, Integer>();
    this.edges        = new LinkedList<Edge>();
    this.link_state_packets = new HashMap<Integer, Packet>();
  }

  /*---------------------------------------------------------*/
  /* Function Name: Router                                   */
  /*                                                         */
  /* Description: Prototype for Router class that takes in   */
  /*              ID.                                        */
  /*                                                         */
  /*---------------------------------------------------------*/
  public Router(String id)
  {
    this.id           = Integer.parseInt(id);
    this.network_cost = 0;
    this.network_on   = true;
    this.direct_links = new HashMap<Integer, Pair>();
    this.sequence_num = 0;
    this.save_cost    = new HashMap<Integer, Integer>();
    this.edges        = new LinkedList<Edge>();
    this.link_state_packets = new HashMap<Integer, Packet>();
  }
  
  /*---------------------------------------------------------*/
  /* Function Name: Router                                   */
  /*                                                         */
  /* Description: Prototype for Router class that takes in   */
  /*              ID and Network.                            */
  /*                                                         */
  /*---------------------------------------------------------*/
  public Router(String id, String network)
  {
    this.id           = Integer.parseInt(id);
    this.network      = network;
    this.network_cost = 0;
    this.network_on   = true;
    this.direct_links = new HashMap<Integer, Pair>();
    this.sequence_num = 0;
    this.save_cost    = new HashMap<Integer, Integer>();
    this.edges        = new LinkedList<Edge>();
    this.link_state_packets = new HashMap<Integer, Packet>();
  }

  /*---------------------------------------------------------*/
  /* Function Name: Router                                   */
  /*                                                         */
  /* Description: Prototype for Router class that takes in   */
  /*              ID and Network and Network Cost.           */
  /*                                                         */
  /*---------------------------------------------------------*/
  public Router(String id, String network, String network_cost)
  {
    this.id           = Integer.parseInt(id);
    this.network      = network;
    this.network_cost = Integer.parseInt(network_cost);
    this.network_on   = true;
    this.direct_links = new HashMap<Integer, Pair>();
    this.sequence_num = 0;
    this.save_cost    = new HashMap<Integer, Integer>();
    this.edges        = new LinkedList<Edge>();
    this.link_state_packets = new HashMap<Integer, Packet>();
  }

  /*---------------------------------------------------------*/
  /* Function Name: Receive_Packet                           */
  /*                                                         */
  /* Description: This function receives link state          */
  /*              packets and forwards on as necessary.      */
  /*                                                         */
  /*---------------------------------------------------------*/
  public void Receive_Packet(Packet input, Map<Integer, Router> routers)
  {
    // Make sure the router is turned on
    if(network_on == true)
    {
      // Decrement time to live
      input.time_to_live--;
      
      // Reset the tick mark since we received a packet
      if(direct_links.containsKey(input.id))
      {
        Pair pairs = direct_links.get(input.id);
        
        // If the router was originally turned off then and
        // now is turned on then put the cost back to normal
        if(save_cost.containsKey(input.id))
        {
          pairs.Set_Cost(save_cost.get(input.id));
          save_cost.remove(input.id);
          this.Update_Routing_Table();
        }
        pairs.Set_Tick(0);
      }

      // If our time to live is over then return
      if(input.time_to_live == 0 || input.id == this.id)
      {
        return;
      }

      // Check to see if we have received a link state packet
      // in the past from the same router. 
      if(link_state_packets.containsKey(input.id))
      {
        // If we have received one then grab the packet
        Packet temp = link_state_packets.get(input.id);

        // If we have seen a packet of higher sequence then return
        if(temp.sequence >= input.sequence)
        {
          return;
        }
        
        // Put the latest link state packet in our memory
        link_state_packets.put(input.id, input);
      }
      else
      {
        // Put the latest link state packet in our memory
        link_state_packets.put(input.id, input);
      }
      
      // Loop through all direct links to forward packet along. 
      for(Map.Entry<Integer, Pair> link : direct_links.entrySet())
      {
        Router router = routers.get(link.getKey());
        router.Receive_Packet(input, routers);
      }  // End for loop
    }
  }  // End function Receive_Packet
  
  /*---------------------------------------------------------*/
  /* Function Name: Originate_Packet                         */
  /*                                                         */
  /* Description: This function creates a link state         */
  /*              packet with how it sees the network.       */
  /*                                                         */
  /*---------------------------------------------------------*/
  public void Originate_Packet()
  {
    current_link_state_packet = null;
    
    // Make sure the network is on
    if(network_on == true)
    {
      // Loop through all direct links
      for(Map.Entry<Integer, Pair> my_map : direct_links.entrySet())
      {
        // Add 1 to the tick level
        my_map.getValue().Set_Tick(my_map.getValue().Get_Tick() + 1);
        
        // If it has been 2 tick levels then change router to 
        // 100000000 (infinity) and then update routing table
        if(my_map.getValue().Get_Tick() == 2)
        {
          save_cost.put(my_map.getKey(), my_map.getValue().Get_Cost());
          my_map.getValue().Set_Cost(100000000);
          this.Update_Routing_Table();
        }
      }  // End for Loop
      
      current_link_state_packet = new Packet(id,++sequence_num, direct_links);
    }
  }  // End function Originate_Packet

  /*---------------------------------------------------------*/
  /* Function Name: Update_Routing_Table                     */
  /*                                                         */
  /* Description: This function uses dijkstras algorithm     */
  /*              to update its routing table.               */
  /*                                                         */
  /*---------------------------------------------------------*/
  public void Update_Routing_Table()
  {
    PriorityQueue<Edge>   queue   = new PriorityQueue<Edge>(new Edge_Cost_Comparator());
    Map<Integer, Boolean> visited = new HashMap<Integer, Boolean>();
    
    // Clear current routing table
    edges.clear();
    
    // Loop through all direct links
    for(Map.Entry<Integer, Pair> links : direct_links.entrySet())
    {
      // Add direct links to priority queue
      queue.add(new Edge(links.getKey(), links.getValue().Get_Cost(), links.getKey()));
    }
    
    // While queue is not empty
    while(!queue.isEmpty())
    {
      // Grab the lowest costing link
      Edge here = queue.remove();
      
      // If we have visited that ID before then we already
      // found its lowest costing link so continue on
      if(visited.containsKey(here.id))
      {
        continue;
      }
      
      // If we have not visited the link before then this
      // Link is the lowest cost to reach it. Add it to 
      // the routing table and set its visit to true
      visited.put(here.id, true);
      edges.add(here);
      
      // Now we take all adj edges from the current link
      // to add its total cost for new edges
      if(this.link_state_packets.containsKey(here.id))
      {
        // Get the direct link information from current edge
        Packet pack = this.link_state_packets.get(here.id);
        
        // Loop through all adjacent edges
        for(Map.Entry<Integer, Pair> links : pack.link_cost.entrySet())
        {
          // Make sure we don't loop back
          if(links.getKey() != this.id)
          {
            // Make sure the edge we want to add has not been found already
            // to have a lower costing edge
            if(!visited.containsKey(links.getKey()))
            {
              // Add the edge with a new total cost
              queue.add(new Edge(links.getKey(),links.getValue().Get_Cost() + here.cost,here.direct_link));
            }
          }
        }  // End for loop
      }
    }  // End While loop
  }  // End function Update_Routing_Table
}  // End Class Router

/*---------------------------------------------------------*/
/* Class Name: Edge_Cost_Comparator                        */
/*                                                         */
/* Description: This class overrides current compare to    */
/*              create a new Comparator to compare cost.   */
/*                                                         */
/*---------------------------------------------------------*/
class Edge_Cost_Comparator implements Comparator<Edge>
{
  @Override
  public int compare(Edge x, Edge y)
  {
    if(x.cost < y.cost)
    {
      return -1;
    }
    else if(x.cost > y.cost)
    {
      return 1;
    }
    return 0;
  }
}
