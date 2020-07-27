package com.jiaochuan.hazakura.api.workorder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiaochuan.hazakura.entity.workorder.EquipmentEntity;
import com.jiaochuan.hazakura.entity.workorder.PartListEntity;
import com.jiaochuan.hazakura.exception.AppException;
import com.jiaochuan.hazakura.service.EquipmentService;
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
@RequestMapping("/api/v1/equipment")
public class EquipmentController {
    @Autowired
    private EquipmentService equipmentService;

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
                                            "    \"deviceName\": \"数控机床轴承\",\n" +
                                            "    \"deviceModel\": \"8*8\",\n" +
                                            "    \"manufacture\": \"四川轴承有限公司\",\n" +
                                            "}")
                    }
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "登记成功，返回200"
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
    public ResponseEntity<String> createEquipment(@RequestBody String jsonRequest) {
        try {
            EquipmentEntity equipmentEntity = objectMapper.convertValue(jsonRequest, EquipmentEntity.class);
            equipmentService.createEquipment(equipmentEntity);
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

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "成功，response body将返回已经分页的工单信息。",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = List.class),
                            examples = {
                                    @ExampleObject(value =
                                            "{\n" +
                                                    "    \"equipments\": [{\n" +
                                                    "        \"deviceName\": \"数控机床轴承\",\n" +
                                                    "        \"deviceModel\": \"8*8\",\n" +
                                                    "        \"manufacture\": \"四川轴承有限公司\",\n" +
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
                            schema = @Schema(implementation = List.class),
                            examples = {
                                    @ExampleObject(value =
                                            "{\n" +
                                                    "    \"equipments\": null" +
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
                                                    "    \"equipments\": null" +
                                                    "}")
                            }
                    )
            )
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EquipmentEntity>> getEquipments(
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
            List<EquipmentEntity> equipmentList = equipmentService.getEquipments(page, size);
            return ResponseEntity.ok(equipmentList);
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
