<?php 

//Remove favorites 
if($_SERVER['REQUEST_METHOD'] == 'GET'){
    if(isset($_GET['id'])){
        session_start();
        //Remove All was clicked, deletes entire session data
        if($_GET['id'] == 0){
            session_unset();
        }
        //Remove on an individual painting was clicked
        //Deletes the painting from the session and decrements amount of favourites 
        else{
            unset($_SESSION['painting'][$_GET['id']]); 
            $_SESSION['count']--;
        }
        header('Location: view-favorites.php');
    }
}
?> 
