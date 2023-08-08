package uz.duol.blogging.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.duol.blogging.dto.BlogDTO;
import uz.duol.blogging.dto.filter.BlogFilter;
import uz.duol.blogging.exchange.ApiResponse;
import uz.duol.blogging.service.BlogService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/blogs")
@Log4j2
@RequiredArgsConstructor
public class BlogController {
  private final BlogService service;

  @PostMapping
  public ResponseEntity<ApiResponse<BlogDTO>> create(
      @RequestBody @Valid BlogDTO.BlogCreateDTO dto, HttpServletRequest request)
      throws URISyntaxException {
    log.debug("REST request to create blog dto: {}", dto);
    ApiResponse<BlogDTO> response = service.create(dto);

    String baseURL = request.getRequestURI();
    return ResponseEntity.created(new URI(baseURL + "/" + response.getData().getId()))
        .body(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<BlogDTO>> findById(@PathVariable("id") Long id) {
    log.debug("REST request to find blog by id: {}", id);
    ApiResponse<BlogDTO> response = service.findById(id);
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<ApiResponse<Page<BlogDTO>>> findAll(BlogFilter filter, Pageable pageable) {
    log.debug("REST request to find all blogs");
    ApiResponse<Page<BlogDTO>> response = service.findAll(filter, pageable);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}/comments")
  public ResponseEntity<ApiResponse<BlogDTO.BlogWithCommentsDTO>> findBlogWithComments(
      @PathVariable("id") Long id) {
    ApiResponse<BlogDTO.BlogWithCommentsDTO> response = service.findBlogWithComments(id);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<BlogDTO>> update (@PathVariable("id") Long id,
                                                      @RequestBody BlogDTO.BlogCreateDTO dto) {
    log.debug("REST request to update blog by id: {}, dto: {}", id, dto);
    ApiResponse<BlogDTO> response = service.update(dto, id);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{id}/verified")
  public ResponseEntity<ApiResponse<BlogDTO>> update (@PathVariable("id") Long id,
                                                      @RequestBody BlogDTO.BlogVerifiedDTO dto) {
    log.debug("REST request to update blog id: {}, dto: {}", id, dto);
    ApiResponse<BlogDTO> response = service.updateVerified(id, dto);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> remove (@PathVariable("id") Long id) {
    log.debug("REST request to remove blog by id: {}", id);
    service.remove(id);
    return ResponseEntity.noContent().build();
  }
}
