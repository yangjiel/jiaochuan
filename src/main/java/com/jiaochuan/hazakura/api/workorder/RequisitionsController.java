package com.jiaochuan.hazakura.api.workorder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiaochuan.hazakura.entity.user.Role;
import com.jiaochuan.hazakura.entity.workorder.RequisitionsEntity;
import com.jiaochuan.hazakura.exception.UserException;
import com.jiaochuan.hazakura.service.RequisitionsService;
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

import javax.annotation.security.RolesAllowed;
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
    @RolesAllowed({Role.Constants.STAFF_CLIENT_SERVICE,
            Role.Constants.MANAGER_AFTER_SALES,
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
}
