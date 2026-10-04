#!/usr/bin/env bash
# 海水养殖台账验收脚本
# 主线：养殖池档案入口 -> 投苗批次落库追溯 -> 日常/饲料/用药 -> 换池 -> 抽检 -> 出池 -> 投苗单禁删
# 任一步失败即报"停点"并以非零码退出。
set -u

BASE="${BASE_URL:-http://127.0.0.1:8080}"
TS=$(date +%s)
TODAY=$(date +%F)
STEP="未开始"

fail() { echo ""; echo "【验收失败】停点：$STEP"; echo "原因：$1"; exit 1; }
pass() { echo "【通过】$STEP"; }

call() { # method path [body]
    if [ $# -ge 3 ]; then
        curl -s -X "$1" -H 'Content-Type: application/json' -d "$3" "$BASE$2"
    else
        curl -s -X "$1" "$BASE$2"
    fi
}

jget() { python3 -c "import sys,json; d=json.loads(sys.argv[1]); print(eval(sys.argv[2]))" "$1" "$2" 2>/dev/null; }

assert_success() { # resp
    local s; s=$(jget "$1" "d.get('success')")
    [ "$s" = "True" ] || fail "期望成功但被拒绝：$(jget "$1" "d.get('message')")"
}

assert_failure() { # resp
    local s; s=$(jget "$1" "d.get('success')")
    [ "$s" = "False" ] || fail "期望被拒绝但返回成功：$1"
    echo "  （按预期拒绝：$(jget "$1" "d.get('message')")）"
}

# 0. 服务健康检查
STEP="0. 服务健康检查"
resp=$(call GET /api/health) || fail "无法连接 $BASE"
assert_success "$resp"; pass

# 1. 建池 A（养殖池档案入口：水深、盐度、所属海区）
STEP="1. 建池A（养殖池档案入口）"
POND_A_CODE="AC-A-$TS"
resp=$(call POST /api/aqua/ponds "{\"pondCode\":\"$POND_A_CODE\",\"pondName\":\"验收A池\",\"seaArea\":\"东海一区\",\"waterDepthM\":2.0,\"salinity\":31.2}")
assert_success "$resp"
POND_A_ID=$(jget "$resp" "d['data']['id']")
[ -n "$POND_A_ID" ] || fail "建池A未返回档案ID"
pass

# 2. 建池 B（换池目标）
STEP="2. 建池B（换池目标池）"
resp=$(call POST /api/aqua/ponds "{\"pondCode\":\"AC-B-$TS\",\"pondName\":\"验收B池\",\"seaArea\":\"东海二区\",\"waterDepthM\":1.6,\"salinity\":29.8}")
assert_success "$resp"
POND_B_ID=$(jget "$resp" "d['data']['id']")
[ -n "$POND_B_ID" ] || fail "建池B未返回档案ID"
pass

# 3. 投苗：批次关联苗种来源、入池数量、计划出池日
STEP="3. 投苗（批次落池A）"
BATCH_NO="PB-$TS"
resp=$(call POST /api/aqua/batches "{\"batchNo\":\"$BATCH_NO\",\"pondId\":$POND_A_ID,\"source\":\"闽东育苗场\",\"quantity\":50000,\"stockDate\":\"$TODAY\",\"plannedHarvestDate\":\"2027-04-01\"}")
assert_success "$resp"
BATCH_ID=$(jget "$resp" "d['data']['id']")
[ -n "$BATCH_ID" ] || fail "投苗未返回批次ID"
pass

# 4. 追溯投苗批次落库：按ID回读并逐字段核对
STEP="4. 追溯投苗批次落库"
resp=$(call GET "/api/aqua/batches/$BATCH_ID")
assert_success "$resp"
[ "$(jget "$resp" "d['data']['batchNo']")" = "$BATCH_NO" ] || fail "落库批次编号不符"
[ "$(jget "$resp" "d['data']['pondId']")" = "$POND_A_ID" ] || fail "落库批次未挂在A池"
[ "$(jget "$resp" "d['data']['quantity']")" = "50000" ] || fail "落库入池数量不符"
[ "$(jget "$resp" "d['data']['source']")" = "闽东育苗场" ] || fail "落库苗种来源不符"
[ "$(jget "$resp" "d['data']['status']")" = "在养" ] || fail "落库批次状态应为在养"
pass

# 5. 批次编号不能重复
STEP="5. 批次编号唯一性"
resp=$(call POST /api/aqua/batches "{\"batchNo\":\"$BATCH_NO\",\"pondId\":$POND_A_ID,\"source\":\"重复单\",\"quantity\":1,\"stockDate\":\"$TODAY\",\"plannedHarvestDate\":\"2027-04-01\"}")
assert_failure "$resp"; pass

# 6. 日常记录
STEP="6. 日常记录（按池按批）"
resp=$(call POST /api/aqua/daily-records "{\"pondId\":$POND_A_ID,\"batchId\":$BATCH_ID,\"recordDate\":\"$TODAY\",\"waterDepthM\":2.0,\"salinity\":31.0,\"note\":\"验收日常巡塘\"}")
assert_success "$resp"; pass

# 7. 饲料留痕：正例 + 错池反例
STEP="7. 饲料留痕（按池按批）"
resp=$(call POST /api/aqua/feed-records "{\"pondId\":$POND_A_ID,\"batchId\":$BATCH_ID,\"feedDate\":\"$TODAY\",\"feedName\":\"对虾配合饲料2号\",\"quantityKg\":12.5}")
assert_success "$resp"
resp=$(call POST /api/aqua/feed-records "{\"pondId\":$POND_B_ID,\"batchId\":$BATCH_ID,\"feedDate\":\"$TODAY\",\"feedName\":\"错池投喂\",\"quantityKg\":1}")
assert_failure "$resp"; pass

# 8. 用药留痕
STEP="8. 用药留痕（按池按批）"
resp=$(call POST /api/aqua/medicine-records "{\"pondId\":$POND_A_ID,\"batchId\":$BATCH_ID,\"useDate\":\"$TODAY\",\"medicineName\":\"聚维酮碘\",\"dosage\":\"0.5ppm\",\"reason\":\"预防性消毒\"}")
assert_success "$resp"; pass

# 9. 换池：形成转移记录，批次改挂B池
STEP="9. 换池（形成转移记录）"
resp=$(call POST "/api/aqua/batches/$BATCH_ID/transfer" "{\"toPondId\":$POND_B_ID,\"transferDate\":\"$TODAY\",\"reason\":\"分池疏养\"}")
assert_success "$resp"
[ "$(jget "$resp" "d['data']['fromPondId']")" = "$POND_A_ID" ] || fail "转移记录转出池不符"
[ "$(jget "$resp" "d['data']['toPondId']")" = "$POND_B_ID" ] || fail "转移记录转入池不符"
resp=$(call GET "/api/aqua/batches/$BATCH_ID")
[ "$(jget "$resp" "d['data']['pondId']")" = "$POND_B_ID" ] || fail "换池后批次未挂到B池"
resp=$(call GET "/api/aqua/transfers?batchId=$BATCH_ID")
[ "$(jget "$resp" "len(d['data'])")" -ge 1 ] || fail "转移记录未落库"
pass

# 10. 抽检（换池后在B池）
STEP="10. 抽检"
resp=$(call POST /api/aqua/inspections "{\"pondId\":$POND_B_ID,\"batchId\":$BATCH_ID,\"inspectDate\":\"$TODAY\",\"sampleCount\":50,\"avgWeightG\":8.6,\"result\":\"规格整齐，活力正常\"}")
assert_success "$resp"; pass

# 11. 出池
STEP="11. 出池"
resp=$(call POST "/api/aqua/batches/$BATCH_ID/harvest" "{\"harvestDate\":\"$TODAY\",\"quantity\":48000,\"destination\":\"验收销售\"}")
assert_success "$resp"
resp=$(call GET "/api/aqua/batches/$BATCH_ID")
[ "$(jget "$resp" "d['data']['status']")" = "已出池" ] || fail "出池后批次状态未更新"
pass

# 12. 出池后禁止再登记养殖记录
STEP="12. 出池后禁止再登记"
resp=$(call POST /api/aqua/daily-records "{\"pondId\":$POND_B_ID,\"batchId\":$BATCH_ID,\"recordDate\":\"$TODAY\",\"note\":\"出池后补录\"}")
assert_failure "$resp"; pass

# 13. 原始投苗单不可删除
STEP="13. 原始投苗单不可删除"
resp=$(call DELETE "/api/aqua/batches/$BATCH_ID")
assert_failure "$resp"
resp=$(call GET "/api/aqua/batches/$BATCH_ID")
assert_success "$resp"
pass

echo ""
echo "【验收通过】养殖池档案入口 -> 投苗批次落库 -> 全流程 13 步全部通过"
echo "  池塘A=$POND_A_ID 池塘B=$POND_B_ID 批次=$BATCH_NO(ID=$BATCH_ID)"
