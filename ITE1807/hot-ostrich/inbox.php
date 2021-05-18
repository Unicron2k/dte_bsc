<?php

spl_autoload_register(function ($class_name) {
    require_once "php/classes/" . $class_name . '.class.php';
});

require_once 'vendor/autoload.php';
@session_start();

$loader = new Twig\Loader\FilesystemLoader('templates');
$twig = new Twig\Environment($loader);


if (isset($_SESSION['AC'])) {
    $AC = $_SESSION['AC'];
    if ($AC->Access_InboxIndividual()) {
        $inboxService = new InboxService(DB::getDBConnection());
        $userId = $_SESSION['bruker']->getIdUser();

            if (isset($_GET['id']) && is_numeric($_GET['id'])) {
                $message = $inboxService->getMessage($_GET['id'], $userId);
                if (!$message->isOpened()) {
                    $inboxService->openMessage($message->getId(), $userId);
                }
                echo $twig->render('inboxItem.twig', array(
                    'user' => $_SESSION['bruker'],
                    'notificationTypes' => $inboxService->getAllInboxTypes(),
                    'unreadNotifications' => $inboxService->getTotalAmountOfUnreadMessages($userId),
                    'unreadNotificationsWithType' => $inboxService->getAmountOfUnreadMessagesWithType($userId),
                    'userRole' => $_SESSION['AC']->getUserRole()->getRoleName(),
                    'message' => $message
                ));
            } else {
                echo $twig->render('inbox.twig', array(
                    'user' => $_SESSION['bruker'],
                    'notificationTypes' => $inboxService->getAllInboxTypes(),
                    'unreadNotifications' => $inboxService->getTotalAmountOfUnreadMessages($userId),
                    'unreadNotificationsWithType' => $inboxService->getAmountOfUnreadMessagesWithType($userId),
                    'userRole' => $_SESSION['AC']->getUserRole()->getRoleName(),
                    'messages' => $inboxService->getAllMessages($userId),
                ));
            }
    } else {
        echo $twig->render('displayMessage.twig', array(
            'message_title' => 'Ingen tilgang',
            'message_description' => 'Du har ikke tilgang til denne ressursen. Vennligst kontakt administrator.'
        ));
    }
} else {
    echo $twig->render('displayMessage.twig', array(
        'message_title' => 'Ingen tilgang',
        'message_description' => 'Du har ikke tilgang til denne ressursen. Vennligst kontakt administrator.'
    ));
}