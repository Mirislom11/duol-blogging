package uz.duol.blogging.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.duol.blogging.constants.ResponseMessageConstant;
import uz.duol.blogging.dao.repository.BlogRepository;
import uz.duol.blogging.dao.repository.ThemeRepository;
import uz.duol.blogging.domain.BlogEntity;
import uz.duol.blogging.domain.ThemeEntity;
import uz.duol.blogging.domain.enumeration.BlogStatus;
import uz.duol.blogging.dto.BlogDTO;
import uz.duol.blogging.dto.filter.BlogFilter;
import uz.duol.blogging.exception.BadRequestException;
import uz.duol.blogging.exception.ResourceNotFoundException;
import uz.duol.blogging.exchange.ApiResponse;
import uz.duol.blogging.service.BlogService;

import javax.persistence.Entity;
import javax.persistence.criteria.Join;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Log4j2
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {
  private final BlogRepository repository;
  private final ThemeRepository themeRepository;
  private static final String ENTITY_NAME = "Blog";
  private static final String ENTITY_THEME = "Theme";

  @Transactional
  @Override
  public ApiResponse<BlogDTO> create(BlogDTO.BlogCreateDTO dto) {
    log.debug("Request to create blog dto: {}", dto);
    BlogEntity blog = dto.map2Entity();
    ThemeEntity theme = null;
    if (Objects.nonNull(dto.getTheme().getId())) {
      theme =
          themeRepository
              .findById(dto.getTheme().getId())
              .orElseThrow(
                  () ->
                      new ResourceNotFoundException(
                          String.format("Theme by id: %d not found", dto.getTheme().getId())));
    } else {
      theme =
          ThemeEntity.builder()
              .name(dto.getTheme().getName())
              .category(dto.getTheme().getCategory())
              .build();
    }
    blog.setTheme(theme);
    blog.setLastUpdatedDate(LocalDateTime.now());
    blog.setCreatedDate(LocalDateTime.now());
    repository.save(blog);

    ApiResponse<BlogDTO> response = new ApiResponse<>();
    response.setData(blog.map2DTO());
    response.setMessage(
        String.format(ResponseMessageConstant.ENTITY_CREATED, ENTITY_NAME, blog.getId()));
    response.setStatus(HttpStatus.CREATED.name());
    return response;
  }

  @Override
  public ApiResponse<BlogDTO> findById(Long id) {
    log.debug("Request to find blog by id: {}", id);
    BlogDTO blog =
        repository
            .findById(id)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        String.format(ResponseMessageConstant.ENTITY_NOT_FOUND, ENTITY_NAME, id)))
            .map2DTO();

    ApiResponse<BlogDTO> response = new ApiResponse<>();
    response.setData(blog);
    response.setMessage(
        String.format(ResponseMessageConstant.ENTITY_FOUND, ENTITY_NAME, blog.getId()));
    response.setStatus(HttpStatus.OK.name());
    return response;
  }

  @Override
  public ApiResponse<Page<BlogDTO>> findAll(BlogFilter filter, Pageable pageable) {
    log.debug("Request to find all blogs filter: {}", filter);
    Specification<BlogEntity> specification =
        Specification.where(
            (root, query, criteriaBuilder) -> {
              return criteriaBuilder.equal(root.get("status"), BlogStatus.ACTIVE);
            });

    if (Objects.nonNull(filter.getThemeId())) {
      specification =
          specification.and(
              ((root, query, criteriaBuilder) -> {
                Join<ThemeEntity, BlogEntity> themeEntityBlogEntityJoin = root.join("theme");
                return criteriaBuilder.equal(
                    themeEntityBlogEntityJoin.get("id"), filter.getThemeId());
              }));
    }
    if (Objects.nonNull(filter.getVerified())) {
      specification =
          specification.and(
              (root, query, criteriaBuilder) -> {
                return criteriaBuilder.equal(root.get("verified"), filter.getVerified());
              });
    }
    if (Objects.nonNull(filter.getTitle())) {
      specification =
          specification.and(
              ((root, query, criteriaBuilder) -> {
                return criteriaBuilder.like(root.get("title"), "%" + filter.getTitle() + "%");
              }));
    }

    Page<BlogDTO> page = repository.findAll(specification, pageable).map(BlogEntity::map2DTO);
    ApiResponse<Page<BlogDTO>> response = new ApiResponse<>();
    response.setStatus(HttpStatus.OK.name());
    response.setMessage(
        String.format(ResponseMessageConstant.ENTITIES_FOUND, ENTITY_NAME.concat("s")));
    response.setData(page);
    return response;
  }

  @Override
  public ApiResponse<BlogDTO.BlogWithCommentsDTO> findBlogWithComments(Long blogId) {
    log.debug("Request to find Blog with comments");
    BlogDTO.BlogWithCommentsDTO blogsWithComments =
        repository
            .findBlogEntityWithComments(blogId)
            .map(BlogEntity::map2WithCommentDTO)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        String.format("Blog by id %d not found", blogId)));

    ApiResponse<BlogDTO.BlogWithCommentsDTO> response = new ApiResponse<>();
    response.setData(blogsWithComments);
    response.setMessage(
        String.format(
            ResponseMessageConstant.ENTITY_FOUND, ENTITY_NAME, blogsWithComments.getId()));
    response.setStatus(HttpStatus.OK.name());
    return response;
  }

  @Transactional
  @Override
  public ApiResponse<BlogDTO> update(BlogDTO.BlogCreateDTO dto, Long id) {
    if (Objects.isNull(dto.getId())) {
      throw new BadRequestException("Id is null");
    }

    BlogEntity blog =
        repository
            .findById(dto.getId())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        String.format(
                            ResponseMessageConstant.ENTITY_NOT_FOUND, ENTITY_NAME, dto.getId())));

    ThemeEntity theme = null;
    if (Objects.nonNull(dto.getTheme().getId())) {
      theme =
          themeRepository
              .findById(dto.getTheme().getId())
              .orElseThrow(
                  () ->
                      new ResourceNotFoundException(
                          String.format(
                              ResponseMessageConstant.ENTITY_FOUND,
                              ENTITY_THEME,
                              dto.getTheme().getId())));
    } else {
      theme =
          ThemeEntity.builder()
              .name(dto.getTheme().getName())
              .category(dto.getTheme().getCategory())
              .build();
    }

    blog.setTheme(theme);
    blog.setText(dto.getText());
    blog.setTitle(dto.getTitle());
    blog.setLastUpdatedDate(LocalDateTime.now());
    repository.save(blog);

    ApiResponse<BlogDTO> response = new ApiResponse<>();
    response.setData(blog.map2DTO());
    response.setMessage(
        String.format(ResponseMessageConstant.ENTITY_UPDATED, ENTITY_NAME, blog.getId()));
    response.setStatus(HttpStatus.OK.name());

    return response;
  }

  @Override
  public ApiResponse<BlogDTO> updateVerified(Long id, BlogDTO.BlogVerifiedDTO dto) {
    if (Objects.isNull(dto.getId())) {
      throw new BadRequestException("Id is null");
    }
    log.debug("Request tp verified blog by id: {}", dto.getId());

    BlogEntity blog =
        repository
            .findById(dto.getId())
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        String.format(
                            ResponseMessageConstant.ENTITY_NOT_FOUND, ENTITY_NAME, dto.getId())));

    blog.setVerified(dto.getVerified());
    blog.setLastUpdatedDate(LocalDateTime.now());
    repository.save(blog);

    ApiResponse<BlogDTO> response = new ApiResponse<>();
    response.setData(blog.map2DTO());
    response.setMessage(
        String.format(
            ResponseMessageConstant.ENTITY_VERIFIED,
            ENTITY_NAME,
            blog.getId(),
            blog.getVerified()));
    response.setStatus(HttpStatus.OK.name());
    return response;
  }

  @Override
  public void updateBlogsNumberViews(List<Long> blogIds) {
    log.debug("Request to update blogs with ids: {} numberViews", blogIds);
    repository.updateNumberViews(blogIds);
  }

  @Transactional
  @Override
  public void remove(Long id) {
    BlogEntity blog =
        repository
            .findById(id)
            .orElseThrow(
                () -> new ResourceNotFoundException(String.format("Blog by id %d not found", id)));
    blog.setStatus(BlogStatus.REMOVED);
    blog.setLastUpdatedDate(LocalDateTime.now());
    repository.save(blog);
  }
}
