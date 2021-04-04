package com.jiaochuan.hazakura.api.workorder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiaochuan.hazakura.entity.user.Role;
import com.jiaochuan.hazakura.entity.user.UserEntity;
import com.jiaochuan.hazakura.entity.workorder.PartListEntity;
import com.jiaochuan.hazakura.exception.UserException;
import com.jiaochuan.hazakura.service.PartListService;
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
import java.util.List;

@RestController
@RequestMapping("/api/v1/part-list")
public class PartListController {
    @Autowired
    private PartListService partListService;

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
                                            "    \"workerId\": \"1\",\n" +
                                            "    \"workOrderId\": \"1\",\n" +
                                            "    \"usage\": \"\",\n" +
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
                                                    "    \"WOid\": \"3\",\n" +
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
                                                    "                ...\n" +
                                                    "            ]\n" +
                                                    "        }\n" +
                                                    "        ...\n" +
                                                    "    ],\n" +
                                                    "    \"createDate\": \"2020-04-30\",\n" +
                                                    "    \"address\": \"四川省成都市高新西区金月路45号高鑫产业园\",\n" +
                                                    "    \"status\": null,\n" +
                                                    "    \"description\": null,\n" +
                                                    "    \"serviceItem\": null,\n" +
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
    public ResponseEntity<String> createPartList(@RequestBody PostPartListDto dto) {
        try {
            PartListEntity partListEntity = partListService.createPartList(dto);
            String json = objectMapper.writeValueAsString(partListEntity.getWorkOrder());
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
            description = "JSON形式的PartListEntity",
            content = @Content(
                    schema = @Schema(implementation = String.class),
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(value =
                                    "{\n" +
                                            "    \"partListId\": \"2\", \n" +
                                            "    \"partListStatus\": \"PENDING_APPROVAL\"\n" +
                                            "}\n" +
                                            "OR\n" +
                                            "{\n" +
                                            "    \"partListId\": 2, \n" +
                                            "    \"workerId\": 1\n" +
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
                                                    "    \"WOid\": \"3\",\n" +
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
                                                    "            \"partListStatus\": \"PENDING_APPROVAL\",\n" +
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
                                                    "                ...\n" +
                                                    "            ]\n" +
                                                    "        }\n" +
                                                    "        ...\n" +
                                                    "    ],\n" +
                                                    "    \"status\": null,\n" +
                                                    "    \"createDate\": \"2020-04-30\",\n" +
                                                    "    \"address\": \"四川省成都市高新西区金月路45号高鑫产业园\",\n" +
                                                    "    \"description\": null,\n" +
                                                    "    \"serviceItem\": null,\n" +
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
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    @RolesAllowed({Role.Constants.MANAGER_PROCUREMENT,
            Role.Constants.STAFF_INVENTORY,
            Role.Constants.VICE_PRESIDENT})
    public ResponseEntity<String> updatePartListStatus(Authentication authentication,
                                                       @RequestBody PostPartListDto dto) {
        try {
            PartListEntity partListEntity = partListService.updatePartListStatusHelper(
                    ((UserEntity) authentication.getPrincipal()).getId(),
                    dto);
            String json = objectMapper.writeValueAsString(partListEntity.getWorkOrder());
            return ResponseEntity.ok(json);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("服务器出现错误，请与管理员联系。内部错误：" + e.getMessage());
        }
    }
}
