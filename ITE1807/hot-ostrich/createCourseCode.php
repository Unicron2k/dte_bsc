<?php
/* Prerequisites */
spl_autoload_register(function ($class_name) {
    require_once "php/classes/" . $class_name . '.class.php';
});
require_once 'vendor/autoload.php';
@session_start();

/* User And active login variables. */
$loader = new Twig\Loader\FilesystemLoader('templates');
$twig = new Twig\Environment($loader, array('cache' => false));

if (isset($_SESSION['AC'])) {
    $AC = $_SESSION['AC'];
    $inboxService = new InboxService(DB::getDBConnection());
    $userId = $_SESSION['bruker']->getIdUser();

    if ($AC->Access_CourseCode()) {
        $allDegrees = (new DegreeService(DB::getDBConnection()))->getAllDegrees();
        $allStudyPoints = (new StudyPointsService(DB::getDBConnection()))->getAllStudyPoints();
        $allCourseLeaders = (new CourseLeaderService(DB::getDBConnection()))->getAllCourseLeaders();
        $allCourse = (new CourseCodeService(DB::getDBConnection()))->getAllCourseCodes();
        $allUser = (new UserService(DB::getDBConnection()))->getAllUsers();

        if (isset($_POST['submitCourseCode'])) {
            $courseCode = $_POST['course_code'];
            $course_name_nb_no = $_POST['course_name_nb_no'];
            $course_name_nn_no = $_POST['course_name_nn_no'];
            $course_name_en_gb = $_POST['course_name_en_gb'];
            $degree = $_POST['degree'];
            $study_point = $_POST['study_point'];
            $course_leader = $_POST['course_leader'];

            $courseCodeService = (new CourseCodeService(DB::getDBConnection()));
            $courseLeaderService = (new CourseLeaderService(DB::getDBConnection()));
            $courseCodeService->addCourseCode($courseCode, $course_name_nb_no, $course_name_nn_no, $course_name_en_gb, $degree, $study_point, $course_leader);

            echo $twig->render('courseCodeOverview.twig', array(
                'user' => $_SESSION['bruker'],
                'notificationTypes' => $inboxService->getAllInboxTypes(),
                'unreadNotifications' => $inboxService->getTotalAmountOfUnreadMessages($userId),
                'unreadNotificationsWithType' => $inboxService->getAmountOfUnreadMessagesWithType($userId),
                'userRole' => $_SESSION['AC']->getUserRole()->getRoleName(),
                'courseCode' => $courseCodeService->getAllCourseCodes()
            ));
        } else {
            echo $twig->render('createCourseCode.twig', array(
                'user' => $_SESSION['bruker'],
                'notificationTypes' => $inboxService->getAllInboxTypes(),
                'unreadNotifications' => $inboxService->getTotalAmountOfUnreadMessages($userId),
                'unreadNotificationsWithType' => $inboxService->getAmountOfUnreadMessagesWithType($userId),
                'userRole' => $_SESSION['AC']->getUserRole()->getRoleName(),
                'degrees' => $allDegrees,
                'allStudyPoints' => $allStudyPoints,
                'all_user' => $allUser,
                'course_code' => $allCourse
            ));
        }
    } else {
        echo $twig->render('displayMessage.twig', array(
            'user' => $_SESSION['bruker'],
            'notificationTypes' => $inboxService->getAllInboxTypes(),
            'unreadNotifications' => $inboxService->getTotalAmountOfUnreadMessages($userId),
            'unreadNotificationsWithType' => $inboxService->getAmountOfUnreadMessagesWithType($userId),
            'userRole' => $_SESSION['AC']->getUserRole()->getRoleName(),
            'message_title' => 'Ingen tilgang',
            'message_description' => 'Du har ikke tilgang til denne ressursen. Vennligst kontakt administrator.'
        ));
    }
} else {
    // No rights to create a courseDescription, display error-message
    require_once 'login.php';
    echo $twig->render('logInPage.twig');
}

