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




if(isset($_SESSION['loggedIn']) && $_SESSION['loggedIn']=='yes' && isset($_POST['submit_comment'])) {
    // Gather date from submitted Comment
    $UserID = $_SESSION['id'];

    $Date = date("Y-m-d H:i:s");
    $commentContent = filter_input(INPUT_POST, 'commentContent', FILTER_SANITIZE_STRING);
    $FileID = $_POST['hiddenFileID']; // TODO: Need to find FileID for the File to comment on

    // Make Comment object
    $comment = new comment();
    $comment->setFileID($FileID); // TODO get file id
    $comment->setUserID($UserID);
    $comment->setDate($Date);
    $comment->setComment($commentContent);

    // Pass the object to $commentRegister to add it to the database
    $commentRegister->addComment($comment);

} elseif (isset($_SESSION['loggedIn']) && $_SESSION['loggedIn']=='yes' && isset($_POST['submit_deleteComment'])) {
    // Gather data from comment to delete
    $UserID = $_SESSION['id'];

    $CommentID = $_GET['fa'];


    // Make Comment object
    $comment = new comment();
    $comment->setCommentID($CommentID);

    // Delete the comment from database
    $commentRegister->deleteComment($CommentID);
}