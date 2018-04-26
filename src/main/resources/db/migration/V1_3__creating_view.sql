CREATE VIEW mp3_view AS
SELECT s.name as singer_name, s.id as singer_id, m.name as mp3_name FROM mp3
  m INNER JOIN singer s ON s.id=m.id;