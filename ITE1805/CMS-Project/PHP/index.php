
<?php
session_start();
require_once "User/User.class.php";
require_once "../vendor/autoload.php";

$loader = new Twig_Loader_Filesystem('../templates');
$twig = new Twig_Environment($loader, array(
//    'cache' => './compilation_cache',
));

include_once 'File/RecentFiles.php';



if(isset($_SESSION['loggedIn']) && $_SESSION['loggedIn']=='yes' && $_SESSION['clientIp']==$_SERVER['REMOTE_ADDR']){

    echo $twig->render('main.twig', array('links'=>$links, 'index'=>true, 'loggedIn' => true, 'name'=>$_SESSION['name']));

} else {

    unset($_SESSION['loggedIn']);
    unset($_SESSION['id']);
    echo $twig->render('main.twig', array('links'=>$links, 'index'=>true, 'loggedIn' => false));
}
?>