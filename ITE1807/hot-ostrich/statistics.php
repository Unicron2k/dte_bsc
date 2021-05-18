<?php

require_once 'vendor/autoload.php';
require_once 'login.php';
session_start();


spl_autoload_register(function ($class_name) {
    require_once "php/classes/" . $class_name . '.class.php';
});


$loader = new Twig\Loader\FilesystemLoader('templates');
$twig = new Twig\Environment($loader);

if (isset($_SESSION['AC'])) {
    $AC = $_SESSION['AC'];
    $inboxService = new InboxService(DB::getDBConnection());
    $userId = $_SESSION['bruker']->getIdUser();


    if ($_SESSION['AC']->Access_Statistics()) {
        if (!isset($_SESSION['bruker'])) {
            echo $twig->render('logInPage.twig');
        } else {

            $statistics = new Statistics(DB::getDBConnection());
            $approval = new ApprovalService(DB::getDBConnection());

            echo $twig->render('statistics.twig', array(
                'user' => $_SESSION['bruker'],
                'notificationTypes' => $inboxService->getAllInboxTypes(),
                'unreadNotifications' => $inboxService->getTotalAmountOfUnreadMessages($userId),
                'unreadNotificationsWithType' => $inboxService->getAmountOfUnreadMessagesWithType($userId),
                'userRole' => $_SESSION['AC']->getUserRole()->getRoleName(),
                'desc_amount' => $statistics->getAmountOfCourseDescriptions(),
                // earliest_version needs extra work. should be able to choose which course code you want to make the query for
                'earliest_version' => $statistics->getEarliestVersionOfCourseDesc(1),
                'pending_approval' => $statistics->getCourseDescriptionsPendingApproval(),
                'approved_amount' => $approval->getNumApproved(),
                'not_approved_amount' => $approval->getNumNonApproved()

            ));
        }
        if (isset($_POST['logout-submit'])) {
            $_SESSION['bruker']->logOut();
            echo ' <script> window.location.replace("index.php"); </script>'; //TODO: Plz ignore, will be replaced in production version.
        }
    } else {
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
} else {
    // No rights to create a courseDescription, display error-message
    echo $twig->render('displayMessage.twig', array(
        'message_title' => 'Ingen tilgang',
        'message_description' => 'Du har ikke tilgang til denne ressursen. Vennligst kontakt administrator.'
    ));
}