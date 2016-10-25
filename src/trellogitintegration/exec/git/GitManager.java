package trellogitintegration.exec.git;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import trellogitintegration.exec.CmdExecutionResult;
import trellogitintegration.exec.CmdExecutor;
import trellogitintegration.exec.CommandUnrecognizedException;
import trellogitintegration.exec.OperationResult;
import trellogitintegration.exec.git.GitConfigException.GitExceptionType;
import trellogitintegration.persist.IOUtils;
import trellogitintegration.rest.JsonStringConverter;
import trellogitintegration.rest.RestApiConnector;

public class GitManager {

  private File workingDir;
  private final Map<String, String> githubRequstHeader;
  
  public GitManager(File workingDir, String token) throws Exception {
    this.workingDir = workingDir;
    gitInstalledOrThrowException();
    this.githubRequstHeader = new HashMap<>();
    this.githubRequstHeader.put("Authorization", String.format("token %s", token));
  }

  private void gitInstalledOrThrowException() throws Exception {
    try {
        runCommand(GitOperation.VERSION, "");
    } catch (Exception e) {
      if (e instanceof CommandUnrecognizedException) {
        throw new GitConfigException(GitExceptionType.NOT_INSTALLED);
      } else {
        throw e;
      }
    }
  }

  public OperationResult init() throws Exception {
    return this.getResult(GitOperation.INIT);
  }

  public OperationResult add(String file) throws Exception {
    return this.getResult(GitOperation.ADD, file);
  }

  public OperationResult addAll() throws Exception {
    return this.add(".");
  }

  public OperationResult commit(String message) throws Exception {
    return this.getResult(GitOperation.COMMIT, message);
  }

  public OperationResult push(String branch) throws Exception {
    return this.getResult(GitOperation.PUSH, branch);
  }

  public OperationResult pull(String branch) throws Exception {
    return this.getResult(GitOperation.PULL, branch);
  }

  public OperationResult newBranch(String branch) throws Exception {
    return this.getResult(GitOperation.NEW_BRANCH, branch);
  }

  public OperationResult checkOutBranch(String branchName) throws Exception {
    OperationResult result = this.getResult(GitOperation.CHECKOUT_BRANCH, branchName); 
    
    if (result.isSuccessful()) {
      return result;
    } else {
      result = this.getResult(GitOperation.NEW_BRANCH, branchName);
      if (result.isSuccessful()) {
        return this.getResult(GitOperation.PULL, branchName);
      } else {
        return result;
      }
    }
  }

  public OperationResult status() throws Exception {
    return this.getResult(GitOperation.STATUS);
  }

  public OperationResult log() throws Exception {
    return this.getResult(GitOperation.LOG);
  }
  
  public String[] getAllbranches() throws Exception {
    String[] branches = getBranchList();
    for (int i = 0; i < branches.length; i++) {
      if (!branches[i].isEmpty()) {
        branches[i] = branches[i].substring(2);
      }
    }
    return branches;
  }
  
  public String getCurrentBranch() throws Exception {
    String[] branches = getBranchList();
    for (int i = 0; i < branches.length; i++) {
      if (branches[i].startsWith("*")) {
        return branches[i];
      }
    }
    return null;
  }

  public OperationResult clone(String target) throws Exception {
    List<String> before = Arrays.asList(this.workingDir.list());
    OperationResult result = this.getResult(GitOperation.CLONE, target);
    
    if (result.isSuccessful()) {
      List<String> after = Arrays.asList(this.workingDir.list());
      // after.removeAll(before); unsupported
      List<String> newFiles = IOUtils.getGeneratedFiles(before, after);
      if (!newFiles.isEmpty() && newFiles.size() == 1) {
        this.workingDir = new File(this.workingDir, newFiles.get(0));
      }
    }
    return result;
  }
  
 // public String 
  

  public File getWorkingDirectory() {
    return this.workingDir;
  }
  
  
  
  
  

  private OperationResult getResult(GitOperation operation) throws Exception {
    return this.getResult(operation, "");
  }
  
  private OperationResult getResult(GitOperation operation, String argument) throws Exception {
    CmdExecutionResult result = CmdExecutor
        .sequentialExecute(String.format(operation.getCommand(), argument), this.workingDir);
    return new GitValidatedResult(operation, result.getMessage());
  }
  

  private boolean runCommand(GitOperation operation, String argument)
      throws Exception {
    CmdExecutionResult result = CmdExecutor.sequentialExecute(
        String.format(operation.getCommand(), argument), this.workingDir);

    if (result.getException() == null) {
      return GitOperationValidator.validateOperation(operation,
          result.getMessage());
    } else {
      throw result.getException();
    }
  }

  
  private String[] getBranchList() throws Exception {
    String branchList = this.getResult(GitOperation.BRANCH).getMessage();
    String[] branches = branchList.split("\n");
    return branches;
  }
  
}
