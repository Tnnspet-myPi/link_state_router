package link_state_router;

import java.util.HashMap;
import java.util.Map;

public class Packet
{
  Integer id;
  Integer sequence;
  Integer time_to_live;
  Map<Integer, Pair> link_cost;
  
  public Packet()
  {
    this.time_to_live = 10;
    this.link_cost    = new HashMap<Integer, Pair>();
  }
  
  public Packet(Integer id, Integer sequence)
  {
    this.id           = id;
    this.sequence     = sequence;
    this.time_to_live = 10;
    this.link_cost    = new HashMap<Integer, Pair>();
  }
  
  public Packet(Integer id, Integer sequence, Map<Integer, Pair> current)
  {
    this.id           = id;
    this.sequence     = sequence;
    this.time_to_live = 10;
    this.link_cost    = current;
  }
}
