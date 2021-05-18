<?php
session_start();
require_once "DB.class.php";
require_once "User/User.class.php";
require_once '../vendor/autoload.php';

$loader = new Twig_Loader_Filesystem('../templates');
$twig = new Twig_Environment($loader, array(
//    'cache' => './compilation_cache',
));

include_once 'File/RecentFiles.php';

$db = DB::getDBConnection();

if ($db==null) {
    echo $twig->render('main.twig', array('links'=>$links, 'error'=>true, 'msg' => 'Unable to connect to the database!'));
    die();  // Abort further execution of the script
}

$user = new User();
$status=$user->checkLogin($db);



if(isset($_SESSION['loggedIn']) && $_SESSION['loggedIn']=='yes' && isset($_SESSION['clientIp']) && $_SESSION['clientIp']==$_SERVER['REMOTE_ADDR'] && !isset($_GET['action'])){
    echo $twig->render('main.twig', array('links'=>$links, 'login'=>true, 'loggedIn' => true, 'name'=>$_SESSION['name']));

}

if (isset($_GET['action']) && $_GET['action'] == 'logout') {
    if ($user->loggedIn()) {
        $user->logOut();
        session_destroy();
        header( "refresh:5; url=index.php" );
        echo $twig->render('main.twig', array('links'=>$links, 'login'=>true, 'loggedOut' => true));
        exit();
    } else {
        echo $twig->render('main.twig', array('links'=>$links, 'login'=>true, 'notLoggedIn' => true));
        exit();
    }
} else {
    if ($user->loggedIn()) {
        if (!isset($_SESSION['clientIp'])) {
            $_SESSION['clientIp'] = $_SERVER['REMOTE_ADDR'];
        } else {
            $_SESSION['clientIp'] = $_SERVER['REMOTE_ADDR'];
        }
        header( "refresh:5; url=index.php" );
        echo $twig->render('main.twig', array('links'=>$links, 'login'=>true, 'loginSuccess' => true));
    } else {
        //Not logged in, show log-in page
        echo $twig->render('main.twig', array('links' => $links, 'login' => true, 'loggedIn' => false, 'status' => $status));
    }
}