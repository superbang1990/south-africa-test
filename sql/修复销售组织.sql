-- 2014313302632443906, Chery
update vehicle_base_info a set a.sale_org_code='2200', a.manufacturer_code='2200', a.logistic_factory_code='2200'
                           where a.material_code in (select code from material where brand_code='Chery');
update stock_subsidiary_vehicle set sale_org_code='2200' where brand_id='2014313302632443906';

-- 2014313302640832514, OJ
update vehicle_base_info a set a.sale_org_code='2201', a.manufacturer_code='2201', a.logistic_factory_code='2201'
                           where a.material_code in (select code from material where brand_code='OJ');
update stock_subsidiary_vehicle set sale_org_code='2201' where brand_id='2014313302640832514';

-- 2014313302640832515, Lepas
update vehicle_base_info a set a.sale_org_code='2202', a.manufacturer_code='2202', a.logistic_factory_code='2202'
                           where a.material_code in (select code from material where brand_code='Lepas');
update stock_subsidiary_vehicle set sale_org_code='2202' where brand_id='2014313302640832515';

-- 2014313302636638210, iCAUR
update vehicle_base_info a set a.sale_org_code='2203', a.manufacturer_code='2203', a.logistic_factory_code='2203'
                           where a.material_code in (select code from material where brand_code='iCAUR');
update stock_subsidiary_vehicle set sale_org_code='2203' where brand_id='2014313302636638210';