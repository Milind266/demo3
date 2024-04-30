package com.example.demo.Service;





import com.example.demo.model.UserDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

    @Service
    public class UserService {

        @Autowired
        private UserRepository userRepository;

        public UserDTO getUserById(Long userId) {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                return convertToDTO(user);
            } else {
                throw new RuntimeException("User not found with id: " + userId);
            }
        }

        public List<UserDTO> getAllUsers() {
            List<User> userList = userRepository.findAll();
            return userList.stream().map(this::convertToDTO).collect(Collectors.toList());
        }

        public UserDTO createUser(UserDTO userDTO) {
            User user = convertToEntity(userDTO);
            user = userRepository.save(user);
            return convertToDTO(user);
        }

        public UserDTO updateUser(Long userId, UserDTO userDTO) {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                User userToUpdate = userOptional.get();
                userToUpdate.setName(userDTO.getName());
                userToUpdate.setEmail(userDTO.getEmail());
                userRepository.save(userToUpdate);
                return convertToDTO(userToUpdate);
            } else {
                throw new RuntimeException("User not found with id: " + userId);
            }
        }

        public void deleteUser(Long userId) {
            if (userRepository.existsById(userId)) {
                userRepository.deleteById(userId);
            } else {
                throw new RuntimeException("User not found with id: " + userId);
            }
        }

        // Convert Entity to DTO
        private UserDTO convertToDTO(User user) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setName(user.getName());
            userDTO.setEmail(user.getEmail());
            return userDTO;
        }

        // Convert DTO to Entity
        private User convertToEntity(UserDTO userDTO) {
            User user = new User();
            user.setName(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            return user;
        }
    }


