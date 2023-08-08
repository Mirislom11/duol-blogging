package uz.duol.blogging.service;

import uz.duol.blogging.dto.ThemeDTO;
import uz.duol.blogging.exchange.ApiResponse;

import java.util.List;

public interface ThemeService {

    ApiResponse<ThemeDTO> create(ThemeDTO dto);

    ApiResponse<ThemeDTO> findById (Long id);

    ApiResponse<List<ThemeDTO>> findAll ();

    ApiResponse<ThemeDTO> update (Long id, ThemeDTO dto);
}
