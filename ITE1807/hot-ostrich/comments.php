<?php
spl_autoload_register(function ($class_name) {
    require_once "php/classes/" . $class_name . '.class.php';
});
require_once 'vendor/autoload.php';
@session_start();

$loader = new Twig\Loader\FilesystemLoader('templates');
$twig = new Twig\Environment($loader);

$AC = $_SESSION['AC'];

if ($AC->Access_Comments()) {
    $commentsService = new CommentsService(DB::getDBConnection());

    if (isset($_POST['post_comment'])) {
        $title = $_POST['comment_title'];
        $description = $_POST['comment_description'];
        $courseDescId = $_POST['course_description_id'];
        $userId = $_SESSION['bruker']->getIdUser();

        if ($commentsService->addComment($userId, $courseDescId, $title, $description)) {
            echo $twig->render('displayMessage.twig', array(
                'message_title' => "Suksess!",
                'message_description' => 'Din kommentar har blitt postet på emnebeskrivelsen.<br><a href="courseDescription.php?id=' . $courseDescId . '"><- Tilbake til emnebeskrivelsen</a>'

            ));
        } else {
            echo $twig->render('displayMessage.twig', array(
                'message_title' => "Feil!",
                'message_description' => "Kunne ikke legge til kommentar."
            ));
        }

    } else if (isset($_POST['edit_comment'])) {
        /**
         * TODO: Currently not supported by backend.
         */
    } else if (isset($_POST['delete_comment'])) {
        $status = false;
        $commentId = $_POST['comment_id'];
        $userId = $_SESSION['bruker']->getIdUser();

        $comment = $commentsService->getComment($commentId);
        if (!is_null($comment)) {
            if ($AC->Access_Administrator() || $userId == $comment->getUserIdUser()) {
                if ($commentsService->deleteEntry($commentId)) {
                    $status = true;
                }
            }
        }

        $courseDescId = $_POST['course_description_id'];
        if ($status) {
            echo $twig->render('displayMessage.twig', array(
                'message_title' => "Suksess!",
                'message_description' => 'Kommentar slettet.<br><a href="courseDescription.php?id=' . $courseDescId . '"><- Tilbake til emnebeskrivelsen</a>'
            ));
        } else {
            echo $twig->render('displayMessage.twig', array(
                'message_title' => "Feil!",
                'message_description' => 'Det er kun kommentarens forfatter og administrator som kan slette en kommentar.<br><a href="courseDescription.php?id=' . $courseDescId . '"><- Tilbake til emnebeskrivelsen</a>'

            ));
        }
    } else {
        echo $twig->render('displayMessage.twig', array(
            'message_title' => "Feil!",
            'message_description' => "Feil bruk av ressurs."
        ));
    }
} else {
    echo $twig->render('displayMessage.twig', array(
        'message_title' => "Feil!",
        'message_description' => "Du har ikke tilgang til oppretting eller modifisering av kommentarer."
    ));
}