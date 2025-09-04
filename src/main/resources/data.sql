-- Clean tables for idempotent demo startup
DELETE FROM goal_day;
DELETE FROM goal;

-- Application starts with no goals - users can create their own goals
