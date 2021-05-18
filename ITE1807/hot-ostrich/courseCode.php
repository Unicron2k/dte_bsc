<?php

require_once 'vendor/autoload.php';
require_once 'login.php';
session_start();

spl_autoload_register(function ($class_name) {
    require_once "php/classes/" . $class_name . '.class.php';
});


$loader = new Twig\Loader\FilesystemLoader('templates');
$twig = new Twig\Environment($loader);

$degree_service = new DegreeService(DB::getDBConnection());
$study_point_service = new StudyPointsService(DB::getDBConnection());
$course_code_service = new CourseCodeService(DB::getDBConnection());
$user_service = new UserService(DB::getDBConnection());
$course_leader_service = new CourseLeaderService(DB::getDBConnection());


if (isset($_SESSION['AC'])) {
    $AC = $_SESSION['AC'];
    $inboxService = new InboxService(DB::getDBConnection());
    $userId = $_SESSION['bruker']->getIdUser();

    if ($AC->Access_ReadCourseCode()) {

        if (isset($_GET['id'])) {
            if (is_numeric($_GET['id'])) {
                $id = htmlspecialchars($_GET['id']);
                $course_code = $course_code_service->getCourseCode($id);

                echo $twig->render('courseCode.twig', array(
                    'user' => $_SESSION['bruker'],
                    'course_code' => $course_code,
                    'degrees' => $degree_service->getAllDegrees(),
                    'all_study_points' => $study_point_service->getAllStudyPoints(),
                    'users' => $user_service->getAllUsers(),
                    'course_leader' => $course_leader_service->getCourseLeaderByCourseCode($id),
                    'course_descriptions' => $course_code_service->getRelatedCourseDescriptions($id),
                    'notificationTypes' => $inboxService->getAllInboxTypes(),
                    'unreadNotifications' => $inboxService->getTotalAmountOfUnreadMessages($userId),
                    'unreadNotificationsWithType' => $inboxService->getAmountOfUnreadMessagesWithType($userId),
                    'userRole' => $_SESSION['AC']->getUserRole()->getRoleName()
                ));
            } else {
                echo $twig->render('displayMessage.twig', array(
                    'user' => $_SESSION['bruker'],
                    'notificationTypes' => $inboxService->getAllInboxTypes(),
                    'unreadNotifications' => $inboxService->getTotalAmountOfUnreadMessages($userId),
                    'unreadNotificationsWithType' => $inboxService->getAmountOfUnreadMessagesWithType($userId),
                    'userRole' => $_SESSION['AC']->getUserRole()->getRoleName(),
                    'message_title' => 'Feil!',
                    'message_description' => 'Emnekodens id må være numerisk.'
                ));
            }
        } else {
            echo $twig->render('displayMessage.twig', array(
                'user' => $_SESSION['bruker'],
                'notificationTypes' => $inboxService->getAllInboxTypes(),
                'unreadNotifications' => $inboxService->getTotalAmountOfUnreadMessages($userId),
                'unreadNotificationsWithType' => $inboxService->getAmountOfUnreadMessagesWithType($userId),
                'userRole' => $_SESSION['AC']->getUserRole()->getRoleName(),
                'message_title' => "Feil!",
                'message_description' => "For å vise en emnekode må være pålogget med riktig tilgang og oppgi en ønsket id som parameter i URL."
            ));
        }
    } else {
        // No rights to create a courseDescription, display error-message
        echo $twig->render('displayMessage.twig', array(
            'user' => $_SESSION['bruker'],
            'notificationTypes' => $inboxService->getAllInboxTypes(),
            'unreadNotifications' => $inboxService->getTotalAmountOfUnreadMessages($userId),
            'unreadNotificationsWithType' => $inboxService->getAmountOfUnreadMessagesWithType($userId),
            'userRole' => $_SESSION['AC']->getUserRole()->getRoleName(),
            'message_title' => 'Ingen tilgang',
            'message_description' => 'Du har ikke tilgang til denne ressursen. Vennligst kontakt administrator.'
        ));
    }
    //
} else {
    echo $twig->render('displayMessage.twig', array(
        'message_title' => 'Ingen tilgang',
        'message_description' => 'Du har ikke tilgang til denne ressursen. Vennligst kontakt administrator.'
    ));
}