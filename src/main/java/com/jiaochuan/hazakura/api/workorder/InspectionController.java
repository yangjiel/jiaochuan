package com.jiaochuan.hazakura.api.workorder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiaochuan.hazakura.entity.user.Role;
import com.jiaochuan.hazakura.entity.user.UserEntity;
import com.jiaochuan.hazakura.entity.workorder.*;
import com.jiaochuan.hazakura.exception.UserException;
import com.jiaochuan.hazakura.service.InspectionService;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/inspection")
public class InspectionController {
    @Autowired
    InspectionService inspectionService;

    @Autowired
    ObjectMapper objectMapper;

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "JSON形式的InspectionEntity",
            content = @Content(
                    schema = @Schema(implementation = String.class),
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(value =
                                    "{\n" +
                                            "    \"creatorId\": \"1\",\n" +
                                            "    \"inspectorId\": \"2\",\n" +
                                            "    \"departmentId\": \"null\",\n" +
                                            "    \"type\": \"PERIPHERAL\",\n" +
                                            "    \"productName\": \"电钻\",\n" +
                                            "    \"model\": \"8x8\",\n" +
                                            "    \"serialNumber\": \"12345\",\n" +
                                            "    \"quantity\": \"10\",\n" +
                                            "    \"unit\": \"件\",\n" +
                                            "    \"manufacturer\": \"四川电气集团\",\n" +
                                            "    \"sizeFit\": \"0\",\n" +
                                            "    \"qualityCertificate\": \"1\",\n" +
                                            "    \"exterior\": \"1\",\n" +
                                            "    \"logo\": \"1\",\n" +
                                            "    \"packaging\": \"1\",\n" +
                                            "    \"note\": \"...\",\n" +
                                            "    \"samplingMethod\": \"取平均\",\n" +
                                            "    \"requisitions\": \"5\",\n" +
                                            "}")
                    }
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "登记成功，返回200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = List.class),
                            examples = {
                                    @ExampleObject(value =
                                            "{\n" +
                                                    "    \"id\": 3,\n" +
                                                    "    \"type\": \"PERIPHERAL\",\n" +
                                                    "    \"productName\": \"电钻\",\n" +
                                                    "    \"model\": \"8x8\",\n" +
                                                    "    \"serialNumber\": \"12345\",\n" +
                                                    "    \"quantity\": 10,\n" +
                                                    "    \"unit\": \"件\",\n" +
                                                    "    \"manufacturer\": \"四川电气集团\",\n" +
                                                    "    \"sizeFit\": 0,\n" +
                                                    "    \"qualityCertificate\": 1,\n" +
                                                    "    \"exterior\": 1,\n" +
                                                    "    \"logo\": 1,\n" +
                                                    "    \"packaging\": 1,\n" +
                                                    "    \"note\": \"...\",\n" +
                                                    "    \"samplingMethod\": \"取平均\",\n" +
                                                    "    \"status\": \"PENDING_APPROVAL\",\n" +
                                                    "    \"createdDate\": \"2021-10-25 20:19:59\",\n" +
                                                    "    \"creatorId\": {\n" +
                                                    "        \"id\": 1,\n" +
                                                    "        \"username\": \"jason\",\n" +
                                                    "        \"firstName\": \"测\",\n" +
                                                    "        \"lastName\": \"试\",\n" +
                                                    "        \"role\": \"VICE_PRESIDENT\",\n" +
                                                    "        \"cell\": \"13800000000\",\n" +
                                                    "        \"email\": \"something@qq.com\",\n" +
                                                    "        \"birthday\": \"1972-02-08\"\n" +
                                                    "    },\n" +
                                                    "    \"inspectorId\": null,\n" +
                                                    "    \"inspectionActions\": null\n" +
                                                    "    \"inspectionActions\": [\n" +
                                                    "        {\n" +
                                                    "            \"id\": 2,\n" +
                                                    "            \"user\": {\n" +
                                                    "                \"id\": 1,\n" +
                                                    "                \"username\": \"jason\",\n" +
                                                    "                \"firstName\": \"测\",\n" +
                                                    "                \"lastName\": \"试\",\n" +
                                                    "                \"role\": \"VICE_PRESIDENT\",\n" +
                                                    "                \"cell\": \"13800000000\",\n" +
                                                    "                \"email\": \"something@qq.com\",\n" +
                                                    "                \"birthday\": \"1972-02-08\"\n" +
                                                    "            },\n" +
                                                    "            \"comment\": null,\n" +
                                                    "            \"date\": \"2021-10-25 20:20:27\",\n" +
                                                    "            \"prevStatus\": \"PENDING_APPROVAL\",\n" +
                                                    "            \"status\": \"PENDING_APPROVAL\"\n" +
                                                    "        }\n" +
                                                    "    ...\n" +
                                                    "    ]\n" +
                                                    "}")
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "用户输入错误，例如：必填项没有填写、客户id有特殊字符等。"
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
    @RolesAllowed({Role.Constants.STAFF_QUALITY_CONTROL,
            Role.Constants.MANAGER_QUALITY_CONTROL,
            Role.Constants.ENGINEER_AFTER_SALES,
            Role.Constants.VICE_PRESIDENT})
    public ResponseEntity<String> createInspection(@RequestBody InspectionDto dto) {
        try {
            InspectionEntity inspectionEntity = inspectionService.createInspection(dto);
            String json = objectMapper.writeValueAsString(inspectionEntity);
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
                    name = "creator",
                    required = false,
                    schema = @Schema(type = "String"),
                    description = "查询关于该工人id的检验单。"
            ),
            @Parameter(
                    name = "requisitions",
                    required = false,
                    schema = @Schema(type = "String"),
                    description = "查询关于该采购单id的检验单。"
            ),
            @Parameter(
                    name = "datetime",
                    required = false,
                    schema = @Schema(type = "String"),
                    description = "查询该日期的工单。"
            ),
            @Parameter(
                    name = "status",
                    required = false,
                    schema = @Schema(type = "String"),
                    description = "查询该状态的言简单。"
            ),
            @Parameter(
                    name = "orderBy",
                    required = false,
                    schema = @Schema(type = "String"),
                    description = "timeDesc, timeAsc，默认根据时间排序。"
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
                    description = "成功，response body将返回已经分页的检验单信息。" +
                            "注意，只有销售主管和副总可以浏览所有记录，销售员工只能查阅自己提交的检验单",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = List.class),
                            examples = {
                                    @ExampleObject(value =
                                            "[\n" +
                                                    "    {\n" +
                                                    "        \"id\": 3,\n" +
                                                    "        \"type\": \"PERIPHERAL\",\n" +
                                                    "        \"productName\": \"电钻\",\n" +
                                                    "        \"model\": \"8x8\",\n" +
                                                    "        \"serialNumber\": \"12345\",\n" +
                                                    "        \"quantity\": 10,\n" +
                                                    "        \"unit\": \"件\",\n" +
                                                    "        \"manufacturer\": \"四川电气集团\",\n" +
                                                    "        \"sizeFit\": 0,\n" +
                                                    "        \"q ualityCertificate\": 1,\n" +
                                                    "        \"exterior\": 1,\n" +
                                                    "        \"logo\": 1,\n" +
                                                    "        \"packaging\": 1,\n" +
                                                    "        \"note\": \"...\",\n" +
                                                    "        \"samplingMethod\": \"取平均\",\n" +
                                                    "        \"status\": \"PENDING_APPROVAL\",\n" +
                                                    "        \"createdDate\": \"2021-10-25 20:19:59\",\n" +
                                                    "        \"creatorId\": {\n" +
                                                    "            \"id\": 1,\n" +
                                                    "            \"username\": \"jason\",\n" +
                                                    "            \"firstName\": \"测\",\n" +
                                                    "            \"lastName\": \"试\",\n" +
                                                    "            \"role\": \"VICE_PRESIDENT\",\n" +
                                                    "            \"cell\": \"13800000000\",\n" +
                                                    "            \"email\": \"something@qq.com\",\n" +
                                                    "            \"birthday\": \"1972-02-08\"\n" +
                                                    "        },\n" +
                                                    "        \"inspectorId\": null,\n" +
                                                    "        \"inspectionActions\": [\n" +
                                                    "            {\n" +
                                                    "                \"id\": 2,\n" +
                                                    "                \"user\": {\n" +
                                                    "                    \"id\": 1,\n" +
                                                    "                    \"username\": \"jason\",\n" +
                                                    "                    \"firstName\": \"测\",\n" +
                                                    "                    \"lastName\": \"试\",\n" +
                                                    "                    \"role\": \"VICE_PRESIDENT\",\n" +
                                                    "                    \"cell\": \"13800000000\",\n" +
                                                    "                    \"email\": \"something@qq.com\",\n" +
                                                    "                    \"birthday\": \"1972-02-08\"\n" +
                                                    "                },\n" +
                                                    "                \"comment\": null,\n" +
                                                    "                \"date\": \"2021-10-25 20:20:27\",\n" +
                                                    "                \"prevStatus\": \"PENDING_APPROVAL\",\n" +
                                                    "                \"status\": \"PENDING_APPROVAL\"\n" +
                                                    "            }\n" +
                                                    "        ...\n" +
                                                    "        ]\n" +
                                                    "    }\n" +
                                                    "    ...\n" +
                                                    "]")
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "请求出错，例如传进来的分页参数page或size < 0。",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = List.class),
                            examples = {
                                    @ExampleObject(value =
                                            "[\n" +
                                                    "]")
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "服务器错误，例如各类异常。异常的详细信息将会在返回的response body中。",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = List.class),
                            examples = {
                                    @ExampleObject(value =
                                            "[\n" +
                                                    "]")
                            }
                    )
            )
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({Role.Constants.STAFF_CLIENT_SERVICE,
            Role.Constants.STAFF_INVENTORY,
            Role.Constants.STAFF_PROCUREMENT,
            Role.Constants.DIRECTOR_AFTER_SALES,
            Role.Constants.MANAGER_AFTER_SALES,
            Role.Constants.ENGINEER_AFTER_SALES,
            Role.Constants.VICE_PRESIDENT})
    public ResponseEntity<String> getInspections(
            Authentication authentication,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Long creator,
            @RequestParam(required = false) Long requisitionsEntity,
            @RequestParam(required = false) LocalDateTime datetime,
            @RequestParam(required = false) InspectionStatus status,
            @RequestParam(required = false) String orderBy
    ) {
        if (page == null) {
            page = 0;
        }
        if (size == null) {
            size = 500;
        }

//        Collection<String> grantedAuthorityList = user.getAuthorityNames();

        try {
            UserEntity user = (UserEntity) authentication.getPrincipal();
            List<InspectionEntity> inspectionListPage;
//            if (grantedAuthorityList.contains(Role.Constants.MANAGER_AFTER_SALES) ||
//                grantedAuthorityList.contains(Role.Constants.DIRECTOR_AFTER_SALES) ||
//                grantedAuthorityList.contains(Role.Constants.VICE_PRESIDENT)) {
            if (user.getRole() == Role.MANAGER_PROCUREMENT ||
                    user.getRole() == Role.STAFF_INVENTORY ||
                    user.getRole() == Role.VICE_PRESIDENT) {
                inspectionListPage = inspectionService.getInspections(
                        page,
                        size,
                        creator,
                        requisitionsEntity,
                        datetime,
                        status,
                        orderBy);
            } else {
                inspectionListPage = inspectionService.getInspections(
                        page,
                        size,
                        user.getId(),
                        requisitionsEntity,
                        datetime,
                        status,
                        orderBy);
            }

            String json = objectMapper.writeValueAsString(inspectionListPage);
            return ResponseEntity.ok(json);
        } catch (UserException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "JSON形式的InspectionEntity",
            content = @Content(
                    schema = @Schema(implementation = String.class),
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(value =
                                    "{\n" +
                                            "    \"inspectionId\": \"1\",\n" +
                                            "    \"departmentId\": \"null\",\n" +
                                            "    \"type\": \"PERIPHERAL\",\n" +
                                            "    \"productName\": \"电钻\",\n" +
                                            "    \"model\": \"8x8\",\n" +
                                            "    \"serialNumber\": \"12345\",\n" +
                                            "    \"quantity\": \"10\",\n" +
                                            "    \"unit\": \"件\",\n" +
                                            "    \"manufacturer\": \"四川电气集团\",\n" +
                                            "    \"sizeFit\": \"0\",\n" +
                                            "    \"qualityCertificate\": \"1\",\n" +
                                            "    \"exterior\": \"1\",\n" +
                                            "    \"logo\": \"1\",\n" +
                                            "    \"packaging\": \"1\",\n" +
                                            "    \"note\": \"...\",\n" +
                                            "    \"samplingMethod\": \"取平均\",\n" +
                                            "}")
                    }
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "登记成功，返回200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = List.class),
                            examples = {
                                    @ExampleObject(value =
                                            "{\n" +
                                                    "    \"id\": 3,\n" +
                                                    "    \"type\": \"PERIPHERAL\",\n" +
                                                    "    \"productName\": \"电钻\",\n" +
                                                    "    \"model\": \"8x8\",\n" +
                                                    "    \"serialNumber\": \"12345\",\n" +
                                                    "    \"quantity\": 10,\n" +
                                                    "    \"unit\": \"件\",\n" +
                                                    "    \"manufacturer\": \"四川电气集团\",\n" +
                                                    "    \"sizeFit\": 0,\n" +
                                                    "    \"qualityCertificate\": 1,\n" +
                                                    "    \"exterior\": 1,\n" +
                                                    "    \"logo\": 1,\n" +
                                                    "    \"packaging\": 1,\n" +
                                                    "    \"note\": \"...\",\n" +
                                                    "    \"samplingMethod\": \"取平均\",\n" +
                                                    "    \"status\": \"PENDING_APPROVAL\",\n" +
                                                    "    \"createdDate\": \"2021-10-25 20:19:59\",\n" +
                                                    "    \"creatorId\": {\n" +
                                                    "        \"id\": 1,\n" +
                                                    "        \"username\": \"jason\",\n" +
                                                    "        \"firstName\": \"测\",\n" +
                                                    "        \"lastName\": \"试\",\n" +
                                                    "        \"role\": \"VICE_PRESIDENT\",\n" +
                                                    "        \"cell\": \"13800000000\",\n" +
                                                    "        \"email\": \"something@qq.com\",\n" +
                                                    "        \"birthday\": \"1972-02-08\"\n" +
                                                    "    },\n" +
                                                    "    \"inspectorId\": null,\n" +
                                                    "    \"inspectionActions\": null\n" +
                                                    "    \"inspectionActions\": [\n" +
                                                    "        {\n" +
                                                    "            \"id\": 2,\n" +
                                                    "            \"user\": {\n" +
                                                    "                \"id\": 1,\n" +
                                                    "                \"username\": \"jason\",\n" +
                                                    "                \"firstName\": \"测\",\n" +
                                                    "                \"lastName\": \"试\",\n" +
                                                    "                \"role\": \"VICE_PRESIDENT\",\n" +
                                                    "                \"cell\": \"13800000000\",\n" +
                                                    "                \"email\": \"something@qq.com\",\n" +
                                                    "                \"birthday\": \"1972-02-08\"\n" +
                                                    "            },\n" +
                                                    "            \"comment\": null,\n" +
                                                    "            \"date\": \"2021-10-25 20:20:27\",\n" +
                                                    "            \"prevStatus\": \"PENDING_APPROVAL\",\n" +
                                                    "            \"status\": \"PENDING_APPROVAL\"\n" +
                                                    "        }\n" +
                                                    "    ...\n" +
                                                    "    ]\n" +
                                                    "}")
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "用户输入错误，例如：必填项没有填写、客户id有特殊字符等。"
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
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    @RolesAllowed({Role.Constants.STAFF_QUALITY_CONTROL,
            Role.Constants.MANAGER_QUALITY_CONTROL,
            Role.Constants.ENGINEER_AFTER_SALES,
            Role.Constants.VICE_PRESIDENT})
    public ResponseEntity<String> updateInspection(Authentication authentication,
                                                   @RequestBody InspectionDto dto) {
        try {
            InspectionEntity inspectionEntity = inspectionService.updateInspection(
                    ((UserEntity) authentication.getPrincipal()).getId(),
                    dto);
            String json = objectMapper.writeValueAsString(inspectionEntity);
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
                    description = "返回系统里所有的status。",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(value = "" +
                                            "[\n" +
                                            "    {\n" +
                                            "        \"statusId\": \"PENDING_APPROVAL\", \n" +
                                            "        \"statusName\": \"待审批\"\n" +
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
            path = "/all-status",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<GetStatusResponseDto>> getAllStatus() {
        List<GetStatusResponseDto> responseList = new ArrayList<>();
        for (InspectionStatus status : InspectionStatus.values()) {
            responseList.add(new GetStatusResponseDto(status.name(), status.statusDescription));
        }
        return ResponseEntity.ok(responseList);
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "返回系统里所有的status。",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(value = "" +
                                            "[\n" +
                                            "    {\n" +
                                            "        \"statusId\": \"PERIPHERAL\", \n" +
                                            "        \"statusName\": \"外协件\"\n" +
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
            path = "/all-types",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<GetTypesResponseDto>> getAllTypes() {
        List<GetTypesResponseDto> responseList = new ArrayList<>();
        for (InspectionType type : InspectionType.values()) {
            responseList.add(new GetTypesResponseDto(type.name(), type.typeDescription));
        }
        return ResponseEntity.ok(responseList);
    }
}
