package trellogitintegration.eclipse.utils;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

/**
 * Class containing util methods for manipulating Eclipse specific objects
 * Created: Nov 3, 2016
 * @author Man Chon Kuok
 */
public class EclipseHelperUtils {
  
  /**
   * @return Returns a list of projects that are opened in eclipse
   */
  public static List<IProject> getOpenedProjects() {
    List<IProject> openedProjects = new LinkedList<>();
    
    IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
    IProject[] projects = workspaceRoot.getProjects();
    for(int i = 0; i < projects.length; i++) {
       IProject project = projects[i];
       if(project.isOpen() ) {
         openedProjects.add(project);
       }
    }
    
    return openedProjects;
  }

}
