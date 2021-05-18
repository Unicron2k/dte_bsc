<?php

spl_autoload_register(function ($class_name) {
    require_once "php/classes/" . $class_name . '.class.php';});

//$cCArchive = new CourseCodeArchive($db);
require_once 'vendor/autoload.php';
require_once 'login.php';
@session_start();

/* User And active login variables. */
$loader = new Twig\Loader\FilesystemLoader('templates');


if(!isset($_SESSION['bruker'])) {
    echo $twig->render('logInPage.twig');
}

/* User related verification/check -> If bruker session is created
   The user should be able to load php functions hidden behind the login. */

// TODO Implement backend features after login.


else {//if($_SESSION['bruker']->isLoggedIn()){           //The Else represent runtime state after login, could be replaced by a if since it's not optimal to have the homepage as a fallback option if the session is reset after login.


    echo $twig->render('createCourseDescription.twig', array('user' => $_SESSION['bruker']));

    if (isset($_POST['changeToMineCourse'])){
    }

    if (isset($_POST['changeToCreateCourseCode'])) {
    }

    if (isset($_POST['changeToMail'])) {
    }

    if (isset($_POST['changeToAllCourseCodes'])){
    }

    if (isset($_POST['logout-submit'])){
        $_SESSION['bruker']->logOut();
        echo' <script> window.location.replace("index.php"); </script>'; //TODO: Plz ignore, will be replaced in production version.
    }

}

