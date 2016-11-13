/**
 * 
 */
package trello;

import org.trello4j.Trello;
import org.trello4j.TrelloException;
import org.trello4j.TrelloImpl;
import org.trello4j.Trello;

/**
 * @author Leon Qu
 *
 * Class to authenticate user account and allow application to access the account
 */
public class AccountAuth {
  
  private String myToken = null;
  private String APIKey = "f7af5802a8798f22ee397600dde10430";
  private String username = "";

  public AccountAuth(String username, String APIKey, String myToken){
    this.username = username;
    this.APIKey = APIKey;
    this.myToken = myToken;
    
    Trello trello = new TrelloImpl(APIKey, myToken);
    trello.getMember(username, myToken);
    
    verification(trello);
  }
  

  public void verification(Trello trl){
    if(trl != null){
      System.out.print("Verification successful.");
    }
    else{
      System.out.print("Verificaiton failed.");
    }
  }
  //TrelloException UserExcep = new TrelloException(username + " has allowed access to Trello.");
  
}
