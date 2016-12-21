package link_state_router;

import java.util.HashMap;
import java.util.Map;

public class Packet
{
  Integer id;
  Integer sequence;
  Integer time_to_live;
  Map<Integer, Pair> link_cost;
  
  /*---------------------------------------------------------*/
  /* Function Name: Packet                                   */
  /*                                                         */
  /* Description: Prototype for Packet class                 */
  /*                                                         */
  /*---------------------------------------------------------*/
  public Packet()
  {
    this.time_to_live = 10;
    this.link_cost    = new HashMap<Integer, Pair>();
  }  // End function Packet
  
  /*---------------------------------------------------------*/
  /* Function Name: Packet                                   */
  /*                                                         */
  /* Description: Prototype for Packet class that takes in   */
  /*              ID and sequence number.                    */
  /*                                                         */
  /*---------------------------------------------------------*/
  public Packet(Integer id, Integer sequence)
  {
    this.id           = id;
    this.sequence     = sequence;
    this.time_to_live = 10;
    this.link_cost    = new HashMap<Integer, Pair>();
  }  // End function Packet
  
  /*---------------------------------------------------------*/
  /* Function Name: Packet                                   */
  /*                                                         */
  /* Description: Prototype for Packet class that takes in   */
  /*              ID and sequence number and current link    */
  /*              cost.                                      */
  /*                                                         */
  /*---------------------------------------------------------*/
  public Packet(Integer id, Integer sequence, Map<Integer, Pair> current)
  {
    this.id           = id;
    this.sequence     = sequence;
    this.time_to_live = 10;
    this.link_cost    = current;
  }  // End function Packet
}  // End Class Packet
