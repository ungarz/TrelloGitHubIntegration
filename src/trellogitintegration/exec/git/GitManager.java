package trellogitintegration.exec.git;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import trellogitintegration.exec.CmdExecutionResult;
import trellogitintegration.exec.CmdExecutor;
import trellogitintegration.exec.CommandUnrecognizedException;
import trellogitintegration.exec.OperationResult;
import trellogitintegration.exec.git.GitConfigException.GitExceptionType;
import trellogitintegration.exec.git.github.pullrequest.PullRequest;
import trellogitintegration.exec.git.github.pullrequest.PullRequestResultMsg;
import trellogitintegration.exec.git.github.GitHubApiCaller;
import trellogitintegration.persist.IOUtils;
import trellogitintegration.persist.config.ProjectConfig.GitConfig;

public class GitManager {

  private File workingDir;
  private final GitConfig gitHubInfo;  
  
  public GitManager(File workingDir, GitConfig gitHubInfo) throws Exception {
    this.workingDir = workingDir;
    gitInstalledOrThrowException();
    this.gitHubInfo = gitHubInfo;
    
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

  public OperationResult<String> init() throws Exception {
    return this.getResult(GitOperation.INIT);
  }

  public OperationResult<String> add(String file) throws Exception {
    return this.getResult(GitOperation.ADD, file);
  }

  public OperationResult<String> addAll() throws Exception {
    return this.add(".");
  }

  public OperationResult<String> commit(String message) throws Exception {
    return this.getResult(GitOperation.COMMIT, message);
  }

  public OperationResult<String> push(String branch) throws Exception {
    return this.getResult(GitOperation.PUSH, branch);
  }

  public OperationResult<String> pull(String branch) throws Exception {
    return this.getResult(GitOperation.PULL, branch);
  }

  public OperationResult<String> newBranch(String branch) throws Exception {
    return this.getResult(GitOperation.NEW_BRANCH, branch);
  }

  public OperationResult<String> checkOutBranch(String branchName) throws Exception {
    OperationResult<String> result = this.getResult(GitOperation.CHECKOUT_BRANCH, branchName); 
    
    if (result.isSuccessful() || result.getDisplayableMessage().contains("Please, commit your changes or stash them")) {
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

  public OperationResult<String> status() throws Exception {
    return this.getResult(GitOperation.STATUS);
  }

  public OperationResult<String> log() throws Exception {
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
        return branches[i].substring(2);
      }
    }
    return null;
  }

  public OperationResult<String> clone(String target) throws Exception {
    List<String> before = Arrays.asList(this.workingDir.list());
    OperationResult<String> result = this.getResult(GitOperation.CLONE, target);
    
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

  public OperationResult<PullRequestResultMsg> createPullRequest(PullRequest pullRequest) throws IOException {
    return GitHubApiCaller.createPullRequest(pullRequest, gitHubInfo);
  }
  
  public OperationResult<String> execute(GitOperation operation, String argument) throws Exception {
    switch(operation) {
    case INIT:
      return this.init();
    case PUSH:
      return this.push(argument);
    case PULL:
      return this.pull(argument);
    case ADD:
      return this.add(argument);
    case ADD_ALL:
      return this.addAll();
    case COMMIT:
      return this.commit(argument);
    case NEW_BRANCH:
      return this.newBranch(argument);
    case CHECKOUT_BRANCH:
      return this.checkOutBranch(argument);
    case STATUS:
      return this.status();
    case LOG:
      return this.log();
    case CLONE:
      return this.clone(argument);
    case BRANCH:
      String[] branches = this.getAllbranches();
      StringBuilder stringBuilder = new StringBuilder();
      Arrays.stream(branches).forEach(branch -> stringBuilder.append(branch).append("\n"));
      GitValidatedResult result = new GitValidatedResult(GitOperation.BRANCH, stringBuilder.toString());
      return result;
    default:
      return new CmdExecutionResult("Success", null);
    }
    
  }
    
  public File getWorkingDirectory() throws IOException {
    return this.workingDir;
  }
  
  private OperationResult<String> getResult(GitOperation operation) throws Exception {
    return this.getResult(operation, "");
  }
  
  private OperationResult<String> getResult(GitOperation operation, String argument) throws Exception {
    CmdExecutionResult result = CmdExecutor
        .sequentialExecute(String.format(operation.getCommand(), argument), this.workingDir);
    return new GitValidatedResult(operation, result.getOutput());
  }
  

  private boolean runCommand(GitOperation operation, String argument)
      throws Exception {
    CmdExecutionResult result = CmdExecutor.sequentialExecute(
        String.format(operation.getCommand(), argument), this.workingDir);

    if (result.getException() == null) {
      return GitOperationValidator.validateOperation(operation,
          result.getOutput());
    } else {
      throw result.getException();
    }
  }

  
  private String[] getBranchList() throws Exception {
    String branchList = this.getResult(GitOperation.BRANCH).getDisplayableMessage();
    String[] branches = branchList.split("\n");
    return branches;
  }

}
