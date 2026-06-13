package com.ecommerce.ecommerceanalysis.controller;

import com.ecommerce.ecommerceanalysis.entity.Result;
import com.ecommerce.ecommerceanalysis.service.selection.SelectionService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/api/profit")
public class ProfitController {

    @Resource
    private SelectionService selectionService;

    @PostMapping("/calculate")
    public Result<Map<String, Object>> calculate(@RequestBody Map<String, Object> params) {
        return Result.success(selectionService.profitCalc(params));
    }
}