<?php
/* Prerequisites */

spl_autoload_register(function ($class_name) {
    require_once "php/classes/" . $class_name . '.class.php';
});

require_once 'vendor/autoload.php';
@session_start();

/* User And active login variables. */
$loader = new Twig\Loader\FilesystemLoader('templates');
$twig = new Twig\Environment($loader, array('cache' => false));


if (!isset($_SESSION['bruker'])) {
    require_once 'login.php';
    echo $twig->render('logInPage.twig');

} else {
    $_SESSION['bruker']->logOut();
    echo ' <script> window.location.replace("index.php"); </script>';
}
