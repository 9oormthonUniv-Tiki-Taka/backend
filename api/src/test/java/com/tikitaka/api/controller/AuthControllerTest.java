package com.tikitaka.api.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.tikitaka.api.jwt.JwtTokenProvider;
import com.tikitaka.api.repository.RoomRepository;
import com.tikitaka.api.repository.UserRepository;

// @WebMvcTest(AuthController.class)
// public class AuthControllerTest {
    
//     @Autowired
//     private MockMvc mockMvc;

//     @MockBean
//     private RoomRepository roomRepository;

//     @MockBean
//     private UserRepository userRepository;

//     @MockBean
//     private JwtTokenProvider jwtTokenProvider;

//     @Test
//     void testLogin() throws Exception {
//          mockMvc.perform(get("/login"))
//             .andExpect(status().isFound())
//             .andExpect(header().string("Location", "/oauth2/authorization/google"));
//     }

//     @Test
//     void testLoginRoom() throws Exception {
//         mockMvc.perform(get("/login/room"))
//             .andExpect(status().isOk());
//     }
// }
