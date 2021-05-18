<!--TESTING PURPOSES.... DELETE WHEN FUNCTIONAL-->

<?php

spl_autoload_register(function ($class_name) {
    require_once "php/classes/" . $class_name . '.class.php';});

//$cCArchive = new CourseCodeArchive($db);
require_once 'vendor/autoload.php';
require_once 'login.php';
@session_start();

/* User And active login variables. */
$loader = new Twig\Loader\FilesystemLoader('templates');

try {
    echo $twig->render('editStatus.twig');
}
catch (Exception $e) {
    echo 'Connection failed';

}