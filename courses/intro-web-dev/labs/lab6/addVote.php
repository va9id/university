<?php 
session_start(); 

if($_SERVER['REQUEST_METHOD'] == "GET"){
    if(isset($_GET['vote'])){
        if($_GET['vote'] == "yes"){
            $_SESSION['question']["y"]++;
        }
        else{
            $_SESSION['question']["n"]++;
        }
    }
    header('Location: tally.php');
}
?>