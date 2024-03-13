package com.bank.customeraccount;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerAccountService customerService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        Customer sCustomer = new Customer();
        sCustomer.setCustomerId(1L);
        sCustomer.setCustomerName("Test");
        sCustomer.setCustomerEmail("test12345@gmail.com");
        sCustomer.setCustomerMobile("09081234567");
        sCustomer.setAddress1("test");
        sCustomer.setAddress2("test");
        Account sAccount = new Account();
        sAccount.setAccountNumber(100);
        sAccount.setAccountType("Savings");
        sAccount.setAvailableBalance(500);
        sAccount.setCustomerAccount(sCustomer);
        sCustomer.setAccount(sAccount);
        Mockito.when(customerService.save(Mockito.any(Customer.class))).thenReturn(sCustomer);

        Mockito.when(customerService.findById(1L)).thenReturn(Optional.of(sCustomer));
    }

    @Test
    public void createCustomerSuccessTest() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerName("Test");
        customer.setCustomerEmail("test12345@gmail.com");
        customer.setCustomerMobile("09081234567");
        customer.setAddress1("test");
        customer.setAddress2("test");
        Account account = new Account();
        account.setAvailableBalance(500);

        customer.setAccount(account);

        mockMvc.perform(post("/api/v1/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").value(1L))
                .andExpect(jsonPath("$.transactionStatusCode").value(201))
                .andExpect(jsonPath("$.transactionStatusDescription").value("Customer account created"));
    }

    @Test
    public void getCustomerDetailsById_NotFound() throws Exception {
        Long customerId = 2L;

        mockMvc.perform(get("/api/v1/account/{customerId}", customerId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.transactionStatusCode").value(404))
                .andExpect(jsonPath("$.transactionStatusDescription").value("Customer not found"));
    }

    @Test
    public void getCustomerDetailsById_Found() throws Exception {
        Long customerId = 1L;

        mockMvc.perform(get("/api/v1/account/{customerId}", customerId))
                .andExpect(status().isFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerNumber").value(1L))
                .andExpect(jsonPath("$.customerName").value("Test"))
                .andExpect(jsonPath("$.customerEmail").value("test12345@gmail.com"))
                .andExpect(jsonPath("$.customerMobile").value("09081234567"))
                .andExpect(jsonPath("$.address1").value("test"))
                .andExpect(jsonPath("$.address2").value("test"))
                .andExpect(jsonPath("$.savings.accountNumber").value(100))
                .andExpect(jsonPath("$.savings.accountType").value("Savings"))
                .andExpect(jsonPath("$.savings.availableBalance").value(500))
                .andExpect(jsonPath("$.transactionStatusCode").value(302))
                .andExpect(jsonPath("$.transactionStatusDescription").value("Customer Account found"));
    }
}