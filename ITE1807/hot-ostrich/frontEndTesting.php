<?php

spl_autoload_register(function ($class_name) {
    require_once "php/classes/" . $class_name . '.class.php';});

    //$cCArchive = new CourseCodeArchive($db);
    require_once 'vendor/autoload.php';
    require_once 'login.php';
    @session_start();

/* User And active login variables. */
$loader = new Twig\Loader\FilesystemLoader('templates');

/* User And active login variables. */
//$loader = new Twig\Loader\FilesystemLoader('templates');
$twig = new Twig\Environment($loader, array('cache' => false));
//$cCArchive = new CourseCodeArchive($db, $twig);

//
//echo $twig->render('createCourseDescription.twig');
//echo $twig->render('courseDescription.twig');
//echo $twig->render('courseCodeOverview.twig');
//echo $twig->render('courseDescription.twig');
//echo $twig->render('courseCode.twig');
//echo $twig->render('createCourseCode.twig');
echo $twig->render('homepage.twig');
//echo $twig->render('editStatus.twig');
//echo $twig->render('configureRolePermissions.twig');
//echo $twig->render('mineCourses.twig');
//echo $twig->render('coursesToBeApproved.twig');
//echo $twig->render('notApprovedMessage.twig');

