package profiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistics {
  private Map<String, List<Long>> durations = new HashMap<>();

  public void add(String method, long duration) {
    List<Long> times = durations.get(method);
    if (times == null) {
      times = new ArrayList<>();
      durations.put(method, times);
    }
    
    times.add(duration);
  }

  public List<Long> getExecutionTime() {
    return durations.values().iterator().next();
  }

  public String getResult() {
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<String, List<Long>> entry : durations.entrySet()) {
      if (sb.length() > 0) sb.append("\n");
      sb.append(entry.getKey()).append(" ").append(entry.getValue().get(0)).append(" ms");
    }
    return sb.toString();
  }
}
