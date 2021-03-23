package com.jiaochuan.hazakura.api.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiaochuan.hazakura.entity.user.Role;
import com.jiaochuan.hazakura.entity.user.UserEntity;
import com.jiaochuan.hazakura.exception.UserException;
import com.jiaochuan.hazakura.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "UserController")

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private ObjectMapper objectMapper;


    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "调用成功，response body将返回\"调用成功！\"。",
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(implementation = String.class),
                            examples = {
                                    @ExampleObject(value = "调用成功！")
                            }
                    )
            )
    })
    @GetMapping(
            path = "/test",
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("调用成功！");
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "JSON形式的UserEntity",
            content = @Content(
                    schema = @Schema(implementation = String.class),
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(value =
                            "{\n" +
                            "    \"username\": \"sam\",\n" +
                            "    \"password\": \"Initial1\",\n" +
                            "    \"firstName\": \"三\",\n" +
                            "    \"lastName\": \"张\",\n" +
                            "    \"role\": \"ENGINEER_AFTER_SALES\",\n" +
                            "    \"cell\": \"13106660000\",\n" +
                            "    \"email\": \"user@example.com\",\n" +
                            "    \"birthday\": \"1900-01-01\"\n" +
                            "}")
                    }
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "注册成功，response body将返回用户信息",
                    content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = List.class),
                    examples = {
                            @ExampleObject(value =
                                    "{\n" +
                                    "    \"id\": 1,\n" +
                                    "    \"username\": \"sam\",\n" +
                                    "    \"password\": \"Initial1\",\n" +
                                    "    \"firstName\": \"三\",\n" +
                                    "    \"lastName\": \"张\",\n" +
                                    "    \"role\": \"ENGINEER_AFTER_SALES\",\n" +
                                    "    \"cell\": \"13106660000\",\n" +
                                    "    \"email\": \"user@example.com\",\n" +
                                    "    \"birthday\": \"1900-01-01\"\n" +
                                    "}")
                    }
            )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "用户输入错误，例如：必填项没有填写、用户名已被使用、密码有特殊字符等。"
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
                            mediaType = "text/plain",
                            schema = @Schema(implementation = String.class),
                            examples = {
                                    @ExampleObject(value = "注册成功！"),
                                    @ExampleObject(value = "用户名不存在。"),
                                    @ExampleObject(value = "服务器出现错误，请与管理员联系。内部错误：RuntimeException ...")
                            }
                    )
            )
    })
    @PostMapping(
            path = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public ResponseEntity<String> createUser(@RequestBody String jsonRequest) {
        try {
            UserEntity userEntity = userService.createUser(objectMapper.readValue(jsonRequest, UserEntity.class));
            String json = objectMapper.writeValueAsString(userEntity);
            return ResponseEntity.ok(json);
        } catch (UserException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("服务器出现错误，请与管理员联系。内部错误：" + e.getMessage());
        }
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "URL Encoded形式的Username, Password",
            content = @Content(
                    schema = @Schema(implementation = String.class),
                    mediaType = "application/x-www-form-urlencoded"
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "登录成功，response body将返回已经登录的用户的信息。",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = {
                                    @ExampleObject(value =
                                    "{\n" +
                                    "    \"id\": 1,\n" +
                                    "    \"firstName\": \"三\",\n" +
                                    "    \"lastName\": \"张\",\n" +
                                    "    \"role\": \"ENGINEER_AFTER_SALES\",\n" +
                                    "    \"cell\": \"13106660000\",\n" +
                                    "    \"email\": \"user@example.com\",\n" +
                                    "    \"birthday\": \"1900-01-01\"\n" +
                                    "    }\n" +
                                    "}")
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "登录失败，用户名或密码错误，或用户不存在。",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = {
                                    @ExampleObject(value =
                                    "{\n" +
                                    "    \"status\": \"登录失败，请检查用户名或密码。\",\n" +
                                    "    \"user\": null" +
                                    "}")
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
                                    "    \"user\": null" +
                                    "}")
                            }
                    )
            )
    })
    @PostMapping(
            path = "/login",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
            Authentication auth = authManager.authenticate(authToken);
            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(auth);

            UserEntity userEntity = (UserEntity) userService.loadUserByUsername(username);
            String json = objectMapper.writeValueAsString(userEntity);
            return ResponseEntity.ok(json);
        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("登录失败，请检查用户名或密码。");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("服务器出现错误，请与管理员联系。内部错误：" + e.getMessage());
        }
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "JSON形式的UserEntity",
            content = @Content(
                    schema = @Schema(implementation = String.class),
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(value =
                                    "{\n" +
                                            "    \"username\": \"sam\",\n" +
                                            "    \"password\": \"Initial1\",\n" +
                                            "    \"firstName\": \"三\",\n" +
                                            "    \"lastName\": \"张\",\n" +
                                            "    \"role\": \"ENGINEER_AFTER_SALES\",\n" +
                                            "    \"cell\": \"13106660000\",\n" +
                                            "    \"email\": \"user@example.com\",\n" +
                                            "    \"birthday\": \"1900-01-01\"\n" +
                                            "}")
                    }
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "修改成功，response body将返回用户信息",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = List.class),
                            examples = {
                                    @ExampleObject(value =
                                            "{\n" +
                                                    "    \"username\": \"sam\",\n" +
                                                    "    \"oldPassword\": \"old\",\n" +
                                                    "    \"password\": \"Initial1\",\n" +
                                                    "    \"cell\": \"13106660000\",\n" +
                                                    "    \"email\": \"user@example.com\",\n" +
                                                    "}")
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "用户输入错误，例如：必填项没有填写、用户名已被使用、密码有特殊字符等。"
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
                            mediaType = "text/plain",
                            schema = @Schema(implementation = String.class),
                            examples = {
                                    @ExampleObject(value = "注册成功！"),
                                    @ExampleObject(value = "用户名不存在。"),
                                    @ExampleObject(value = "服务器出现错误，请与管理员联系。内部错误：RuntimeException ...")
                            }
                    )
            )
    })
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public ResponseEntity<String> updateUser(Authentication authentication,
                                             @RequestBody UpdateUserDto dto) {
        try {
            UserEntity userEntity = userService.updateUser(
                    ((UserEntity)authentication.getPrincipal()).getId(),
                    dto);
            String json = objectMapper.writeValueAsString(userEntity);
            return ResponseEntity.ok(json);
        } catch (UserException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("服务器出现错误，请与管理员联系。内部错误：" + e.getMessage());
        }
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "退出成功，response body将返回\"退出成功！\"。",
                    content = @Content(
                            mediaType = "text/plain",
                            schema = @Schema(implementation = String.class),
                            examples = {
                                    @ExampleObject(value = "退出成功！")
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
    })
    @PostMapping(
            path = "/logout",
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(null);
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok("退出成功！");
    }


    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "返回系统里所有的user role。",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(value = "" +
                                    "[\n" +
                                    "    {\n" +
                                    "        \"roleId\": \"STAFF_CLIENT_SERVICE\", \n" +
                                    "        \"roleName\": \"客服人员\"\n" +
                                    "    }, \n" +
                                    "    ...\n" +
                                    "]"
                                    )
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
    })
    @GetMapping(
            path = "/all-roles",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<GetRolesResponseDto>> getAllRoles() {
        List<GetRolesResponseDto> responseList = new ArrayList<>();
        for (Role role : Role.values()) {
            responseList.add(new GetRolesResponseDto(role.name(), role.roleDescription));
        }
        return ResponseEntity.ok(responseList);
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
                                    "        \"id: 1,\n" +
                                    "        \"firstName\": \"三\",\n" +
                                    "        \"lastName\": \"张\",\n" +
                                    "        \"role\": \"ENGINEER_AFTER_SALES\",\n" +
                                    "        \"cell\": \"13106660000\",\n" +
                                    "        \"email\": \"user@example.com\",\n" +
                                    "        \"birthday\": \"1900-01-01\"\n" +
                                    "    }\n" +
                                    "    ...\n" +
                                    "]\n")
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "请求出错，例如传进来的分页参数page或size < 0。",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
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
    public ResponseEntity<String> getUsers(
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
            List<UserEntity> usersList = userService.getUsers(page, size);
            String json = objectMapper.writeValueAsString(usersList);
            return ResponseEntity.ok(json);
        } catch (UserException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("服务器出现错误，请与管理员联系。内部错误：" + e.getMessage());
        }
    }

}
