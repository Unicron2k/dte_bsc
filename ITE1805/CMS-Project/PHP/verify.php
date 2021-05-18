<?php
require_once 'DB.class.php';
require_once('User/UsersInterface.class.php');
require_once 'User/Users.class.php';
require_once '../vendor/autoload.php';

$loader = new Twig_Loader_Filesystem('../templates');
$twig = new Twig_Environment($loader, array(
//    'cache' => './compilation_cache',
));

include_once 'File/RecentFiles.php';

if(isset($_GET['key']) && !empty($_GET['key'])){

    $users = new Users(DB::getDBConnection());
    if($users->verifyUser($_GET['key'])){
        echo $twig->render('main.twig', array('links'=>$links, 'verify'=> true, 'verified' => true));
    } else {
        echo $twig->render('main.twig', array('links'=>$links, 'verify'=> true, 'verified' => false));
    }
} else {
    echo $twig->render('main.twig', array('links'=>$links, 'verify'=> true, 'noVerify' => true));
}