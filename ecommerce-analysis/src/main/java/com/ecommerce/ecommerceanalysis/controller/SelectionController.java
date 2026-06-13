package com.ecommerce.ecommerceanalysis.controller;

import com.ecommerce.ecommerceanalysis.entity.Result;
import com.ecommerce.ecommerceanalysis.service.selection.SelectionService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/selection")
public class SelectionController {

    @Resource
    private SelectionService selectionService;

    @GetMapping("/recommend")
    public Result<Map<String, Object>> recommend(@RequestParam(defaultValue = "10") int top) {
        return Result.success(selectionService.recommend(top));
    }

    @GetMapping("/score")
    public Result<Map<String, Object>> score(@RequestParam String category) {
        return Result.success(selectionService.score(category));
    }

    @GetMapping("/blue-ocean")
    public Result<List<Map<String, Object>>> blueOcean() {
        return Result.success(selectionService.blueOcean());
    }
    
    @GetMapping("/supply-match")
    public Result<List<Map<String, Object>>>supplyMatch(@RequestParam String category) {
        return Result.success(selectionService.supplyMatch(category));
    }
    
    @PostMapping("/profit-calc")
    public Result<Map<String, Object>> profitCalc(@RequestBody Map<String, Object> params) {
        return Result.success(selectionService.profitCalc(params));
    }
}