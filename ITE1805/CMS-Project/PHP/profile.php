<?php
session_start();
require_once "DB.class.php";
require_once "User/User.class.php";
require_once "../vendor/autoload.php";
require_once "User/UsersInterface.class.php";
require_once "User/Users.class.php";

$loader = new Twig_Loader_Filesystem('../templates');
$twig = new Twig_Environment($loader, array(
//    'cache' => './compilation_cache',
));

include_once "File/RecentFiles.php";

if(isset($_SESSION['loggedIn']) && $_SESSION['loggedIn']=='yes' && $_SESSION['clientIp']==$_SERVER['REMOTE_ADDR']){
    $users=new Users(DB::getDBConnection());

    $id= $_SESSION['id'];
    $user=$users->showUser($id);

    echo $twig->render('main.twig', array('links'=>$links, 'user'=>$user, 'profile'=>true, 'loggedIn' => true, 'name'=>$_SESSION['name']));

} else {

    echo $twig->render('main.twig', array('links'=>$links, 'profile'=>true, 'loggedIn' => false, 'name'=>$_SESSION['name']));
}