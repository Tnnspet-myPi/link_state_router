package link_state_router;

import java.util.HashMap;
import java.util.Map;

public class Router
{
  Integer id;
  Integer network_cost;
  Integer sequence_num;
  String  network;
  boolean network_on;
  Map<Integer, Packet> link_state_packets;
  Map<Integer, Pair> direct_links;
  Map<Integer, Integer> save_cost;
  Packet current_link_state_packet;

  public Router()
  {
    this.network_on   = true;
    this.direct_links = new HashMap<Integer, Pair>();
    this.sequence_num = 0;
    this.save_cost    = new HashMap<Integer, Integer>();
    this.link_state_packets = new HashMap<Integer, Packet>();
  }

  public Router(String id)
  {
    this.id           = Integer.parseInt(id);
    this.network_cost = 0;
    this.network_on   = true;
    this.direct_links = new HashMap<Integer, Pair>();
    this.sequence_num = 0;
    this.save_cost    = new HashMap<Integer, Integer>();
    this.link_state_packets = new HashMap<Integer, Packet>();
  }
  
  public Router(String id, String network)
  {
    this.id           = Integer.parseInt(id);
    this.network      = network;
    this.network_cost = 0;
    this.network_on   = true;
    this.direct_links = new HashMap<Integer, Pair>();
    this.sequence_num = 0;
    this.save_cost    = new HashMap<Integer, Integer>();
    this.link_state_packets = new HashMap<Integer, Packet>();
  }
  
  public Router(String id, String network, String network_cost)
  {
    this.id           = Integer.parseInt(id);
    this.network      = network;
    this.network_cost = Integer.parseInt(network_cost);
    this.network_on   = true;
    this.direct_links = new HashMap<Integer, Pair>();
    this.sequence_num = 0;
    this.save_cost    = new HashMap<Integer, Integer>();
    this.link_state_packets = new HashMap<Integer, Packet>();
  }

  public void Receive_Packet(Packet input, Map<Integer, Router> routers)
  {
    if(network_on == true)
    {
      input.time_to_live--;
      
      // Reset the tick
      if(direct_links.containsKey(input.id))
      {
        Pair pairs = direct_links.get(input.id);
        
        if(save_cost.containsKey(input.id))
        {
          pairs.Set_Cost(save_cost.get(input.id));
          save_cost.remove(input.id);
        }
        pairs.Set_Tick(0);
      }

      // If our time to live is over then return
      if(input.time_to_live == 0 || input.id == this.id)
      {
        return;
      }

      if(link_state_packets.containsKey(input.id))
      {
        Packet temp = link_state_packets.get(input.id);

        // If we have seen a packet of higher sequence then return
        if(temp.sequence >= input.sequence)
        {
          return;
        }
        
        link_state_packets.put(input.id, input);
      }
      else
      {
        link_state_packets.put(input.id, input);
      }
      
      for(Map.Entry<Integer, Pair> link : direct_links.entrySet())
      {
        Router router = routers.get(link.getKey());
        router.Receive_Packet(input, routers);
      }
    }
  }
  
  public void Originate_Packet()
  {
    current_link_state_packet = null;
    
    if(network_on == true)
    {
      for(Map.Entry<Integer, Pair> my_map : direct_links.entrySet())
      {
        my_map.getValue().Set_Tick(my_map.getValue().Get_Tick() + 1);
        
        if(my_map.getValue().Get_Tick() == 2)
        {
          save_cost.put(my_map.getKey(), my_map.getValue().Get_Cost());
          my_map.getValue().Set_Cost(100000000);
        }
      }
      
      current_link_state_packet = new Packet(id,++sequence_num, direct_links);
    }
  }
}
