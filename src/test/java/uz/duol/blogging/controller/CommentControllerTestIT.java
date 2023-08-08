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
import uz.duol.blogging.domain.enumeration.CommentStatus;
import uz.duol.blogging.dto.CommentDTO;


import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CommentControllerTestIT extends AbstractMockMvcBase {

  private static final String BASE_URL = "/api/v1/comments";
  private static CommentEntity ENTITY;
  private static BlogEntity BLOG_ENTITY;
  private static ThemeEntity THEME_ENTITY;

  @Autowired private CommentRepository repository;

  @Autowired private BlogRepository blogRepository;

  public static void createEntity() {
    THEME_ENTITY = ThemeEntity.builder().name("AAA").category(Category.SCIENCE).build();

    ENTITY =
        CommentEntity.builder()
            .status(CommentStatus.ACTIVE)
            .content("CCC")
            .verified(Boolean.FALSE)
            .usability(0)
            .notUsability(0)
            .build();

    BLOG_ENTITY =
        BlogEntity.builder()
            .verified(false)
            .title("AAA")
            .text("BBB")
            .status(BlogStatus.ACTIVE)
            .numberViews(0L)
            .theme(THEME_ENTITY)
            .build();
  }

  public void saveEntity() {
    BLOG_ENTITY = blogRepository.save(BLOG_ENTITY);

    ENTITY.setBlog(BLOG_ENTITY);

    repository.save(ENTITY);
  }

  @BeforeEach
  public void set() {
    createEntity();
  }

  @Transactional
  @Test
  public void create() throws Exception {
    BLOG_ENTITY = blogRepository.save(BLOG_ENTITY);

    CommentDTO.CommentCreateDTO dto =
        CommentDTO.CommentCreateDTO.builder().blogId(BLOG_ENTITY.getId()).content("AAA").build();

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

  @Transactional
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

  @Transactional
  @Test
  public void findAll() throws Exception {
    saveEntity();
    this.mockMvc
        .perform(MockMvcRequestBuilders.get(BASE_URL).accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()));
  }

  @Transactional
  @Test
  public void update() throws Exception {
    saveEntity();

    CommentDTO.CommentUpdateDTO testDTO =
        CommentDTO.CommentUpdateDTO.builder()
            .id(ENTITY.getId())
            .content(ENTITY.getContent())
            .isUsability(Boolean.TRUE)
            .build();

    this.mockMvc
        .perform(
            MockMvcRequestBuilders.put(BASE_URL + "/" + ENTITY.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(testDTO))
                .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
        .andExpect(jsonPath("$.data.id").value(ENTITY.getId()));
  }

  @Transactional
  @Test
  public void delete () throws Exception {
    saveEntity();
    this.mockMvc
            .perform(MockMvcRequestBuilders.delete(BASE_URL + "/" + ENTITY.getId()))
            .andDo(print())
            .andExpect(status().isNoContent());
  }
}
