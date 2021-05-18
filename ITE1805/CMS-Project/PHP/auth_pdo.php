<?php
    $host = "kark.uit.no";
    $dbname = "stud_v19_ese";
    $username = "stud_v19_ese";
    $password = "HVjEeKbuKkHXoFGJ";

	try {
		$db = new PDO("mysql:host=$host;dbname=$dbname", $username, $password);
	}
	catch(PDOException $e) {
		//throw new Exception($e->getMessage(), $e->getCode);
		print($e->getMessage());
	}
?>