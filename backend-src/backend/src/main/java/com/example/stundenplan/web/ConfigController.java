package com.example.stundenplan.web;

import org.springframework.web.bind.annotation.*;
import com.example.stundenplan.web.dto.BlockMapDto;
import com.example.stundenplan.config.StundenplanProps;

@RestController
@RequestMapping("/api/v1/config")
public class ConfigController {

  private final StundenplanProps props;

  public ConfigController(StundenplanProps props) {
    this.props = props;
  }

  @GetMapping("/blocks")
  public BlockMapDto blocks(){
    return new BlockMapDto(props.getBlockmap());
  }
}


