package com.jiaochuan.hazakura.api.workorder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiaochuan.hazakura.api.user.LoginResponseDto;
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
                    description = "登记成功，返回200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponseDto.class),
                            examples = {
                                    @ExampleObject(value =
                                            "{\n" +
                                                    "    \"status\": \"登录成功！\",\n" +
                                                    "    \"equipment\": {\n" +
                                                    "        \"id\": \"1\",\n" +
                                                    "        \"deviceName\": \"数控机床轴承\",\n" +
                                                    "        \"deviceModel\": \"8*8\",\n" +
                                                    "        \"manufacture\": \"四川轴承有限公司\",\n" +
                                                    "    }\n" +
                                                    "}")
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "用户输入错误，例如：必填项没有填写、客户id有特殊字符等。",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponseDto.class),
                            examples = {
                                    @ExampleObject(value =
                                            "{\n" +
                                                    "    \"status\": \"服务器出现错误，请与管理员联系。内部错误：\",\n" +
                                                    "    \"equipment\": null" +
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
                            mediaType = "text/plain",
                            schema = @Schema(implementation = String.class),
                            examples = {
                                    @ExampleObject(value =
                                            "{\n" +
                                                    "    \"status\": \"Error Message\",\n" +
                                                    "    \"equipment\": null" +
                                                    "}")
                            }
                    )
            )
    })
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public ResponseEntity<GetEquipmentResponseDto> createEquipment(@RequestBody String jsonRequest) {
        try {
            EquipmentEntity equipmentEntity = objectMapper.readValue(jsonRequest, EquipmentEntity.class);
            equipmentService.createEquipment(equipmentEntity);
            return ResponseEntity.ok(new GetEquipmentResponseDto("Created", equipmentEntity));
        } catch (AppException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GetEquipmentResponseDto(
                            "服务器出现错误，请与管理员联系。内部错误：" + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new GetEquipmentResponseDto(e.getMessage(), null));
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
