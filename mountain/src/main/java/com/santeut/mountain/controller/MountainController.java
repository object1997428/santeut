package com.santeut.mountain.controller;

import com.santeut.mountain.common.response.BasicResponse;
import com.santeut.mountain.common.util.ResponseUtil;
import com.santeut.mountain.service.MountainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MountainController {

  private final MountainService mountainService;

  @GetMapping("/")
  public ResponseEntity<BasicResponse> searchMountainByName(
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "region", required = false) String region) {
    return ResponseUtil.buildBasicResponse(HttpStatus.OK,
        mountainService.findByNameAndRegion(name, region));
  }

  @GetMapping("/popular")
  public ResponseEntity<BasicResponse> getPopularMountain() {
    return ResponseUtil.buildBasicResponse(HttpStatus.OK, mountainService.getMountainByViews());
  }

  @GetMapping("/{mountainId}")
  public ResponseEntity<BasicResponse> getMountainById(
      @PathVariable("mountainId") int mountainId) {
    return ResponseUtil.buildBasicResponse(HttpStatus.OK,
        mountainService.findMountainById(mountainId));
  }

  @GetMapping("/v2/{mountainId}")
  public ResponseEntity<BasicResponse> getMountainDetailById(
      @PathVariable("mountainId") int mountainId
  ) {
    return ResponseUtil.buildBasicResponse(HttpStatus.OK,
        mountainService.getMountainInfoById(mountainId));
  }

}
