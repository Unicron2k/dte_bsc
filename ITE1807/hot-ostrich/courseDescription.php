<?php

require_once 'vendor/autoload.php';
require_once 'login.php';
session_start();


spl_autoload_register(function ($class_name) {
    require_once "php/classes/" . $class_name . '.class.php';
});


$loader = new Twig\Loader\FilesystemLoader('templates');
$twig = new Twig\Environment($loader);

if (isset($_SESSION['AC'])) {
    $AC = $_SESSION['AC'];
    $inboxService = new InboxService(DB::getDBConnection());
    $userId = $_SESSION['bruker']->getIdUser();

    if ($AC->Access_PreviewCourseDescription()) {
        if (isset($_GET['id']) && is_numeric($_GET['id'])) {
            $courseCodeService = new CourseCodeService(DB::getDBConnection());
            $courseDescService = new CourseDescriptionService(DB::getDBConnection());
            $languageService = new LanguageService(DB::getDBConnection());
            $teachingService = new TeachingLocationService(DB::getDBConnection());
            $examService = new ExamTypeService(DB::getDBConnection());
            $gradeService = new GradeScaleService(DB::getDBConnection());
            $onlineOffersService = new OffersOnlineStudentsService(DB::getDBConnection());
            $academicContentService = new AcademicContentService(DB::getDBConnection());
            $competenceGoalsService = new CompetenceGoalsService(DB::getDBConnection());
            $learningMethodsService = new LearningMethodsService(DB::getDBConnection());
            $workRequirementsService = new WorkRequirementsService(DB::getDBConnection());
            $courseCoordinatorService = new CourseCoordinatorService(DB::getDBConnection());
            $prerequisiteService = new PrerequisitesService(DB::getDBConnection());
            $userService = new UserService(DB::getDBConnection());
            $commentsService = new CommentsService(DB::getDBConnection());
            $id = htmlspecialchars($_GET['id']);
            $courseDesc = $courseDescService->getCourseDescription($id);
            if (is_null($courseDesc)) {
                echo $twig->render('displayMessage.twig', array(
                    'user' => $_SESSION['bruker'],
                    'notificationTypes' => $inboxService->getAllInboxTypes(),
                    'unreadNotifications' => $inboxService->getTotalAmountOfUnreadMessages($userId),
                    'unreadNotificationsWithType' => $inboxService->getAmountOfUnreadMessagesWithType($userId),
                    'userRole' => $_SESSION['AC']->getUserRole()->getRoleName(),
                    'message_title' => "Emnebeskrivelse ikke funnet.",
                    'message_description' => "Ingen emnebeskrivelse med oppgitt id eksisterer."
                ));
            } else {
                echo $twig->render('courseDescription.twig', array(
                    'user' => $_SESSION['bruker'],
                    'notificationTypes' => $inboxService->getAllInboxTypes(),
                    'unreadNotifications' => $inboxService->getTotalAmountOfUnreadMessages($userId),
                    'unreadNotificationsWithType' => $inboxService->getAmountOfUnreadMessagesWithType($userId),
                    'userRole' => $_SESSION['AC']->getUserRole()->getRoleName(),
                    'courseDesc' => $courseDesc,
                    'teachingLanguages' => $languageService->getAllLanguages(),
                    'examType' => $examService->getAllExamTypes(),
                    'gradeScale' => $gradeService->getAllGradeScales(),
                    'onlineOffers' => $onlineOffersService->getOffersByCourseDescription($id),
                    'teachingLocations' => $teachingService->getTeachingLocationsByCourseDescription($id),
                    'courseDescriptions' => $courseCodeService->getRelatedCourseDescriptions($_GET['id']),
                    'courseCode' => $courseDescService->getCourseCodeFromCourseDescription($id),
                    'academicContent' => $academicContentService->getLastEntryForCourseDescription($id),
                    'competenceGoals' => $competenceGoalsService->getLastEntryForCourseDescription($id),
                    'learningMethods' => $learningMethodsService->getLastEntryForCourseDescription($id),
                    'workRequirements' => $workRequirementsService->getLastEntryForCourseDescription($id),
                    'courseCoordinators' => $courseCoordinatorService->getCourseCoordinatorsByDescWithUser($id),
                    'prerequisites' => $prerequisiteService->getAllPrerequisitesByCourseDescription($id),
                    'course_codes' => $courseCodeService->getAllCourseCodes(),
                    'users' => $userService->getAllUsers(),
                    'comments' => $commentsService->getAllCommentsByCourseDescription($id),
                    'AC' => $_SESSION['AC']
                ));
            }
        } else {
            echo $twig->render('displayMessage.twig', array(
                'user' => $_SESSION['bruker'],
                'notificationTypes' => $inboxService->getAllInboxTypes(),
                'unreadNotifications' => $inboxService->getTotalAmountOfUnreadMessages($userId),
                'unreadNotificationsWithType' => $inboxService->getAmountOfUnreadMessagesWithType($userId),
                'userRole' => $_SESSION['AC']->getUserRole()->getRoleName(),
                'message_title' => "Feil parameter.",
                'message_description' => "Emnebeskrivelsens id kan kun inneholde tall."
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
    echo $twig->render('displayMessage.twig', array(
        'message_title' => 'Ingen tilgang',
        'message_description' => 'Du har ikke tilgang til denne ressursen. Vennligst kontakt administrator.'
    ));
}