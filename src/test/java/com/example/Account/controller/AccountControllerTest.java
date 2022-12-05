package com.example.Account.controller;

import com.example.Account.domain.Account;
import com.example.Account.dto.AccountDto;
import com.example.Account.dto.CreateAccount;
import com.example.Account.dto.DeleteAccount;
import com.example.Account.exception.AccountException;
import com.example.Account.service.AccountService;
import com.example.Account.type.AccountStatus;
import com.example.Account.type.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {
    @MockBean
    private AccountService accountService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void successCreateAccount() throws Exception {
        //given => 서비스에서 값을 요청하고 받는값은 이런형태이고
        BDDMockito.given(accountService.createAccount(anyLong(), anyLong()))
                .willReturn(AccountDto.builder()
                        .userId(1L)
                        .accountNumber("1234567890")
                        .registeredAt(LocalDateTime.now())
                        .build());
        //when

        //then => 컨트롤러에서 요청들어오면 처리하는 로직은 이런형태이다
        mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(   //object => json
                                new CreateAccount.Request(1L, 100L)
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.accountNumber").value("1234567890"))
                .andDo(print());

    }

    @Test
    void successDeleteAccount() throws Exception {
        //given => 서비스에서 값을 요청하고 받는값은 이런형태이고
        BDDMockito.given(accountService.deleteAccount(anyLong(), anyString()))
                .willReturn(AccountDto.builder()
                        .userId(1L)
                        .accountNumber("1234567890")
                        .registeredAt(LocalDateTime.now())
                        .build());
        //when

        //then => 컨트롤러에서 요청들어오면 처리하는 로직은 이런형태이다
        mockMvc.perform(delete("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(   //object => json
                                new DeleteAccount.Request(1L, "0987654321")
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.accountNumber").value("1234567890"))
                .andDo(print());

    }

    @Test
    void successGetAccountByUserId() throws Exception {
        //given
        List<AccountDto> accountDtos = Arrays.asList(
                AccountDto.builder()
                        .accountNumber("1234567890")
                        .balance(100L).build(),
                AccountDto.builder()
                        .accountNumber("1111111111")
                        .balance(200L).build(),
                AccountDto.builder()
                        .accountNumber("2222222222")
                        .balance(300L).build()
        );
        BDDMockito.given(accountService.getAccountsByUserId(anyLong()))
                .willReturn(accountDtos);

        //when

        //then
        mockMvc.perform(get("/account?user_id=1"))
                .andDo(print())
                .andExpect(jsonPath("$[0].accountNumber").value("1234567890"))
                .andExpect(jsonPath("$[0].balance").value(100))
                .andExpect(jsonPath("$[1].accountNumber").value("1111111111"))
                .andExpect(jsonPath("$[1].balance").value(200))
                .andExpect(jsonPath("$[2].accountNumber").value("2222222222"))
                .andExpect(jsonPath("$[2].balance").value(300));
    }

    @Test
    void failGetAccount() throws Exception {
        //given
        BDDMockito.given(accountService.getAccountsByUserId(anyLong()))
                .willThrow(new AccountException(ErrorCode.ACCOUNT_NOT_FOUNT));

        //when
        //then
        mockMvc.perform(get("/account/876"))
                .andDo(print())
                .andExpect(jsonPath("$.errorCode").value("ACCOUNT_NOT_FOUNT"))
                .andExpect(jsonPath("$.errorMessage").value("계좌가 없습니다"))
                .andExpect(status().isOk());

    }
}