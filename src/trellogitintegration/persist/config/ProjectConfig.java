package trellogitintegration.persist.config;

/**
 * Data structure to save Project Configuration, there should only be one 
 * ProjectConfig object per project.
 * 
 * The list is expandable, but be sure to only store member variables that 
 * are in primitive types: int, long, float, double, String, List in order for 
 * the object to be serialized properly into JSON.
 * 
 * For each member variable / class you add to this class, be sure to change 
 * the equals(Object) and hashCode() functions to properly reflect the change 
 * 
 * Example serialized JSON object:
 * {
 *  "trelloConfig":
 *    {
 *     "url":"abc.com",
 *     "board":"testing board",
 *     "key":"23456789!@#$%^&*()",
 *     "token":"@#$%^&*0987654SWERTYUJI"
 *    }
 * 
 * }
 * Created: Oct 20, 2016
 * @author Man Chon Kuok
 */
public class ProjectConfig {
  
  private TrelloConfig trelloConfig  = new TrelloConfig();
  private GitConfig gitConfig = new GitConfig();
    
  public class GitConfig {
    private String token = "";
    private String username = "";
    private String repo = "";
    
    public GitConfig(){}
    
    public GitConfig(String token, String username, String repo) {
      this.token = token;
      this.username = username;
      this.repo = repo;
    }

    public String getToken() {
      return token;
    }

    public void setToken(String token) {
      this.token = token;
    }

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public String getRepo() {
      return repo;
    }

    public void setRepo(String repo) {
      this.repo = repo;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + getOuterType().hashCode();
      result = prime * result + ((repo == null) ? 0 : repo.hashCode());
      result = prime * result + ((token == null) ? 0 : token.hashCode());
      result = prime * result + ((username == null) ? 0 : username.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      GitConfig other = (GitConfig) obj;
      if (!getOuterType().equals(other.getOuterType()))
        return false;
      if (repo == null) {
        if (other.repo != null)
          return false;
      } else if (!repo.equals(other.repo))
        return false;
      if (token == null) {
        if (other.token != null)
          return false;
      } else if (!token.equals(other.token))
        return false;
      if (username == null) {
        if (other.username != null)
          return false;
      } else if (!username.equals(other.username))
        return false;
      return true;
    }

    private ProjectConfig getOuterType() {
      return ProjectConfig.this;
    }
    
    
    
  }
  
  
  public class TrelloConfig {
    private String url = "";
    private String board = "";
    private String key = "";
    private String token = "";
  
    public TrelloConfig() {}
    
    public TrelloConfig(String url,
                        String board,
                        String key,
                        String token) {
      this.url = url;
      this.board = board;
      this.key = key;
      this.token = token;
    }    
    
    public String getUrl() {
      return this.url;
    }
    public void setUrl(String url) {
      this.url = url;
    }
    public String getBoard() {
      return this.board;
    }
    public void setBoard(String board) {
      this.board = board;
    }
    public String getKey() {
      return this.key;
    }
    public void setKey(String key) {
      this.key = key;
    }
    public String getToken() {
      return this.token;
    }
    public void setToken(String token) {
      this.token = token;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((board == null) ? 0 : board.hashCode());
      result = prime * result + ((key == null) ? 0 : key.hashCode());
      result = prime * result + ((token == null) ? 0 : token.hashCode());
      result = prime * result + ((url == null) ? 0 : url.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      TrelloConfig other = (TrelloConfig) obj;
      if (board == null) {
        if (other.board != null)
          return false;
      } else if (!board.equals(other.board))
        return false;
      if (key == null) {
        if (other.key != null)
          return false;
      } else if (!key.equals(other.key))
        return false;
      if (token == null) {
        if (other.token != null)
          return false;
      } else if (!token.equals(other.token))
        return false;
      if (url == null) {
        if (other.url != null)
          return false;
      } else if (!url.equals(other.url))
        return false;
      return true;
    }     
  }
  
  public TrelloConfig getTrelloConfig() {
    return this.trelloConfig;
  }
  
  public void setTrelloConfig(TrelloConfig trelloConfig) {
    this.trelloConfig = trelloConfig;
  }

  public GitConfig getGitConfig() {
    return gitConfig;
  }

  public void setGitConfig(GitConfig gitConfig) {
    this.gitConfig = gitConfig;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((gitConfig == null) ? 0 : gitConfig.hashCode());
    result = prime * result
        + ((trelloConfig == null) ? 0 : trelloConfig.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ProjectConfig other = (ProjectConfig) obj;
    if (gitConfig == null) {
      if (other.gitConfig != null)
        return false;
    } else if (!gitConfig.equals(other.gitConfig))
      return false;
    if (trelloConfig == null) {
      if (other.trelloConfig != null)
        return false;
    } else if (!trelloConfig.equals(other.trelloConfig))
      return false;
    return true;
  }

  

  
  
}
  