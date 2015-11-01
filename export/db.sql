update users set u_default_es_id = null where u_default_es_id not in (1,2);
select * FROM expense_sheets where es_id in (1,2);
select * FROM user_expense_sheet where ues_es_id not in (1,2);
select * FROM categories where c_es_id not in (1,2);
select * from expenses where e_es_id not in (1,2);
select * from users_limits where ul_es_id not in (1,2);
select * from users_summaries where us_ul_id in (8,9,10);