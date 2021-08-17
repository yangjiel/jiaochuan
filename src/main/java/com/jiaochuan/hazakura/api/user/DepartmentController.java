package com.jiaochuan.hazakura.api.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiaochuan.hazakura.entity.user.DepartmentEntity;
import com.jiaochuan.hazakura.exception.UserException;
import com.jiaochuan.hazakura.service.DepartmentService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/department")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "JSON形式的ClientEntity",
            content = @Content(
                    schema = @Schema(implementation = String.class),
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(value =
                                    "{\n" +
                                            "    \"department\": \"开发部\",\n" +
                                            "    \"leaderId\": \"1\"\n" +
                                            "}")
                    }
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "登记成功，返回200",
                    content = @Content(
                            schema = @Schema(implementation = String.class),
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(value =
                                            "{\n" +
                                                    "    \"id\": 1,\n" +
                                                    "    \"department\": \"开发部\",\n" +
                                                    "    \"leader\": {\n" +
                                                    "        \"id\": 1,\n" +
                                                    "        \"userName\": \"jason\",\n" +
                                                    "        \"firstName\": \"杰\",\n" +
                                                    "        \"lastName\": \"杨\",\n" +
                                                    "        \"role\": \"VICE_PRESIDENT\",\n" +
                                                    "        \"cell\": \"13813249988\",\n" +
                                                    "        \"email\": \"null\",\n" +
                                                    "        \"companyAddress\": \"四川省成都市高新西区金月路45号高鑫产业园\",\n" +
                                                    "        \"birthday\": \"1982-02-08\",\n" +
                                                    "        \"notes\": \"D5数控机床购买及安装\"\n" +
                                                    "    \"}\n" +
                                                    "}")
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "用户输入错误，例如：必填项没有填写、手机号码有特殊字符等。"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "没有访问权限，用户没登录，登录状态已过期或者该用户无权访问。",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = List.class),
                            examples = {
                                    @ExampleObject(value = "Forbidden")
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "客户已存在，无法创新新客户。",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = List.class),
                            examples = {
                                    @ExampleObject(value = "该客户名已存在！或 该客户手机号码已存在！")
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "服务器错误，例如各类异常。异常的详细信息将会在返回的response body中。",
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(implementation = String.class),
                            examples = {
                                    @ExampleObject(value = "服务器出现错误，请与管理员联系。内部错误：RuntimeException ...")
                            }
                    )
            )
    })
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public ResponseEntity<String> createDepartment(@RequestBody String jsonRequest) {
        try {
            Map<String, String> map = new ObjectMapper().readValue(jsonRequest, HashMap.class);
            DepartmentEntity departmentEntity = departmentService.createDepartment(
                    map.get("department"), map.get("leaderId"));
            String json = objectMapper.writeValueAsString(departmentEntity);
            return ResponseEntity.ok(json);
        } catch (UserException e) {
            return ResponseEntity
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}
