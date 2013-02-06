/* Create Tables */

-- バイオデータファイルを表すテーブル
CREATE TABLE genetic_sequence
(
	id bigint NOT NULL AUTO_INCREMENT,
	-- 名前
	name varchar DEFAULT 'Untitled' NOT NULL,
	length int DEFAULT 0 NOT NULL,
	accession varchar NOT NULL,
	namespace varchar NOT NULL,
	version int NOT NULL,
	definition varchar NOT NULL,
	source_text varchar NOT NULL,
	organism varchar NOT NULL,
	date date,
	unit varchar NOT NULL,
	molecule_type varchar NOT NULL,
	file_type int NOT NULL,
	file_uri varchar NOT NULL,
	PRIMARY KEY (id)
);


/* Comments */

COMMENT ON TABLE genetic_sequence IS '配列 : バイオデータファイルを表すテーブル';
COMMENT ON COLUMN genetic_sequence.id IS '展示物識別番号';
COMMENT ON COLUMN genetic_sequence.name IS '名前 : 名前';
COMMENT ON COLUMN genetic_sequence.length IS '配列長';
COMMENT ON COLUMN genetic_sequence.accession IS 'アクセッション番号';
COMMENT ON COLUMN genetic_sequence.namespace IS '名前空間';
COMMENT ON COLUMN genetic_sequence.version IS 'バージョン番号';
COMMENT ON COLUMN genetic_sequence.definition IS '定義';
COMMENT ON COLUMN genetic_sequence.source_text IS 'ソース';
COMMENT ON COLUMN genetic_sequence.organism IS '生物種';
COMMENT ON COLUMN genetic_sequence.date IS '更新年月日';
COMMENT ON COLUMN genetic_sequence.unit IS '配列単位';
COMMENT ON COLUMN genetic_sequence.molecule_type IS '分子型';
COMMENT ON COLUMN genetic_sequence.file_type IS 'ファイル形式';
COMMENT ON COLUMN genetic_sequence.file_uri IS 'ファイル保管場所URI';



