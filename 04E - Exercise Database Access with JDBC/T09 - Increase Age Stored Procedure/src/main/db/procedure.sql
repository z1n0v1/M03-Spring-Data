CREATE PROCEDURE usp_get_older(minion_id INT)
BEGIN
    UPDATE minions SET age = age + 1 WHERE id = minion_id;
END;