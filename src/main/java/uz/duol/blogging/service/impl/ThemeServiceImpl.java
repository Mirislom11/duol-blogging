package uz.duol.blogging.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.duol.blogging.constants.ResponseMessageConstant;
import uz.duol.blogging.dao.repository.ThemeRepository;
import uz.duol.blogging.domain.ThemeEntity;
import uz.duol.blogging.dto.ThemeDTO;
import uz.duol.blogging.exception.BadRequestException;
import uz.duol.blogging.exception.ResourceNotFoundException;
import uz.duol.blogging.exchange.ApiResponse;
import uz.duol.blogging.service.ThemeService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ThemeServiceImpl implements ThemeService {
  private final ThemeRepository repository;
  private static final String ENTITY_NAME = "Theme";

  @Transactional
  @Override
  public ApiResponse<ThemeDTO> create(ThemeDTO dto) {
    log.debug("Request to create theme dto: {}", dto);
    ThemeEntity theme = dto.map2Entity();
    theme.setCreatedDate(LocalDateTime.now());
    theme.setLastUpdatedDate(LocalDateTime.now());
    repository.save(theme);

    ApiResponse<ThemeDTO> response = new ApiResponse<>();
    response.setData(theme.map2DTO());
    response.setStatus(HttpStatus.CREATED.name());
    response.setMessage(String.format(ResponseMessageConstant.ENTITY_CREATED, ENTITY_NAME, theme.getId()));
    return response;
  }

  @Transactional(readOnly = true)
  @Override
  public ApiResponse<ThemeDTO> findById(Long id) {
    log.debug("Request to findBy Id : {}", id);
    ThemeDTO theme =
        repository
            .findById(id)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        String.format(ResponseMessageConstant.ENTITY_NOT_FOUND, ENTITY_NAME, id)))
            .map2DTO();

    ApiResponse<ThemeDTO> response = new ApiResponse<>();
    response.setData(theme);
    response.setMessage(String.format(ResponseMessageConstant.ENTITY_FOUND, ENTITY_NAME, theme.getId()));
    response.setStatus(HttpStatus.OK.name());
    return response;
  }

  @Transactional(readOnly = true)
  @Override
  public ApiResponse<List<ThemeDTO>> findAll() {
    log.debug("Request to find all themes");
    List<ThemeDTO> themes =
        repository.findAll().stream().map(ThemeEntity::map2DTO).collect(Collectors.toList());

    ApiResponse<List<ThemeDTO>> response = new ApiResponse<>();
    response.setStatus(HttpStatus.OK.name());
    response.setMessage(ResponseMessageConstant.ENTITIES_FOUND);
    response.setData(themes);
    return response;
  }

  @Transactional
  @Override
  public ApiResponse<ThemeDTO> update(Long id, ThemeDTO dto) {
    log.debug("Request to update theme: {}, by id: {}", dto, id);
    if (Objects.isNull(dto.getId())) {
      throw new BadRequestException("dto id null");
    }
    if (!Objects.equals(id, dto.getId())) {
      throw new BadRequestException("id invalid");
    }
    ThemeEntity current =
        repository
            .findById(id)
            .orElseThrow(
                () -> new ResourceNotFoundException(String.format("theme by id %d not found", id)));

    current.setName(dto.getName());
    current.setCategory(dto.getCategory());
    current.setLastUpdatedDate(LocalDateTime.now());
    repository.save(current);

    ApiResponse<ThemeDTO> response = new ApiResponse<>();
    response.setData(current.map2DTO());
    response.setMessage(
        String.format(ResponseMessageConstant.ENTITY_UPDATED, ENTITY_NAME, current.getId()));
    response.setStatus(HttpStatus.OK.name());
    return response;
  }
}
