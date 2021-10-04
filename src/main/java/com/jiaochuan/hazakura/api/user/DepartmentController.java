package com.jiaochuan.hazakura.api.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiaochuan.hazakura.entity.user.DepartmentEntity;
import com.jiaochuan.hazakura.exception.UserException;
import com.jiaochuan.hazakura.service.DepartmentService;
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
            ),
            @Parameter(
                    name = "role",
                    required = false,
                    description = "此参数用于筛选出特定用户类别"
            )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "JSON形式的Page, Size",
            content = @Content(
                    schema = @Schema(implementation = String.class),
                    mediaType = "application/x-www-form-urlencoded"
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功，response body将返回已经分页的用户信息。",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = {
                                    @ExampleObject(value =
                                            "[\n" +
                                                    "    {\n" +
                                                    "        \"id\": 1,\n" +
                                                    "        \"department\": \"开发部\",\n" +
                                                    "        \"leader\": {\n" +
                                                    "            \"id\": 1,\n" +
                                                    "            \"userName\": \"jason\",\n" +
                                                    "            \"firstName\": \"杰\",\n" +
                                                    "            \"lastName\": \"杨\",\n" +
                                                    "            \"role\": \"VICE_PRESIDENT\",\n" +
                                                    "            \"cell\": \"13813249988\",\n" +
                                                    "            \"email\": \"null\",\n" +
                                                    "            \"companyAddress\": \"四川省成都市高新西区金月路45号高鑫产业园\",\n" +
                                                    "            \"birthday\": \"1982-02-08\",\n" +
                                                    "            \"notes\": \"D5数控机床购买及安装\"\n" +
                                                    "        \"}\n" +
                                                    "    }\n" +
                                                    "    ...\n" +
                                                    "]\n")
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "请求出错。",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = {
                                    @ExampleObject(value =
                                            "{\n" +
                                                    "    \"status\": \"请求出错，请检查调用参数。\",\n" +
                                                    "    \"users\": null" +
                                                    "}")
                            }
                    )
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
                    responseCode = "500",
                    description = "服务器错误，例如各类异常。异常的详细信息将会在返回的response body中。",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
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
    public ResponseEntity<String> getDepartment() {
        try {
            List<DepartmentEntity> list = this.departmentService.getDepartment();
            String json = objectMapper.writeValueAsString(list);
            return ResponseEntity.ok(json);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("服务器出现错误，请与管理员联系。内部错误：" + e.getMessage());
        }
    }
}
