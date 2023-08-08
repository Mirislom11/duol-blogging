package uz.duol.blogging.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import uz.duol.blogging.core.AbstractMockMvcBase;
import uz.duol.blogging.core.TestUtil;
import uz.duol.blogging.dao.repository.ThemeRepository;
import uz.duol.blogging.domain.ThemeEntity;
import uz.duol.blogging.domain.enumeration.Category;
import uz.duol.blogging.dto.ThemeDTO;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ThemeControllerTestIT extends AbstractMockMvcBase {
  private static final String BASE_URL = "/api/v1/themes";
  private static ThemeEntity ENTITY;

  @Autowired private ThemeRepository repository;

  public static void createEntity() {
    ENTITY = ThemeEntity.builder().name("AAA").category(Category.SCIENCE).build();
  }

  @BeforeEach
  public void set() {
    createEntity();
  }

  @Test
  public void create() throws Exception {
    ThemeDTO dto = ENTITY.map2DTO();
    this.mockMvc
        .perform(
            MockMvcRequestBuilders.post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(dto)))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.name()));
  }

  @Test
  @Transactional
  public void findById() throws Exception {
    ThemeEntity theme = repository.save(ENTITY);

    this.mockMvc
        .perform(
            MockMvcRequestBuilders.get(BASE_URL + "/" + theme.getId())
                .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
        .andExpect(jsonPath("$.data.id").value(theme.getId()));
  }

  @Test
  @Transactional
  public void findAll() throws Exception {
    repository.save(ENTITY);
    this.mockMvc
        .perform(MockMvcRequestBuilders.get(BASE_URL).accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()));
  }

  @Test
  @Transactional
  public void update() throws Exception {
    ThemeEntity entity = repository.save(ENTITY);
    this.mockMvc
        .perform(
            MockMvcRequestBuilders.put(BASE_URL + "/" + entity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(entity.map2DTO())))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()));
  }


}
