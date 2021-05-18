<?php
spl_autoload_register(function ($class_name) {
    require_once "php/classes/" . $class_name . '.class.php';
});
require_once 'vendor/autoload.php';
session_start();


$loader = new Twig\Loader\FilesystemLoader('templates');
$twig = new Twig\Environment($loader);


if (isset($_SESSION['AC'])) {
    $AC = $_SESSION['AC'];
    $inboxService = new InboxService(DB::getDBConnection());
    $userId = $_SESSION['bruker']->getIdUser();

    if ($AC->Access_Administrator()) {
        $AccessControlStart_variable = null;
        $roleControlService = new RoleControlService(DB::getDBConnection());
        $fakultetService = new FakultetService(DB::getDBConnection());

        $UserRole = $roleControlService->getUserRole($_SESSION['bruker']->getRoleIdRole());
        //TODO: Put this behind the accesscontrol instead of the UserRoleId for better security.

        if (isset($_POST['roles'])) {
            $arrNewPermissions = array();
            for ($i = 1; $i < sizeof($_POST['roles']) + 1; $i++) {
                //TODO: proper error checking
                $arrNewPermissions[$i][] = isset($_POST['roles'][$i]['read']) ? intval($_POST['roles'][$i]['read']) : 0;
                $arrNewPermissions[$i][] = isset($_POST['roles'][$i]['write']) ? intval($_POST['roles'][$i]['write']) : 0;
                $arrNewPermissions[$i][] = isset($_POST['roles'][$i]['edit']) ? intval($_POST['roles'][$i]['edit']) : 0;
                $arrNewPermissions[$i][] = isset($_POST['roles'][$i]['delete']) ? intval($_POST['roles'][$i]['delete']) : 0;
                $arrNewPermissions[$i][] = isset($_POST['roles'][$i]['create']) ? intval($_POST['roles'][$i]['create']) : 0;
                $arrNewPermissions[$i][] = isset($_POST['roles'][$i]['approve']) ? intval($_POST['roles'][$i]['approve']) : 0;
                $arrNewPermissions[$i][] = $i;
            }
            $roleControlService->updateAllRoles($arrNewPermissions);
        }

        if ($UserRole->getIdRole() === 1) {
            echo $twig->render('configureRolePermissions.twig', array(
                'user' => $_SESSION['bruker'],
                'notificationTypes' => $inboxService->getAllInboxTypes(),
                'unreadNotifications' => $inboxService->getTotalAmountOfUnreadMessages($userId),
                'unreadNotificationsWithType' => $inboxService->getAmountOfUnreadMessagesWithType($userId),
                'userRole' => $_SESSION['AC']->getUserRole()->getRoleName(),
                'roles' => $roleControlService->getAllRoles(), 'facultys' => $fakultetService->getAllFakultet()));
        } else {
            echo "error";
        }
    } else {
        // No rights to create a courseDescription, display error-message
        echo $twig->render('configureRolePermissions.twig', array(
            'user' => $_SESSION['bruker'],
            'notificationTypes' => $inboxService->getAllInboxTypes(),
            'unreadNotifications' => $inboxService->getTotalAmountOfUnreadMessages($userId),
            'unreadNotificationsWithType' => $inboxService->getAmountOfUnreadMessagesWithType($userId),
            'userRole' => $_SESSION['AC']->getUserRole()->getRoleName(),
            'insufficientRights' => true
        ));
    }
} else {
    echo $twig->render('displayMessage.twig', array(
        'message_title' => 'Ingen tilgang',
        'message_description' => 'Du har ikke tilgang til denne ressursen. Vennligst kontakt administrator.'
    ));
}