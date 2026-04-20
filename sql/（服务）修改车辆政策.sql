-- #1 根据vin查询，LNNBBDEG3SC218957
-- 2026-02-04	LNNBBDEG3SC218957	TCHEVE	T7150U4**NG0002	1(usage)
SELECT DATE( idms_sale_time ) AS idms_sale_date,-- 仅日期
    vin, model_code, vehicle_version_code, vehicle_usage FROM service_vehicle_base_info
WHERE vin = 'LNNBBDEG3SC218957' AND tenant_id = '570272200';

-- #2 查询vehicle_usage
SELECT
    t.id,
    t.CODE,
    t.NAME,
    -- 如查出vehicle_usage与脚本1查出的vehicle_usage不一致，执行脚本3（更新车辆使用用途）修改为一致
    pvu.vehicle_usage
FROM
    vehicle_warranty_policy t
        INNER JOIN policy_biz_object pbo ON ( pbo.biz_id = t.id )
        AND t.tenant_id = 570272200
        AND pbo.tenant_id = 570272200
        INNER JOIN policy_vehicle_usage pvu ON pvu.bisiness_id = t.id
        AND pvu.tenant_id = 570272200
WHERE
    t.biz_status = 2
  AND t.del_flag = 0
  -- 例如脚本1查出的idms_sale_date为2025-10-31，
  -- 此时#idms_sale_date_end填入'2025-10-31 23:59:59'
  -- 此时#idms_sale_date_start填入2025-10-31 00:00:00
  AND t.sale_start_date <= '2026-02-04 23:59:59' AND t.sale_end_date >= '2026-02-04 00:00:00'
  AND pbo.biz_type IN ( 1, 2 )
  -- 分别填入脚本1查出vin、model_code、vehicle_version_code相应的值
  AND pbo.biz_value IN ( 'LNNBBDEG3SC218957', 'TCHEVE', 'T7150U4**NG0002' )
  AND pbo.del_flag = 0
GROUP BY
    t.id
ORDER BY
    CASE t.type WHEN 1 THEN 3 WHEN 2 THEN 1 WHEN 3 THEN 2 ELSE 4 END ASC, t.update_time ASC LIMIT 1; -->

-- #3，修改vehicle_usage
UPDATE service_vehicle_base_info
SET vehicle_usage = '2'
WHERE
    vin = 'LNNBBDEG3SC218957'
  AND del_flag = 0
  AND tenant_id = 570272200;