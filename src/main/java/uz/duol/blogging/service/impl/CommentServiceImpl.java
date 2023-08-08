package uz.duol.blogging.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.duol.blogging.constants.ResponseMessageConstant;
import uz.duol.blogging.dao.repository.BlogRepository;
import uz.duol.blogging.dao.repository.CommentRepository;
import uz.duol.blogging.domain.BlogEntity;
import uz.duol.blogging.domain.CommentEntity;
import uz.duol.blogging.domain.enumeration.CommentStatus;
import uz.duol.blogging.dto.CommentDTO;
import uz.duol.blogging.exception.BadRequestException;
import uz.duol.blogging.exception.ResourceNotFoundException;
import uz.duol.blogging.exchange.ApiResponse;
import uz.duol.blogging.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class CommentServiceImpl implements CommentService {
  private final CommentRepository repository;
  private final BlogRepository blogRepository;
  private static final String ENTITY_NAME = "Comment";
  private static final String ENTITY_BLOG = "Blog";

  @Transactional
  @Override
  public ApiResponse<CommentDTO> create(CommentDTO.CommentCreateDTO dto) {
    log.debug("Request to create comment dto: {}", dto);
    CommentEntity commentEntity = dto.map2Entity();
    BlogEntity blog =
        blogRepository
            .findById(dto.getBlogId())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        String.format(
                            ResponseMessageConstant.ENTITY_FOUND, ENTITY_BLOG, dto.getBlogId())));

    commentEntity.setBlog(blog);
    commentEntity.setLastUpdatedDate(LocalDateTime.now());
    commentEntity.setCreatedDate(LocalDateTime.now());
    repository.save(commentEntity);

    ApiResponse<CommentDTO> response = new ApiResponse<>();
    response.setData(commentEntity.map2DTO());
    response.setMessage(
        String.format(ResponseMessageConstant.ENTITY_CREATED, ENTITY_NAME, commentEntity.getId()));
    response.setStatus(HttpStatus.CREATED.name());
    return response;
  }

  @Transactional(readOnly = true)
  @Override
  public ApiResponse<CommentDTO> findById(Long id) {
    log.debug("Request to find comment by id: {}", id);
    CommentDTO comment =
        repository
            .findById(id)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        String.format(ResponseMessageConstant.ENTITY_NOT_FOUND, ENTITY_NAME, id)))
            .map2DTO();

    ApiResponse<CommentDTO> response = new ApiResponse<>();
    response.setStatus(HttpStatus.OK.name());
    response.setData(comment);
    response.setMessage(
        String.format(ResponseMessageConstant.ENTITY_FOUND, ENTITY_NAME, comment.getId()));
    return response;
  }

  @Transactional(readOnly = true)
  @Override
  public ApiResponse<List<CommentDTO>> findAll() {
    log.debug("Request to find all comments");
    List<CommentDTO> comments =
        repository.findAll().stream()
            .filter(commentEntity -> commentEntity.getStatus().equals(CommentStatus.ACTIVE))
            .map(CommentEntity::map2DTO)
            .collect(Collectors.toList());

    ApiResponse<List<CommentDTO>> response = new ApiResponse<>();
    response.setData(comments);
    response.setMessage(
        String.format(ResponseMessageConstant.ENTITIES_FOUND, ENTITY_NAME.concat("s")));
    response.setStatus(HttpStatus.OK.name());
    return response;
  }

  @Transactional
  @Override
  public ApiResponse<CommentDTO> update(CommentDTO.CommentUpdateDTO dto, Long id) {
    log.debug("Request to update comment id : {}, dto: {}", id, dto);
    if (!Objects.equals(dto.getId(), id)) {
      throw new BadRequestException("id invalid");
    }

    CommentEntity current =
        repository
            .findById(id)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        String.format(ResponseMessageConstant.ENTITY_NOT_FOUND, ENTITY_NAME, id)));

    current.setContent(dto.getContent());

    if (Objects.nonNull(dto.getIsUsability()) && dto.getIsUsability()) {
      current.setUsability(current.getUsability() + 1);
    } else if (Objects.nonNull(dto.getIsUsability()) && !dto.getIsUsability()) {
      current.setUsability(current.getUsability() - 1);
    }
    current.setLastUpdatedDate(LocalDateTime.now());
    repository.save(current);

    ApiResponse<CommentDTO> response = new ApiResponse<>();
    response.setData(current.map2DTO());
    response.setMessage(
        String.format(ResponseMessageConstant.ENTITY_UPDATED, ENTITY_NAME, current.getId()));
    response.setStatus(HttpStatus.OK.name());
    return response;
  }

  @Transactional
  @Override
  public ApiResponse<CommentDTO> updateVerified(CommentDTO.CommentVerifiedUpdateDTO dto, Long id) {
    log.debug("Request to verified comment by id: {}", id);

    if (Objects.equals(id, dto.getId())) {
      throw new BadRequestException("invalid id");
    }
    CommentEntity comment =
        repository
            .findById(id)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        String.format(ResponseMessageConstant.ENTITY_NOT_FOUND, ENTITY_NAME, id)));

    comment.setVerified(dto.getVerified());
    comment.setLastUpdatedDate(LocalDateTime.now());
    repository.save(comment);

    ApiResponse<CommentDTO> response = new ApiResponse<>();
    response.setData(comment.map2DTO());
    response.setMessage(
        String.format(
            ResponseMessageConstant.ENTITY_VERIFIED,
            ENTITY_NAME,
            comment.getId(),
            comment.getVerified()));
    return response;
  }

  @Transactional
  @Override
  public void remove(Long id) {
    log.debug("Request to remove comment by id: {}", id);
    CommentEntity comment =
        repository
            .findById(id)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        String.format(ResponseMessageConstant.ENTITY_NOT_FOUND, ENTITY_NAME, id)));
    comment.setStatus(CommentStatus.REMOVED);
    comment.setLastUpdatedDate(LocalDateTime.now());
    repository.save(comment);
  }
}
