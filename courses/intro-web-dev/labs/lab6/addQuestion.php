<?php 
session_start(); 

if($_SERVER['REQUEST_METHOD'] == "GET"){
    if(isset($_GET['question']) && $_GET['question'] != ""){
        $_SESSION['question']= array("q" => $_GET['question'], "y" => 0, "n" => 0); 
        header('Location: vote.php');
    }
    else{ //user submitted a blank question, send them back to register question page
        header('Location: lab6.html');

    }
}
?>