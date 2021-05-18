<?php

spl_autoload_register(function ($class_name) {
    require_once "php/classes/" . $class_name . '.class.php';
});

//$cCArchive = new CourseCodeArchive($db);
require_once 'vendor/autoload.php';
require_once 'login.php';
@session_start();

/* User And active login variables. */
$loader = new Twig\Loader\FilesystemLoader('templates');

/* User And active login variables. */
$twig = new Twig\Environment($loader);


if (!isset($_SESSION['AC'])) {
    echo $twig->render('logInPage.twig');
} else {
    $AC = $_SESSION['AC'];
    $inboxService = new InboxService(DB::getDBConnection());
    $userId = $_SESSION['bruker']->getIdUser();
    $courseCodeService = new CourseCodeService(DB::getDBConnection());
    $searchService = new Search(DB::getDBConnection());

    if (isset($_POST['searchCode'])) {
        $userSearch = htmlspecialchars($_POST['searchCode']);
        $result = $searchService->getAllCourseCodesWithSearch($userSearch);

        //Get coruses with search word
        echo $twig->render('courseCodeOverview.twig', array(
            'user' => $_SESSION['bruker'],
            'notificationTypes' => $inboxService->getAllInboxTypes(),
            'unreadNotifications' => $inboxService->getTotalAmountOfUnreadMessages($userId),
            'unreadNotificationsWithType' => $inboxService->getAmountOfUnreadMessagesWithType($userId),
            'userRole' => $_SESSION['AC']->getUserRole()->getRoleName(),
            'courseCode' => $result
        ));
    } else {
        echo $twig->render('courseCodeOverview.twig', array(
            'user' => $_SESSION['bruker'],
            'notificationTypes' => $inboxService->getAllInboxTypes(),
            'unreadNotifications' => $inboxService->getTotalAmountOfUnreadMessages($userId),
            'unreadNotificationsWithType' => $inboxService->getAmountOfUnreadMessagesWithType($userId),
            'userRole' => $_SESSION['AC']->getUserRole()->getRoleName(),
            'courseCode' => $courseCodeService->getAllCourseCodes(),
        ));
    }

    if (isset($_POST['logout-submit'])) {
        $_SESSION['bruker']->logOut();
        echo ' <script> window.location.replace("index.php"); </script>'; //TODO: Plz ignore, will be replaced in production version.
    }
}

