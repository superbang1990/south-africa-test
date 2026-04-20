-- Sales date
-- Dealer
-- Owner
-- Name
-- Surname
-- Gender
-- Language
-- Cell number
-- Email
-- City
-- Model
-- Date of Birth
-- Marital Status
-- Race
-- Previous Vehicle
-- Compared model
-- First Purchase
-- Sales Type

select
    f.code as 'Customer Code',
   -- f.name as 'Customer Name',
    c.brand_code as 'Brand Name',
    b.engine_serial_no as 'Engine No',
    a.vin,
    a.report_time as 'Report Time',
    c.code as 'Material Code',
    d.code as 'Model Code',
    d.name as 'Model Name',
    e.name as 'Color Name'
from actual_so a
         left join vehicle_base_info b on a.vin=b.vin
         left join material c on b.material_code=c.code
         left join model d on c.model_code=d.code
         left join color e on c.ex_color=e.code
         left join customer f on a.customer_no=f.code
where a.so_status='Reported' and a.del_flag=0;

