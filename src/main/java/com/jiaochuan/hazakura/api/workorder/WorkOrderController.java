package com.jiaochuan.hazakura.api.workorder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiaochuan.hazakura.entity.user.ClientEntity;
import com.jiaochuan.hazakura.entity.user.Role;
import com.jiaochuan.hazakura.entity.user.UserEntity;
import com.jiaochuan.hazakura.entity.workorder.WorkOrderEntity;
import com.jiaochuan.hazakura.exception.UserException;
import com.jiaochuan.hazakura.service.WorkOrderService;
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
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/v1/work-order")
public class WorkOrderController {
    @Autowired
    private WorkOrderService workOrderService;

    @Autowired
    private ObjectMapper objectMapper;

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "JSON形式的WorkOrderEntity",
            content = @Content(
                    schema = @Schema(implementation = String.class),
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(value =
                                    "{\n" +
                                            "    \"clientId\": \"1\",\n" +
                                            "    \"workerId\": \"1\",\n" +
                                            "    \"address\": \"四川省成都市高新西区金月路45号高鑫产业园\",\n" +
                                            "    \"status\": \"\",\n" +
                                            "    \"description\": \"\",\n" +
                                            "    \"serviceItem\": \"\",\n" +
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
                                                    "    \"client\": {\n" +
                                                    "        \"id\": \"1\",\n" +
                                                    "        \"userName\": \"四川电器集团\",\n" +
                                                    "        \"contactName\": \"刘晓东\",\n" +
                                                    "        \"cell\": \"13813249988\",\n" +
                                                    "        \"email\": \"abc@example.com\",\n" +
                                                    "        \"companyAddress\": \"四川省成都市高新西区金月路45号高鑫产业园\" \n" +
                                                    "    },\n" +
                                                    "    \"worker\": {\n" +
                                                    "        \"id\": \"1\",\n" +
                                                    "        \"username\": \"petertan\",\n" +
                                                    "        \"firstName\": \"四\",\n" +
                                                    "        \"lastName\": \"张\",\n" +
                                                    "        \"role\": \"ENGINEER_AFTER_SALES\",\n" +
                                                    "        \"cell\": \"13106660000\",\n" +
                                                    "        \"email\": \"pj.t@outlook.com\",\n" +
                                                    "        \"birthday\": \"1900-01-01\"\n" +
                                                    "    },\n" +
                                                    "    \"createDate\": \"2020-04-30\",\n" +
                                                    "    \"address\": \"四川省成都市高新西区金月路45号高鑫产业园\",\n" +
                                                    "    \"status\": null,\n" +
                                                    "    \"description\": null,\n" +
                                                    "    \"serviceItem\": null,\n" +
                                                    "    \"actions\": [],\n" +
                                                    "    \"partLists\": [\n" +
                                                    "        {\n" +
                                                    "            \"id\": \"2\",\n" +
                                                    "            \"worker\": {\n" +
                                                    "                \"id\": \"1\",\n" +
                                                    "                \"username\": \"petertan\",\n" +
                                                    "                \"firstName\": \"四\",\n" +
                                                    "                \"lastName\": \"张\",\n" +
                                                    "                \"role\": \"ENGINEER_AFTER_SALES\",\n" +
                                                    "                \"cell\": \"13106660000\",\n" +
                                                    "                \"email\": \"pj.t@outlook.com\",\n" +
                                                    "                \"birthday\": \"1900-01-01\"\n" +
                                                    "            },\n" +
                                                    "            \"usage\": null,\n" +
                                                    "            \"createDate\": \"2020-04-30\",\n" +
                                                    "            \"partListEquipments\": [\n" +
                                                    "                {\n" +
                                                    "                    \"id\": \"1\",\n" +
                                                    "                    \"equipment\": \"数控机床轴承\",\n" +
                                                    "                    \"model\": \"8x8\",\n" +
                                                    "                    \"quantity\": \"10\"\n" +
                                                    "                },\n" +
                                                    "                {\n" +
                                                    "                    \"id\": \"1\",\n" +
                                                    "                    \"equipment\": \"数控机床轴承\",\n" +
                                                    "                    \"model\": \"8x8\",\n" +
                                                    "                    \"quantity\": \"10\"\n" +
                                                    "                },\n" +
                                                    "            ]\n" +
                                                    "        }\n" +
                                                    "    ]\n" +
                                                    "    ...\n" +
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
    @RolesAllowed({Role.Constants.STAFF_CLIENT_SERVICE,
            Role.Constants.MANAGER_AFTER_SALES,
            Role.Constants.ENGINEER_AFTER_SALES,
            Role.Constants.VICE_PRESIDENT})
    public ResponseEntity<String> createWorkOrder(@RequestBody PostWorkOrderDto dto) {
        try {
            WorkOrderEntity workOrderEntity = workOrderService.createWorkOrder(dto);
            String json = objectMapper.writeValueAsString(workOrderEntity);
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
                    name = "client",
                    required = false,
                    schema = @Schema(type = "String"),
                    description = "查询关于该客户id的工单。"
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
                    description = "查询该日期的工单。"
            ),
            @Parameter(
                    name = "status",
                    required = false,
                    schema = @Schema(type = "String"),
                    description = "查询该状态的工单。"
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
                                                    "        \"client\": {\n" +
                                                    "            \"id\": \"1\",\n" +
                                                    "            \"userName\": \"四川电器集团\",\n" +
                                                    "            \"contactName\": \"刘晓东\",\n" +
                                                    "            \"cell\": \"13813249988\",\n" +
                                                    "            \"email\": \"abc@example.com\",\n" +
                                                    "            \"companyAddress\": \"四川省成都市高新西区金月路45号高鑫产业园\" \n" +
                                                    "        },\n" +
                                                    "        \"worker\": {\n" +
                                                    "            \"id\": \"1\",\n" +
                                                    "            \"username\": \"petertan\",\n" +
                                                    "            \"firstName\": \"四\",\n" +
                                                    "            \"lastName\": \"张\",\n" +
                                                    "            \"role\": \"ENGINEER_AFTER_SALES\",\n" +
                                                    "            \"cell\": \"13106660000\",\n" +
                                                    "            \"email\": \"pj.t@outlook.com\",\n" +
                                                    "            \"birthday\": \"1900-01-01\"\n" +
                                                    "        },\n" +
                                                    "        \"createDate\": \"2020-04-30\",\n" +
                                                    "        \"address\": \"四川省成都市高新西区金月路45号高鑫产业园\",\n" +
                                                    "        \"status\": null,\n" +
                                                    "        \"description\": null,\n" +
                                                    "        \"serviceItem\": null,\n" +
                                                    "        \"actions\": [],\n" +
                                                    "        \"partLists\": [\n" +
                                                    "            {\n" +
                                                    "                \"id\": \"2\",\n" +
                                                    "                \"worker\": {\n" +
                                                    "                    \"id\": \"1\",\n" +
                                                    "                    \"username\": \"petertan\",\n" +
                                                    "                    \"firstName\": \"四\",\n" +
                                                    "                    \"lastName\": \"张\",\n" +
                                                    "                    \"role\": \"ENGINEER_AFTER_SALES\",\n" +
                                                    "                    \"cell\": \"13106660000\",\n" +
                                                    "                    \"email\": \"pj.t@outlook.com\",\n" +
                                                    "                    \"birthday\": \"1900-01-01\"\n" +
                                                    "                },\n" +
                                                    "                \"usage\": null,\n" +
                                                    "                \"createDate\": \"2020-04-30\",\n" +
                                                    "                \"partListEquipments\": [\n" +
                                                    "                    {\n" +
                                                    "                        \"id\": \"1\",\n" +
                                                    "                        \"equipment\": \"数控机床轴承\",\n" +
                                                    "                        \"model\": \"8x8\",\n" +
                                                    "                        \"quantity\": \"10\"\n" +
                                                    "                    },\n" +
                                                    "                    {\n" +
                                                    "                        \"id\": \"1\",\n" +
                                                    "                        \"equipment\": \"数控机床轴承\",\n" +
                                                    "                        \"model\": \"8x8\",\n" +
                                                    "                        \"quantity\": \"10\"\n" +
                                                    "                    },\n" +
                                                    "                    ...\n" +
                                                    "                ]\n" +
                                                    "            }\n" +
                                                    "            ...\n" +
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
    @RolesAllowed({Role.Constants.STAFF_CLIENT_SERVICE,
            Role.Constants.MANAGER_AFTER_SALES,
            Role.Constants.ENGINEER_AFTER_SALES,
            Role.Constants.VICE_PRESIDENT})
    public ResponseEntity<String> getWorkOrders(
            @AuthenticationPrincipal UserEntity user,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) ClientEntity client,
            @RequestParam(required = false) UserEntity worker,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) String status
            ) {
        if (page == null) {
            page = 0;
        }
        if (size == null) {
            size = 500;
        }

        Collection<?> grantedAuthorityList = user.getAuthorities();

        try {
            List<WorkOrderEntity> workOrderList;
            if (grantedAuthorityList.contains(Role.Constants.MANAGER_AFTER_SALES) ||
                grantedAuthorityList.contains(Role.Constants.VICE_PRESIDENT)) {
                workOrderList = workOrderService.getWorkOrders(page, size, client, worker, date, status);
            } else {
                workOrderList = workOrderService.getWorkOrders(page, size, client, user, date, status);
            }

            String json = objectMapper.writeValueAsString(workOrderList);
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
}
