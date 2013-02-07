

/* Create Tables */

CREATE TABLE collection_item
(
	id bigint NOT NULL AUTO_INCREMENT,
	folder_id bigint NOT NULL,
	genetic_sequence_id bigint NOT NULL,
	PRIMARY KEY (id)
);


-- フォルダとその階層構造
CREATE TABLE folder
(
	id bigint NOT NULL AUTO_INCREMENT,
	parent_id bigint,
	name varchar NOT NULL DEFAULT '',
	-- 0: 階層, 1: コレクション, 2: フィルター
	type smallint DEFAULT 0 NOT NULL,
	PRIMARY KEY (id)
);


-- バイオデータファイルを表すテーブル
CREATE TABLE genetic_sequence
(
	id bigint NOT NULL AUTO_INCREMENT,
	name varchar DEFAULT 'Untitled' NOT NULL,
	length int DEFAULT 0 NOT NULL,
	accession varchar NOT NULL DEFAULT '',
	namespace varchar NOT NULL DEFAULT '',
	version int DEFAULT 0 NOT NULL,
	definition varchar NOT NULL DEFAULT '',
	source varchar NOT NULL DEFAULT '',
	organism varchar NOT NULL DEFAULT '',
	date date,
	-- 0: 不明, 1: bp, 2: aa
	unit smallint DEFAULT 0 NOT NULL,
	molecule_type varchar NOT NULL DEFAULT '',
	-- 0: 不明, 1: GenBank, 2: FASTA
	file_type smallint DEFAULT 0 NOT NULL,
	-- 相対パスの時は、ライブラリをルートとする
	file_uri varchar,
	PRIMARY KEY (id)
);



/* Create Foreign Keys */

ALTER TABLE collection_item
	ADD CONSTRAINT fk_collection_item_folder_id FOREIGN KEY (folder_id)
	REFERENCES folder (id)
	ON UPDATE RESTRICT
	ON DELETE CASCADE
;


ALTER TABLE folder
	ADD CONSTRAINT fk_folder_parent FOREIGN KEY (parent_id)
	REFERENCES folder (id)
	ON UPDATE RESTRICT
	ON DELETE CASCADE
;


ALTER TABLE collection_item
	ADD CONSTRAINT fk_collection_item_genetic_sequence_id FOREIGN KEY (genetic_sequence_id)
	REFERENCES genetic_sequence (id)
	ON UPDATE RESTRICT
	ON DELETE CASCADE
;



/* Comments */

COMMENT ON TABLE collection_item IS '配列コレクション';
COMMENT ON COLUMN collection_item.id IS '識別子';
COMMENT ON COLUMN collection_item.folder_id IS 'フォルダ id';
COMMENT ON COLUMN collection_item.genetic_sequence_id IS '配列識別子';
COMMENT ON TABLE folder IS 'フォルダ : フォルダとその階層構造';
COMMENT ON COLUMN folder.id IS '識別子';
COMMENT ON COLUMN folder.parent_id IS '親の識別子';
COMMENT ON COLUMN folder.name IS '名前';
COMMENT ON COLUMN folder.type IS '構造型 : 0: 階層, 1: コレクション, 2: フィルター';
COMMENT ON TABLE genetic_sequence IS '配列 : バイオデータファイルを表すテーブル';
COMMENT ON COLUMN genetic_sequence.id IS '識別子';
COMMENT ON COLUMN genetic_sequence.name IS '名前';
COMMENT ON COLUMN genetic_sequence.length IS '配列長';
COMMENT ON COLUMN genetic_sequence.accession IS 'アクセッション番号';
COMMENT ON COLUMN genetic_sequence.namespace IS '名前空間';
COMMENT ON COLUMN genetic_sequence.version IS 'バージョン番号';
COMMENT ON COLUMN genetic_sequence.definition IS '定義';
COMMENT ON COLUMN genetic_sequence.source IS 'ソース';
COMMENT ON COLUMN genetic_sequence.organism IS '生物種';
COMMENT ON COLUMN genetic_sequence.date IS '更新年月日';
COMMENT ON COLUMN genetic_sequence.unit IS '配列単位 : 0: 不明, 1: bp, 2: aa';
COMMENT ON COLUMN genetic_sequence.molecule_type IS '分子型';
COMMENT ON COLUMN genetic_sequence.file_type IS 'ファイル形式 : 0: 不明, 1: GenBank, 2: FASTA';
COMMENT ON COLUMN genetic_sequence.file_uri IS 'ファイル保管場所URI : 相対パスの時は、ライブラリをルートとする';



