package com.jiaochuan.hazakura.api.workorder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiaochuan.hazakura.entity.workorder.PartListEntity;
import com.jiaochuan.hazakura.entity.workorder.WorkOrderEntity;
import com.jiaochuan.hazakura.exception.AppException;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/part_list")
public class PartListController {
    @Autowired
    private PartListService partListService;

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
                                            "    \"workerId\": \"1\",\n" +
                                            "    \"workOrderId\": \"2\",\n" +
                                            "    \"count\": \"2\",\n" +
                                            "    \"usage\": \"给水管道通水试验记录，排水干道通球试验记录...\",\n" +
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
                    description = "用户输入错误，例如：必填项没有填写、用户id有特殊字符等。"
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
    public ResponseEntity<String> createPartList(@RequestBody String jsonRequest) {
        try {
            PartListEntity partListEntity = objectMapper.convertValue(jsonRequest, PartListEntity.class);
            partListService.createPartList(partListEntity);
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
                                                    "    \"partLists\": [{\n" +
                                                    "        \"workerId\": \"2\",\n" +
                                                    "        \"workOrderId\": \"1\",\n" +
                                                    "        \"count\": \"2\",\n" +
                                                    "        \"usage\": \"给水管道通水试验记录，排水干道通球试验记录...\",\n" +
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
                                                    "    \"partLists\": null" +
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
                                                    "    \"partLists\": null" +
                                                    "}")
                            }
                    )
            )
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PartListEntity>> getPartLists(
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
            List<PartListEntity> partListList = partListService.getPartLists(page, size);
            return ResponseEntity.ok(partListList);
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
