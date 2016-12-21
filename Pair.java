package link_state_router;

public class Pair {

  private Integer cost;
  private Integer tick;

  public Pair(Integer cost, Integer tick) {
      this.cost  = cost;
      this.tick  = tick;
  }
  
  public Integer Get_Cost()  
  { 
    return cost;  
  }
  
  public Integer Get_Tick()  
  { 
    return tick;  
  }
  
  public void Set_Cost(Integer cost) 
  { 
    this.cost = cost; 
  }
  
  public void Set_Tick(Integer tick) 
  { 
    this.tick = tick; 
  }
}
