# 清除前端缓存并刷新
Write-Host "正在清除前端缓存..." -ForegroundColor Green

# 清除浏览器缓存（需要用户手动操作）
Write-Host "`n请在前端页面执行以下操作：" -ForegroundColor Yellow
Write-Host "1. 按 Ctrl + Shift + R 强制刷新页面" -ForegroundColor Cyan
Write-Host "2. 或按 F12 打开开发者工具，在 Console 中输入：localStorage.clear(); location.reload();" -ForegroundColor Cyan

# 检查后端服务状态
Write-Host "`n检查后端服务状态..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/dashboard/overview" -Method Get -UseBasicParsing -TimeoutSec 5
    $data = $response.Content | ConvertFrom-Json
    Write-Host "✓ 后端服务正常" -ForegroundColor Green
    Write-Host "  巴西订单数：$($data.data.brazilOrders)" -ForegroundColor Cyan
    Write-Host "  国内订单数：$($data.data.chinaOrders)" -ForegroundColor Cyan
} catch {
    Write-Host "✗ 后端服务未启动或无法访问" -ForegroundColor Red
    Write-Host "  错误：$($_.Exception.Message)" -ForegroundColor Red
}

# 检查前端服务状态
Write-Host "`n检查前端服务状态..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:5173" -Method Get -UseBasicParsing -TimeoutSec 5
    Write-Host "✓ 前端服务正常" -ForegroundColor Green
} catch {
    Write-Host "✗ 前端服务未启动或无法访问" -ForegroundColor Red
    Write-Host "  错误：$($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n如果前端仍显示旧数据，请检查：" -ForegroundColor Yellow
Write-Host "1. 浏览器是否开启了开发者模式并禁用了缓存" -ForegroundColor Cyan
Write-Host "2. 前端 API 地址配置是否正确（应该是 http://localhost:8080）" -ForegroundColor Cyan
Write-Host "3. 前端代码中的 fetchData 函数是否有错误（按 F12 查看 Console）" -ForegroundColor Cyan
