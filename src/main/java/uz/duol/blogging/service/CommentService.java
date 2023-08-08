package uz.duol.blogging.service;

import uz.duol.blogging.dto.CommentDTO;
import uz.duol.blogging.exchange.ApiResponse;

import java.util.List;

public interface CommentService {

  ApiResponse<CommentDTO> create(CommentDTO.CommentCreateDTO dto);

  ApiResponse<CommentDTO> findById(Long id);

  ApiResponse<List<CommentDTO>> findAll();

  ApiResponse<CommentDTO> update(CommentDTO.CommentUpdateDTO dto, Long id);

  void remove(Long id);

  ApiResponse<CommentDTO> updateVerified(CommentDTO.CommentVerifiedUpdateDTO dto, Long id);
}
