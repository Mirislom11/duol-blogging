package uz.duol.blogging.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.duol.blogging.dto.CommentDTO;
import uz.duol.blogging.exchange.ApiResponse;
import uz.duol.blogging.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Log4j2
public class CommentController {
  private final CommentService service;

  @PostMapping
  public ResponseEntity<ApiResponse<CommentDTO>> create(
      @RequestBody @Valid CommentDTO.CommentCreateDTO dto, HttpServletRequest request)
      throws URISyntaxException {
    log.debug("REST request to create comment dto: {}", dto);
    ApiResponse<CommentDTO> response = service.create(dto);

    String baseURL = request.getRequestURI();
    return ResponseEntity.created(new URI(baseURL + "/" + response.getData().getId()))
        .body(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<CommentDTO>> findById(@PathVariable("id") Long id) {
    log.debug("REST request to find comment by id: {}", id);
    ApiResponse<CommentDTO> response = service.findById(id);
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<CommentDTO>>> findAll() {
    log.debug("REST request to find all comments");
    ApiResponse<List<CommentDTO>> comments = service.findAll();
    return ResponseEntity.ok(comments);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<CommentDTO>> update(
      @PathVariable("id") Long id, @RequestBody @Valid CommentDTO.CommentUpdateDTO dto) {
    log.debug("REST request to update comment by id: {} , dto : {}", id, dto);
    ApiResponse<CommentDTO> response = service.update(dto, id);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{id}/verified")
  public ResponseEntity<ApiResponse<CommentDTO>> verified(
      @PathVariable("id") Long id, @RequestBody @Valid CommentDTO.CommentVerifiedUpdateDTO dto) {
    log.debug("REST request to update comment by id: {} dto: {}", id, dto);
    ApiResponse<CommentDTO> response = service.updateVerified(dto, id);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> remove(@PathVariable("id") Long id) {
    log.debug("REST request remove comment by id: {}", id);
    service.remove(id);
    return ResponseEntity.noContent().build();
  }
}
