package base;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.*;
import org.json.simple.JSONObject;
import parse.JhistFileParser;
import util.FactorStatistics;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/**
 * Created by zhangyun on 4/29/15.
 */
public class MapTask extends Task {
  /**The MapTask processing split, input file paths are split file paths*/
  private List<String> splitFiles;
  private String inputFormat;

  public MapTask( JhistFileParser.TaskInfo taskInfo, Path taskLogPath,
                 LinkedHashMap<String, Node> operators ,List<String> splitFiles,
                 String inputFormat, long startProcTime, long doneProcTime) {
    super(taskLogPath, taskInfo, operators, startProcTime, doneProcTime);
    this.splitFiles = new ArrayList<String>();
    this.splitFiles.addAll(splitFiles);
    this.inputFormat = inputFormat;
  }

  public void printAll(){
    System.out.println("TASK_ID: " + taskID);
    System.out.println(taskInfo.getTaskType());
    System.out.println("INPUT FILES:");
    System.out.println(splitFiles);
    System.out.println("INPUT FORMAT: " + inputFormat);
    System.out.println("OPTREE: ");
    for (String k : operators.keySet()){
      System.out.println(k + " => " + operators.get(k).toString());
    }
  }


  public String toJSON() throws IOException {
    JSONObject jobJson = new JSONObject();

    setJSONObject(jobJson);
    jobJson.put("splitFiles", splitFiles.toString());
    jobJson.put("inputFormat", inputFormat.toString());

    LinkedHashMap mapcounter = new LinkedHashMap();
    FactorStatistics.mapCounter(mapcounter, taskInfo.getCounters());
    jobJson.put("Counter",mapcounter);

    StringWriter out = new StringWriter();
    jobJson.writeJSONString(out);

    return jobJson.toJSONString();
  }

  public List<String> getSplitFiles() {
    return splitFiles;
  }

  public String getInputFormat() {
    return inputFormat;
  }
}
