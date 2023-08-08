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
import uz.duol.blogging.dao.repository.BlogRepository;
import uz.duol.blogging.dao.repository.CommentRepository;
import uz.duol.blogging.domain.BlogEntity;
import uz.duol.blogging.domain.CommentEntity;
import uz.duol.blogging.domain.ThemeEntity;
import uz.duol.blogging.domain.enumeration.BlogStatus;
import uz.duol.blogging.domain.enumeration.Category;
import uz.duol.blogging.dto.BlogDTO;
import uz.duol.blogging.dto.ThemeDTO;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BlogControllerTestIT extends AbstractMockMvcBase {

  private static final String BASE_URL = "/api/v1/blogs";
  private static BlogEntity ENTITY;
  private static CommentEntity COMMENT_ENTITY;
  private static ThemeEntity THEME_ENTITY;

  @Autowired private BlogRepository repository;
  @Autowired private CommentRepository commentRepository;

  public static void createEntity() {
    THEME_ENTITY = ThemeEntity.builder().name("AAA").category(Category.SCIENCE).build();
    ENTITY =
        BlogEntity.builder()
            .status(BlogStatus.ACTIVE)
            .numberViews(0L)
            .theme(THEME_ENTITY)
            .verified(false)
            .title("BBB")
            .text("CCC")
            .build();

    COMMENT_ENTITY = CommentEntity.builder().content("SDD").build();
  }

  public void saveEntity() {
    ENTITY = repository.save(ENTITY);

    COMMENT_ENTITY.setBlog(ENTITY);
    COMMENT_ENTITY = commentRepository.save(COMMENT_ENTITY);
  }

  @BeforeEach
  public void set() {
    createEntity();
  }

  @Transactional
  @Test
  public void create() throws Exception {
    ThemeDTO theme = ThemeDTO.builder().category(Category.SCIENCE).name("AAA").build();
    BlogDTO.BlogCreateDTO dto =
        BlogDTO.BlogCreateDTO.builder().title("AAA").text("BBB").theme(theme).build();

    this.mockMvc
        .perform(
            MockMvcRequestBuilders.post(BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(dto)))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.name()));
  }

  @Test
  public void findById() throws Exception {
    saveEntity();
    this.mockMvc
        .perform(
            MockMvcRequestBuilders.get(BASE_URL + "/" + ENTITY.getId())
                .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
        .andExpect(jsonPath("$.data.id").value(ENTITY.getId()));
  }

  @Test
  public void findAll() throws Exception {
    saveEntity();

    this.mockMvc
        .perform(MockMvcRequestBuilders.get(BASE_URL).accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
        .andDo(print());
  }

  @Test
  public void findAllWithComment() throws Exception {
    saveEntity();

    this.mockMvc
        .perform(
            MockMvcRequestBuilders.get(BASE_URL + "/" + ENTITY.getId() + "/comments")
                .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
        .andExpect(jsonPath("$.data.id").value(ENTITY.getId()))
        .andExpect(jsonPath("$.data.comments").isArray());
  }

  @Transactional
  @Test
  public void update() throws Exception {
    saveEntity();

    BlogDTO.BlogCreateDTO dto =
        BlogDTO.BlogCreateDTO.builder()
            .id(ENTITY.getId())
            .title(ENTITY.getTitle())
            .text(ENTITY.getText())
            .theme(ENTITY.getTheme().map2DTO())
            .build();

    this.mockMvc
        .perform(
            MockMvcRequestBuilders.put(BASE_URL + "/" + ENTITY.getId())
                .accept(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(dto))
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
        .andExpect(jsonPath("$.data.id").value(ENTITY.getId()));
  }

  @Transactional
  @Test
  public void delete() throws Exception {
    saveEntity();
    this.mockMvc
        .perform(MockMvcRequestBuilders.delete(BASE_URL + "/" + ENTITY.getId()))
        .andDo(print())
        .andExpect(status().isNoContent());
  }
}
