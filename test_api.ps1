# 测试后端 API 接口
Write-Host "正在测试后端接口..." -ForegroundColor Green

$baseUrl = "http://localhost:8080/api"

# 测试 1: 数据大屏概览
Write-Host "`n[测试 1] 数据大屏概览接口" -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/dashboard/overview" -Method Get -UseBasicParsing
    $data = $response.Content | ConvertFrom-Json
    Write-Host "巴西订单数：$($data.data.brazilOrders)" -ForegroundColor Cyan
    Write-Host "巴西销售额：$($data.data.brazilAmount)" -ForegroundColor Cyan
    Write-Host "国内订单数：$($data.data.chinaOrders)" -ForegroundColor Cyan
    Write-Host "国内销售额：$($data.data.chinaAmount)" -ForegroundColor Cyan
} catch {
    Write-Host "请求失败：$($_.Exception.Message)" -ForegroundColor Red
}

# 测试 2: 巴西订单列表
Write-Host "`n[测试 2] 巴西订单列表接口" -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/orders/list?page=1&size=5" -Method Get -UseBasicParsing
    $data = $response.Content | ConvertFrom-Json
    Write-Host "返回数据条数：$($data.data.Count)" -ForegroundColor Cyan
    if ($data.data.Count -gt 0) {
        Write-Host "第一条订单ID：$($data.data[0].id)" -ForegroundColor Cyan
        Write-Host "第一条订单状态：$($data.data[0].status)" -ForegroundColor Cyan
    }
} catch {
    Write-Host "请求失败：$($_.Exception.Message)" -ForegroundColor Red
}

# 测试 3: 巴西订单总数
Write-Host "`n[测试 3] 巴西订单总数接口" -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/orders/total" -Method Get -UseBasicParsing
    $data = $response.Content | ConvertFrom-Json
    Write-Host "订单总数：$($data.data.total)" -ForegroundColor Cyan
} catch {
    Write-Host "请求失败：$($_.Exception.Message)" -ForegroundColor Red
}

# 测试 4: 国内订单列表
Write-Host "`n[测试 4] 国内订单列表接口" -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/domestic/order/list?page=1&size=5" -Method Get -UseBasicParsing
    $data = $response.Content | ConvertFrom-Json
    Write-Host "返回数据条数：$($data.data.Count)" -ForegroundColor Cyan
    if ($data.data.Count -gt 0) {
        Write-Host "第一条订单ID：$($data.data[0].id)" -ForegroundColor Cyan
        Write-Host "第一条订单产品：$($data.data[0].productName)" -ForegroundColor Cyan
    }
} catch {
    Write-Host "请求失败：$($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n测试完成！" -ForegroundColor Green
