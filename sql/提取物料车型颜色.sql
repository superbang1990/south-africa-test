select
    a.code as '物料号', a.name as '物料名称', a.in_color  as '内饰颜色编码', a.ex_color  as '外饰颜色编码', b.m_mode,
    a.model_code as '型号编码', a.status as '物料状态', b.name as '型号描述',
    a.vehicle_version as '版本', c.name as '版本名称' , a.power_system as '动力系统',
    a.engine_cylinder, a.brand_code as '品牌编码', ic.name as '内饰颜色', ec.name as '外饰颜色',
    a.update_by , a.update_time, a.row_version, a.pt_name, a.ar_name, a.tr_name ,
    a.th_name, a.es_name, a.it_name, a.eu_name
from material a
         left join model b on a.model_code = b.code and b.del_flag = '0' and b.tenant_id = 570272200
         left join vehicle_version c on a.vehicle_version = c.code and c.del_flag = '0' and c.tenant_id = 570272200
         left join color as ic on ic.code=a.in_color and ic.del_flag='0'
         left join color as ec on ec.code=a.ex_color and ec.del_flag='0'
where a.del_flag = '0' and a.tenant_id = 570272200;