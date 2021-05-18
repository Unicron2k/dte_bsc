<?php

session_start();
require_once('../DB.class.php');
// File
require_once('../File/File.class.php');
require_once('../File/FileInterface.php');
require_once('../File/FileRegister.class.php');
// Comments
require_once('../Comments/Comment.class.php');
require_once('../Comments/CommentInterface.php');
require_once('../Comments/CommentRegister.class.php');
// Catalogue
require_once('Catalogue.class.php');
require_once('CatalogueInterface.class.php');
require_once('CatalogueRegister.class.php');

// twig
require_once('../../vendor/autoload.php');
$loader = new Twig_Loader_Filesystem('../../templates');
$twig = new Twig_Environment($loader);

// Registers
$fileRegister = new FileRegister(DB::getDBConnection());
$commentRegister = new CommentRegister(DB::getDBConnection());
$catalogueRegister = new CatalogueRegister(DB::getDBConnection());


// Show add Catalogue form if logged in
if (isset($_SESSION['loggedIn']) && $_SESSION['loggedIn']=='yes') {
    try {
        $catalogues = $catalogueRegister->showAllCatalogues();
        echo $twig->render('displayCatalogueScheme.twig', array('catalogues' => $catalogues));
    } catch (Exception $e) {
        print $e->getMessage() . PHP_EOL;
    }
}
if (isset($_SESSION['loggedIn']) && $_SESSION['loggedIn']=='yes' && $_SESSION['clientIp']==$_SERVER['REMOTE_ADDR'] && isset($_POST['submit_catalogueUpload'])) {
    //Gather data
    $Name = filter_input(INPUT_POST, 'catalogueName', FILTER_SANITIZE_STRING);
    $Catalogue_CatalogueID = filter_input(INPUT_POST, 'Catalogue_CatalogueID', FILTER_SANITIZE_NUMBER_INT);

    if ($Catalogue_CatalogueID == 0) {
        // Add sub catalogue
        $catalogueRegister->addCatalogue($Name, 0);
    } else {
        // Add master catalogue
        $catalogueRegister->addCatalogue($Name, $Catalogue_CatalogueID);
    }

}




function alert($msg) {
    echo "<script type='text/javascript'>alert('$msg');</script>";
}