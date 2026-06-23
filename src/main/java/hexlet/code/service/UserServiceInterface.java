package hexlet.code.service;

import hexlet.code.dto.UserCreateDTO;
import hexlet.code.dto.UserResponseDTO;
import hexlet.code.dto.UserUpdateDTO;
import hexlet.code.model.User;

import java.util.List;

public interface UserServiceInterface {
    List<UserResponseDTO> getAll();
    UserResponseDTO getById(Long id);
    User getUserById(Long id);
    UserResponseDTO getByEmail(String email);
    boolean existsByEmail(String email);
    UserResponseDTO create(UserCreateDTO createDTO);
    UserResponseDTO update(Long id, UserUpdateDTO updateDTO);
    void delete(Long id);
}