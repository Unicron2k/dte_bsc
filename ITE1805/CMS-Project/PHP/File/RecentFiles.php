<?php

require_once('DB.class.php');

// File
require_once('File.class.php');
require_once('FileInterface.php');
require_once('FileRegister.class.php');
$fileRegister = new FileRegister(DB::getDBConnection());


$last5=$fileRegister->fetchLast5();

$links = array();
foreach ($fileRegister->fetchLast5() as $file){
    $links[]=$file->getFileID();
    $links[]=$file->getFilename();
}