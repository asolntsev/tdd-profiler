package profiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;

public class Statistics {
  private Map<String, List<Long>> durations = new HashMap<>();

  public void add(String method, long duration) {
    this.durations.computeIfAbsent(method, k -> new ArrayList<>()).add(duration);
  }

  public List<Long> getExecutionTime() {
    return durations.values().iterator().next();
  }

  public String getResult() {
    return durations.entrySet().stream().map(e ->
        e.getKey() + " " + e.getValue().get(0) + " ms"
    ).collect(joining("\n"));
  }
}
