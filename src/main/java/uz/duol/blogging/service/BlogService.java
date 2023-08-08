package uz.duol.blogging.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.duol.blogging.dto.BlogDTO;
import uz.duol.blogging.dto.filter.BlogFilter;
import uz.duol.blogging.exchange.ApiResponse;

import java.util.List;

public interface BlogService {

    ApiResponse<BlogDTO> create(BlogDTO.BlogCreateDTO dto);

    ApiResponse<BlogDTO> findById (Long id);

    ApiResponse<Page<BlogDTO>> findAll (BlogFilter filter, Pageable pageable);

    ApiResponse<BlogDTO.BlogWithCommentsDTO> findBlogWithComments (Long blogId);

    ApiResponse<BlogDTO> update (BlogDTO.BlogCreateDTO dto, Long id);

    ApiResponse<BlogDTO> updateVerified (Long id, BlogDTO.BlogVerifiedDTO dto);

    void updateBlogsNumberViews (List<Long> blogIds);

    void remove(Long id);
}
