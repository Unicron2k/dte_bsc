<?php

spl_autoload_register(function ($class_name) {
    require_once "php/classes/" . $class_name . '.class.php';
});
require_once 'vendor/autoload.php';
session_start();


$loader = new Twig\Loader\FilesystemLoader('templates');
$twig = new Twig\Environment($loader);

if ($_SESSION['AC']->Access_Administrator()) {
    if (isset($_SESSION['bruker'])) {
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
            echo $twig->render('configureRolePermissions.twig', array('roles' => $roleControlService->getAllRoles(), 'facultys' => $fakultetService->getAllFakultet()));
        } else {
            echo "error";
        }
    }
} else {
    // No rights to create a courseDescription, display error-message
    echo $twig->render('configureRolePermissions.twig', array(
        'user' => $_SESSION['bruker'],
        'insufficientRights' => true
    ));
}