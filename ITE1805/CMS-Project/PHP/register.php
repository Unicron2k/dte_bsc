<?php
session_start();
require_once('DB.class.php');
require_once('User/User.class.php');
require_once('User/UsersInterface.class.php');
require_once('User/Users.class.php');
require_once('mailVerify.php');
require_once '../vendor/autoload.php'; // ../ For relativ path

$loader = new Twig_Loader_Filesystem('../templates'); // ../?
$twig = new Twig_Environment($loader);
$users = new Users(DB::getDBConnection());
include_once 'File/RecentFiles.php';
if(isset($_SESSION['loggedIn']) && $_SESSION['loggedIn']=='yes' && $_SESSION['clientIp']==$_SERVER['REMOTE_ADDR']){
    echo $twig->render('main.twig', array('links'=>$links, 'register'=>true, 'loggedIn' => true, 'name'=>$_SESSION['name']));

} else if (isset($_POST['submit_signup'])) {
    if(isset($_POST['psw']) && isset($_POST['psw-repeat']) && $_POST['psw'] == $_POST['psw-repeat']) {

        $username = filter_input(INPUT_POST, 'username', FILTER_SANITIZE_STRING);
        $email = filter_input(INPUT_POST, 'email', FILTER_SANITIZE_EMAIL);
        $firstName = filter_input(INPUT_POST, 'firstName', FILTER_SANITIZE_STRING);
        $lastName = filter_input(INPUT_POST, 'lastName', FILTER_SANITIZE_STRING);
        $passHash = password_hash($_POST['psw'], PASSWORD_DEFAULT);
        $key = md5(uniqid(rand(), 1)).md5(uniqid(rand(), 1));

        $user = User::createUser($username, $email, $firstName, $lastName);
        $user->setPassHash($passHash);
        $user->setVerificationKey($key);

        $userExists=false;
        if($users->showUserByName($username)){
            $userExists=true;
        }

        if(!$userExists &&$users->addUser($user)) {
            sendVerificationMail($user->getEmail(), $user->getVerificationKey());
            header( "refresh:5; url=index.php" );
            echo $twig->render('main.twig', array('links'=>$links, 'register'=>true, 'POST'=>$_POST, 'registerSuccess' => true));

        } else {
            echo $twig->render('main.twig', array('links'=>$links, 'register'=>true, 'POST'=>$_POST, 'userExists'=>$userExists, 'registerSuccess' => false));
        }

    } else {
        echo $twig->render('main.twig', array('links'=>$links, 'register'=>true, 'passNoMatch' => true, 'POST'=>$_POST, 'registerSuccess' => false));
    }
} elseif (isset($_POST['cancel_signup'])) {
    // Redirect back to index page...
    header('index.php');
    // Exit current script
    die();
} else {
    echo $twig->render('main.twig', array('links'=>$links, 'register'=>true, 'loggedIn' => false, 'POST'=>$_POST, 'registerSuccess' => false));
}
?>