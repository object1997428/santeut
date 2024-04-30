package com.santeut.mountain.controller;

import com.santeut.mountain.common.response.BasicResponse;
import com.santeut.mountain.common.util.ResponseUtil;
import com.santeut.mountain.service.MountainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mountain")
@RequiredArgsConstructor
public class MountainController {

  private final MountainService mountainService;

  @GetMapping("")
  public ResponseEntity<BasicResponse> searchMountain(@RequestParam("name") String name)  {
    return ResponseUtil.buildBasicResponse(HttpStatus.OK, mountainService.findByName(name));
  }

}
