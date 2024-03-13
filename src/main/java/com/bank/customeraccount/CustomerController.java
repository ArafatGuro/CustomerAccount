package com.bank.customeraccount;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/account")
public class CustomerController {

    private final CustomerAccountService customerService;

    @Autowired
    public CustomerController(CustomerAccountService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Object> getCustomerDetailsById(@PathVariable Long customerId) {
        Optional<Customer> optionalCustomer = customerService.findById(customerId);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            Account account = customer.getAccount();

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("customerNumber", customer.getCustomerId());
            response.put("customerName", customer.getCustomerName());
            response.put("customerMobile", customer.getCustomerMobile());
            response.put("customerEmail", customer.getCustomerEmail());
            response.put("address1", customer.getAddress1());
            response.put("address2", customer.getAddress2());

            if (account != null) {

                Map<String, Object> accountMap = new HashMap<>();
                accountMap.put("accountNumber", account.getAccountNumber());
                accountMap.put("accountType", account.getAccountType());
                accountMap.put("availableBalance", account.getAvailableBalance());

                if ("Savings".equalsIgnoreCase(account.getAccountType())) {
                    response.put("savings", accountMap);
                } else if ("Credit".equalsIgnoreCase(account.getAccountType())) {
                    response.put("credit", accountMap);
                }

            }

            response.put("transactionStatusCode", HttpStatus.FOUND.value());
            response.put("transactionStatusDescription", "Customer Account found");

            return new ResponseEntity<>(response, HttpStatus.FOUND);

        } else {

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("transactionStatusDescription", "Customer not found");
            response.put("transactionStatusCode", HttpStatus.NOT_FOUND.value());


            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<CustomResponse> createCustomer(@Valid @RequestBody Customer customer, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new CustomResponse(
                    0L,
                    HttpStatus.BAD_REQUEST.value(),
                    "Email is a required field"
            ), HttpStatus.BAD_REQUEST);
        }

        if (customer.getAccount() != null) {

            customer.getAccount().setAccountType(customer.getAccountType().getValue());

            //Creating a random accoount number here
            int accountNumber = new Random().nextInt(99999999);

            customer.getAccount().setAccountNumber(accountNumber);

            customer.getAccount().setCustomerAccount(customer);
        }

        Customer savedCustomer = customerService.save(customer);

        CustomResponse response = new CustomResponse(
                savedCustomer.getCustomerId(),
                HttpStatus.CREATED.value(),
                "Customer account created"
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}