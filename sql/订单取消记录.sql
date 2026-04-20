select * from order_preallot
select distinct a.cancel_user_id, date(a.cancel_time), c.name, e.role_name
from order_preallot a
    left join chery_iotd_admin.sys_user b on a.cancel_user_id=b.username
    left join chery_iotd_admin.sys_dept c on b.dept_id=c.dept_id
    left join chery_iotd_admin.sys_user_role d on b.user_id=d.user_id
    left join chery_iotd_admin.sys_role e on d.role_id=e.role_id
where a.cancel_status <> 'NotCancelled' order by a.cancel_time desc;
