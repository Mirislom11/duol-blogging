package uz.duol.blogging.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.duol.blogging.dto.ThemeDTO;
import uz.duol.blogging.exchange.ApiResponse;
import uz.duol.blogging.service.ThemeService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("api/v1/themes")
@Log4j2
@RequiredArgsConstructor
public class ThemeController {
  private final ThemeService service;

  @PostMapping
  public ResponseEntity<ApiResponse<ThemeDTO>> create(
      @RequestBody @Valid ThemeDTO dto, HttpServletRequest httpServletRequest)
      throws URISyntaxException {
    log.debug("REST request to create theme dto: {}", dto);
    ApiResponse<ThemeDTO> response = service.create(dto);

    String baseURL = httpServletRequest.getRequestURI();
    return ResponseEntity.created(new URI(baseURL + "/" + response.getData().getId()))
        .body(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<ThemeDTO>> findById(@PathVariable("id") Long id) {
    log.debug("REST request to find by id: {}", id);
    ApiResponse<ThemeDTO> response = service.findById(id);
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<ThemeDTO>>> findAll() {
    log.debug("REST request to find all themes");
    ApiResponse<List<ThemeDTO>> response = service.findAll();
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<ThemeDTO>> update(
      @PathVariable("id") Long id, @RequestBody @Valid ThemeDTO dto) {
    log.debug("REST request ot update theme by id: {} dto: {}", id, dto);
    ApiResponse<ThemeDTO> response = service.update(id, dto);
    return ResponseEntity.ok(response);
  }
}
