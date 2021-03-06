/**
 * 
 */
package com.isencia.passerelle.project.repository.impl.filesystem;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import ptolemy.actor.CompositeActor;

import com.isencia.passerelle.core.IEventLog;
import com.isencia.passerelle.model.Flow;
import com.isencia.passerelle.model.FlowManager;
import com.isencia.passerelle.project.repository.api.MetaData;
import com.isencia.passerelle.project.repository.api.Project;
import com.isencia.passerelle.project.repository.api.RepositoryService;

/**
 * @author delerw
 * 
 */
public class FileSystemBasedRepositoryService implements RepositoryService {

  private File rootFolder;
  private File submodelFolder;

  /**
   * Instantiates a service instance with the default root folder (C:/temp/passerelle-repository)
   */

  public FileSystemBasedRepositoryService(String rootFolderPath, String submodelFolderPath) {
    this(new File(rootFolderPath), new File(submodelFolderPath));
  }

  /**
   * Instantiates a service instance with the given root folder
   * 
   * @param rootFolder
   */
  public FileSystemBasedRepositoryService(File rootFolder, File submodelFolder) {
    if (!rootFolder.exists()) {
      if (rootFolder.mkdirs()) {
        this.rootFolder = rootFolder;
      }
    } else {
      this.rootFolder = rootFolder;
    }
    if (!submodelFolder.exists()) {
      if (submodelFolder.mkdirs()) {
        this.submodelFolder = submodelFolder;
      }
    } else {
      this.submodelFolder = submodelFolder;
    }
  }

  /**
   * @return the submodelFolder
   */
  public File getSubmodelFolder() {
    return submodelFolder;
  }

  /**
   * @param submodelFolder
   *          the submodelFolder to set
   */
  public void setSubmodelFolder(File submodelFolder) {
    this.submodelFolder = submodelFolder;
  }

  public String[] getAllProjectCodes() {
    List<String> results = new ArrayList<String>();
    File[] files = rootFolder.listFiles((FileFilter) null);
    for (File file : files) {
      if (file.isDirectory()) {
        results.add(file.getName());
      }
    }
    return results.toArray(new String[results.size()]);
  }

  public Flow getFlow(String sequenceCode) {
    Flow result = null;
    File[] projectFolders = rootFolder.listFiles((FileFilter) null);
    for (File projectFolder : projectFolders) {
      if (projectFolder.isDirectory()) {
        Project p = getProject(projectFolder.getName());
        result = p.getFlow(sequenceCode);
        if (result != null) {
          break;
        }
      }
    }
    return result;
  }

  public Project getProject(String projectCode) {
    File projectFolder = new File(rootFolder, projectCode);
    Project result = null;
    if (projectFolder.exists() && projectFolder.isDirectory()) {
      result = new FileSystemBasedProject(rootFolder, projectCode);
    }
    return result;
  }

  public MetaData getFlowMetaData(String flowCode) {
    return new MetaData("Flow", null, null, flowCode, null, null);
  }

  public void commitFlow(Flow flow, String comment) throws Exception {
  }

  public String[] getAllSubmodels() {

    Set<String> submodels = new TreeSet<String>();
    try {
      Properties properties = getSubmodelProperties();
      for (Object key : properties.keySet()) {
        submodels.add(key.toString());
      }
    } catch (FileNotFoundException e) {

    } catch (IOException e) {

    }

    return submodels.toArray(new String[submodels.size()]);
  }

  private Properties getSubmodelProperties() throws IOException, FileNotFoundException {
    File props = getSubmodelPropertiesFile();
    Properties properties = loadProperties(new FileInputStream(props));
    return properties;
  }

  private File getSubmodelPropertiesFile() {
    File props = new File(submodelFolder, "submodels.properties");
    if (!props.exists()) {
      try {
        props.createNewFile();
      } catch (IOException e) {
      }
    }
    return props;
  }

  public void createSubmodel(CompositeActor flow) {
    Properties properties;
    FileOutputStream fos = null;
    try {
      properties = getSubmodelProperties();
      properties.put(flow.getName(), "");
      File file = getSubmodelPropertiesFile();
      properties.store(new FileOutputStream(file), "Submodel Properties. Please do not edit this file.");
      String exportMoML = flow.exportMoMLPlain();
      if (exportMoML != null) {
        File momlFile = new File(submodelFolder, flow.getName() + ".moml");
        if (!momlFile.exists()) {
          momlFile.createNewFile();
        }

        fos = new FileOutputStream(momlFile);
        fos.write(exportMoML.getBytes());

      }
    } catch (FileNotFoundException e) {

    } catch (IOException e) {

    } finally {
      if (fos != null) {
        try {
          fos.close();
        } catch (IOException e) {

        }
      }
    }

  }

  public void deleteSubmodel(String flow) {
    Properties properties;
    try {
      properties = getSubmodelProperties();
      properties.remove(flow);
      File file = getSubmodelPropertiesFile();
      properties.store(new FileOutputStream(file), "Submodel Properties. Please do not edit this file.");

    } catch (FileNotFoundException e) {

    } catch (IOException e) {

    }
  }

  public Flow getSubmodel(String submodelCode) {
    File flowFile = new File(submodelFolder, submodelCode + ".moml");
    Flow result = null;
    if (flowFile.exists() && flowFile.isFile()) {
      try {
        result = FlowManager.readMoml(flowFile.toURL());
        if (result.isClassDefinition()) {
          return result;
        }
      } catch (Exception e) {
      }
    }

    return result;

  }

  public Properties loadProperties(final InputStream stream) throws IOException {

    final Properties fileProps = new Properties();
    try {
      final BufferedInputStream in = new BufferedInputStream(stream);
      fileProps.load(in);
    } finally {
      stream.close();
    }
    return fileProps;
  }

  public List<IEventLog> getLogs(String name, Integer maxResult) {
    return new ArrayList<IEventLog>();
  }

  public boolean existNewSubModel(String flowCode) {
    return true;
  }

  public MetaData getSubmodelMetaData(String flowCode) {
    return new MetaData(flowCode, null);
  }

  public MetaData[] getAllSubmodelMetaData() {
    String[] codes = getAllSubmodels();
    if (codes == null) {
      return new MetaData[] {};
    }
    List<String> codeList = Arrays.asList(codes);
    List<MetaData> metaDatas = new ArrayList<MetaData>();
    for (String code : codeList) {
      MetaData submodelMetaData = getSubmodelMetaData(code);
      if (submodelMetaData != null) {
        metaDatas.add(submodelMetaData);
      }
    }
    return metaDatas.toArray(new MetaData[metaDatas.size()]);
  }
}
