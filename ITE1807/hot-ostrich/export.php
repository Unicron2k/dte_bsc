<?php

require_once 'vendor/autoload.php';
require_once 'login.php';
session_start();


spl_autoload_register(function ($class_name) {
    require_once "php/classes/" . $class_name . '.class.php';
});


$loader = new Twig\Loader\FilesystemLoader('templates');
$twig = new Twig\Environment($loader);

if (isset($_GET['courseId']) && is_numeric($_GET['courseId'])) {
    PDF::getPDF(htmlspecialchars($_GET['courseId']));
} else {
    echo $twig->render('displayMessage.twig', array(
        'message_title' => 'Ugyldig bruk av eksport',
        'message_description' => 'Vennligst oppgi gyldige numeriske parametere.'
    ));
}