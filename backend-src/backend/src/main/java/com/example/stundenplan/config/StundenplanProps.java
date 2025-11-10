package com.example.stundenplan.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "stundenplan")
public class StundenplanProps {

  /**
   * Block no → Zeit Slot (exp. 1 -> "08:00-08:45")
   */
  private Map<Integer, String> blockmap = Map.of();

  public Map<Integer, String> getBlockmap() {
    return blockmap;
  }

  public void setBlockmap(Map<Integer, String> blockmap) {
    this.blockmap = blockmap;
  }
}

