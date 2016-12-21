package link_state_router;

public class Pair {

  private Integer cost;
  private Integer tick;

  /*---------------------------------------------------------*/
  /* Function Name: Pair                                     */
  /*                                                         */
  /* Description: Prototype for Pair class                   */
  /*                                                         */
  /*---------------------------------------------------------*/
  public Pair(Integer cost, Integer tick) {
      this.cost  = cost;
      this.tick  = tick;
  }  // End function Pair
  
  /*---------------------------------------------------------*/
  /* Function Name: Get_Cost                                 */
  /*                                                         */
  /* Description: Get function for cost.                     */
  /*                                                         */
  /*---------------------------------------------------------*/
  public Integer Get_Cost()  
  { 
    return cost;  
  }  // End function Get_Cost
  
  /*---------------------------------------------------------*/
  /* Function Name: Get_Tick                                 */
  /*                                                         */
  /* Description: Get function for tick.                     */
  /*                                                         */
  /*---------------------------------------------------------*/
  public Integer Get_Tick()  
  { 
    return tick;  
  }  // End function Get_Tick
  
  /*---------------------------------------------------------*/
  /* Function Name: Set_Cost                                 */
  /*                                                         */
  /* Description: Set function for cost.                     */
  /*                                                         */
  /*---------------------------------------------------------*/
  public void Set_Cost(Integer cost) 
  { 
    this.cost = cost; 
  }  // End function Set_Cost
  
  /*---------------------------------------------------------*/
  /* Function Name: Set_Tick                                 */
  /*                                                         */
  /* Description: Set function for tick.                     */
  /*                                                         */
  /*---------------------------------------------------------*/
  public void Set_Tick(Integer tick) 
  { 
    this.tick = tick; 
  }  // End function Set_Tick
}  // End Class Pair
