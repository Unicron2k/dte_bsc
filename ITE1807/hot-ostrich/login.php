<?php

use Twig\Error\LoaderError;
use Twig\Error\RuntimeError;
use Twig\Error\SyntaxError;

spl_autoload_register(function ($class_name) {require "php/classes/" . $class_name . '.class.php';});


require_once 'vendor/autoload.php';
$loader = new Twig\Loader\FilesystemLoader('templates');

// TODO: UNCOMMENT BEFORE PRODUCTION!!!
//$twig = new Twig\Environment($loader, array('cache' => './compilation_cache'));
// TODO: REMOVE BEFORE PRODUCTION!!!
$twig = new Twig\Environment($loader, array('cache' => false));
$twig->addExtension(new \Twig\Extension\DebugExtension());

$registerUser = new Register(DB::getDBConnection());


if (!isset($_SESSION['bruker'])) {
    if (isset($_POST['login'])) {
        $user = new UserService(DB::getDBConnection());
        try {
            $_SESSION['bruker'] = $user->login(DB::getDBConnection(), $_POST['usernameEmail'], $_POST['password']);

            if(is_null($_SESSION['bruker'])){
                $loginstate = false;
            }else{
                $loginstate = $_SESSION['bruker']->isLoggedIn();
            }

            if ($loginstate == true){
                $RoleControl = new RoleControlService(DB::getDBConnection());
                //$_SESSION['AC'] = new UserAccessControl($RoleControl->getUserRole(6));
                $_SESSION['AC'] = new UserAccessControl($RoleControl->getUserRole($_SESSION['bruker']->getRoleIdRole()));
                header("Refresh:0");}

        } catch (InvalidArgumentException $e) {
            //TODO: Move to error.twig or return it in getLastError?
            print $e->getMessage() . PHP_EOL;
        }
    }
}

    if (isset($_POST['register'])) {
        $username = filter_input(INPUT_POST, 'username', FILTER_SANITIZE_STRING);
        $password = filter_input(INPUT_POST, 'password', FILTER_SANITIZE_STRING);
        $confirm_password = filter_input(INPUT_POST, 'confirm_password', FILTER_SANITIZE_STRING);
        $email = filter_input(INPUT_POST, 'email', FILTER_SANITIZE_STRING);
        $firstname = filter_input(INPUT_POST, 'firstname', FILTER_SANITIZE_STRING);
        $lastname = filter_input(INPUT_POST, 'lastname', FILTER_SANITIZE_STRING);
        if ($_POST['password'] != $_POST['confirm_password']) echo '<p class="alert-danger">Passwords does not match</p>';
        if ($_POST['password'] == $_POST['confirm_password']) {
            //$validationToken = md5(uniqid(rand(), 1));

            $user = new User;
            $user->setFirstname($firstname);
            $user->setLastname($lastname);
            $user->setUsername($username);
            $user->setEmail($email);
            $registerUser->register($user, $password);

        } else {
            try {
                echo $twig->render('logInPage.twig', array());
            } catch (LoaderError $e) {
                print("Loader error logging inn");
            } catch (RuntimeError $e) {
                print("Runtime error logging inn");
            } catch (SyntaxError $e) {
                print("Syntax error logging inn");
            }
            exit;

        }
    }


?>
