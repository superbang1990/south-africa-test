-- Chery, OJ, Lepas, iCAUR
select a.code as 'material_code', b.code as 'model_code', b.name as 'model_name', c.label as 'enats_code',
    case
        when d.code in('KMG4', 'CE', 'CG', 'CL', 'CM', 'KM02', 'KME8') then 'Black'
        when d.code in('KM00','VQ','VS','KME0','KMD4','KMP6','WA','WB','WS','KM89','KMS7','KM006','KM60','KM015') then 'Blue'
        when d.code in('KM007','KM004','KMS6','KMD9','KMD7','KMF9','KMH0','SK','KMV9','KMX5','SJ','SY','Z6','KMS8','KMJ7','KMY3','X6','KM012') then 'Green'
        when d.code in('KML6','KM01','KM005','GV','GT','UE','KMB9','KMF5','KM008','KMM5','KMH6','KM010','KMG5','KM011','KM018','KMB0','GX','KM019') then 'Grey'
        when d.code in('KM013', 'KMT7') then 'Orange'
        when d.code in('JK', 'KMX6', 'KMP9') then 'Purple'
        when d.code in('KMS1', 'KMX8', 'KM27', 'NL', 'KM014', 'ZF', 'XN', 'KMF2', 'KMY4') then 'Red'
        when d.code in('KM016','KMG9','KX','KH','KU','KMR0','KME1','KMY2','KM017','KME5','KMJ8','ZL','X4') then 'Silver'
        when d.code in('BW', 'BK', 'BX', 'KM021', 'KM90', 'KMG6', 'XT', 'KM023', 'ZE', 'KMF3') then 'White'
        end as 'color_description'
from material a
         left join model b on a.model_code=b.code
         left join (select item_value, label from chery_iotd_admin.sys_dict_item where dict_type='sa_model_code_2_number') c on b.code=c.item_value
         left join color d on a.ex_color=d.code
where a.brand_code='iCAUR';