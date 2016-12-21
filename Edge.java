package link_state_router;

public class Edge
{
  Integer id;
  Integer cost;
  Integer direct_link;
  
  /*---------------------------------------------------------*/
  /* Function Name: Edge                                     */
  /*                                                         */
  /* Description: Prototype for Edge class                   */
  /*                                                         */
  /*---------------------------------------------------------*/
  Edge(Integer id, Integer cost, Integer direct_link)
  {
    this.id = id;
    this.cost = cost;
    this.direct_link = direct_link;
  }  // End prototype Edge
}  // End Class Edge
