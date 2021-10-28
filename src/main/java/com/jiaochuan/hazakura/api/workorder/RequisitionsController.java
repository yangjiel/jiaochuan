package com.jiaochuan.hazakura.api.workorder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiaochuan.hazakura.entity.user.Role;
import com.jiaochuan.hazakura.entity.user.UserEntity;
import com.jiaochuan.hazakura.entity.workorder.RequisitionsEntity;
import com.jiaochuan.hazakura.entity.workorder.RequisitionsStatus;
import com.jiaochuan.hazakura.exception.UserException;
import com.jiaochuan.hazakura.service.RequisitionsService;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/requisitions")
public class RequisitionsController {
    @Autowired
    private RequisitionsService requisitionsService;

    @Autowired
    private ObjectMapper objectMapper;

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "JSON形式的PartListEntity",
            content = @Content(
                    schema = @Schema(implementation = String.class),
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(value =
                                    "{\n" +
                                            "    \"creatorId\": \"1\",\n" +
                                            "    \"departmentId\": \"1\",\n" +
                                            "    \"workOrderId\": \"1\",\n" +
                                            "    \"equipments\": [ \n" +
                                            "        {\n" +
                                            "            \"equipment\": \"机床\",\n" +
                                            "            \"model\": \"8x8\",\n" +
                                            "            \"quantity\": \"10\" \n" +
                                            "        }, \n" +
                                            "        {\n" +
                                            "            \"equipment\": \"电钻\",\n" +
                                            "            \"model\": \"8x8\",\n" +
                                            "            \"quantity\": \"10\" \n" +
                                            "        } \n" +
                                            "    ] \n" +
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
                                                    "    \"id\": \"3\",\n" +
                                                    "    \"creator\": {\n" +
                                                    "        \"id\": \"1\",\n" +
                                                    "        \"username\": \"petertan\",\n" +
                                                    "        \"firstName\": \"四\",\n" +
                                                    "        \"lastName\": \"张\",\n" +
                                                    "        \"role\": \"ENGINEER_AFTER_SALES\",\n" +
                                                    "        \"cell\": \"13106660000\",\n" +
                                                    "        \"email\": \"pj.t@outlook.com\",\n" +
                                                    "        \"birthday\": \"1900-01-01\"\n" +
                                                    "    },\n" +
                                                    "    \"purchaser\": {\n" +
                                                    "        \"id\": \"1\",\n" +
                                                    "        \"username\": \"jason\",\n" +
                                                    "        \"firstName\": \"五\",\n" +
                                                    "        \"lastName\": \"王\",\n" +
                                                    "        \"role\": \"PURCHASE_MANAGER\",\n" +
                                                    "        \"cell\": \"13106660000\",\n" +
                                                    "        \"email\": \"pj.t@outlook.com\",\n" +
                                                    "        \"birthday\": \"1900-01-01\"\n" +
                                                    "    },\n" +
                                                    "    \"equipments\": [\n" +
                                                    "        {\n" +
                                                    "            \"id\": \"1\",\n" +
                                                    "            \"equipment\": \"数控机床轴承\",\n" +
                                                    "            \"model\": \"8x8\",\n" +
                                                    "            \"quantity\": \"10\"\n" +
                                                    "        },\n" +
                                                    "        {\n" +
                                                    "            \"id\": \"1\",\n" +
                                                    "            \"equipment\": \"数控机床轴承\",\n" +
                                                    "            \"model\": \"8x8\",\n" +
                                                    "            \"quantity\": \"10\"\n" +
                                                    "        },\n" +
                                                    "        ...\n" +
                                                    "    ],\n" +
                                                    "    \"purchaseOrderId\": \"18356\",\n" +
                                                    "    \"createDate\": \"2020-04-30\",\n" +
                                                    "    \"supplier\": \"四川省成都市工业产品有限公司\",\n" +
                                                    "    \"status\": \"PENDING_PURCHASE\",\n" +
                                                    "    \"purchaseDate\": null,\n" +
                                                    "    \"actions\": []\n" +
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
    @RolesAllowed({Role.Constants.STAFF_INVENTORY,
            Role.Constants.STAFF_PROCUREMENT,
            Role.Constants.ENGINEER_AFTER_SALES,
            Role.Constants.VICE_PRESIDENT})
    public ResponseEntity<String> createRequisitions(@RequestBody RequisitionsDto dto) {
        try {
            RequisitionsEntity requisitionsEntity = requisitionsService.createRequisitions(dto);
            String json = objectMapper.writeValueAsString(requisitionsEntity);
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
                    description = "查询关于开单员工id的工单。"
            ),
            @Parameter(
                    name = "purchaser",
                    required = false,
                    schema = @Schema(type = "String"),
                    description = "查询关于采购员工id的工单。"
            ),
            @Parameter(
                    name = "worker",
                    required = false,
                    schema = @Schema(type = "String"),
                    description = "查询关于该员工id的工单。"
            ),
            @Parameter(
                    name = "date",
                    required = false,
                    schema = @Schema(type = "String"),
                    description = "查询该日期的采购单。"
            ),
            @Parameter(
                    name = "status",
                    required = false,
                    schema = @Schema(type = "String"),
                    description = "查询该状态的采购单。"
            ),
            @Parameter(
                    name = "orderBy",
                    required = false,
                    schema = @Schema(type = "String"),
                    description = "timeDesc, timeAsc, nameDesc或nameAsc，默认根据时间排序。"
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
                    description = "成功，response body将返回已经分页的工单信息。" +
                            "注意，只有销售主管和副总可以浏览所有记录，销售员工只能查阅自己提交的工单",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = List.class),
                            examples = {
                                    @ExampleObject(value =
                                            "[\n" +
                                                    "    {\n" +
                                                    "        \"id\": \"3\",\n" +
                                                    "        \"creator\": {\n" +
                                                    "            \"id\": \"1\",\n" +
                                                    "            \"username\": \"petertan\",\n" +
                                                    "            \"firstName\": \"四\",\n" +
                                                    "            \"lastName\": \"张\",\n" +
                                                    "            \"role\": \"ENGINEER_AFTER_SALES\",\n" +
                                                    "            \"cell\": \"13106660000\",\n" +
                                                    "            \"email\": \"pj.t@outlook.com\",\n" +
                                                    "            \"birthday\": \"1900-01-01\"\n" +
                                                    "        },\n" +
                                                    "        \"purchaser\": {\n" +
                                                    "            \"id\": \"1\",\n" +
                                                    "            \"username\": \"jason\",\n" +
                                                    "            \"firstName\": \"五\",\n" +
                                                    "            \"lastName\": \"王\",\n" +
                                                    "            \"role\": \"PURCHASE_MANAGER\",\n" +
                                                    "            \"cell\": \"13106660000\",\n" +
                                                    "            \"email\": \"pj.t@outlook.com\",\n" +
                                                    "            \"birthday\": \"1900-01-01\"\n" +
                                                    "        },\n" +
                                                    "        \"equipments\": [\n" +
                                                    "            {\n" +
                                                    "                \"id\": \"1\",\n" +
                                                    "                \"equipment\": \"数控机床轴承\",\n" +
                                                    "                \"model\": \"8x8\",\n" +
                                                    "                \"quantity\": \"10\"\n" +
                                                    "            },\n" +
                                                    "            {\n" +
                                                    "                \"id\": \"1\",\n" +
                                                    "                \"equipment\": \"数控机床轴承\",\n" +
                                                    "                \"model\": \"8x8\",\n" +
                                                    "                \"quantity\": \"10\"\n" +
                                                    "            },\n" +
                                                    "            ...\n" +
                                                    "        ],\n" +
                                                    "        \"purchaseOrderId\": \"18356\",\n" +
                                                    "        \"createDate\": \"2020-04-30\",\n" +
                                                    "        \"supplier\": \"四川省成都市工业产品有限公司\",\n" +
                                                    "        \"status\": \"PENDING_PURCHASE\",\n" +
                                                    "        \"purchaseDate\": null,\n" +
                                                    "        \"actions\": []\n" +
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
                                            "{\n" +
                                                    "    \"workOrders\": null" +
                                                    "}")
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
                                            "{\n" +
                                                    "    \"workOrders\": null" +
                                                    "}")
                            }
                    )
            )
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({Role.Constants.MANAGER_PROCUREMENT,
            Role.Constants.STAFF_INVENTORY,
            Role.Constants.STAFF_PROCUREMENT,
            Role.Constants.DIRECTOR_AFTER_SALES,
            Role.Constants.MANAGER_AFTER_SALES,
            Role.Constants.ENGINEER_AFTER_SALES,
            Role.Constants.VICE_PRESIDENT})
    public ResponseEntity<String> getRequisitions(
            @AuthenticationPrincipal UserEntity user,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) UserEntity creator,
            @RequestParam(required = false) UserEntity purchaser,
            @RequestParam(required = false) UserEntity worker,
            @RequestParam(required = false) LocalDateTime datetime,
            @RequestParam(required = false) RequisitionsStatus status,
            @RequestParam(required = false) String orderBy
    ) {
        if (page == null) {
            page = 0;
        }
        if (size == null) {
            size = 500;
        }

        try {
            List<RequisitionsEntity> requisitionsEntityListPage;
            if (user.getRole() == Role.MANAGER_PROCUREMENT ||
                    user.getRole() == Role.STAFF_INVENTORY ||
                    user.getRole() == Role.STAFF_PROCUREMENT ||
                    user.getRole() == Role.STAFF_QUALITY_CONTROL ||
                    user.getRole() == Role.MANAGER_QUALITY_CONTROL ||
                    user.getRole() == Role.VICE_PRESIDENT) {
                requisitionsEntityListPage = requisitionsService.getRequisitions(size,
                        size,
                        creator,
                        purchaser,
                        worker,
                        datetime,
                        status,
                        orderBy);
                String json = objectMapper.writeValueAsString(requisitionsEntityListPage);
                return ResponseEntity.ok(json);
            }
            return ResponseEntity.ok("");
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
}
