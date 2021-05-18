<?php
/* Prerequisites */

spl_autoload_register(function ($class_name) {
    require_once "php/classes/" . $class_name . '.class.php';
});

require_once 'vendor/autoload.php';
error_reporting(0);
@session_start();

/* User And active login variables. */
$loader = new Twig\Loader\FilesystemLoader('templates');


// TODO: UNCOMMENT BEFORE PRODUCTION!!!
//$twig = new Twig\Environment($loader, array('cache' => './compilation_cache'));
// TODO: REMOVE BEFORE PRODUCTION!!!
$twig = new Twig\Environment($loader, array('cache' => false));


if(!isset($_SESSION['bruker'])) {
    require_once 'login.php';
    echo $twig->render('logInPage.twig');

    /* User related verification/check -> If bruker session is created
       The user should be able to load php functions hidden behind the login. */
    // TODO: Implement backend features after login, Using a AccessControl.
} else {
        $inboxService = new InboxService(DB::getDBConnection());
        $userId = $_SESSION['bruker']->getIdUser();

        echo $twig->render('homepage.twig', array(
            'user' => $_SESSION['bruker'],
            'notificationTypes' => $inboxService->getAllInboxTypes(),
            'unreadNotifications' => $inboxService->getTotalAmountOfUnreadMessages($userId),
            'unreadNotificationsWithType' => $inboxService->getAmountOfUnreadMessagesWithType($userId),
            'userRole' => $_SESSION['AC']->getUserRole()->getRoleName()
        ));


    if (isset($_POST['logout-submit'])) {
        $_SESSION['bruker']->logOut();
        echo ' <script> window.location.replace("index.php"); </script>'; //TODO: Plz ignore, will be replaced in production version.
    }
}

