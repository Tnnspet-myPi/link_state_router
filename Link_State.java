package link_state_router;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Link_State
{
  Map<Integer, Router> routers;
  
  public Link_State()
  {
    routers = new HashMap<Integer, Router>();
  }
  
  public void Process_File(File input_file) throws FileNotFoundException
  {
    Scanner scanner = new Scanner(input_file);
    Router current  = null;
    while (scanner.hasNextLine()) 
    {
      String[] split =scanner.nextLine().split(" ");
      
      if(!split[0].equals(""))
      {
        Integer id = Integer.parseInt(split[0]);
  
        if(routers.containsKey(id))
        {
          current = routers.get(id);
          current.network = split[1];

          if(split.length == 3)
          {
            current.network_cost = Integer.parseInt(split[2]);
          }
          else
          {
            current.network_cost = 1;
          }
        }
        else
        {
          Router temp;
          if(split.length == 3)
          {
            temp = new Router(split[0], split[1], split[2]);
          }
          else
          {
            temp = new Router(split[0], split[1],"1");
          }
          routers.put(id, temp);
          current = temp;
        }
      }
      else
      {
        Integer id = Integer.parseInt(split[1]);
        Router temp;
        
        if(routers.containsKey(id))
        {
          temp = routers.get(id);
        }
        else
        {
          temp = new Router(split[1]);
          routers.put(id, temp);
        }
        
        Integer cost = 1; 
        if(split.length == 3)
        {
          cost = Integer.parseInt(split[2]);
        }

        current.direct_links.put(id, new Pair(cost, 0));
      }
    }
    scanner.close();
  }
  
  public void Print_Routing_Table(String input)
  {
    Integer id = Integer.parseInt(input);
    if (routers.containsKey(id))
    {
      Router current = routers.get(id);
      PriorityQueue<Edge> queue = new PriorityQueue<Edge>(new Edge_Cost_Comparator());
      LinkedList<Edge> edges = new LinkedList<Edge>();
      Map<Integer, Boolean> visited = new HashMap<Integer, Boolean>();
      
      for(Map.Entry<Integer, Pair> links : current.direct_links.entrySet())
      {
        queue.add(new Edge(links.getKey(), links.getValue().Get_Cost(), links.getKey()));
      }
      
      
      while(!queue.isEmpty())
      {
        Edge here = queue.remove();
        
        if(visited.containsKey(here.id) || here.cost >= 100000000)
        {
          continue;
        }
        
        visited.put(here.id, true);
        edges.add(here);
        
        if(current.link_state_packets.containsKey(here.id))
        {
          Packet pack = current.link_state_packets.get(here.id);
          
          for(Map.Entry<Integer, Pair> links : pack.link_cost.entrySet())
          {
            if(links.getKey() != current.id)
            {
              if(!visited.containsKey(links.getKey()))
              {
                queue.add(new Edge(links.getKey(),links.getValue().Get_Cost() + here.cost,here.direct_link));
              }
            }
          }
        }
      }
      
      for(Edge s : edges)
      {
        Router print = routers.get(s.id);
        System.out.println(print.network + ", " + s.cost + ", " + s.direct_link);
      }
    }
  }
  
  public void Change_Router_State(String input, boolean new_state)
  {
    Integer id = Integer.parseInt(input);
    
    if(routers.containsKey(id))
    {
      Router temp = routers.get(id);
      temp.network_on = new_state;
    }
  }
  
  public void Continue_Process()
  {
    Router current;
    for(Map.Entry<Integer, Router> outer : routers.entrySet())
    {
      current = outer.getValue();
      current.Originate_Packet();
    }
    
    for(Map.Entry<Integer, Router> outer : routers.entrySet())
    {
      current = outer.getValue();
      if(current.current_link_state_packet != null)
      {
        for(Map.Entry<Integer, Pair> inner : current.direct_links.entrySet())
        {
          if(routers.containsKey(inner.getKey()))
          {
            Router temp = routers.get(inner.getKey());
            temp.Receive_Packet(current.current_link_state_packet, routers);
          }
        }
      }
    }
  }
}

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