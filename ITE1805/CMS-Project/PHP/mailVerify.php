<?php
//Code retrieved from https://kark.uit.no/internett/php/mailer/curltest.phps
//and adapted for use in this project

function sendVerificationMail($email, $key){
    $ch = curl_init();

    $string = "https://kark.uit.no/internett/php/mailer/mailer.php?address=" . $email . "&url=http://localhost/Prosjektarbeid-CMS-Gruppe-25/PHP/verify.php?key=" . $key;

    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($ch, CURLOPT_URL, $string);
    curl_setopt($ch, CURLOPT_HEADER, 0);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);

    $output = curl_exec($ch);

    curl_close($ch);

    return $output;
}