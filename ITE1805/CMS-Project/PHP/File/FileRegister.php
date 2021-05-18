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


// Alert msg of number of files in database, implement this on counter at index.php
alert("Det er " . $fileRegister->countAllFiles() . " filer i tabellen File");


//<editor-fold desc="Show all files">
    /* Display all files depending on access
     * logged in -> show ALL files   and   logged out -> show ONLY files with access=false */
if (isset($_SESSION['loggedIn']) && $_SESSION['loggedIn']=='yes') $access = 1; else $access = 0;
try {
    $files = $fileRegister->showAllFiles($access);
    echo $twig->render('displayFilesExample.twig', array('files' => $files));
} catch (Exception $e) {
    print "Could not show files! " . $e->getMessage() . PHP_EOL;
}
//</editor-fold>

//<editor-fold desc="Show the users file if logged in">
if (isset($_SESSION['loggedIn']) && $_SESSION['loggedIn']=='yes') {
    $UserID = $_SESSION['id'];
    try {
        $files = $fileRegister->showUsersFiles($UserID);
        echo $twig->render('displayFilesExample.twig', array('userFiles' => $files));
    } catch (Exception $e) {
        print "Could not retrieve user files! " . $e->getMessage() . PHP_EOL;
    }
}
//</editor-fold>

//<editor-fold desc="File Uploading">
// Show file upload screen if logged in
if (isset($_SESSION['loggedIn']) && $_SESSION['loggedIn']=='yes') {
    try {
        $catalogues = $catalogueRegister->showAllCatalogues();
        echo $twig->render('fileUpload.twig', array('catalogues' => $catalogues));
    } catch (Exception $e) {
        print "Could not show all catalogues!" . $e->getMessage() . PHP_EOL;
    }
}
// Upload File to database
if (isset($_SESSION['loggedIn']) && $_SESSION['loggedIn']=='yes' && isset($_POST['submit_fileUpload'])) {
    // Med noe hjelp fra https://bytes.com/topic/php/insights/740327-uploading-files-into-mysql-database-using-php
    // Gather data
    $UserID = $_SESSION['id'];
    $blob = $_FILES['uploadedFile']['tmp_name'];
    $name = $_FILES['uploadedFile']['name'];
    $type = $_FILES['uploadedFile']['type'];
    $size = $_FILES['uploadedFile']['size'];
    $access = filter_input(INPUT_POST, 'fileAccess', FILTER_SANITIZE_NUMBER_INT);
    $catalogueID = filter_input(INPUT_POST, 'fileCatalogue', FILTER_SANITIZE_NUMBER_INT);
    $description = filter_input(INPUT_POST, 'fileDescription', FILTER_SANITIZE_STRING);
    $tags = explode(',', filter_input(INPUT_POST, 'fileTages', FILTER_SANITIZE_STRING)); // TODO: Add tags

    if (is_uploaded_file($blob) && $size != 0 && $size <= 16777215) {
        try {
            $data = file_get_contents($blob);
            $file = new file();
            $file->setFile($data);
            $file->setUserID($UserID);
            $file->setAuthor($fileRegister->fetchAuthor($UserID));
            $file->setFilename($name);
            $file->setServerFilename($name);
            $file->setSize($size);
            $file->setMimetype($type);
            $file->setDescription($description);
            $file->setAccess($access);
            $file->setUserUserID($UserID);
            $file->setCatalogueID($catalogueID);
            $file->setCatalogueCatalogueID($catalogueID);
            $fileRegister->addFile($file, $UserID);
        } catch (Exception $e) {
            print $e->getMessage() . PHP_EOL;
        }
    }
}
//</editor-fold>

//<editor-fold desc="File Deletion">
// Delete File from database
if (isset($_SESSION['loggedIn']) && $_SESSION['loggedIn']=='yes') {
    $UserID = $_SESSION['id'];
    try {
        $files = $fileRegister->showUsersFiles($UserID);
        echo $twig->render('displayFilesExample.twig', array('userFilesToDelete' => $files));
    } catch (Exception $e) {
        print "Could not retrieve user files! " . $e->getMessage() . PHP_EOL;
    }
}
if (isset($_POST['submit_fileDelete'])) {
    // Gather UserID and FileID
    $FileID = filter_input(INPUT_POST, 'fileDeleteButton', FILTER_SANITIZE_NUMBER_INT);
    $UserID = $_SESSION['id'];

    // Check if userID is owner of file before delete
    $file = $fileRegister->isFileOwner($FileID, $UserID);
    if ($file->getUserID() == $UserID){
        // Delete certain File
        $fileRegister->deleteFile($FileID);
        // Delete all comments in deleted File
        $commentRegister->deleteAllCommentsFromFile($FileID);
        alert("Fil slettet, filens kommentarer er flyttet til historikken vår :)");
    } else {
        alert("You are not the owner of this file... did not delete file!");
    }
}
//</editor-fold>


function alert($msg) {
    echo "<script type='text/javascript'>alert('$msg');</script>";
}