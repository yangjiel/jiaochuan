package com.jiaochuan.hazakura.api.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiaochuan.hazakura.entity.user.CustomerEntity;
import com.jiaochuan.hazakura.exception.AppException;
import com.jiaochuan.hazakura.service.CustomerService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> createCustomer(@RequestBody String jsonRequest) {
        try {
            CustomerEntity customerEntity = objectMapper.convertValue(jsonRequest, CustomerEntity.class);
            customerService.createCustomer(customerEntity);
            return ResponseEntity.ok().build();
        } catch (AppException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("服务器出现错误，请与管理员联系。内部错误：" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @Parameters(value = {
            @Parameter(
                    name = "page",
                    required = false,
                    description = "此参数用于说明第几个分页，如果没有传进来，默认是page = 0。"
            ),
            @Parameter(
                    name = "size",
                    required = false,
                    description = "此参数用于说明一个分页里面有多少个数据，如果没有传进来，size = 500。"
            )
    })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功，response body将返回已经分页的用户信息。",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponseDto.class),
                            examples = {
                                    @ExampleObject(value =
                                            "{\n" +
                                                    "    \"customers\": [{\n" +
                                                    "        \"user_name\": \"四川电器集团\",\n" +
                                                    "        \"contact_name\": \"刘晓东\",\n" +
                                                    "        \"cell\": \"13106660000\",\n" +
                                                    "        \"email\": \"user@example.com\",\n" +
                                                    "        \"company_address\": \"四川省成都市高新四区金月璐45号高鑫产业园\"\n" +
                                                    "    }\n" +
                                                    "    ...\n" +
                                                    "    ]\n" +
                                                    "}")
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "请求出错，例如传进来的分页参数page或size < 0。",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponseDto.class),
                            examples = {
                                    @ExampleObject(value =
                                            "{\n" +
                                                    "    \"status\": \"登录失败，请检查用户名或密码。\",\n" +
                                                    "    \"users\": null" +
                                                    "}")
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "服务器错误，例如各类异常。异常的详细信息将会在返回的response body中。",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponseDto.class),
                            examples = {
                                    @ExampleObject(value =
                                            "{\n" +
                                                    "    \"status\": \"服务器出现错误，请与管理员联系。内部错误：RuntimeException ...\",\n" +
                                                    "    \"users\": null" +
                                                    "}")
                            }
                    )
            )
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomerEntity>> getUsers(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        if (page == null) {
            page = 0;
        }
        if (size == null) {
            size = 500;
        }

        try {
            List<CustomerEntity> customersList = customerService.getCustomers(page, size);
            return ResponseEntity.ok(customersList);
        } catch (AppException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}
