<?php

class DB {
    private static $db = null;
    private $dsn = 'mysql:dbname=stud_v19_ese;host=kark.uit.no';
    private $user = "stud_v19_ese";
    private $password = "HVjEeKbuKkHXoFGJ";
    private $dbHandle = null;

    private function __construct() {
        try {
            $this->dbHandle = new PDO($this->dsn, $this->user, $this->password);
        } catch (PDOException $e) {
            return $e->getMessage();
        }
    }

    public static function getDBConnection() {
        if (DB::$db==null) {
            DB::$db = new self();
        }
        return DB::$db->dbHandle;
    }
}