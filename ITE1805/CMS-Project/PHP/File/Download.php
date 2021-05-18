<?php

session_start();
require_once('../DB.class.php');
// File
require_once('File.class.php');
require_once('FileInterface.php');
require_once('FileRegister.class.php');
// Comments
require_once('../Comments/Comment.class.php');
require_once('../Comments/CommentInterface.php');
require_once('../Comments/CommentRegister.class.php');
// Catalogue
require_once('../Catalogue/Catalogue.class.php');
require_once('../Catalogue/CatalogueInterface.class.php');
require_once('../Catalogue/CatalogueRegister.class.php');

// twig
require_once('../../vendor/autoload.php');
$loader = new Twig_Loader_Filesystem('../../templates');
$twig = new Twig_Environment($loader);

// Registers
$fileRegister = new FileRegister(DB::getDBConnection());
$commentRegister = new CommentRegister(DB::getDBConnection());
$catalogueRegister = new CatalogueRegister(DB::getDBConnection());



// Fetch the file
$FileID = 44;//$_SESSION['FileID']; // TODO: NEED TO GET FileID FROM SESSION/POST
$file = $fileRegister->showFile($FileID);

$type = $file->getMimetype();
$size = $file->getSize();
$name = $file->getFilename();
$fileAccess = $file->getAccess();

// Download the file, and check if allowed to download
if (isset($_SESSION['loggedIn']) && $_SESSION['loggedIn']=='yes') {
    // loggedIn -> allow downloading of all files
    alert("File downloading started!");
    header("Content-type: $type");
    header("Content-length: $size");
    header("Content-Disposition: attachment; filename=$name");
    header("Content-Description: PHP Generated Data");
    echo $file->getFile();
} else {
    if ($fileAccess == 0) {
        // not loggedIn -> allow downloading of open files ONLY
        alert("File downloading started!");
        header("Content-type: $type");
        header("Content-length: $size");
        header("Content-Disposition: attachment; filename=$name");
        header("Content-Description: PHP Generated Data");
        echo $file->getFile();
    } else
        alert("You are not logged in, did not download file!");
}

// HTML link -> <a href="download.php">{{ file.getFilename() }}  Størrelse: {{ file.getSizeInMb() }}</a>



function alert($msg) {
    echo "<script type='text/javascript'>alert('$msg');</script>";
}